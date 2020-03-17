package com.example.cpq.hconfiguration02.a_import;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author chenpiqian
 * @date: 2020-03-17
 */
@Data
public class ImportBean {

    @Value("${user.id}")
    private Long id;

    @Value("${user.name}")
    private String name;

}
