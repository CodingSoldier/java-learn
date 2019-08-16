package com.cpq.mybatispulslearn.linyi.village_cottage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpq.mybatispulslearn.excel_2222.ExcelProperties;
import com.cpq.mybatispulslearn.excel_2222.ExcelUtil;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.entity.ThirdToCottage;
import com.cpq.mybatispulslearn.linyi.third_to_cottage.service.ThirdToCottageService;
import com.cpq.mybatispulslearn.linyi.village_cottage.entity.VillageCottage;
import com.cpq.mybatispulslearn.linyi.village_cottage.mapper.VillageCottageMapper;
import com.cpq.mybatispulslearn.linyi.village_cottage.service.VillageCottageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-07
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    VillageCottageService villageCottageService;
    @Autowired
    ThirdToCottageService third2CottageService;

    @Autowired
    VillageCottageMapper villageCottageMapper;

    LinkedList<ThirdToCottage> t2cList = new LinkedList<>();

    Map<String, String> ldMap = new HashMap(){{
        put("南座", "02");
        put("南座1", "02");
        put("北座", "04");
        put("北座1", "04");
        put("商铺", "05");
        put("商铺1", "05");
        put("多种经营", "06");
        put("多种经营1", "06");
        put("物业服务中心", "07");
    }};


    Map<String, String> ldzhMap = new HashMap(){{
        put("0200", "南座");
        put("0400", "北座");
        put("0500", "商铺");
        put("0600", "多种经营");
        put("0700", "物业服务中心");
    }};

    /**
     田丁后台 住户身份
     用户身份：1业主，2租客，3家人，4其他，5写字楼管理员，6写字楼职员

     零壹 住户身份
     1 租客  2 业主  3家人
     */
    Long getShenFen(String numstr){
        if (numstr == null || "null".equals(numstr))
            return 3L;
        Long num = Long.parseLong(numstr);
        if (num == 1L)
            return 2L;

        if (num == 2L)
            return 1L;

        return 3L;
    }

    String getRoom(String room){
        String r = "";

        String chart = StringUtils.substring(room, -1);
        if (chart.matches("[A-Z]")){
            int num = Integer.valueOf(chart.charAt(0)) - 64;
            r = String.valueOf(num);
        }else if (room.contains("裙楼")){
            r = StringUtils.substring(room, 3, 5);
        }else if ("物业办公室".equals(room)){
            r = "00";
        }else if (Pattern.compile("^\\d+$").matcher(room).matches()){
            r = StringUtils.substring(room, 1, 3);
        }
        return r.length() == 1 ? "0"+r : r;
    }



    // 新建映射表
    @Test
    public void mappingTable(){
        QueryWrapper qw = new QueryWrapper();
        qw.eq("villageID", 12);
        qw.eq("isDel", 1);

        // 排除 裙楼110-2
        qw.ne("cottageID", 5924);
        // 排除 裙楼（旧）
        qw.ne("cottageID", 5925);
        // 多种经营101-其他
        qw.ne("cottageID", 5934);
        List<VillageCottage> vcList =  villageCottageService.list(qw);

        for (VillageCottage vc:vcList){
            // 田丁后台与第三方项目 映射表
            ThirdToCottage third2Cottage = new ThirdToCottage();
            third2Cottage.setVillageID(vc.getVillageID());
            third2Cottage.setBuildingID(vc.getBuildingID());
            third2Cottage.setCottageID(vc.getCottageID());

            // 楼栋号
            String loudong = ldMap.get(vc.getBuildingUnitName());
            // 单元
            String danyuan = "00";
            // 实际楼层
            String loucheng = vc.getFloor().length() == 1 ? "0"+vc.getFloor() : vc.getFloor();
            // 室号
            String room = getRoom(vc.getRoom());

            third2Cottage.setThirdVillageCode("10450");
            third2Cottage.setThirdBuildingCode(loudong + danyuan);
            third2Cottage.setThirdCottageCode(loudong + danyuan + loucheng + room);
            third2Cottage.setSource(1);
            t2cList.add(third2Cottage);
        }

        third2CottageService.saveBatch(t2cList);
    }




    // 生成房屋excel
    @Test
    public void loudong01(){

        String excelName = "房屋-住户-全量";
        List<Map<String, String>> roomPersonList = villageCottageMapper.shengchan();

        //String excelName = "房屋-住户-有效";
        //List<Map<String, String>> roomPersonList = villageCottageMapper.allRoomPersonExist();

        // 一个房子一个业主
        Set<String> set = new HashSet<>();
        for (Map<String, String> map:roomPersonList){
            if (!set.contains(map.get("room"))){
                set.add(map.get("room"));
                //// identity为空设置为业主
                //if (StringUtils.isBlank(map.get("identity"))){
                //    map.put("identity", "1");
                //}
            }else if (set.contains(map.get("room")) && "1".equals(String.valueOf(map.get("identity")))){
                map.put("identity", "3");
            }
        }

        LinkedList<ExcelProperties.ColTitle> colTitles = new LinkedList(){{
            add(new ExcelProperties.ColTitle("*楼栋号"));
            add(new ExcelProperties.ColTitle("*单元号"));
            add(new ExcelProperties.ColTitle("*楼层"));
            add(new ExcelProperties.ColTitle("*室号"));
            add(new ExcelProperties.ColTitle("房号名称"));
            add(new ExcelProperties.ColTitle("*房屋类型"));
            add(new ExcelProperties.ColTitle("*面积"));

            add(new ExcelProperties.ColTitle("住户姓名"));
            add(new ExcelProperties.ColTitle("证件类型"));
            add(new ExcelProperties.ColTitle("证件号码"));
            add(new ExcelProperties.ColTitle("*手机号码"));
            add(new ExcelProperties.ColTitle("使用状态"));
            add(new ExcelProperties.ColTitle("*住户类型"));

        }};

        ExcelProperties ep = new ExcelProperties(excelName, "", "", colTitles);

        ExcelUtil.exportExcelFile(ep, roomPersonList, (HSSFRow row, Integer orderNum, Map<String, String> rp) -> {
            //回调中将数据添加到excel单元格中
            Integer cellIndex = 0;
            // 楼栋号
            String loudong = ldMap.get(rp.get("building_unit_name"));
            ExcelUtil.createCell(row, cellIndex++, loudong);
            // 单元
            String danyuan = "00";
            ExcelUtil.createCell(row, cellIndex++, danyuan);
            // 实际楼层

            String loucheng = String.valueOf(rp.get("floor"));
            loucheng = loucheng.length() == 1 ? "0"+loucheng : loucheng;
            ExcelUtil.createCell(row, cellIndex++, loucheng);
            // 室号
            String room = getRoom(rp.get("room"));
            ExcelUtil.createCell(row, cellIndex++, room);
            // 房号名称
            ExcelUtil.createCell(row, cellIndex++, rp.get("room"));
            // *房屋类型
            ExcelUtil.createCell(row, cellIndex++, 6);
            // *面积
            ExcelUtil.createCell(row, cellIndex++, Float.parseFloat(rp.get("area")) < 1 ? 1.1 : rp.get("area"));

            // 住户姓名
            String trueName = StringUtils.isNotBlank(rp.get("truename")) ? rp.get("truename") : rp.get("customer_name");
            ExcelUtil.createCell(row, cellIndex++, trueName);
            //证件类型	证件号码
            cellIndex = cellIndex + 2;
            // *手机号码
            String phone = rp.get("userMobile") != null ?
                    rp.get("userMobile") : rp.get("customer_cellphone");
            ExcelUtil.createCell(row, cellIndex++, phone);
            // 使用状态
            cellIndex++;
            // *住户类型
            ExcelUtil.createCell(row, cellIndex++, getShenFen(String.valueOf(rp.get("identity"))));

        });

    }





    // 生成住户excel
/*    @Test
    public void zhuhu01(){

        List<Map<String, String>> zhuhuList = villageCottageMapper.zhuhu();
        System.out.println("***********"+zhuhuList);

        // 一个房子一个业主
        Set<String> set = new HashSet<>();
        for (Map<String, String> map:zhuhuList){
            if (!set.contains(map.get("room"))){
                set.add(map.get("room"));
            }else if (set.contains(map.get("room")) && "1".equals(String.valueOf(map.get("identity")))){
               map.put("identity", "3");
            }
        }

        LinkedList<ExcelProperties.ColTitle> colTitles = new LinkedList(){{
            add(new ExcelProperties.ColTitle("*楼栋名称"));
            add(new ExcelProperties.ColTitle("*房屋名称"));
            add(new ExcelProperties.ColTitle("*身份类型"));
            add(new ExcelProperties.ColTitle("*住户姓名"));
            add(new ExcelProperties.ColTitle("证件类型"));
            add(new ExcelProperties.ColTitle("证件号码"));
            add(new ExcelProperties.ColTitle("手机号码"));
            add(new ExcelProperties.ColTitle("与业主关系"));
            add(new ExcelProperties.ColTitle("租赁到期日期"));
            add(new ExcelProperties.ColTitle("性别"));
            add(new ExcelProperties.ColTitle("民族"));
            add(new ExcelProperties.ColTitle("籍贯"));
            add(new ExcelProperties.ColTitle("出生日期"));
            add(new ExcelProperties.ColTitle("户籍地址"));
            add(new ExcelProperties.ColTitle("出生地址"));
            add(new ExcelProperties.ColTitle("工作单位"));
            add(new ExcelProperties.ColTitle("婚姻状况"));
            add(new ExcelProperties.ColTitle("政治面貌"));
            add(new ExcelProperties.ColTitle("学历"));
            add(new ExcelProperties.ColTitle("特殊人员类型"));
            add(new ExcelProperties.ColTitle("职业"));
            add(new ExcelProperties.ColTitle("部门"));
            add(new ExcelProperties.ColTitle("证件有效期至"));
            add(new ExcelProperties.ColTitle("英文名"));
            add(new ExcelProperties.ColTitle("英文姓"));
            add(new ExcelProperties.ColTitle("国籍"));
            add(new ExcelProperties.ColTitle("居住方式"));
            add(new ExcelProperties.ColTitle("入住日期"));
        }};

        ExcelProperties ep = new ExcelProperties("住户", "", "", colTitles);

        ExcelUtil.exportExcelFile(ep, zhuhuList, (HSSFRow row, Integer orderNum, Map<String, String> map) -> {
            //回调中将数据添加到excel单元格中
            Integer cellIndex = 0;
            if (orderNum != null){
                ExcelUtil.createCell(row, cellIndex++, orderNum);  //序号
            }
            // *楼栋名称
            ExcelUtil.createCell(row, cellIndex++, ldzhMap.get(map.get("third_building_code")));
            // *房屋名称
            ExcelUtil.createCell(row, cellIndex++, map.get("room"));
            // *身份类型  零壹 住户身份  1 租客  2 业主  3家人
            Long shenfen = getShenFen(String.valueOf(map.get("identity")));
            ExcelUtil.createCell(row, cellIndex++, shenfen);
            // *住户姓名
            ExcelUtil.createCell(row, cellIndex++, map.get("truename"));

            cellIndex = cellIndex + 2;

            // *住户姓名
            ExcelUtil.createCell(row, cellIndex++, map.get("userMobile"));

            // 与业主关系
            cellIndex++;

            if (shenfen == 1){
                // 租赁到期日期
                ExcelUtil.createCell(row, cellIndex++, "2024-09-01");
            }

        });

    }*/

}
