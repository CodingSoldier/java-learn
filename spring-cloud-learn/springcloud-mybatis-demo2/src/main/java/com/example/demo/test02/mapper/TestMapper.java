package com.example.demo.test02.mapper;

import com.example.demo.test02.entity.Test;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by lorne on 2017/6/28.
 */
@Mapper
public interface TestMapper {


    @Select("SELECT * FROM T_TEST2")
    List<Test> findAll();

    @Insert("INSERT INTO T_TEST2(id, NAME) VALUES(#{id},#{name})")
    int save(@Param("id") String id, @Param("name") String name);

}
