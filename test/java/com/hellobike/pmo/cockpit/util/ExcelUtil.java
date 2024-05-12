package com.hellobike.pmo.cockpit.util;


import com.hellobike.pmo.cockpit.model.common.ExcelInfo;
import org.apache.poi.ss.usermodel.*;

import java.io.File;

/**
 * @author renmengxiwb304
 * @date 2024/2/16
 */
public class ExcelUtil {

    public static Object[][] readFile(ExcelInfo excelInfo){
        Object[][] obj=null;
        try {
            //获取workbook对象
            Workbook workbook=WorkbookFactory.create(new File(excelInfo.getPath()));
            //获取Sheet对象
            Sheet sheet=workbook.getSheet("Sheet1");
            obj=new Object[excelInfo.getEndRow()- excelInfo.getBeginRow()+1][excelInfo.getEndColumn()- excelInfo.getBeginColumn()+1];
            for (int i =excelInfo.getBeginRow(); i <= excelInfo.getEndRow(); i++) {
                //获取行
                Row row=sheet.getRow(i);
                for (int j = excelInfo.getBeginColumn(); j <= excelInfo.getEndColumn(); j++) { // 修改此处的索引
                    //获取列，并指定单元格数据为空的策略，否则单元格为空时，会报空指针异常。
                    Cell cell=row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    //将列设置为字符串类型
                    cell.setCellType(CellType.STRING);
                    //获取单元格数据
                    String value=cell.getStringCellValue();
                    //将单元格数据存储到数组中
                    obj[i-excelInfo.getBeginRow()][j-excelInfo.getBeginColumn()]=value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}