package com.hellobike.pmo.cockpit.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author renmengxiwb304
 * @date 2024/2/18
 */
@AllArgsConstructor
@Data
public class ExcelInfo {
    /**
     * 首行
     */
    private Integer beginRow;
    /**
     * 结束行
     */
    private Integer endRow;
    /**
     * 首列
     */
    private Integer beginColumn;
    /**
     * 结束列
     */
    private Integer endColumn;
    /**
     * 测试用例路径
     */
    private String path;
}
