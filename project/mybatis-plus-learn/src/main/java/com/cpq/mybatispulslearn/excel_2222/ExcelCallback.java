package com.cpq.mybatispulslearn.excel_2222;

import org.apache.poi.hssf.usermodel.HSSFRow;

public interface ExcelCallback<T> {
    void fillCellValue(HSSFRow row, Integer orderNum, T bean);
}
