package com.example.thingdemo.cache;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author chenpq
 * @since 2023-05-04 14:08:27
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ThingProfileCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "版本")
    private String version;

}


