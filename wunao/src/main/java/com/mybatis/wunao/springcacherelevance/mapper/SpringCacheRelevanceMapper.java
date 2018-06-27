package com.mybatis.wunao.springcacherelevance.mapper;

import com.mybatis.wunao.springcacherelevance.model.SpringCacheRelevance;
import com.mybatis.wunao.springcacherelevance.model.SpringCacheRelevanceExample;
import java.util.List;

public interface SpringCacheRelevanceMapper {
    long countByExample(SpringCacheRelevanceExample example);

    int deleteByExample(SpringCacheRelevanceExample example);

    int deleteByPrimaryKey(String id);

    int insert(SpringCacheRelevance record);

    int insertSelective(SpringCacheRelevance record);

    List<SpringCacheRelevance> selectByExample(SpringCacheRelevanceExample example);

    SpringCacheRelevance selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SpringCacheRelevance record);

    int updateByPrimaryKey(SpringCacheRelevance record);
}