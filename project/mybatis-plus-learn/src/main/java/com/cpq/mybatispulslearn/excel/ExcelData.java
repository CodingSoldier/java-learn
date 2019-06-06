package com.cpq.mybatispulslearn.excel;

import lombok.Data;
import lombok.ToString;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-04
 */

@Data
@ToString
public class ExcelData {

    private String name;
    private String idCard;
    private String phone;
    private String idCardAddress;
    private String xiangMu;
    private String type;  //个人、企业
    private String filePath;



}
