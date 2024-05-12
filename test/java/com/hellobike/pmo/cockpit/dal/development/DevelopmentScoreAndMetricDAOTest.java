package com.hellobike.pmo.cockpit.dal.development;

import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricCampaignDOTest;
import com.hellobike.pmo.cockpit.model.development.detail.DevelopmentMetricDeptDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreBuDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreCampaignDOTest;
import com.hellobike.pmo.cockpit.model.development.score.DevelopmentScoreDeptDOTest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DevelopmentScoreAndMetricDAOTest {
    public static final Map<DateDimensionType, String> T_DEVELOPMENT_BU_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_bu_m");
            put(DateDimensionType.quarter, "t_development_bu_q");
            put(DateDimensionType.year, "t_development_bu_y");
        }
    };
    public static final Map<DateDimensionType, String> T_DEVELOPMENT_DEPT_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_dept_m");
            put(DateDimensionType.quarter, "t_development_dept_q");
            put(DateDimensionType.year, "t_development_dept_y");
        }
    };
    public static final Map<DateDimensionType, String> T_DEVELOPMENT_CAMPAIGN_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_campaign_m");
            put(DateDimensionType.quarter, "t_development_campaign_q");
            put(DateDimensionType.year, "t_development_campaign_y");
        }
    };

    public static final Map<DateDimensionType, String> T_DEVELOPMENT_BU_SCORE = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_s_bu_m");
            put(DateDimensionType.quarter, "t_development_s_bu_q");
            put(DateDimensionType.year, "t_development_s_bu_y");
        }
    };
    public static final Map<DateDimensionType, String> T_DEVELOPMENT_DEPT_SCORE = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_s_dept_m");
            put(DateDimensionType.quarter, "t_development_s_dept_q");
            put(DateDimensionType.year, "t_development_s_dept_y");
        }
    };
    public static final Map<DateDimensionType, String> T_DEVELOPMENT_CAMPAIGN_SCORE = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_development_s_campaign_m");
            put(DateDimensionType.quarter, "t_development_s_campaign_q");
            put(DateDimensionType.year, "t_development_s_campaign_y");
        }
    };

    /**
     * 根据buid查询指标
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentMetricTable

     * @return
     */
    List<DevelopmentMetricBuDOTest> queryDevelopmentMetricByBuIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentMetricTable") String developmentMetricTable);

    /**
     * 根据部门id查询指标
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentMetricTable
     * @return
     */
    List<DevelopmentMetricDeptDOTest> queryDevelopmentMetricByDeptIdTest(@Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentMetricTable") String developmentMetricTable);

    /**
     * 根据buid查询品质分
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentScoreTable
     * @return
     */
    List<DevelopmentScoreBuDOTest> queryDevelopmentScoreByBuIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentScoreTable") String developmentScoreTable);

    /**
     * 根据deptid查询品质分
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentScoreTable
     * @return
     */
    List<DevelopmentScoreDeptDOTest> queryDevelopmentScoreByDeptIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentScoreTable") String developmentScoreTable);
    /**
     * 根据战役id查询指标
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentMetricTable
     * @return
     */
    List<DevelopmentMetricCampaignDOTest> queryDevelopmentMetricByCampaignIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentMetricTable") String developmentMetricTable);

    /**
     * 根据战役id查询品质分
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param developmentScoreTable
     * @return
     */
    List<DevelopmentScoreCampaignDOTest> queryDevelopmentScoreByCampaignIdTest(@Param("dateFrom") Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("developmentScoreTable") String developmentScoreTable);

}
