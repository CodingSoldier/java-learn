package com.cpq.jpa01.controller;

import com.cpq.jpa01.dao.Test01Dao;
import com.cpq.jpa01.model.Test01;
import com.cpq.jpa01.service.Test01Service;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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


  @PostMapping("/add")
  public Test01 add(@RequestBody Test01 saveDTO) {
    Test01 dbSave = test01Service.save(saveDTO);
    return dbSave;
  }

  @PostMapping("/test")
  public Object test(@RequestBody Test01 test01) {
    //List<Test01> all = test01Dao.findAll();
    //log.info("#############all={}", all);

    //List<Test01> list = test01Dao.findByCompanyCodeInAndCompanyNameLike(Arrays.asList("71123363334354545", "7112336336463474688"), test01.getCompanyName());
    //log.info("#############list={}", list);

    //Test01 db = test01Dao.getById(test01.getId());
    //
    //test01Dao.save(test01);
    //log.info("#############{}", test01);

    //// page从0开始，跟limit一样
    //PageRequest pageRequest = PageRequest.of(1, 2, Sort.by(Direction.DESC, "id"));
    //Example<Test01> test01Example = Example.of(test01);
    //Page<Test01> page = test01Dao.findAll(test01Example, pageRequest);

    int my = test01Dao.getMy();
    log.info("##############{}", my);
    return "page";
  }

}
