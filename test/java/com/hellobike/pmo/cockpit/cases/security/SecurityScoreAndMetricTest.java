package com.hellobike.pmo.cockpit.cases.security;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.dal.security.SecurityScoreAndMetricDAOTest;
import com.hellobike.pmo.cockpit.enums.EntityEnum;
import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
import com.hellobike.pmo.cockpit.model.security.detail.SecurityMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.security.detail.SecurityMetricDeptDOTest;
import com.hellobike.pmo.cockpit.model.security.score.SecurityScoreBuDOTest;
import com.hellobike.pmo.cockpit.model.security.score.SecurityScoreDeptDOTest;
import com.hellobike.pmo.cockpit.util.DateUtil;
import com.hellobike.pmo.cockpit.util.ExcelUtil;
import com.hellobike.pmo.cockpit.util.HttpUtil;
import com.hellobike.pmo.cockpit.util.ParseDataUtil;
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

@Slf4j
public class SecurityScoreAndMetricTest extends BaseTest {
    @Autowired
    private SecurityScoreAndMetricDAOTest securityScoreAndMetricDAO;
    private static String token;

    @DataProvider(name="dp1")
    public Object[][] dp1() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(1,6,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/安全生产--测试用例.xlsx"));
        return allData;
    }
    @DataProvider(name="dp2")
    public Object[][] dp2() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(7,12,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/安全生产--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp3")
    public Object[][] dp3() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(13,18,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/安全生产--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp4")
    public Object[][] dp4() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(19,21,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/安全生产--测试用例.xlsx"));
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
    public void summaryTest(String url,String path, String param){
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
        BigDecimal score = BigDecimal.valueOf(data.getDoubleValue("score"));
        // 获取响应体指标列表
        JSONArray metricList = data.getJSONArray("cateScoreList");
        BigDecimal sla = BigDecimal.valueOf(metricList.getJSONObject(0).getDoubleValue("score"));
        BigDecimal faultScore = BigDecimal.valueOf(metricList.getJSONObject(1).getDoubleValue("score"));
        BigDecimal riskOverDueRate = BigDecimal.valueOf(metricList.getJSONObject(2).getDoubleValue("score"));
        BigDecimal onTimeWarningRate = BigDecimal.valueOf(metricList.getJSONObject(3).getDoubleValue("score"));
        // 获取数据库列表,断言品质分及指标
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if(entityType == EntityEnum.OVERALL){
            String metricTableName = securityScoreAndMetricDAO.T_SECURITY_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            String scoreTableName = securityScoreAndMetricDAO.T_SECURITY_BU_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityMetricBuDOTest> buMetricDos =  securityScoreAndMetricDAO.querySecurityMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            List<SecurityScoreBuDOTest> buScoreDos =  securityScoreAndMetricDAO.querySecurityScoreByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            if(buScoreDos.size()!=0){
                BigDecimal actualScore = buScoreDos.get(0).getScore();
                Assert.assertTrue(score.compareTo(actualScore)==0 ,"品质分不相等");
            }


            Assert.assertTrue(sla.compareTo(buMetricDos.get(0).getSla()) == 0, "系统可用SLA不一致");
            Assert.assertTrue(faultScore.compareTo(buMetricDos.get(0).getFaultScore()) == 0, "故障分不一致");
            Assert.assertTrue(riskOverDueRate.compareTo(buMetricDos.get(0).getRiskOverDueRate()) == 0, "风险逾期率不一致");
            Assert.assertTrue(onTimeWarningRate.compareTo(buMetricDos.get(0).getOnTimeWarningRate()) == 0, "告警认领及时率不一致");
        } else if (entityType == EntityEnum.DEPARTMENT) {
            String metricTableName = securityScoreAndMetricDAO.T_SECURITY_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            String scoreTableName = securityScoreAndMetricDAO.T_SECURITY_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityMetricDeptDOTest> deptMetricDos =  securityScoreAndMetricDAO.querySecurityMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            List<SecurityScoreDeptDOTest> deptScoreDos =  securityScoreAndMetricDAO.querySecurityScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            BigDecimal actualScore = deptScoreDos.get(0).getScore();
            log.info("score"+score);
            log.info("actualScore"+actualScore);
            Assert.assertTrue(score.compareTo(actualScore)==0 ,"品质分不相等");
            Assert.assertEquals(sla,deptMetricDos.get(0).getSla(),"系统可用SLA不一致");
            Assert.assertTrue(faultScore.compareTo(deptMetricDos.get(0).getFaultScore())==0, "故障分不一致");
            if(riskOverDueRate!=null&&deptMetricDos.get(0).getRiskOverDueRate()!=null){
                Assert.assertTrue(riskOverDueRate.compareTo(deptMetricDos.get(0).getRiskOverDueRate())==0, "风险逾期率不一致");
            }
            if(onTimeWarningRate!=null&&deptMetricDos.get(0).getOnTimeWarningRate()!=null){
                Assert.assertTrue(onTimeWarningRate.compareTo(deptMetricDos.get(0).getOnTimeWarningRate())==0, "告警认领及时率不一致");
            }
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
        // 获取数据库列表
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if(entityType==EntityEnum.OVERALL){
            String scoreTableName = securityScoreAndMetricDAO.T_SECURITY_BU_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityScoreBuDOTest> buDos =  securityScoreAndMetricDAO.querySecurityScoreByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);
            // 断言长度
            Assert.assertEquals(scoreListSize,buDos.size(),"品质分列表长度一致");
            for (int i= 0;i<scoreListSize;i++){
                // 获取开始时间的分数
                BigDecimal score = BigDecimal.valueOf(scoreDataList.getJSONObject(i).getDoubleValue("value"));
                BigDecimal actualScore = buDos.get(i).getScore();
                Assert.assertTrue(score.compareTo(actualScore)==0,"品质分不相等");
            }
        } else if (entityType==EntityEnum.DEPARTMENT) {
            String scoreTableName = securityScoreAndMetricDAO.T_SECURITY_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityScoreDeptDOTest> deptDos =  securityScoreAndMetricDAO.querySecurityScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),scoreTableName);

            BigDecimal score = (BigDecimal) JSONPath.eval(json,"$.data.scoreDataList[name == '两轮出行研发'][0].value");
            BigDecimal actualScore = deptDos.get(0).getScore();
            log.info("actualScore"+actualScore);
            Assert.assertTrue(score.compareTo(actualScore)==0,"品质分不相等");

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
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取指标列表
        JSONArray trendList = data.getJSONArray("trendList");
        // 获取指标长度
        int trendListSize = trendList.size()/4;
        // 获取首个周期的指标
        BigDecimal sla = BigDecimal.valueOf(trendList.getJSONObject(0).getDoubleValue("value"));
        BigDecimal faultScore = BigDecimal.valueOf(trendList.getJSONObject(1).getDoubleValue("value"));
        BigDecimal riskOverDueRate = BigDecimal.valueOf(trendList.getJSONObject(2).getDoubleValue("value"));
        BigDecimal onTimeWarningRate = BigDecimal.valueOf(trendList.getJSONObject(3).getDoubleValue("value"));
        // 获取数据库列表
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if(entityType==EntityEnum.OVERALL){
            String metricTableName = securityScoreAndMetricDAO.T_SECURITY_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityMetricBuDOTest> buDos =  securityScoreAndMetricDAO.querySecurityMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            Assert.assertEquals(trendListSize,buDos.size(),"品质分列表长度一致");
            if(trendListSize==buDos.size()){

                Assert.assertTrue(sla.compareTo(buDos.get(0).getSla()) == 0, "系统可用SLA不一致");
                Assert.assertTrue(faultScore.compareTo(buDos.get(0).getFaultScore()) == 0, "故障分不一致");
                Assert.assertTrue(riskOverDueRate.compareTo(buDos.get(0).getRiskOverDueRate()) == 0, "风险逾期率不一致");
                Assert.assertTrue(onTimeWarningRate.compareTo(buDos.get(0).getOnTimeWarningRate()) == 0, "告警认领及时率不一致");
            }
        } else if (entityType == EntityEnum.DEPARTMENT) {
            String metricTableName = securityScoreAndMetricDAO.T_SECURITY_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<SecurityMetricDeptDOTest> deptDos =  securityScoreAndMetricDAO.querySecurityMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),metricTableName);
            Assert.assertEquals(trendListSize,deptDos.size(),"品质分列表长度一致");
            Assert.assertTrue(sla.compareTo(deptDos.get(0).getSla()) == 0, "系统可用SLA不一致");
            Assert.assertTrue(faultScore.compareTo(deptDos.get(0).getFaultScore()) == 0, "故障分不一致");
            if(riskOverDueRate!=null&&deptDos.get(0).getRiskOverDueRate()!=null){
                Assert.assertTrue(riskOverDueRate.compareTo(deptDos.get(0).getRiskOverDueRate()) == 0, "风险逾期率不一致");
            }

            Assert.assertTrue(onTimeWarningRate.compareTo(deptDos.get(0).getOnTimeWarningRate()) == 0, "告警认领及时率不一致");
            // if(trendListSize==deptDos.size()){
            //
            //
            // }
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
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        // 断言响应码
        Assert.assertEquals(json.getString("code"),"10000","响应码不一致");
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 获取响应内容的data数据
        JSONObject data = json.getJSONObject("data");
        // 获取数据库列表
        String scoreTableName = securityScoreAndMetricDAO.T_SECURITY_DEPT_SCORE.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
        List<SecurityScoreDeptDOTest> deptDos = securityScoreAndMetricDAO.querySecurityScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), null,scoreTableName);
        JSONArray drillingList = data.getJSONArray("drillingList");
        int entityListSize =  drillingList.getJSONObject(0).getJSONArray("subEntityList").size();
        // 断言长度是否一致
        Assert.assertEquals(entityListSize, deptDos.size(),"部门列表长度不一致");
        if (drillingList.getJSONObject(0).getString("dimensionName").contains("部门")){
            for (int i= 0;i<entityListSize;i++){
                Long entityId = drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getLong("entityId");
                BigDecimal entityScore = BigDecimal.valueOf(drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getDoubleValue("score"));
                BigDecimal actualScore = securityScoreAndMetricDAO.querySecurityScoreByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), entityId,scoreTableName).get(0).getScore();
                Assert.assertTrue(entityScore.compareTo(actualScore)==0,"部门品质分不一致");
            }

        }
    }

}
