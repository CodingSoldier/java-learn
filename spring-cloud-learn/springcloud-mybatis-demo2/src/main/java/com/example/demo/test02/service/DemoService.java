package com.example.demo.test02.service;

import com.example.demo.test02.entity.Test;

import java.util.List;

/**
 * Created by lorne on 2017/6/26.
 */
public interface DemoService  {

    List<Test> list();

    int save();

}
