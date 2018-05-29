package com.demo.boy.mapper;

import com.demo.boy.model.Boy;
import com.demo.boy.model.BoyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BoyMapper {
    long countByExample(BoyExample example);

    int deleteByExample(BoyExample example);

    int deleteByPrimaryKey(String id);

    int insert(Boy record);

    int insertSelective(Boy record);

    List<Boy> selectByExample(BoyExample example);

    Boy selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Boy record, @Param("example") BoyExample example);

    int updateByExample(@Param("record") Boy record, @Param("example") BoyExample example);

    int updateByPrimaryKeySelective(Boy record);

    int updateByPrimaryKey(Boy record);
}