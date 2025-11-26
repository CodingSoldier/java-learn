package com.cpq.mcp.tool;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cpq.entity.Product;
import com.cpq.enums.ListSortEnum;
import com.cpq.enums.PriceCompareEnum;
import com.cpq.mapper.ProductMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Component
@Slf4j
public class ProductTool {

    @Resource
    private ProductMapper productMapper;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductAdd implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @ToolParam(description = "商品的名称")
        private String productName;

        @ToolParam(description = "商品的品牌")
        private String brand;

        @ToolParam(description = "商品的简介（可以为空）", required = false)
        private String description;

        @ToolParam(description = "商品的价格")
        private Double price;

        @ToolParam(description = "商品的库存数量")
        private Integer stock;

        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）")
        private Integer status=1;

    }

    @Tool(description = "创建/新增商品")
    public String addProduct(ProductAdd productAdd) {
        log.info("========== 创建/新增商品 productAdd={} ==========", productAdd);
        Product product = new Product();
        BeanUtils.copyProperties(productAdd, product);
        product.setProductNumber(RandomStringUtils.randomNumeric(12));
        productMapper.insert(product);
        return "商品信息创建成功";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductUpdate implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @ToolParam(description = "商品的编号")
        private String productNumber;

        @ToolParam(description = "商品的名称", required = false)
        private String productName;

        @ToolParam(description = "商品的品牌", required = false)
        private String brand;

        @ToolParam(description = "商品的简介（可以为空）", required = false)
        private String description;

        @ToolParam(description = "商品的价格", required = false)
        private Double price;

        @ToolParam(description = "商品的库存数量", required = false)
        private Integer stock;

        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）", required = false)
        private Integer status;

    }

    @Tool(description = "根据商品编号修改商品信息")
    public String updateProduct(ProductUpdate productUpdate) {
        log.info("========== 修改商品 updateProduct={} ==========", productUpdate);
        Product product = new Product();
        BeanUtils.copyProperties(productUpdate, product);

        LambdaQueryWrapper<Product> lqwUp = Wrappers.lambdaQuery();
        lqwUp.eq(Product::getProductNumber, productUpdate.getProductNumber());
        productMapper.update(product, lqwUp);
        return "商品信息更新成功";
    }

    @Tool(description = "根据商品编号删除商品")
    public String deleteProduct(String productNumber) {
        log.info("========== 根据商品编号删除商品 productNumber={} ==========", productNumber);
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.eq(Product::getProductNumber, productNumber);
        productMapper.delete(lqw);
        return "商品信息删除成功";
    }

    @Tool(description = "把排序（正序/倒序）转换为对应的枚举")
    public ListSortEnum getSortEnum(String sort) {
        log.info("========== 调用getSortEnum()，sort={} ==========", sort);
        if (ListSortEnum.ASC.value.equals(sort)) {
            return ListSortEnum.ASC;
        }
        return ListSortEnum.DESC;
    }

    @Tool(description = "把商品价格的比较（大于/小于/大于等于/小于等于/高于/低于/不高于/不低于/等于）转换为对应的枚举")
    public PriceCompareEnum getPriceCompareEnum(String priceCompare) {
        log.info("========== getPriceCompareEnum() ，priceCompare={}==========", priceCompare);
        if (priceCompare.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN.value)) {
            return PriceCompareEnum.GREATER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LESS_THAN.value)) {
            return PriceCompareEnum.LESS_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO.value)) {
            return PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LESS_THAN_OR_EQUAL_TO.value)) {
            return PriceCompareEnum.LESS_THAN_OR_EQUAL_TO;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.HIGHER_THAN.value)) {
            return PriceCompareEnum.HIGHER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.LOWER_THAN.value)) {
            return PriceCompareEnum.LOWER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.NOT_HIGHER_THAN.value)) {
            return PriceCompareEnum.NOT_HIGHER_THAN;
        } else if (priceCompare.equalsIgnoreCase(PriceCompareEnum.NOT_LOWER_THAN.value)) {
            return PriceCompareEnum.NOT_LOWER_THAN;
        } else {
            return PriceCompareEnum.EQUAL_TO;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductQuery implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @ToolParam(description = "商品的编号", required = false)
        private String productNumber;

        @ToolParam(description = "商品的名称", required = false)
        private String productName;

        @ToolParam(description = "商品的品牌", required = false)
        private String brand;

        @ToolParam(description = "商品的价格", required = false)
        private Double price;

        @ToolParam(description = "查询列表的排序", required = false)
        private ListSortEnum sortEnum;

        @ToolParam(description = "比较价格大小", required = false)
        private PriceCompareEnum priceCompare;

    }

    @Tool(description = "根据条件查询商品（product）信息")
    public List<Product> queryProduct(ProductQuery productQuery) {
        log.info("========== 根据条件查询商品 productQuery={}==========", productQuery);
        LambdaQueryWrapper<Product> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(productQuery.getProductNumber()),
                Product::getProductNumber, productQuery.getProductNumber());
        lqw.like(StringUtils.isNotBlank(productQuery.getProductName()),
                Product::getProductName, productQuery.getProductName());
        lqw.like(StringUtils.isNotBlank(productQuery.getBrand()),
                Product::getBrand, productQuery.getBrand());

        Double price = productQuery.getPrice();
        PriceCompareEnum priceCompareEnum = productQuery.getPriceCompare();
        if (price != null && priceCompareEnum != null) {
            if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN.type)) {
                lqw.gt(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN.type)) {
                lqw.lt(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO.type)) {
                lqw.ge(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN_OR_EQUAL_TO.type)) {
                lqw.le(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.HIGHER_THAN.type)) {
                lqw.gt(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LOWER_THAN.type)) {
                lqw.lt(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_HIGHER_THAN.type)) {
                lqw.le(Product::getPrice, price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_LOWER_THAN.type)) {
                lqw.ge(Product::getPrice, price);
            } else {
                lqw.eq(Product::getPrice, price);
            }

            ListSortEnum sortEnum = productQuery.getSortEnum();
            if (sortEnum != null) {
                if (sortEnum.type.equals(ListSortEnum.ASC.type)) {
                    lqw.orderByAsc(Product::getPrice);
                } else {
                    lqw.orderByDesc(Product::getPrice);
                }
            }
        }

        return productMapper.selectList(lqw);
    }


}









