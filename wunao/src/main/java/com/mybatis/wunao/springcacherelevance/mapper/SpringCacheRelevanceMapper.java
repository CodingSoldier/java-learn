package com.mybatis.wunao.springcacherelevance.mapper;

import com.mybatis.wunao.springcacherelevance.model.SpringCacheRelevance;
import com.mybatis.wunao.springcacherelevance.model.SpringCacheRelevanceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SpringCacheRelevanceMapper {
    long countByExample(SpringCacheRelevanceExample example);

    int deleteByExample(SpringCacheRelevanceExample example);

    int deleteByPrimaryKey(String id);

    int insert(SpringCacheRelevance record);

    int insertSelective(SpringCacheRelevance record);

    List<SpringCacheRelevance> selectByExample(SpringCacheRelevanceExample example);

    SpringCacheRelevance selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SpringCacheRelevance record, @Param("example") SpringCacheRelevanceExample example);

    int updateByExample(@Param("record") SpringCacheRelevance record, @Param("example") SpringCacheRelevanceExample example);

    int updateByPrimaryKeySelective(SpringCacheRelevance record);

    int updateByPrimaryKey(SpringCacheRelevance record);
}