package com.cpq.mybatispulslearn.sysrole.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author cpq
 * @since 2019-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private Integer sysType;

    private String remark;

    private Integer status;

    private LocalDateTime createTime;

    private String createUserId;

    private LocalDateTime updateTime;

    private String updateUserId;

    private String resources;

}
