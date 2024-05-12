package com.hellobike.pmo.cockpit.model.development.detail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DevelopmentMetricDOTest {
    private Long id;
    private Long buId;
    private String buName;
    /**
     * 项目交付周期
     */
    private BigDecimal projectDeliveryDuration;
    /**
     * 需求交付时长
     */
    private BigDecimal requirementDevDeliveryDuration;
    /**
     * 人均交付需求数
     */
    private BigDecimal requirementDeliveryPerPersonDay;
    /**
     * 一次冒烟通过率
     */
    private BigDecimal firstSmokeTestPassRate;
    /**
     * bug reopen率
     */
    private BigDecimal bugReopenRate;
    /**
     * 严重缺陷处理时长
     */
    private BigDecimal severityBugResolutionDuration;
    /**
     * 发布成功率
     */
    private BigDecimal appPublishSuccessRate;
    private Date recordDate;



}
