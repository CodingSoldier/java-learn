package com.demo.springcache.mapper;

import com.demo.springcache.model.SpringCache;
import com.demo.springcache.model.SpringCacheExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpringCacheMapper {
    long countByExample(SpringCacheExample example);

    int deleteByExample(SpringCacheExample example);

    int deleteByPrimaryKey(String id);

    int insert(SpringCache record);

    int insertSelective(SpringCache record);

    List<SpringCache> selectByExample(SpringCacheExample example);

    SpringCache selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SpringCache record, @Param("example") SpringCacheExample example);

    int updateByExample(@Param("record") SpringCache record, @Param("example") SpringCacheExample example);

    int updateByPrimaryKeySelective(SpringCache record);

    int updateByPrimaryKey(SpringCache record);
}