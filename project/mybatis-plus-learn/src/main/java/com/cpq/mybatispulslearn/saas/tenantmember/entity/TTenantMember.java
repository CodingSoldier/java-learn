package com.cpq.mybatispulslearn.saas.tenantmember.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户成员表
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TTenantMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 激活状态 0禁用 1已激活(默认)
     */
    private Integer status;

    /**
     * 逻辑删除 0未删除（默认）1已删除
     */
    private Boolean deleted;

    /**
     * t_sys_user用户id
     */
    private Long userId;

    /**
     * 用户邮箱
     */
    private String personalEmail;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 所属部门id
     */
    private Long departId;

    /**
     * 员工编码
     */
    private String employeeCode;

    /**
     * 性别 0女1男(默认)
     */
    private Integer gender;

    /**
     * 所属租户编码
     */
    private String tenantCode;

    /**
     * 职位
     */
    private String positionName;

    /**
     * 直属上级
     */
    private String managerId;

    /**
     * 备注
     */
    private String comments;

    /**
     * 0管理员、1普通成员
     */
    private Integer type;

    private Long createBy;

    /**
     * 创建人账号
     */
    private String createByName;

    private Date createTime;

    private Long updateBy;

    /**
     * 更新人账号
     */
    private String updateByName;

    private Date updateTime;


}
