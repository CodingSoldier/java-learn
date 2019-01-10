package com.cpq.mybatispulslearn.sysuser.entity;

import java.time.LocalDateTime;
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
 * @since 2019-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String loginId;

    private String name;

    private String password;

    private String keySign;

    private String areaCode;

    private String approveCode;

    private String department;

    private Integer status;

    private String roleId;

    private Integer sysType;

    private String cooperationMode;

    private String email;

    private String phone;

    private Boolean deleted;

    private String createUserId;

    private LocalDateTime createTime;

    private String updateUserId;

    private LocalDateTime updateTime;


}
