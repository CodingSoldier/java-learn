package com.example.multidatasource.shiro.users.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenpiqian
 * @since 2020-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_users")
public class Users implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    private String account;

    private String pwd;


}
