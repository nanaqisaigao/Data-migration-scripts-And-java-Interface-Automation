package com.hellobike.pmo.cockpit.cases.home;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.dal.home.AnalysisReportDAOTest;
import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
import com.hellobike.pmo.cockpit.model.home.AnalysisReportDOTest;
import com.hellobike.pmo.cockpit.report.ZTestReport;
import com.hellobike.pmo.cockpit.util.ExcelUtil;
import com.hellobike.pmo.cockpit.util.HttpUtil;
import com.hellobike.pmo.cockpit.util.ParseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Listeners({ZTestReport.class})
@Slf4j
public class AnalysisDashBoardTest extends BaseTest {
    private static String token;

    @Autowired
    private AnalysisReportDAOTest analysisReportDAOTest;

    @BeforeClass
    public void setup(ITestContext context) {
        token = context.getCurrentXmlTest().getParameter("token");
    }

    @DataProvider(name="dp1")
    public Object[][] dp1() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(1,6,4,6,"src/test/java/com/hellobike/pmo/cockpit/data/效能分析--UI优化.xlsx"));
//        log.info("log->"+(String) allData[1][1]);
        return allData;
    }
    @DataProvider(name="dp2")
    public Object[][] dp2() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(7,12,4,6,"src/test/java/com/hellobike/pmo/cockpit/data/效能分析--UI优化.xlsx"));
//        log.info("log->"+(String) allData[1][1]);
        return allData;
    }

    /**
     * 查询软研-分析部门概要总结
     * @param url url
     * @param path 接口路径
     * @param param 参数
     */
    @Test(dataProvider="dp1")
    public void queryAnalysisReportTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        // 获取响应内容
        String response = HttpUtil.doPost(u,headers,param);
        // 解析响应内容
        JSONObject json = JSONObject.parseObject(response);
        // 获取响应内容的data部门
        JSONArray data = json.getJSONArray("data");
        // 解析参数
        Map<String, Object> params= ParseDataUtil.parsePostParams(param);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        List<Long> deptIdList = new ArrayList<>();
        if((Integer)params.get("deptId.length")!=null){
            for(int i = 0;i<(Integer)params.get("deptId.length");i++){
                deptIdList.add(Long.valueOf((Integer)params.get("deptId["+i+"]")));
            }
        }

        List<String> analysisDateList = new ArrayList<>();
        for(int i = 0;i<(Integer)params.get("analysisDate.length");i++){
            analysisDateList.add((String) params.get("analysisDate["+i+"]"));
        }
        // 获取期望值
        List<AnalysisReportDOTest> analysisReportDOTests = analysisReportDAOTest.queryDeptAnalysisReportTest(Long.valueOf((String) params.get("buId")),deptIdList,(Integer) params.get("analysisType"),analysisDateList);
        // 断言
        Assert.assertEquals(data.size(),analysisReportDOTests.size(),"查询不对应");
    }

    /**
     * 修改并添加月报模块
     * @param url url
     * @param path 接口路径
     * @param param 参数
     */
    @Test(dataProvider = "dp2")
    public void addAnalysisReportTest(String url,String path, String param){
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        String response = HttpUtil.doPost(u,headers,param);
        JSONObject json = JSONObject.parseObject(response);
        // log.info("json->"+json);
        Assert.assertTrue(json.getBooleanValue("data"),"修改并添加模块失败");
    }
}
