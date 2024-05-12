package com.hellobike.pmo.cockpit.cases.basic.devteamapproval;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.dal.basic.devteamapproval.DevTeamMappingDAOTest;
import com.hellobike.pmo.cockpit.dal.basic.devteamapproval.DevTeamNotMappingDAOTest;
import com.hellobike.pmo.cockpit.model.basic.devteamapproval.DevTeamMappingDOTest;
import com.hellobike.pmo.cockpit.model.basic.devteamapproval.DevTeamNotMappingDOTest;
import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
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
import java.util.List;
import java.util.Map;

/**
 * @author renmengxiwb304
 * @date 2024/3/5
 */
@Slf4j
public class DevTeamTest extends BaseTest {
    @Autowired
    private DevTeamNotMappingDAOTest devTeamNotDAO;

    @Autowired
    private DevTeamMappingDAOTest devTeamDAO;
    private  static String token ;

    @BeforeClass
    public void setup(ITestContext context) {
        token = context.getCurrentXmlTest().getParameter("token");
    }


    @DataProvider(name="dp1")
    public Object[][] dp1() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(1,3,2,4,"src/test/java/com/hellobike/pmo/cockpit/data/驾驶舱配置-测试用例.xlsx"));
        log.info("log->"+(String) allData[1][1]);
        return allData;
    }
    @DataProvider(name="dp2")
    public Object[][] dp2() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(4,6,2,4,"src/test/java/com/hellobike/pmo/cockpit/data/驾驶舱配置-测试用例.xlsx"));
        return allData;
    }

    @DataProvider(name="dp3")
    public Object[][] dp3() {
        Object[][] allData = ExcelUtil.readFile(new ExcelInfo(7,7,2,4,"src/test/java/com/hellobike/pmo/cockpit/data/驾驶舱配置-测试用例.xlsx"));
        return allData;
    }

    /**
     * 获取没有关联的研发团队列表
     * @param url 域名
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider="dp1")
    public void getNotDevTeamListTest(String url,String path, String param) {
        String u = url + path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        String response = HttpUtil.doGet(u,headers,param);
        log.info(response);
        JSONObject json = JSONObject.parseObject(response);
        JSONObject data = json.getJSONObject("data");
        // 获取没有关联的研发团队列表
        JSONArray notMappingList = data.getJSONArray("notMappingList");
        // 获取关联分析部门的研发团队列表
        JSONArray mappingList = data.getJSONArray("mappingList");
        // 获取查询数据库参数
        Map<String,String> params = ParseDataUtil.parseGetParams(param);
        // 断言研发团队记录是否正确
        Assert.assertEquals(mappingList.size(),devTeamDAO.queryListTest(params.get("devTeamName")).size(),"返回值不相等");
        if(notMappingList.size()==devTeamNotDAO.queryListTest(params.get("devTeamName")).size()){
            for (int i = 0; i < notMappingList.size(); i++) {
                JSONObject devTeam = notMappingList.getJSONObject(i);
                DevTeamNotMappingDOTest devTeamNotMapping = devTeamNotDAO.queryListTest(params.get("devTeamName")).get(i);
                Assert.assertEquals(devTeam.getLong("devTeamId"),devTeamNotMapping.getDevTeamId(),"返回值不相等");
                Assert.assertEquals(devTeam.getString("devTeamName"),devTeamNotMapping.getDevTeamName(),"返回值不相等");
            }
        }
        if(mappingList.size()==devTeamDAO.queryListTest(params.get("devTeamName")).size()){
            for (int i = 0; i < mappingList.size(); i++) {
                JSONObject devTeam = mappingList.getJSONObject(i);
                DevTeamMappingDOTest devTeamMapping = devTeamDAO.queryListTest(params.get("devTeamName")).get(i);
                Assert.assertEquals(devTeam.getLong("devTeamId"),devTeamMapping.getDevTeamId(),"返回值不相等");
                Assert.assertEquals(devTeam.getString("devTeamName"),devTeamMapping.getDevTeamName(),"返回值不相等");
            }
        }

    }

    /**
     * 获取关联和没有关联的研发团队列表
     * @param url 域名
     * @param path 接口
     * @param param 参数
     */
    @Test(dataProvider = "dp2")
    public void getEditableDevTeamListTest(String url,String path, String param){
        String u = url +path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        String response = HttpUtil.doPost(u,headers,param);
        JSONObject jsonResponse = JSONObject.parseObject(response);
        JSONObject data = jsonResponse.getJSONObject("data");
        log.info("data->"+data);
        JSONArray notMappingList = data.getJSONArray("list");
        // 获取参数
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(param);
        // 分页
        PageHelper.startPage((Integer) paramMap.get("pageNum"), (Integer) paramMap.get("pageSize"));
        //获取没有关联分析部门的研发团队列表
        List<DevTeamNotMappingDOTest> devTeamNameDos = devTeamNotDAO.queryListTest((String) paramMap.get("devTeamName"));
        PageInfo<DevTeamNotMappingDOTest> pageInfo = new PageInfo<>(devTeamNameDos);
        Assert.assertEquals(notMappingList.size(),devTeamNameDos.size(),"返回值不相等");
        if(notMappingList.size()==devTeamNameDos.size()){
            for (int i = 0; i < notMappingList.size(); i++) {
                JSONObject devTeam = notMappingList.getJSONObject(i);
                DevTeamNotMappingDOTest devTeamNotMapping = devTeamNotDAO.queryListTest((String) paramMap.get("devTeamName")).get(i);
                Assert.assertEquals(devTeam.getLong("devTeamId"),devTeamNotMapping.getDevTeamId(),"返回值不相等");
                Assert.assertEquals(devTeam.getString("devTeamName"),devTeamNotMapping.getDevTeamName(),"返回值不相等");
            }
        }

    }

    @Test(dataProvider = "dp3",enabled=false)
    public void setDevTeamTest(String url,String path, String param){
        String u = url +path;
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        String response = HttpUtil.doPost(u, headers, param);
        JSONObject json = JSONObject.parseObject(response);
        Assert.assertEquals(10000,json.getIntValue("code"),"与响应码状态不符");
    }
}
