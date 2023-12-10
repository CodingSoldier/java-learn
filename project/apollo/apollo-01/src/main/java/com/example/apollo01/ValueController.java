package com.example.apollo01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenpq05
 * @since 2023/11/14 15:59
 */
@RestController
public class ValueController {

  @Value("${test.value01}")
  private String value01;

  @Value("${test9}")
  private String test9;

  @Value("${test.public1}")
  private String testpublic1;

  //@Value("${cluster-value01}")
  //private String clusterValue01;

  @GetMapping("/value01")
  public String value01() {
    System.out.println("#################value01="+value01);
    System.out.println("#################test9="+test9);
    System.out.println("#################testpublic1="+testpublic1);
    //System.out.println("#################clusterValue01="+clusterValue01);
    return "";
  }

}
