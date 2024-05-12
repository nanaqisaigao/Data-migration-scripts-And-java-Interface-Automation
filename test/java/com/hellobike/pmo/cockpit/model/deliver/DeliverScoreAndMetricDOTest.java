package com.hellobike.pmo.cockpit.model.deliver;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
public class DeliverScoreAndMetricDOTest {
    private Long id;
    private Long buId;
    private String buName;
    // 品质分，需要得到四个指标后再进行计算
    private BigDecimal score = BigDecimal.ZERO;;
    private BigDecimal onTimeCompletionRate;
    private BigDecimal projectChangeRate;
    private BigDecimal onTimeDeliveryRate;
    private BigDecimal requirementChangeRate;
    private Date recordDate;

    /**
     * 根据给定的分数和阈值计算等级.(按时结项和按时交付)
     *
     * @param score 输入的分数
     * @param threshold1 第一个阈值
     * @param threshold2 第二个阈值
     * @param threshold3 第三个阈值
     * @param threshold4 第四个阈值
     * @param threshold5 第五个阈值
     * @param threshold6 第六个阈值
     * @return 计算得出的等级
     */
    public static BigDecimal calculateGrade(BigDecimal score, int threshold1, int threshold2, int threshold3,
                                            int threshold4, int threshold5, int threshold6) {
        if (score.compareTo(BigDecimal.valueOf(threshold1)) < 0 ) {
            return BigDecimal.ZERO;
        } else if (score.compareTo(BigDecimal.valueOf(threshold1)) >= 0 && score.compareTo(BigDecimal.valueOf(threshold2)) < 0) {
            return score.subtract(BigDecimal.valueOf(threshold1)).divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
        } else if (score.compareTo(BigDecimal.valueOf(threshold2)) >= 0 && score.compareTo(BigDecimal.valueOf(threshold3)) < 0) {
            return score.subtract(BigDecimal.valueOf(threshold2)).divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(BigDecimal.ONE);
        } else if (score.compareTo(BigDecimal.valueOf(threshold3)) >= 0 && score.compareTo(BigDecimal.valueOf(threshold4)) < 0) {
            return score.subtract(BigDecimal.valueOf(threshold3)).divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(new BigDecimal(2));
        } else if (score.compareTo(BigDecimal.valueOf(threshold4)) >= 0 && score.compareTo(BigDecimal.valueOf(threshold5)) < 0) {
            return score.subtract(BigDecimal.valueOf(threshold4)).divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(new BigDecimal(3));
        } else if (score.compareTo(BigDecimal.valueOf(threshold5)) >= 0 && score.compareTo(BigDecimal.valueOf(threshold6)) < 0) {
            return score.subtract(BigDecimal.valueOf(threshold5)).divide(new BigDecimal(5), 2, RoundingMode.HALF_UP).add(new BigDecimal(4));
        } else {
            return new BigDecimal(5);
        }
    }

    /**
     * 根据给定的分数和阈值计算等级.(项目变更和需求变更)
     *
     * @param score 输入的分数
     * @param threshold1 第一个阈值
     * @param threshold2 第二个阈值
     * @param threshold3 第三个阈值
     * @param threshold4 第四个阈值
     * @param threshold5 第五个阈值
     * @param threshold6 第六个阈值
     * @return 计算得出的等级
     */
    public static BigDecimal calculateGrade2(BigDecimal score, int threshold1, int threshold2, int threshold3,
                                             int threshold4, int threshold5, int threshold6) {

        if (score.compareTo(BigDecimal.valueOf(threshold1)) > 0 ) {
            return BigDecimal.ZERO;
        } else if (score.compareTo(BigDecimal.valueOf(threshold1)) <= 0 && score.compareTo(BigDecimal.valueOf(threshold2)) > 0) {
            return score.subtract(BigDecimal.valueOf(threshold1)).abs().divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
        } else if (score.compareTo(BigDecimal.valueOf(threshold2)) <= 0 && score.compareTo(BigDecimal.valueOf(threshold3)) > 0) {
            return score.subtract(BigDecimal.valueOf(threshold2)).abs().divide(new BigDecimal(10), 2, RoundingMode.HALF_UP).add(BigDecimal.ONE);
        } else if (score.compareTo(BigDecimal.valueOf(threshold3)) <= 0 && score.compareTo(BigDecimal.valueOf(threshold4)) > 0) {
            return score.subtract(BigDecimal.valueOf(threshold3)).abs().divide(new BigDecimal(5), 2, RoundingMode.HALF_UP).add(new BigDecimal(2));
        } else if (score.compareTo(BigDecimal.valueOf(threshold4)) <= 0 && score.compareTo(BigDecimal.valueOf(threshold5)) > 0) {
            return score.subtract(BigDecimal.valueOf(threshold4)).abs().divide(new BigDecimal(5), 2, RoundingMode.HALF_UP).add(new BigDecimal(3));
        } else if (score.compareTo(BigDecimal.valueOf(threshold5)) <= 0 && score.compareTo(BigDecimal.valueOf(threshold6)) > 0) {
            return score.subtract(BigDecimal.valueOf(threshold5)).abs().divide(new BigDecimal(5), 2, RoundingMode.HALF_UP).add(new BigDecimal(4));
        } else {
            return new BigDecimal(5);
        }

    }

    public  BigDecimal calDeliverScore(){

        // BigDecimal onTimeCompletionRate = calculateGrade(this.onTimeCompletionRate,50,60,70,80,90,95);
        // BigDecimal projectChangeRate = calculateGrade2(this.projectChangeRate,40,30,20,15,10,5);
        // BigDecimal onTimeDeliveryRate = calculateGrade(this.onTimeDeliveryRate,50,60,70,80,90,95);
        // BigDecimal requirementChangeRate = calculateGrade2(this.requirementChangeRate,40,30,20,15,10,5);

        BigDecimal onTimeCompletionRate = this.onTimeCompletionRate != null ? calculateGrade(this.onTimeCompletionRate,50,60,70,80,90,95) : BigDecimal.ZERO;
        BigDecimal projectChangeRate = this.projectChangeRate != null ? calculateGrade2(this.projectChangeRate,40,30,20,15,10,5) : BigDecimal.ZERO;
        BigDecimal onTimeDeliveryRate = this.onTimeDeliveryRate != null ? calculateGrade(this.onTimeDeliveryRate,50,60,70,80,90,95) : BigDecimal.ZERO;
        BigDecimal requirementChangeRate = this.requirementChangeRate != null ? calculateGrade2(this.requirementChangeRate,40,30,20,15,10,5) : BigDecimal.ZERO;
        // System.out.println("onTimeCompletionRate->"+onTimeCompletionRate);
        // System.out.println("projectChangeRate->"+projectChangeRate);
        // System.out.println("onTimeDeliveryRate->"+onTimeDeliveryRate);
        // System.out.println("requirementChangeRate->"+requirementChangeRate);


        BigDecimal[] weights = {new BigDecimal("0.3"), new BigDecimal("0.2"), new BigDecimal("0.3"), new BigDecimal("0.2")};
        BigDecimal[] values = {onTimeCompletionRate, projectChangeRate, onTimeDeliveryRate, requirementChangeRate};

        for (int i = 0; i < values.length; i++) {
            BigDecimal product = values[i].multiply(weights[i]);
            this.score = this.score.add(product);
        }
        return this.getScore().setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
