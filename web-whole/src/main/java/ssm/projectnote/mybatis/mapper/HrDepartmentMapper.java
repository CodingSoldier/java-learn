package ssm.projectnote.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import ssm.projectnote.mybatis.model.HrDepartment;
import ssm.projectnote.mybatis.model.HrDepartmentExample;

import java.util.List;

public interface HrDepartmentMapper {
    long countByExample(HrDepartmentExample example);

    int deleteByExample(HrDepartmentExample example);

    int insert(HrDepartment record);

    int insertSelective(HrDepartment record);

    List<HrDepartment> selectByExample(HrDepartmentExample example);

    int updateByExampleSelective(@Param("record") HrDepartment record, @Param("example") HrDepartmentExample example);

    int updateByExample(@Param("record") HrDepartment record, @Param("example") HrDepartmentExample example);
}