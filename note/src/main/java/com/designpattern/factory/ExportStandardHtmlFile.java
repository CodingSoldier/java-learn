package com.designpattern.factory;

public class ExportStandardHtmlFile implements ExportFile {

    @Override
    public boolean export(String data) {
        System.out.println("导出标准HTML文件");
        return true;
    }

}
