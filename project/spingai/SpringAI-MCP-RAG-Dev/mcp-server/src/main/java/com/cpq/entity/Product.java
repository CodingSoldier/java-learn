package com.cpq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品表
 *
 * @author cpq
 * @since 2025-11-26 21:17:15
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "商品表", description = "商品表")
public class Product implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  @Schema(description = "商品的编号")
  private String productNumber;

  @Schema(description = "商品的名称")
  private String productName;

  @Schema(description = "商品的品牌")
  private String brand;

  @Schema(description = "商品的简介（可以为空）")
  private String description;

  @Schema(description = "商品的价格")
  private Double price;

  @Schema(description = "商品的库存数量")
  private Integer stock;

  @Schema(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）")
  private Integer status;

  @Schema(description = "更新时间")
  private LocalDateTime createdTime;

  @Schema(description = "更新时间")
  private LocalDateTime updatedTime;


}
