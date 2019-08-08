package com.cpq.mybatispulslearn.saas.tenantrole.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租户角色表，用来控制用户的数据查看范围，如管理员，普通员工，2000自增
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TTenantRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色类型：0初始化时候的管理员
     */
    private Integer roleType;

    /**
     * 所属租户编码
     */
    private String tenantCode;

    /**
     * 描述
     */
    private String comments;

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

    /**
     * 老系统数据，登录短信验证0为开启1为关闭
     */
    private Boolean smsStatus;

    /**
     * 老系统数据,角色id
     */
    private Integer linkRoleId;


}
