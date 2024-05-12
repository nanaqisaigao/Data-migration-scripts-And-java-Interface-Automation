package com.hellobike.pmo.cockpit.model.development.score;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DevelopmentScoreDOTest {
    private Long id;
    private Long buId;
    private String buName;
    /**
     * 项目交付周期
     */
    private BigDecimal score;
    private Date recordDate;
}
