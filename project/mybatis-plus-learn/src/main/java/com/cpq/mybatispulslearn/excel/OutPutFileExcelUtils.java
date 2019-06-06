//package com.cpq.mybatispulslearn.excel;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
///**
// * Created by Administrator on 2018/5/14.
// */
//public class OutPutFileExcelUtils<T> {
//
//    public static <T> void exportExcel( OutputExcelProperties properties, List<T> dataList, OutPutExcelCallback<T> callback)
//         throws IOException{
//
//        HSSFWorkbook wb = OutPutExcelUtil.createExcel(properties, dataList, callback);
//        wb.write(new File("E:\\数据.xlsx"));
//    }
//
//
//}
