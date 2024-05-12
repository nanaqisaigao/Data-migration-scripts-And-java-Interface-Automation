package com.hellobike.pmo.cockpit.cases.deliver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.dal.deliver.DeliverScoreAndMetricDAOTest;
import com.hellobike.pmo.cockpit.enums.EntityEnum;
import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
import com.hellobike.pmo.cockpit.model.deliver.DeliverScoreAndMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.deliver.DeliverScoreAndMetricDeptDOTest;
import com.hellobike.pmo.cockpit.report.ZTestReport;
import com.hellobike.pmo.cockpit.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.hellobike.pmo.cockpit.util.AssertUtil.assertNumbers;

@Listeners({ZTestReport.class})
@Slf4j
public class DeliverScoreAndMetricTest extends BaseTest {

    @Autowired
    private DeliverScoreAndMetricDAOTest deliverScoreAndMetricDAO ;
    private static String token ;

    @DataProvider(name="dp1")
    public Object[][] dp1() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(1,6,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/交付履约--测试用例.xlsx"));
        return allData;
    }
    @DataProvider(name="dp2")
    public Object[][] dp2() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(7,12,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/交付履约--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp3")
    public Object[][] dp3() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(13,18,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/交付履约--测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp4")
    public Object[][] dp4() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(19,21,3,5,"src/test/java/com/hellobike/pmo/cockpit/data/交付履约--测试用例.xlsx"));
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

        //given

        //when

        //then

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
        BigDecimal onTimeCompletionRate = BigDecimal.valueOf(metricList.getJSONObject(2).getDoubleValue("score"));
        BigDecimal projectChangeRate = BigDecimal.valueOf(metricList.getJSONObject(3).getDoubleValue("score"));
        BigDecimal onTimeDeliveryRate = BigDecimal.valueOf(metricList.getJSONObject(0).getDoubleValue("score"));
        BigDecimal requirementChangeRate = BigDecimal.valueOf(metricList.getJSONObject(1).getDoubleValue("score"));
        log.info("onTimeCompletionRate->"+onTimeCompletionRate);
        // 获取数据库列表,断言品质分及指标
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if(entityType == EntityEnum.OVERALL){
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricBuDOTest> buDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")),tableName);
            BigDecimal actualScore = buDos.get(0).calDeliverScore();
            Assert.assertTrue(score.compareTo(actualScore)==0,"品质分不相等");
            if(onTimeCompletionRate!=null && buDos.get(0).getOnTimeCompletionRate()!=null){
                Assert.assertTrue(onTimeCompletionRate.compareTo(buDos.get(0).getOnTimeCompletionRate())==0,"按时结项率不一致");
            }
            if(projectChangeRate!=null && buDos.get(0).getProjectChangeRate()!=null){
                Assert.assertTrue(projectChangeRate.compareTo(buDos.get(0).getProjectChangeRate())==0,"项目变更率不一致");
            }
            if(onTimeDeliveryRate!=null && buDos.get(0).getOnTimeDeliveryRate()!=null){
                Assert.assertTrue(onTimeDeliveryRate.compareTo(buDos.get(0).getOnTimeDeliveryRate())==0,"按时交付率不一致");            }
            if(requirementChangeRate!=null && buDos.get(0).getRequirementChangeRate()!=null){
                Assert.assertTrue(requirementChangeRate.compareTo(buDos.get(0).getRequirementChangeRate())==0,"需求变更率不一致");
            }

        } else if (entityType == EntityEnum.DEPARTMENT) {
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricDeptDOTest> deptDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), Long.valueOf((String) paramMap.get("entityId")) ,tableName);
            BigDecimal actualScore = deptDos.get(0).calDeliverScore();
            if(onTimeCompletionRate!=null && deptDos.get(0).getOnTimeCompletionRate()!=null){
                Assert.assertTrue(onTimeCompletionRate.compareTo(deptDos.get(0).getOnTimeCompletionRate())==0,"按时结项率不一致");
            }
            if(projectChangeRate!=null && deptDos.get(0).getProjectChangeRate()!=null){
                Assert.assertTrue(projectChangeRate.compareTo(deptDos.get(0).getProjectChangeRate())==0,"项目变更率不一致");
            }
            if(onTimeDeliveryRate!=null && deptDos.get(0).getOnTimeDeliveryRate()!=null){
                Assert.assertTrue(onTimeDeliveryRate.compareTo(deptDos.get(0).getOnTimeDeliveryRate())==0,"按时交付率不一致");            }
            if(requirementChangeRate!=null && deptDos.get(0).getRequirementChangeRate()!=null){
                Assert.assertTrue(requirementChangeRate.compareTo(deptDos.get(0).getRequirementChangeRate())==0,"需求变更率不一致");
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
    public void getScoreTrendTest(String url,String path, String param){
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
        if(entityType == EntityEnum.OVERALL){
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricBuDOTest> buDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")),  Long.valueOf((String) paramMap.get("entityId")),tableName);
            // 断言长度
            Assert.assertEquals(scoreListSize,buDos.size(),"品质分列表长度一致");
            for (int i= 0;i<scoreListSize;i++){
                // 获取开始时间的分数
                BigDecimal score = BigDecimal.valueOf(scoreDataList.getJSONObject(i).getDoubleValue("value"));
                BigDecimal actualScore = buDos.get(i).calDeliverScore();
                log.info("actualScore"+actualScore);
                AssertUtil.assertNumbers(score,actualScore,"品质分不相等");
            }
        } else if (entityType == EntityEnum.DEPARTMENT) {
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricDeptDOTest> deptDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")),  Long.valueOf((String) paramMap.get("entityId")),tableName);
            // 断言长度
            // Assert.assertEquals(scoreListSize/2,deptDos.size(),"品质分列表长度一致");
            if(scoreListSize==deptDos.size()){
                // 获取开始时间的分数
                BigDecimal score = BigDecimal.valueOf(scoreDataList.getJSONObject(0).getDoubleValue("value"));
                BigDecimal actualScore = deptDos.get(0).calDeliverScore();
                // Assert.assertTrue(score.compareTo(actualScore)==0,"品质分不相等");
                AssertUtil.assertNumbers(score,actualScore,"品质分不相等");
            }
        }

    }

    /**
     * 断言 根据起始时间和结束时间获取指标数据列表
     * @param url url
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp3")
    public void getMetricTrendTest(String url,String path, String param){
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
        int trendListSize = trendList.size()/4;
        // 获取首个周期的指标
        BigDecimal onTimeCompletionRate = BigDecimal.valueOf(trendList.getJSONObject(6).getDoubleValue("value"));
        BigDecimal projectChangeRate = BigDecimal.valueOf(trendList.getJSONObject(7).getDoubleValue("value"));
        BigDecimal onTimeDeliveryRate = BigDecimal.valueOf(trendList.getJSONObject(4).getDoubleValue("value"));
        BigDecimal requirementChangeRate = BigDecimal.valueOf(trendList.getJSONObject(5).getDoubleValue("value"));
        // 获取数据库列表
        EntityEnum entityType = EntityEnum.valueOf((String) paramMap.get("entityType"));
        if( entityType == EntityEnum.OVERALL){
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricBuDOTest> buDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByBuIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")),  Long.valueOf((String) paramMap.get("entityId")),tableName);
            Assert.assertEquals(trendListSize,buDos.size(),"品质分列表长度一致");
            if(trendListSize==buDos.size()){
                Assert.assertTrue(onTimeCompletionRate.compareTo(buDos.get(1).getOnTimeCompletionRate())==0,"按时结项率不一致");
                Assert.assertTrue(projectChangeRate.compareTo(buDos.get(1).getProjectChangeRate())==0,"项目变更率不一致");
                Assert.assertTrue(onTimeDeliveryRate.compareTo(buDos.get(1).getOnTimeDeliveryRate())==0,"按时交付率不一致");
                Assert.assertTrue(requirementChangeRate.compareTo(buDos.get(1).getRequirementChangeRate())==0,"需求变更率不一致");

            }
        } else if ( entityType == EntityEnum.DEPARTMENT) {
            String tableName = deliverScoreAndMetricDAO.T_DELIVER_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
            List<DeliverScoreAndMetricDeptDOTest> deptDos =  deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")),  Long.valueOf((String) paramMap.get("entityId")),tableName);
            Assert.assertEquals(trendListSize,deptDos.size(),"品质分列表长度一致");
            if(trendListSize==deptDos.size()){
                assertNumbers(onTimeCompletionRate,deptDos.get(1).getOnTimeCompletionRate(),"按时结项率不一致");
                assertNumbers(projectChangeRate, deptDos.get(1).getProjectChangeRate(), "项目变更率不一致");
                assertNumbers(onTimeDeliveryRate, deptDos.get(1).getOnTimeDeliveryRate(), "按时交付率不一致");
                assertNumbers(requirementChangeRate, deptDos.get(1).getRequirementChangeRate(), "需求变更率不一致");

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
        log.info("data->"+data);
        // 获取数据库列表
        String tableName = deliverScoreAndMetricDAO.T_DELIVER_DEPT_METRIC.get(DateDimensionType.valueOf((String) paramMap.get("timeType")));
        List<DeliverScoreAndMetricDeptDOTest> deptDos = deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), null,tableName);
        JSONArray drillingList = data.getJSONArray("drillingList");
        int entityListSize =  drillingList.getJSONObject(0).getJSONArray("subEntityList").size();
        // 断言长度是否一致
        Assert.assertEquals(entityListSize, deptDos.size(),"部门列表长度不一致");
        if (drillingList.getJSONObject(0).getString("dimensionName").contains("部门")){
            for (int i= 0;i<entityListSize;i++){
                Long entityId = drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getLong("entityId");
                BigDecimal entityScore = BigDecimal.valueOf(drillingList.getJSONObject(0).getJSONArray("subEntityList").getJSONObject(i).getDoubleValue("score"));
                BigDecimal actualScore =deliverScoreAndMetricDAO.queryDeliverScoreAndMetricByDeptIdTest(DateUtil.parseDate((String) paramMap.get("dateTo")),DateUtil.parseDate((String) paramMap.get("dateTo")), entityId,tableName).get(0).calDeliverScore();
                AssertUtil.assertNumbers(entityScore,actualScore,"部门品质分不一致");
            }

            // Assert.assertEquals(entityScore,actualScore,"部门品质分不一致");
        }
    }

}
