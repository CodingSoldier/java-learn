package com.cpq.mybatispulslearn.excel;

import java.util.LinkedList;

public class OutputExcelProperties {
    private String excelName; //Excel文件名
    private String sheetName;  //sheet名称
    private String titleTop;  //第一行、标题名
    private LinkedList<ColTitle> column;

    public OutputExcelProperties() {
    }

    public OutputExcelProperties(String excelName, String sheetName, String titleTop, LinkedList<ColTitle> column) {
        this.excelName = excelName;
        this.sheetName = sheetName;
        this.titleTop = titleTop;
        this.column = column;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getTitleTop() {
        return titleTop;
    }

    public void setTitleTop(String titleTop) {
        this.titleTop = titleTop;
    }

    public LinkedList<ColTitle> getColumn() {
        return column;
    }

    public void setColumn(LinkedList<ColTitle> column) {
        this.column = column;
    }

    public static class ColTitle{
        private String text;  //列标题名称
        private Integer width;  //列宽
        private Boolean isOrder;  //是否为序号

        public ColTitle() {
        }
        public ColTitle(String text) {
            this.text = text;
        }
        public ColTitle(String text, Integer width) {
            this.text = text;
            this.width = width;
        }

        public ColTitle(String text, Integer width, Boolean isOrder) {
            this.text = text;
            this.width = width;
            this.isOrder = isOrder;
        }

        public Boolean getOrder() {
            return isOrder;
        }

        public void setOrder(Boolean order) {
            isOrder = order;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }

}
