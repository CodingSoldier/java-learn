package com.cpq.jpa01.service;

import com.cpq.jpa01.dao.Test01Dao;
import com.cpq.jpa01.model.Test01;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务-公司
 * </p>
 *
 * @author chenpq05
 * @since 2023-05-31 15:57:44
 */
@Service
@Slf4j
public class Test01Service {
  @Autowired
  private Test01Dao test01Dao;

  @Transactional(rollbackOn = Exception.class)
  public Test01 save(Test01 test01) {
    Test01 dbSave = test01Dao.save(test01);
    log.info("######{}", dbSave);
    return dbSave;
  }

  @Transactional
  public void test(Test01 test01) {
    int my = test01Dao.getMy();
    log.info("##############{}", my);

    List<Test01> all = test01Dao.findAll();
    log.info("#############all={}", all);

    List<Test01> list = test01Dao.findByCompanyCodeInAndCompanyNameLike(
        Arrays.asList("7112336ssfdf3334354545", "711233633646dfs3474688"), test01.getCompanyName());
    log.info("#############list={}", list);

    Test01 db = test01Dao.getById(test01.getId());

    test01Dao.save(test01);
    log.info("#############{}", test01);

    // page从0开始，跟limit一样
    PageRequest pageRequest = PageRequest.of(1, 2, Sort.by(Direction.DESC, "id"));
    Example<Test01> test01Example = Example.of(test01);
    Page<Test01> page = test01Dao.findAll(test01Example, pageRequest);


  }

}
