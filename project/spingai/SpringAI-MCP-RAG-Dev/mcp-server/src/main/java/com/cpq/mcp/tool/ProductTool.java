package com.cpq.mcp.tool;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpq.entity.Product;
import com.cpq.enums.ListSortEnum;
import com.cpq.enums.PriceCompareEnum;
import com.cpq.mapper.ProductMapper;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Component
@Slf4j
public class ProductTool {

    @Resource
    private ProductMapper productMapper;

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateProductRequest {
        @ToolParam(description = "商品的名称")
        private String productName;
        @ToolParam(description = "商品的品牌")
        private String brand;
        @ToolParam(description = "商品的简介（可以为空）")
        private String description;

        @ToolParam(description = "商品的价格")
        private Integer price;
        @ToolParam(description = "商品的库存数量")
        private Integer stock;
        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）")
        private Integer status;
    }

    @Tool(description = "创建/新增商品信息记录")
    public String createNewProduct(CreateProductRequest createProductRequest) {

        log.info("========== 调用MCP工具：createNewProduct() ==========");
        log.info(String.format("| 参数 createProductRequest 为： %s", createProductRequest.toString()));
        log.info("========== End ==========");

        Product product = new Product();
        BeanUtils.copyProperties(createProductRequest, product);

        // 生成12为的随机数字
        product.setProductNumber(RandomStringUtils.randomNumeric(12));

        productMapper.insert(product);

        return "商品信息创建成功";
    }

    @Transactional
    @Tool(description = "根据商品id删除商品记录")
    public String deleteProduct(String productId) {

        log.info("========== 调用MCP工具：deleteProduct() ==========");
        log.info(String.format("| 参数 productId 为： %s", productId));
        log.info("========== End ==========");

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_number", productId);

        productMapper.delete(queryWrapper);

        return "商品信息删除成功";
    }


    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryProductRequest {

        // required = true 默认谁自动填充数据，所以查询的时候建议使用 false

        @ToolParam(description = "商品的编号", required = false)
        private String productId;
        @ToolParam(description = "商品的名称", required = false)
        private String productName;
        @ToolParam(description = "商品的品牌", required = false)
        private String brand;
        @ToolParam(description = "具体商品价格大小", required = false)
        private Integer price;

        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）", required = false)
        private Integer status;

        @ToolParam(description = "查询列表的排序", required = false)
        private ListSortEnum sortEnum;

        @ToolParam(description = "比较价格的大小", required = false)
        private PriceCompareEnum priceCompareEnum;
    }

    @Transactional
    @Tool(description = "把排序（正序/倒序）转换为对应的枚举")
    public ListSortEnum getSortEnum(String sort) {

        log.info("========== 调用MCP工具：getSortEnum() ==========");
        log.info(String.format("| 参数 sort 为： %s", sort));
        log.info("========== End ==========");

        if (sort.equalsIgnoreCase(ListSortEnum.ASC.value)) {
            return ListSortEnum.ASC;
        } else {
            return ListSortEnum.DESC;
        }

    }

    @Transactional
    @Tool(description = "把商品价格的比较（大于/小于/大于等于/小于等于/高于/低于/不高于/不低于/等于）转换为对应的枚举")
    public PriceCompareEnum getPriceCompareEnum(String priceCompare) {

        log.info("========== 调用MCP工具：getPriceCompareEnum() ==========");
        log.info(String.format("| 参数 priceCompare 为： %s", priceCompare));
        log.info("========== End ==========");

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

    @Tool(description = "根据条件查询商品（product）信息")
    public List<Product> queryProductListByCondition(QueryProductRequest queryProductRequest) {

        log.info("========== 调用MCP工具：queryProductListByCondition() ==========");
        log.info(String.format("| 参数 queryProductRequest 为： %s", queryProductRequest.toString()));
        log.info("========== End ==========");

        String productId = queryProductRequest.getProductId();
        String productName = queryProductRequest.getProductName();
        String brand = queryProductRequest.getBrand();

        Integer status = queryProductRequest.getStatus();
        ListSortEnum sortEnum = queryProductRequest.getSortEnum();

        Integer price = queryProductRequest.getPrice();
        PriceCompareEnum priceCompareEnum = queryProductRequest.getPriceCompareEnum();

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(productId)) {
            queryWrapper.eq("product_number", productId);
        }
        if (StringUtils.isNotBlank(productName)) {
            queryWrapper.like("product_name", productName);
        }
        if (StringUtils.isNotBlank(brand)) {
            queryWrapper.like("brand", brand);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        if (price != null && priceCompareEnum != null) {
            if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN.type)) {
                queryWrapper.gt("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN.type)) {
                queryWrapper.lt("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.GREATER_THAN_OR_EQUAL_TO.type)) {
                queryWrapper.ge("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LESS_THAN_OR_EQUAL_TO.type)) {
                queryWrapper.le("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.HIGHER_THAN.type)) {
                queryWrapper.gt("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.LOWER_THAN.type)) {
                queryWrapper.lt("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_HIGHER_THAN.type)) {
                queryWrapper.le("price", price);
            } else if (priceCompareEnum.type.equals(PriceCompareEnum.NOT_LOWER_THAN.type)) {
                queryWrapper.ge("price", price);
            } else {
                queryWrapper.eq("price", price);
            }
        }

        if (sortEnum != null && sortEnum.type.equals(ListSortEnum.ASC.type)) {
            queryWrapper.orderByAsc("price");
        }
        if (sortEnum != null && sortEnum.type.equals(ListSortEnum.DESC.type)) {
            queryWrapper.orderByDesc("price");
        }

        List<Product> productList = productMapper.selectList(queryWrapper);

        return productList;
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyProductRequest {

        @ToolParam(description = "商品的编号", required = false)
        private String productId;
        @ToolParam(description = "商品的名称", required = false)
        private String productName;
        @ToolParam(description = "商品的品牌", required = false)
        private String brand;
        @ToolParam(description = "商品的简介", required = false)
        private String description;
        @ToolParam(description = "具体商品价格大小", required = false)
        private Integer price;
        @ToolParam(description = "商品的库存数量", required = false)
        private Integer stock;
        @ToolParam(description = "商品的状态（上架状态的值为1/下架状态的值为0/预售状态的值为2）", required = false)
        private Integer status;

    }

    @Tool(description = "根据商品的ID/编号修改商品信息")
    public String modifyProduct(ModifyProductRequest modifyProductRequest) {

        log.info("========== 调用MCP工具：modifyProduct() ==========");
        log.info(String.format("| 参数 modifyProductRequest 为： %s", modifyProductRequest.toString()));
        log.info("========== End ==========");

        Product product = new Product();
        BeanUtils.copyProperties(modifyProductRequest, product);

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_number", modifyProductRequest.getProductId());

        int update = productMapper.update(product, queryWrapper);
        if (update <= 0) {
            return "商品信息更新失败，或商品可能不存在";
        }

        return "商品信息更新成功";
    }


}
