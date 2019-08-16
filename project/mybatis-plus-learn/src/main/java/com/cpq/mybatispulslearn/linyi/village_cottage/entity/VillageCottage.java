package com.cpq.mybatispulslearn.linyi.village_cottage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 社区物业信息表
 * </p>
 *
 * @author cpq
 * @since 2019-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VillageCottage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "cottageID", type = IdType.AUTO)
    private Integer cottageID;

    /**
     * 旧库房屋ID
     */
    @TableField("FWID")
    private Integer fwid;

    /**
     * 楼栋ID
     */
    @TableField("buildingID")
    private Integer buildingID;

    /**
     * 楼栋单元名称
     */
    private String buildingUnitName;

    /**
     * 旧库楼栋ID
     */
    @TableField("LYID")
    private Integer lyid;

    /**
     * 单元号
     */
    private String unit;

    /**
     * 楼层号
     */
    private String floor;

    /**
     * 房间号
     */
    private String room;

    /**
     * 社区ID
     */
    @TableField("villageID")
    private Integer villageID;

    /**
     * 旧库物业ID
     */
    @TableField("WYID")
    private Integer wyid;

    /**
     * 物业位置
     */
    private String position;

    /**
     * 建筑面积
     */
    private String area;

    /**
     * 物业类型：1住宅，2商铺，3写字楼
     */
    private Integer type;

    /**
     * 业主/管理员姓名
     */
    private String ownername;

    /**
     * 业主/管理员电话
     */
    private String ownermobile;

    /**
     * 是否公开：1为公开，2为不公开
     */
    @TableField("isPublic")
    private Integer isPublic;

    /**
     * 是否删除：1未删除，2已删除
     */
    @TableField("isDel")
    private Integer isDel;

    /**
     * 关联用户数
     */
    @TableField("relateUsers")
    private Integer relateUsers;

    @TableField("createTime")
    private Integer createTime;

    @TableField("createBy")
    private String createBy;

    @TableField("updateTime")
    private Integer updateTime;

    @TableField("updateBy")
    private String updateBy;



}
