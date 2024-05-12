package com.hellobike.pmo.cockpit.cases.security;

import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.dal.security.SecurityScoreAndMetricDAOTest;
import com.hellobike.pmo.cockpit.model.security.detail.SecurityMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.security.score.SecurityScoreBuDOTest;
import com.hellobike.pmo.cockpit.util.DateUtil;
import com.hellobike.pmo.cockpit.util.ParseDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleTest extends BaseTest {

    private static String json1 = "{\"entityType\":\"DEPARTMENT\",\"entityId\":\"10\",\"timeType\":\"month\",\"dateFrom\":\"2023-02-28T16:00:00.000Z\",\"dateTo\":\"2024-03-31T16:00:00.000Z\"}";;

    private static String json2 = "{\"entityType\":\"OVERALL\",\"entityId\":\"1\",\"timeType\":\"month\",\"dateFrom\":\"2023-02-28T16:00:00.000Z\",\"dateTo\":\"2024-03-31T16:00:00.000Z\"}";;

    @Autowired
    private SecurityScoreAndMetricDAOTest securityScoreAndMetricDAOTest;

    @Test
    public void test() throws ParseException {
        Map<String,Object> paramMap = ParseDataUtil.parsePostParams(json1);
        Map<String,Object> paramMap2 = ParseDataUtil.parsePostParams(json2);
        List<SecurityMetricBuDOTest> buDos= securityScoreAndMetricDAOTest.querySecurityMetricByBuIdTest(DateUtil.parseDate((String) paramMap2.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")) ,Long.valueOf((String) paramMap2.get("entityId")) ,securityScoreAndMetricDAOTest.T_SECURITY_BU_METRIC.get(DateDimensionType.valueOf((String) paramMap2.get("timeType"))));
        List<SecurityScoreBuDOTest> buScoreDos= securityScoreAndMetricDAOTest.querySecurityScoreByBuIdTest(DateUtil.parseDate((String) paramMap2.get("dateFrom")),DateUtil.parseDate((String) paramMap.get("dateTo")) ,Long.valueOf((String) paramMap2.get("entityId")) ,securityScoreAndMetricDAOTest.T_SECURITY_BU_SCORE.get(DateDimensionType.valueOf((String) paramMap2.get("timeType"))));

        if(buDos.size()!=0){
            System.out.println(buDos.get(0).getRiskOverDueRate());
            System.out.println(buDos.get(0).getOnTimeWarningRate());
            System.out.println(buDos.get(0).getSla());
            System.out.println(buDos.get(0).getOnTimeWarningRate());
        }
        buScoreDos.get(0).getScore();
     }

}
