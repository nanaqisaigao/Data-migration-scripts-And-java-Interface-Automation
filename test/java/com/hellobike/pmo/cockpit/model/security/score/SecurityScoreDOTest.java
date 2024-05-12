package com.hellobike.pmo.cockpit.model.security.score;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SecurityScoreDOTest {
    private Long id;
    private Long buId;
    private String buName;
    private BigDecimal score;
    private Date recordDate;
    // 品质分，需要得到四个指标后再进行计算
}
