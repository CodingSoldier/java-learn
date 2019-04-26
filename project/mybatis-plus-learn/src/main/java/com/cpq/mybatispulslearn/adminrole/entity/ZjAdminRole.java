package com.cpq.mybatispulslearn.adminrole.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ZjAdminRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,角色ID
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDetail;

    /**
     * 角色的权限
     */
    private String roleAccess;

    /**
     * 角色状态:1为启用，0为禁用
     */
    private Boolean roleStatus;

    /**
     * 添加时间
     */
    private Integer roleAddtime;

    /**
     * 是否删除：0为否，1为是
     */
    private Boolean roleIsdelete;

    /**
     * 登录短信验证0为开启1为关闭
     */
    private Boolean smsStatus;

    /**
     * 报表查看权限
     */
    private String reportAccess;


}
