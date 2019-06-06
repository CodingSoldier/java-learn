package com.cpq.mybatispulslearn.excel;

import org.apache.poi.hssf.usermodel.HSSFRow;

public interface OutPutExcelCallback<T> {
    void fillCellValue(HSSFRow row, Integer orderNum, T bean);
}
