package com.cpq.mybatispulslearn.excel;

import org.apache.commons.collections4.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-04
 */
public class ShouJiImport {

    // 数据
    public static TreeMap<String, List<ExcelData>> shouJiMap = new TreeMap<>();
    public static TreeMap<String, Object> shouJiWanShan = new TreeMap<>();

    public static TreeMap<String, List<ExcelData>> zaiKuJiMap = new TreeMap<>();

    // 数据
    public static List<Map<String, String>> dataList = new ArrayList<>();

    public static void main(String args[]) throws Exception {

        ////收集数据
        //ArrayList<String> shoujiFileList = getDirFileList("E:\\excel_shouji");
        //for (String filePath : shoujiFileList) {
        //    sjExcelData(filePath);
        //}
        //System.out.println("******************收集数据********************");
        //for (Map.Entry<String, List<ExcelData>> entry: shouJiMap.entrySet()){
        //    System.out.println("\n");
        //    System.out.println("所属项目："+entry.getKey());
        //    System.out.println("个人客户[客户姓名][所属项目]都不为空 + 企业客户[企业名称][所属项目]都不为空  的数量"+entry.getValue().size());
        //}



        //在库数据
        TreeList<String> zaikuFileList = getDirFileList("E:\\收费系统客户信息_2019.4.24");
        for (String filePath : zaikuFileList) {
            zkExcelData(filePath);
        }
        //System.out.println("\n\n******************在库数据********************");
        //for (Map.Entry<String, List<ExcelData>> entry: zaiKuJiMap.entrySet()){
        //    System.out.println("\n");
        //    System.out.println("所属项目："+entry.getKey());
        //    System.out.println("个人客户[客户姓名][所属项目]都不为空 + 企业客户[企业名称][所属项目]都不为空  的数量"+entry.getValue().size());
        //    //printWanZhengLu(entry.getValue());
        //}


    }

    public static void zkExcelData(String filePath) throws Exception {
        System.out.println("\n");
        System.out.println(filePath);
        int count = 0;
        int num = 0;

        Workbook wookbook = WorkbookFactory.create(new FileInputStream(filePath));

        Sheet geRenSheet = wookbook.getSheet("个人客户");
        Sheet qiYeSheet = wookbook.getSheet("企业客户");

        // 个人客户
        if (geRenSheet != null){
            //获取到Excel文件中的所有行数
            int rows = geRenSheet.getPhysicalNumberOfRows();
            count = count + rows-1;
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格
                Row row = geRenSheet.getRow(i);
                if (row != null) {
                    if ("客户姓名".equals(getCellValue(row.getCell(1)))) {
                        continue;
                    }

                    //姓名
                    Cell nameCell = row.getCell(1);
                    String  name = getCellValue(nameCell);
                    //证件号码
                    Cell idCardCell = row.getCell(2);
                    String idCard = getCellValue(idCardCell);
                    //手机号码
                    Cell phoneCell = row.getCell(3);
                    String phone = getCellValue(phoneCell);
                    //身份证地址
                    Cell idCardAddressCell = row.getCell(4);
                    String idCardAddress = getCellValue(idCardAddressCell);

                    ////所属项目
                    //Cell xiangMuCell = row.getCell(5);
                    //String xiangMu = getCellValue(xiangMuCell);

                    if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(idCardAddress) ){
                        num++;
                    }
                    //
                    //if (StringUtils.isNotBlank(phone) && (StringUtils.isNotBlank(idCard) || StringUtils.isNotBlank(idCardAddress)) ){
                    //    num++;
                    //}
                }
            }
        }

        //企业客户
        if (qiYeSheet != null){
            int rowsQiYe = qiYeSheet.getPhysicalNumberOfRows();
            count = count + rowsQiYe-1;
            for (int i = 1; i < rowsQiYe; i++) {
                Row row = qiYeSheet.getRow(i);
                if (row != null){
                    //企业名称
                    Cell nameCell = row.getCell(1);
                    String name = getCellValue(nameCell);

                    Cell yignyeCell = row.getCell(2);
                    String yignye  = getCellValue(yignyeCell);

                    Cell dianhuaCell = row.getCell(4);
                    String dianhau = getCellValue(dianhuaCell);

                    if (StringUtils.isNotBlank(yignye) && StringUtils.isNotBlank(dianhau)){
                        num++;
                    }
                }
            }
        }
        System.out.println("总数 "+count);
        System.out.println("有效 "+num);
    }





    public static void sjExcelData(String filePath) throws Exception {
        //数据
        List<ExcelData> list = new ArrayList<>();

        Workbook wookbook = WorkbookFactory.create(new FileInputStream(filePath));

        Sheet geRenSheet = wookbook.getSheet("个人客户") != null
                ? wookbook.getSheet("个人客户") : wookbook.getSheet("个人(业主)客户") != null
                ? wookbook.getSheet("个人(业主)客户") : wookbook.getSheet("学府小区");
        Sheet qiYeSheet = wookbook.getSheet("企业客户");

        if (wookbook.getNumberOfSheets() != 1 && (geRenSheet == null || qiYeSheet == null)){
            System.out.println("无法统计的表格："+filePath);
        }

        if (filePath.contains("南昌现代名门客户信息.xlsx")){
            sjNanChangXianDai(geRenSheet, qiYeSheet, filePath);
            return;
        }

        // 个人客户
        if (geRenSheet != null){
            //获取到Excel文件中的所有行数
            int rows = geRenSheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格
                Row row = geRenSheet.getRow(i);
                if (row != null) {
                    if ("客户姓名".equals(getCellValue(row.getCell(1)))) {
                        continue;
                    }

                    //姓名
                    Cell nameCell = row.getCell(1);
                    String  name = getCellValue(nameCell);
                    //证件号码
                    Cell idCardCell = row.getCell(2);
                    String idCard = getCellValue(idCardCell);
                    //手机号码
                    Cell phoneCell = row.getCell(3);
                    String phone = getCellValue(phoneCell);
                    //身份证地址
                    Cell idCardAddressCell = row.getCell(4);
                    String idCardAddress = getCellValue(idCardAddressCell);
                    //所属项目
                    Cell xiangMuCell = row.getCell(5);
                    String xiangMu = getCellValue(xiangMuCell);

                    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(xiangMu)) {
                        ExcelData excelData = new ExcelData();
                        excelData.setName(name);
                        excelData.setIdCard(idCard);
                        excelData.setPhone(phone);
                        excelData.setIdCardAddress(idCardAddress);
                        excelData.setXiangMu(xiangMu);
                        excelData.setType("个人");
                        excelData.setFilePath(filePath);
                        if (shouJiMap.get(xiangMu) != null){
                            shouJiMap.get(xiangMu).add(excelData);
                        }else {
                            ArrayList<ExcelData> excelDataArrayList = new ArrayList<>();
                            excelDataArrayList.add(excelData);
                            shouJiMap.put(xiangMu, excelDataArrayList);
                        }
                    }
                }
            }
        }

        //企业客户
        if (qiYeSheet != null){
            int rowsQiYe = qiYeSheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowsQiYe; i++) {
                Row row = qiYeSheet.getRow(i);
                if (row != null){
                    //企业名称
                    Cell nameCell = row.getCell(1);
                    String name = getCellValue(nameCell);
                    //所属项目
                    Cell xiangMuCell = row.getCell(6);
                    String xiangMu = getCellValue(xiangMuCell);

                    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(xiangMu)){
                        ExcelData excelData = new ExcelData();
                        excelData.setName(name);
                        excelData.setXiangMu(xiangMu);
                        excelData.setType("企业");
                        excelData.setFilePath(filePath);
                        if (shouJiMap.get(xiangMu) != null){
                            shouJiMap.get(xiangMu).add(excelData);
                        }else {
                            ArrayList<ExcelData> excelDataArrayList = new ArrayList<>();
                            excelDataArrayList.add(excelData);
                            shouJiMap.put(xiangMu, excelDataArrayList);
                        }
                    }
                }
            }
        }

    }

    public static void sjNanChangXianDai(Sheet geRenSheet, Sheet qiYeSheet, String filePath){
        // 个人客户
        if (geRenSheet != null){
            //获取到Excel文件中的所有行数
            int rows = geRenSheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rows; i++) {
                // 读取左上端单元格
                Row row = geRenSheet.getRow(i);
                if (row != null) {
                    if ("客户姓名".equals(getCellValue(row.getCell(3)))) {
                        continue;
                    }

                    //姓名
                    Cell nameCell = row.getCell(3);
                    String  name = getCellValue(nameCell);
                    //证件号码
                    Cell idCardCell = row.getCell(5);
                    String idCard = getCellValue(idCardCell);
                    //手机号码
                    Cell phoneCell = row.getCell(7);
                    String phone = getCellValue(phoneCell);
                    //身份证地址
                    Cell idCardAddressCell = row.getCell(6);
                    String idCardAddress = getCellValue(idCardAddressCell);
                    //所属项目
                    //Cell xiangMuCell = row.getCell("南昌现代名门");
                    String xiangMu = "南昌现代名门";

                    if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(xiangMu)) {
                        ExcelData excelData = new ExcelData();
                        excelData.setName(name);
                        excelData.setIdCard(idCard);
                        excelData.setPhone(phone);
                        excelData.setIdCardAddress(idCardAddress);
                        excelData.setXiangMu(xiangMu);
                        excelData.setType("个人");
                        excelData.setFilePath(filePath);
                        if (shouJiMap.get(xiangMu) != null){
                            shouJiMap.get(xiangMu).add(excelData);
                        }else {
                            ArrayList<ExcelData> excelDataArrayList = new ArrayList<>();
                            excelDataArrayList.add(excelData);
                            shouJiMap.put(xiangMu, excelDataArrayList);
                        }
                    }
                }
            }
        }
    }



    public static TreeList<String> getDirFileList(String dir) {
        TreeList<String> pathList = new TreeList<>();
        File fileDir = new File(dir);
        File[] fileList = fileDir.listFiles();
        for (File file : fileList) {
            String filePath = file.getAbsolutePath();
            pathList.add(filePath);
        }
        pathList.sort((o1, o2)->(o1.compareTo(o2)));
        return pathList;
    }

    //获取单元格的值
    public static String getCellValue(Cell cellObj) {
        if (null == cellObj) {
            return "";
        }
        if (CellType.BOOLEAN.equals(cellObj.getCellType())) {
            return StringUtils.trim(String.valueOf(cellObj.getBooleanCellValue()));
        } else if (CellType.NUMERIC.equals(cellObj.getCellType())) {
            return StringUtils.trim(String.valueOf(cellObj.getNumericCellValue()));
        } else {
            return StringUtils.trim(cellObj.getStringCellValue());
        }
    }

    //打印完整率
    public static void printWanZhengLu(TreeMap<String, List<ExcelData>> shouJiMap, TreeMap<String, List<ExcelData>> zaiKuJiMap){
        List<ExcelData> geRenAllData = new ArrayList<>();
        List<ExcelData> geRenActiveData = new ArrayList<>();
        List<ExcelData> qiYeAllData = new ArrayList<>();
        List<ExcelData> qiYeActiveAllData = new ArrayList<>();

        List<ExcelData> activeAllData = new ArrayList<>();

        //for (Map.Entry<String, List<ExcelData>> entryZaiKu:zaiKuJiMap.entrySet()){
        //    for (Map.Entry<String, List<ExcelData>> entryShouJi:shouJiMap.entrySet()){
        //        if (entryZaiKu.getKey().contains(entryShouJi.getKey())){
        //            for (ExcelData zaiKuExcelData:entryZaiKu.getValue()){
        //
        //            }
        //        }
        //    }
        //}

        //收集
        for (Map.Entry<String, List<ExcelData>> entryShouJi:shouJiMap.entrySet()){
            //在库
            for (Map.Entry<String, List<ExcelData>> entryZaiKu:zaiKuJiMap.entrySet()){
                if (entryZaiKu.getKey().contains(entryShouJi.getKey())){
                    int num = 0;
                    //在库
                    for (ExcelData zaiKuExcelData:entryZaiKu.getValue()){
                        //收集
                        for (ExcelData shouJiExcelData:entryZaiKu.getValue()){
                            if (zaiKuExcelData.getName().equals(shouJiExcelData.getName())){
                                if (StringUtils.isBlank(zaiKuExcelData.getPhone()) && StringUtils.isNotBlank(shouJiExcelData.getPhone())
                                        && !"无".equals(shouJiExcelData.getPhone())){
                                    num++;
                                }else if (StringUtils.isBlank(zaiKuExcelData.getIdCard()) && StringUtils.isNotBlank(shouJiExcelData.getIdCard())
                                        && !"无".equals(shouJiExcelData.getIdCard())){
                                    num++;
                                }else if (StringUtils.isBlank(zaiKuExcelData.getIdCardAddress()) && StringUtils.isNotBlank(shouJiExcelData.getIdCardAddress())
                                        && !"无".equals(shouJiExcelData.getIdCardAddress())){
                                    num++;
                                }
                            }
                        }
                    }
                    shouJiWanShan.put(entryZaiKu.getKey(), num);
                }
            }
        }

        //for (ExcelData excelData:excelDataList){
        //    if ("个人".equals(excelData.getType())){
        //        geRenAllData.add(excelData);
        //        if (StringUtils.isNotBlank(excelData.getPhone())){
        //            geRenActiveData.add(excelData);
        //            activeAllData.add(excelData);
        //        }
        //    }else if ("企业".equals(excelData.getType())){
        //        qiYeAllData.add(excelData);
        //        if (StringUtils.isNotBlank(excelData.getPhone())){
        //            qiYeActiveAllData.add(excelData);
        //            activeAllData.add(excelData);
        //        }
        //    }
        //}
        //if (geRenAllData.size() > 0){
        //    System.out.println("个人客户，有手机号/个人客户 "+geRenActiveData.size()+"/"+geRenAllData.size()+"="+new DecimalFormat("0.0%").format((float)geRenActiveData.size()/geRenAllData.size()));
        //}
        //
        //if (qiYeAllData.size() > 0){
        //    System.out.println("企业客户，有手机号/企业客户 "+qiYeActiveAllData.size()+"/"+qiYeAllData.size()+"="+new DecimalFormat("0.0%").format((float)qiYeActiveAllData.size()/qiYeAllData.size()));
        //}
        //
        //if (excelDataList.size() > 0){
        //    System.out.println("汇总，有手机号/客户 "+activeAllData.size()+"/"+excelDataList.size()+"="+new DecimalFormat("0.0%").format((float)activeAllData.size()/excelDataList.size()));
        //}
    }

}


