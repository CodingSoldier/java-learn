package com.cpq.mybatispulslearn.t_ip_address.entity;

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
 * @since 2019-04-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TIpAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    private String ipStart;

    private String ipEnd;

    private Long ip10dStart;

    private Long ip10dEnd;

    private String address;


}
