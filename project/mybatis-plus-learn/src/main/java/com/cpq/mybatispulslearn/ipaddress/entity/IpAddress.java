package com.cpq.mybatispulslearn.ipaddress.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * ip网段对应地址
 * </p>
 *
 * @author chenpiqian
 * @since 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_ip_address")
public class IpAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ip起始地址
     */
    private String ipStart;

    /**
     * ip结束地址
     */
    private String ipEnd;

    /**
     * IP转10进制
     */
    private Long ip10dStart;

    /**
     * IP转10进制
     */
    private Long ip10dEnd;

    /**
     * 地址
     */
    private String address;


}
