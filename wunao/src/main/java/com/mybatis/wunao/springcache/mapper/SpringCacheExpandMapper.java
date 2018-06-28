package com.mybatis.wunao.springcache.mapper;

import com.mybatis.wunao.springcache.model.SpringCacheExpand;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpringCacheExpandMapper extends SpringCacheMapper{

    List<SpringCacheExpand> selectNameEqual(@Param("name") String name);

    List<Map<String, Object>> selectNameEqualMap(@Param("name") String name);
}













