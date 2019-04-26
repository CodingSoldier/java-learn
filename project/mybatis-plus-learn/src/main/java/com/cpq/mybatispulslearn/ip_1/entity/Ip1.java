package com.cpq.mybatispulslearn.ip_1.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cpq
 * @since 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ip_1")
public class Ip1 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stringAll;


}
