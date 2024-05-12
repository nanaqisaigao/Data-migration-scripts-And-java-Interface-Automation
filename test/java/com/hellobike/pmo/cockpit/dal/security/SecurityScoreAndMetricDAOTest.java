package com.hellobike.pmo.cockpit.dal.security;

import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.model.security.detail.SecurityMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.security.detail.SecurityMetricDeptDOTest;
import com.hellobike.pmo.cockpit.model.security.score.SecurityScoreBuDOTest;
import com.hellobike.pmo.cockpit.model.security.score.SecurityScoreDeptDOTest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SecurityScoreAndMetricDAOTest {
    public static final Map<DateDimensionType, String> T_SECURITY_BU_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_security_bu_m");
            put(DateDimensionType.quarter, "t_security_bu_q");
            put(DateDimensionType.year, "t_security_bu_y");
        }
    };
    public static final Map<DateDimensionType, String> T_SECURITY_DEPT_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_security_dept_m");
            put(DateDimensionType.quarter, "t_security_dept_q");
            put(DateDimensionType.year, "t_security_dept_y");
        }
    };

    public static final Map<DateDimensionType, String> T_SECURITY_BU_SCORE = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_security_s_bu_m");
            put(DateDimensionType.quarter, "t_security_s_bu_q");
            put(DateDimensionType.year, "t_security_s_bu_y");
        }
    };
    public static final Map<DateDimensionType, String> T_SECURITY_DEPT_SCORE = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_security_s_dept_m");
            put(DateDimensionType.quarter, "t_security_s_dept_q");
            put(DateDimensionType.year, "t_security_s_dept_y");
        }
    };

    /**
     * 根据buId查询品质分和指标
     */
    List<SecurityMetricBuDOTest> querySecurityMetricByBuIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId,  @Param("securityMetricTable") String securityMetricTable);

    /**
     * 根据deptId查询品质分和指标
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param securityMetricTable
     * @return
     */
    List<SecurityMetricDeptDOTest> querySecurityMetricByDeptIdTest(@Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("securityMetricTable") String securityMetricTable);

    List<SecurityScoreBuDOTest> querySecurityScoreByBuIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("securityScoreTable") String securityScoreTable);

    List<SecurityScoreDeptDOTest> querySecurityScoreByDeptIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("securityScoreTable") String securityScoreTable);

}
