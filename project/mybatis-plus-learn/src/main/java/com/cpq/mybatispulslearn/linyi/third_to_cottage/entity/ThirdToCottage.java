package com.cpq.mybatispulslearn.linyi.third_to_cottage.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
     * 社区id
     */
    @TableField("villageID")
    private Integer villageID;

    /**
     * 社区楼栋id
     */
    @TableField("buildingID")
    private Integer buildingID;

    /**
     * 社区房屋id
     */
    @TableField("cottageID")
    private Integer cottageID;

    /**
     * 第三方社区code
     */
    private String thirdVillageCode;

    /**
     * 第三方社区楼栋code
     */
    private String thirdBuildingCode;

    /**
     * 第三方社区房屋code
     */
    private String thirdCottageCode;

    /**
     * 第三方系统：1-零壹系统
     */
    private Integer source;


}
