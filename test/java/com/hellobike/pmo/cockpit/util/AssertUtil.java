package com.hellobike.pmo.cockpit.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.math.BigDecimal;

@Slf4j
public class AssertUtil {

    public static void assertNumbers(BigDecimal actual, BigDecimal expected,String var) {
        if (expected == null) {
            log.info("expected->"+expected);
            log.info("actual->"+actual);
            Assert.assertNull( actual,var);
        }else {
            log.info("expected->"+expected);
            log.info("actual->"+actual);
            Assert.assertTrue(expected.compareTo(actual)==0,var);
        }
    }

}
