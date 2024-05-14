package com.cpq.mybatispulslearn.linyi.third_to_cottage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 第三方系统、田丁后台的社区、房屋映射表
 * </p>
 *
 * @author cpq
 * @since 2019-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ThirdToCottage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方社区code
     */
    private String thirdVillageCode;

    /**
     * 第三方社区楼栋code
     */
    private String thirdBuildingCode;

}
