package com.cpq.mybatispulslearn.tenantmember2role.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 成员角色映射, 2000自增
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TTenantMember2role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户成员id
     */
    private Long tenantMemberId;

    /**
     * 租户角色id
     */
    private Long tenantRoleId;


}
