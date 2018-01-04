package com.mybatis.mapper;

import com.mybatis.model.HrDepartment;
import com.mybatis.model.HrDepartmentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HrDepartmentMapper {
    long countByExample(HrDepartmentExample example);

    int deleteByExample(HrDepartmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(HrDepartment record);

    int insertSelective(HrDepartment record);

    List<HrDepartment> selectByExample(HrDepartmentExample example);

    HrDepartment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") HrDepartment record, @Param("example") HrDepartmentExample example);

    int updateByExample(@Param("record") HrDepartment record, @Param("example") HrDepartmentExample example);

    int updateByPrimaryKeySelective(HrDepartment record);

    int updateByPrimaryKey(HrDepartment record);
}