package com.demo.girl.mapper;

import com.demo.girl.model.Girl;
import com.demo.girl.model.GirlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GirlMapper {
    long countByExample(GirlExample example);

    int deleteByExample(GirlExample example);

    int deleteByPrimaryKey(String id);

    int insert(Girl record);

    int insertSelective(Girl record);

    List<Girl> selectByExample(GirlExample example);

    Girl selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Girl record, @Param("example") GirlExample example);

    int updateByExample(@Param("record") Girl record, @Param("example") GirlExample example);

    int updateByPrimaryKeySelective(Girl record);

    int updateByPrimaryKey(Girl record);
}