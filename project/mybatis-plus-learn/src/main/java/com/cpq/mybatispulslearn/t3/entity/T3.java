package com.cpq.mybatispulslearn.t3.entity;

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
 * @since 2019-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class T3 implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Boolean age;

    private String info;

    private Float f1;


}
