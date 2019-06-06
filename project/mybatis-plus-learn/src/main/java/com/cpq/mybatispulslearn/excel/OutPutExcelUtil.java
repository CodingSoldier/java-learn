package com.cpq.mybatispulslearn.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by piqian.chen on 2018/7/3
 */
public class OutPutExcelUtil<T> {

    public static boolean hasOrder = false;

    //公共导出方法
    public static void createCell(HSSFRow row, Integer cellIndex, Object value){
        row.createCell(cellIndex++)
            .setCellValue(new HSSFRichTextString(String.valueOf(value)));
    }

    //创建excel
    public static <T> HSSFWorkbook createExcel(OutputExcelProperties properties, List<T> dataList, OutPutExcelCallback<T> callback){
        int rowIndex = 0;
        // 工作薄
        HSSFWorkbook wb = new HSSFWorkbook();

        // sheet
        String sheetName = StringUtils.isNotBlank(properties.getSheetName()) ? properties.getSheetName() : "sheet1";
        HSSFSheet sheet = wb.createSheet(sheetName);

        String titleTop = properties.getTitleTop();
        Integer columnNum = properties.getColumn() != null ? properties.getColumn().size() : 0;
        if (StringUtils.isNotBlank(titleTop)){
            //字体
            HSSFFont font = wb.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short) 16);//设置字体大小
            // 生成一个样式
            HSSFCellStyle styleCenter = wb.createCellStyle();
            styleCenter.setAlignment(HorizontalAlignment.CENTER);
            styleCenter.setFont(font);
            // 创建第一行
            sheet.addMergedRegion(new CellRangeAddress(0,0,0,columnNum-1));
            HSSFRow row = sheet.createRow(rowIndex++);
            // 给表头第一行创建单元格
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(new HSSFRichTextString(properties.getTitleTop()));
            cell.setCellStyle(styleCenter);
        }

        //第二行
        HSSFCellStyle style = wb.createCellStyle();
        HSSFFont font12 = wb.createFont();
        font12.setFontName("黑体");
        font12.setFontHeightInPoints((short) 12);//设置字体大小
        font12.setBold(true);
        style.setFont(font12);
        style.setAlignment(HorizontalAlignment.LEFT);

        LinkedList<OutputExcelProperties.ColTitle> column = properties.getColumn();
        Integer columnWidth = 50 * 256;
        Integer columnIndex = 0;
        if (column != null){
            HSSFRow row = sheet.createRow(rowIndex++);
            for (OutputExcelProperties.ColTitle colTitle : column){
                if (StringUtils.isNotBlank(colTitle.getText())){
                    //每行的标题
                    HSSFCell cell = row.createCell(columnIndex);
                    columnWidth = colTitle.getWidth() != null ? colTitle.getWidth() : 50 * 256;
                    sheet.setColumnWidth(columnIndex, columnWidth);
                    cell.setCellValue(colTitle.getText());
                    cell.setCellStyle(style);
                    columnIndex++;
                }
            }
        }

        HSSFCellStyle contextStyle = wb.createCellStyle();
        contextStyle.setAlignment(HorizontalAlignment.LEFT);
        hasOrder = column != null && column.size() > 0 && column.get(0).getOrder() != null && column.get(0).getOrder().equals(true) ? true : false;
        Integer orderIndex = 0;
        if (dataList != null && column != null){
            for (T bean:dataList){
                HSSFRow row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                if (hasOrder){
                    callback.fillCellValue(row, orderIndex++ , bean);
                }else {
                    callback.fillCellValue(row, null , bean);
                }
                columnIndex++;
            }
        }
        return wb;
    }
}
