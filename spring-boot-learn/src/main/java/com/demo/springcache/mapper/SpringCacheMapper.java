package com.demo.springcache.mapper;

import com.demo.springcache.model.SpringCache;
import com.demo.springcache.model.SpringCacheExample;
import java.util.List;

public interface SpringCacheMapper {
    long countByExample(SpringCacheExample example);

    int deleteByExample(SpringCacheExample example);

    int deleteByPrimaryKey(String id);

    int insert(SpringCache record);

    int insertSelective(SpringCache record);

    List<SpringCache> selectByExample(SpringCacheExample example);

    SpringCache selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SpringCache record);

    int updateByPrimaryKey(SpringCache record);
}