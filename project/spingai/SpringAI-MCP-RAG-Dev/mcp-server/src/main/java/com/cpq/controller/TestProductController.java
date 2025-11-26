package com.cpq.controller;

import com.cpq.entity.Product;
import com.cpq.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 商品表 前端控制器
 *
 * @author cpq
 * @since 2025-11-26 21:17:15
 */
@Tag(name = "商品表-API")
@Slf4j
@RestController
@RequestMapping("/test-product")
public class TestProductController {

  @Autowired
  private ProductMapper productMapper;

  @Operation(summary = "新增", description = "返回id")
  @PostMapping("/add")
  public Object add(@RequestBody Product addDTO) {
    int insert = productMapper.insert(addDTO);
    return insert;
  }

  @Operation(summary = "详情")
  @GetMapping("/detail")
  public Object detail(@RequestParam("id") @Parameter(description = "主键") Long id) {
    Product product = productMapper.selectById(id);
    return product;
  }


}
