package com.cpq.mybatispulslearn.sysuserextend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TSysUserExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * sys_user表id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 密码盐
     */
    private String encrypt;

    /**
     * 是否为超级管理员
     */
    private Boolean issuper;

    /**
     * 所属角色
     */
    private Integer roleId;

    /**
     * 组织机构权限
     */
    private String organizationIds;

    /**
     * 登录时间
     */
    private Integer adminLasttime;

    /**
     * 上次登录的IP
     */
    private String adminLastip;

    /**
     * 登录次数
     */
    private Integer adminLoginnum;

    /**
     * 登录短信验证0为开启1为关闭
     */
    private Boolean smsStatus;

    private String aisinoName;

    /**
     * 物业公司ID
     */
    private Integer companyId;

    /**
     * 是否已加密（0未加密，1已加密）
     */
    private Boolean encryptFlag;


}
