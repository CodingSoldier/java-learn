package com.designpattern.factory;

public class ExportFinancialHtmlFile implements ExportFile {

    @Override
    public boolean export(String data) {
        System.out.println("导出财务版本html文件");
        return true;
    }

}
