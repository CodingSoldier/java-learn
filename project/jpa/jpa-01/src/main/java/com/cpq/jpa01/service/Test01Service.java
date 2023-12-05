package com.cpq.jpa01.service;

import com.cpq.jpa01.dao.Test01Dao;
import com.cpq.jpa01.model.Test01;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

}
