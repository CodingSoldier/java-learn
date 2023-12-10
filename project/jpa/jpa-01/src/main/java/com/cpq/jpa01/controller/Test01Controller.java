package com.cpq.jpa01.controller;

import com.cpq.jpa01.dao.Test01Dao;
import com.cpq.jpa01.model.Test01;
import com.cpq.jpa01.service.Test01Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenpq05
 * @since 2023/12/4 13:49
 */
@Slf4j
@RestController
@RequestMapping("/test01")
public class Test01Controller {

  @Autowired
  private Test01Service test01Service;
  @Autowired
  private Test01Dao test01Dao;
  @Autowired
  private TaskExecutor taskExecutor;


  @PostMapping("/add")
  public Test01 add(@RequestBody Test01 saveDTO) {
    Test01 dbSave = test01Service.save(saveDTO);
    return dbSave;
  }

  @PostMapping("/test")
  public Object test(@RequestBody Test01 test01) {
    taskExecutor.execute(() -> {
      test01Service.test(test01);
    });
    return "page";
  }

}
