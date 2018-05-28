package com.demo.testgenerator.mapper;

import com.demo.testgenerator.model.TestGenerator;
import com.demo.testgenerator.model.TestGeneratorExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestGeneratorMapper {
    long countByExample(TestGeneratorExample example);

    int deleteByExample(TestGeneratorExample example);

    int deleteByPrimaryKey(String idTest);

    int insert(TestGenerator record);

    int insertSelective(TestGenerator record);

    List<TestGenerator> selectByExample(TestGeneratorExample example);

    TestGenerator selectByPrimaryKey(String idTest);

    int updateByExampleSelective(@Param("record") TestGenerator record, @Param("example") TestGeneratorExample example);

    int updateByExample(@Param("record") TestGenerator record, @Param("example") TestGeneratorExample example);

    int updateByPrimaryKeySelective(TestGenerator record);

    int updateByPrimaryKey(TestGenerator record);
}