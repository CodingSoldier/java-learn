package com.cpq.jpa01.dao;

import com.cpq.jpa01.model.Test01;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author chenpq05
 * @since 2023/12/4 13:44
 */
public interface Test01Dao extends JpaRepository<Test01, Long> {



  Logger LOGGER = LoggerFactory.getLogger(Test01Dao.class);

  List<Test01> findByCompanyCodeInAndCompanyNameLike(List<String> companyCode, String companyName);

  default int getMy() {
    Test01 dbOne = this.getById(1L);
    LOGGER.info("^^^^^^^^^^^^^^^^^^^^^^{}", dbOne);
    return 1;
  }

}
