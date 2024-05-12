package com.hellobike.pmo.cockpit.dal.deliver;

import com.hellobike.pmo.cockpit.common.util.DateDimensionType;
import com.hellobike.pmo.cockpit.model.deliver.DeliverScoreAndMetricBuDOTest;
import com.hellobike.pmo.cockpit.model.deliver.DeliverScoreAndMetricDOTest;
import com.hellobike.pmo.cockpit.model.deliver.DeliverScoreAndMetricDeptDOTest;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DeliverScoreAndMetricDAOTest {
    public static final Map<DateDimensionType, String> T_DELIVER_BU_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_deliver_bu_m");
            put(DateDimensionType.quarter, "t_deliver_bu_q");
            put(DateDimensionType.year, "t_deliver_bu_y");
        }
    };
    public static final Map<DateDimensionType, String> T_DELIVER_DEPT_METRIC = new HashMap<DateDimensionType, String>() {
        {
            put(DateDimensionType.month, "t_deliver_dept_m");
            put(DateDimensionType.quarter, "t_deliver_dept_q");
            put(DateDimensionType.year, "t_deliver_dept_y");
        }
    };

    /**
     * 根据buId查询品质分和指标
     */
    List<DeliverScoreAndMetricBuDOTest> queryDeliverScoreAndMetricByBuIdTest(@Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("deliverScoreAndMetricTable") String deliverScoreAndMetricTable);

    /**
     * 根据deptId查询品质分和指标
     * @param dateFrom
     * @param dateTo
     * @param entityId
     * @param deliverScoreAndMetricTable
     * @return
     */
    List<DeliverScoreAndMetricDeptDOTest> queryDeliverScoreAndMetricByDeptIdTest(@Param("dateFrom")Date dateFrom, @Param("dateTo")Date dateTo, @Param("entityId")Long entityId, @Param("deliverScoreAndMetricTable") String deliverScoreAndMetricTable);

}
