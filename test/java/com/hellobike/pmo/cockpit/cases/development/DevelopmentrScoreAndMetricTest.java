package com.hellobike.pmo.cockpit.cases.development;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.dal.development.DevelopmentScoreAndMetricDAOTest;
import com.hellobike.pmo.cockpit.enums.EntityEnum;
import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricCampaignDOTest;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricDeptDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreBuDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreCampaignDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreDeptDOTest;
import com.hellobike.pmo.cockpit.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.hellobike.pmo.cockpit.util.AssertUtil.assertNumbers;


@Slf4j
public class DevelopmentrScoreAndMetricTest extends BaseTest {
    @Autowired
    private DevelopmentScoreAndMetricDAOTest developmentScoreAndMetricDAO;
    private static String token;

    @DataProvider(name="dp1")
    public Object[][] dp1() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(1,9,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/研发效能--测试用例.xlsx"));
        return allData;
    }
    @DataProvider(name="dp2")
    public Object[][] dp2() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(10,18,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/研发效能--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp3")
    public Object[][] dp3() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(19,27,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/研发效能--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp4")
    public Object[][] dp4() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(28,33,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/研发效能--测试用例.xlsx"));
        return allData;
    }

    @BeforeClass
    public void setup(ITestContext context){
        token = context.getCurrentXmlTest().getParameter("token");
    }

    /**
     * 断言 根据结束时间获取获取品质分和指标数据
     * @param url url
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp1")
    public void getScoreAndMetricTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        // 获取响应内容
        String response = HttpUtil.doPost(u,headers,param);
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取响应体品质分
        BigDecimal score = (BigDecimal) JSONPath.eval(json,"$.data.score");
        log.info("socre"+score);
        // 获取响应体指标列表
        JSONArray metricList = data.getJSONArray("cateScoreList");

        BigDecimal projectDeliveryDuration = (BigDecimal) metricList.getJSONObject(0).get("score");
        BigDecimal requirementDevDeliveryDuration = (BigDecimal) metricList.getJSONObject(1).get("score");
        BigDecimal requirementDeliveryPerPersonDay = (BigDecimal) metricList.getJSONObject(2).get("score");
        BigDecimal firstSmokeTestPassRate = (BigDecimal) metricList.getJSONObject(3).get("score");
        BigDecimal bugReopenRate = (BigDecimal) metricList.getJSONObject(4).get("score");
        BigDecimal severityBugResolutionDuration = (BigDecimal) metricList.getJSONObject(5).get("score");
        BigDecimal appPublishSuccessRate = (BigDecimal) metricList.getJSONObject(6).get("score");
        // 获取数据库列表,断言品质分及指标
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if(entityType == EntityEnum.OVERALL){
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_BU_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentMetricBuDOTest> buMetricDos =  developmentScoreAndMetricDAO.queryDevelopmentMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            List<DevelopmentScoreBuDOTest> buScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);

            BigDecimal actualScore = buScoreDos.get(0).getScore();
            log.info("buScoreDos.get(0).getScore()"+buScoreDos.get(0).getScore());
            assertNumbers(score, actualScore, "品质分不相等");
            assertNumbers(projectDeliveryDuration, buMetricDos.get(0).getProjectDeliveryDuration(), "项目交付时长不一致");
            assertNumbers(requirementDevDeliveryDuration, buMetricDos.get(0).getRequirementDevDeliveryDuration(), "需求交付时长不一致");
            assertNumbers(requirementDeliveryPerPersonDay, buMetricDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
            assertNumbers(firstSmokeTestPassRate, buMetricDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
            assertNumbers(bugReopenRate, buMetricDos.get(0).getBugReopenRate(), "bug reopen率不一致");
            assertNumbers(severityBugResolutionDuration, buMetricDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
            assertNumbers(appPublishSuccessRate, buMetricDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");
        } else if (entityType == EntityEnum.DEPARTMENT) {
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreDeptDOTest> deptScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            List<DevelopmentMetricDeptDOTest> deptDos =  developmentScoreAndMetricDAO.queryDevelopmentMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")) ,metricTableName);
            BigDecimal actualScore = deptScoreDos.get(0).getScore();
            Assert.assertEquals(score, actualScore, "品质分不相等");
            assertNumbers(projectDeliveryDuration, deptDos.get(0).getProjectDeliveryDuration(), "项目交付周期不一致");
            assertNumbers(requirementDevDeliveryDuration, deptDos.get(0).getRequirementDevDeliveryDuration(), "研发交付时长不一致");
            assertNumbers(requirementDeliveryPerPersonDay, deptDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
            assertNumbers(firstSmokeTestPassRate, deptDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
            assertNumbers(bugReopenRate, deptDos.get(0).getBugReopenRate(), "bug reopen率不一致");
            assertNumbers(severityBugResolutionDuration, deptDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
            assertNumbers(appPublishSuccessRate, deptDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");
        } else if(entityType == EntityEnum.CAMPAIGN){
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_CAMPAIGN_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_CAMPAIGN_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreCampaignDOTest> camScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            List<DevelopmentMetricCampaignDOTest> camDos =  developmentScoreAndMetricDAO.queryDevelopmentMetricByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")) ,metricTableName);
            BigDecimal actualScore = camScoreDos.get(0).getScore();
            assertNumbers(score, actualScore,"品质分不相等");
            assertNumbers(projectDeliveryDuration, camDos.get(0).getProjectDeliveryDuration(), "项目交付时长不一致");
            assertNumbers(requirementDevDeliveryDuration, camDos.get(0).getRequirementDevDeliveryDuration(), "需求交付时长不一致");
            assertNumbers(requirementDeliveryPerPersonDay, camDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
            assertNumbers(firstSmokeTestPassRate, camDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
            assertNumbers(bugReopenRate, camDos.get(0).getBugReopenRate(), "bug reopen率不一致");
            assertNumbers(severityBugResolutionDuration, camDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
            assertNumbers(appPublishSuccessRate, camDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");

        }

    }

    /**
     * 断言 根据起始时间和结束时间获取详细品质分列表
     * @param url url
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp2")
    public void getScoreDetailTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        // 获取响应内容
        String response = HttpUtil.doPost(u,headers,param);
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取品质分列表
        JSONArray scoreDataList = data.getJSONArray("scoreDataList");
        // 获取列表长度
        int scoreListSize = scoreDataList.size();
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        // 获取数据库列表
        if(entityType==EntityEnum.OVERALL){
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_BU_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreBuDOTest> buScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            // 断言品质分
            BigDecimal score = (BigDecimal)JSONPath.eval(json,"$.data.scoreDataList[0].value");
            BigDecimal actualScore = buScoreDos.get(0).getScore();
            AssertUtil.assertNumbers(score,actualScore,"品质分不相等");
        } else if (entityType==EntityEnum.DEPARTMENT) {
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreDeptDOTest> deptScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            BigDecimal score = (BigDecimal) JSONPath.eval(json,"$.data.scoreDataList[name == '两轮出行研发'][0].value");
            BigDecimal actualScore = deptScoreDos.get(0).getScore();
            AssertUtil.assertNumbers(score,actualScore,"品质分不相等");
        } else if(entityType==EntityEnum.CAMPAIGN){
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_CAMPAIGN_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreCampaignDOTest> deptScoreDos =  developmentScoreAndMetricDAO.queryDevelopmentScoreByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            BigDecimal score = (BigDecimal) JSONPath.eval(json,"$.data.scoreDataList[name == '两轮出行'][0].value");
            BigDecimal actualScore = deptScoreDos.get(0).getScore();
            AssertUtil.assertNumbers(score,actualScore,"品质分不相等");
        }

    }

    /**
     * 断言 根据起始时间和结束时间获取指标数据列表
     * @param url url
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp3")
    public void getMetricDetailTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        // 获取响应内容
        String response = HttpUtil.doPost(u,headers,param);
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        log.info("json->"+json);
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取指标列表
        JSONArray trendList = data.getJSONArray("trendList");
        // 获取指标长度
        int trendListSize = trendList.size()/7;
        // 获取首个周期的指标
        BigDecimal projectDeliveryDuration = (BigDecimal) trendList.getJSONObject(0).get("value");
        BigDecimal requirementDevDeliveryDuration = (BigDecimal) trendList.getJSONObject(1).get("value");
        BigDecimal requirementDeliveryPerPersonDay = (BigDecimal) trendList.getJSONObject(2).get("value");
        BigDecimal firstSmokeTestPassRate = (BigDecimal) trendList.getJSONObject(3).get("value");
        BigDecimal bugReopenRate = (BigDecimal) trendList.getJSONObject(4).get("value");
        BigDecimal severityBugResolutionDuration = (BigDecimal) trendList.getJSONObject(5).get("value");
        BigDecimal appPublishSuccessRate = (BigDecimal) trendList.getJSONObject(6).get("value");
        // 获取数据库列表
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));

        if(entityType==EntityEnum.OVERALL){
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentMetricBuDOTest> buDos =  developmentScoreAndMetricDAO.queryDevelopmentMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")) ,DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            Assert.assertEquals(trendListSize, buDos.size(), "品质分列表长度一致");
            if(trendListSize==buDos.size()){
                assertNumbers(projectDeliveryDuration, buDos.get(0).getProjectDeliveryDuration(), "项目交付周期不一致");
                assertNumbers(requirementDevDeliveryDuration, buDos.get(0).getRequirementDevDeliveryDuration(), "研发交付时长不一致");
                assertNumbers(requirementDeliveryPerPersonDay, buDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
                assertNumbers(firstSmokeTestPassRate, buDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
                assertNumbers(bugReopenRate, buDos.get(0).getBugReopenRate(), "bug reopen率不一致");
                assertNumbers(severityBugResolutionDuration, buDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
                assertNumbers(appPublishSuccessRate, buDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");
            }
        } else if (entityType==EntityEnum.DEPARTMENT) {
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentMetricDeptDOTest> deptDos =  developmentScoreAndMetricDAO.queryDevelopmentMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            Assert.assertEquals(trendListSize, deptDos.size(), "品质分列表长度一致");
            if(trendListSize==deptDos.size()){
                assertNumbers(projectDeliveryDuration, deptDos.get(0).getProjectDeliveryDuration(), "项目交付时长不一致");
                assertNumbers(requirementDevDeliveryDuration, deptDos.get(0).getRequirementDevDeliveryDuration(), "需求交付时长不一致");
                assertNumbers(requirementDeliveryPerPersonDay, deptDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
                assertNumbers(firstSmokeTestPassRate, deptDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
                assertNumbers(bugReopenRate, deptDos.get(0).getBugReopenRate(), "bug reopen率不一致");
                assertNumbers(severityBugResolutionDuration, deptDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
                assertNumbers(appPublishSuccessRate, deptDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");
            }
        }else if (entityType==EntityEnum.CAMPAIGN) {
            String metricTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_CAMPAIGN_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentMetricCampaignDOTest> camDos = developmentScoreAndMetricDAO.queryDevelopmentMetricByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")), DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")), metricTableName);
            Assert.assertEquals(trendListSize, camDos.size(), "品质分列表长度一致");
            if (trendListSize == camDos.size()) {
                assertNumbers(projectDeliveryDuration, camDos.get(0).getProjectDeliveryDuration(), "项目交付时长不一致");
                assertNumbers(requirementDevDeliveryDuration, camDos.get(0).getRequirementDevDeliveryDuration(), "需求交付时长不一致");
                assertNumbers(requirementDeliveryPerPersonDay, camDos.get(0).getRequirementDeliveryPerPersonDay(), "人均交付需求数不一致");
                assertNumbers(firstSmokeTestPassRate, camDos.get(0).getFirstSmokeTestPassRate(), "一次冒烟通过率不一致");
                assertNumbers(bugReopenRate, camDos.get(0).getBugReopenRate(), "bug reopen率不一致");
                assertNumbers(severityBugResolutionDuration, camDos.get(0).getSeverityBugResolutionDuration(), "严重缺陷处理时长不一致");
                assertNumbers(appPublishSuccessRate, camDos.get(0).getAppPublishSuccessRate(), "发布成功率不一致");
            }
        }







    }

    /**
     * 断言 根据结束时间获取部门数据
     * @param url url
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp4")
    public void getDrillListTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        // 获取响应内容
        String response = HttpUtil.doPost(u,headers,param);
        log.info("data->"+response);
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取数据库列表
        JSONArray drillingList = data.getJSONArray("drillingList");

        // 断言长度是否一致

        if (drillingList.getJSONObject(0).getString("dimensionName").contains("部门")){
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreDeptDOTest> deptDos = developmentScoreAndMetricDAO.queryDevelopmentScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), null,scoreTableName);
            int entityListSize =  drillingList.getJSONObject(0).getJSONArray("subEntityList").size();
            Assert.assertEquals(entityListSize, deptDos.size(),"部门列表长度不一致");
            for (int i= 0;i<entityListSize;i++){
                Long entityId = drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getLong("entityId");
                BigDecimal entityScore = BigDecimal.valueOf(drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getDoubleValue("score"));
                BigDecimal actualScore =developmentScoreAndMetricDAO.queryDevelopmentScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), entityId,scoreTableName).get(0).getScore();
                assertNumbers(entityScore,actualScore,"部门品质分不一致");
            }

        } else if(drillingList.getJSONObject(1).getString("dimensionName").contains("战役")){
            String scoreTableName = developmentScoreAndMetricDAO.T_DEVELOPMENT_CAMPAIGN_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DevelopmentScoreCampaignDOTest> deptDos = developmentScoreAndMetricDAO.queryDevelopmentScoreByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), null,scoreTableName);
            int entityListSize =  drillingList.getJSONObject(0).getJSONArray("subEntityList").size();
            Assert.assertEquals(entityListSize, deptDos.size(),"战役列表长度不一致");

            for (int i= 0;i<entityListSize;i++){
                Long entityId = drillingList.getJSONObject(1).getJSONArray("subEntityList").getJSONObject(i).getLong("entityId");
                BigDecimal entityScore = BigDecimal.valueOf(drillingList.getJSONObject(1).getJSONArray("subEntityList").getJSONObject(i).getDoubleValue("score"));
                BigDecimal actualScore =developmentScoreAndMetricDAO.queryDevelopmentScoreByCampaignIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), entityId,scoreTableName).get(0).getScore();
                assertNumbers(entityScore,actualScore,"战役品质分不一致");
            }
        }
    }
}
