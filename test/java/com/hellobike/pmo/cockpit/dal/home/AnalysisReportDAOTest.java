package com.hellobike.pmo.cockpit.dal.home;

import com.hellobike.pmo.cockpit.model.home.AnalysisReportDOTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AnalysisReportDAOTest {
    /**
     * 查询效能分析报告概要总结
     *
     * @param buId
     * @param deptId
     * @param reportType
     * @param reportDate
     */
    List<AnalysisReportDOTest> queryDeptAnalysisReportTest(@Param("buId") Long buId,
                                                       @Param("deptId") List<Long> deptId,
                                                       @Param("reportType") Integer reportType,
                                                       @Param("reportDate") List<String> reportDate
                                                       );
}
