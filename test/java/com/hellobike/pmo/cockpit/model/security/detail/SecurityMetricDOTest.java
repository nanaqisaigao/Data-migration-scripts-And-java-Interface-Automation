package com.hellobike.pmo.cockpit.model.security.detail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SecurityMetricDOTest {
    private Long id;
    private Long buId;
    private String buName;
    private BigDecimal sla;
    private BigDecimal faultScore;
    private BigDecimal riskOverDueRate;
    private BigDecimal onTimeWarningRate;
    private Date recordDate;


}
