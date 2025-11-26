package com.cpq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpq.entity.Product;
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author cpq
 * @since 2025-11-26 21:17:15
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
