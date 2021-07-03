package com.example.yyghtestredis.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private List<Long> roleIds;

}
