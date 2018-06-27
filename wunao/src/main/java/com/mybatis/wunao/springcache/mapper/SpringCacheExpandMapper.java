package com.mybatis.wunao.springcache.mapper;

import com.mybatis.wunao.springcache.model.SpringCacheExpand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SpringCacheExpandMapper extends SpringCacheMapper{
    List<SpringCacheExpand> selectNameEqual(@Param("name") String name);
}