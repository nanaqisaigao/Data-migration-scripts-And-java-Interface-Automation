package com.hellobike.pmo.cockpit.model.home;

import lombok.Data;

import java.util.Date;

@Data
public class AnalysisReportDOTest {
    /**
     * 主键id
     */
    private Long id;
    /**
     * buid
     */
    private Long buId;
    /**
     * bu名称
     */
    private String buName;
    /**
     * 部门id
     */
    private Long deptId;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 报告类型, 月, 季, 年
     */
    private Integer reportType;
    /**
     * 报告时间, 用-01表示
     */
    private String reportDate;
    /**
     * 概要总结
     */
    private String analysisSummary;
    /**
     * 分析报告模板id(t_report_template表 1-n)
     */
    private Long reportTemplateId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 是否删除
     */
    private Integer isDelete;
    /**
     * 新增模板内容
     */
    private String analysisModuleContent;
}
