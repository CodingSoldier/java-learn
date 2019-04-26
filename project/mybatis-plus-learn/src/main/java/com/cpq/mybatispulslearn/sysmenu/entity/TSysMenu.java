package com.cpq.mybatispulslearn.sysmenu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * SASS功能权限清单表，管理员管理此菜单表
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String url;

    /**
     * 菜单级别
     */
    private Integer level;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 是否是根节点
     */
    private Boolean rootFlag;

    /**
     * 状态，0,：未激活 1：激活
     */
    private Integer status;

    private Integer createBy;

    private Date createTime;

    private Integer updateBy;

    private Date updateTime;

    /**
     * 菜单类型（0：运维后台菜单，1：门户菜单）
     */
    private Integer menuType;

    /**
     * 老系统数据,是否在权限表中显示
     */
    private Boolean menuIsshow;


}
