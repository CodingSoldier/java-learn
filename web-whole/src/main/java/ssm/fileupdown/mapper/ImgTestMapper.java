package ssm.fileupdown.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import ssm.fileupdown.model.ImgTest;
import ssm.fileupdown.model.ImgTestExample;

public interface ImgTestMapper {
    long countByExample(ImgTestExample example);

    int deleteByExample(ImgTestExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ImgTest record);

    int insertSelective(ImgTest record);

    List<ImgTest> selectByExampleWithBLOBs(ImgTestExample example);

    List<ImgTest> selectByExample(ImgTestExample example);

    ImgTest selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ImgTest record, @Param("example") ImgTestExample example);

    int updateByExampleWithBLOBs(@Param("record") ImgTest record, @Param("example") ImgTestExample example);

    int updateByExample(@Param("record") ImgTest record, @Param("example") ImgTestExample example);

    int updateByPrimaryKeySelective(ImgTest record);

    int updateByPrimaryKeyWithBLOBs(ImgTest record);

    int updateByPrimaryKey(ImgTest record);
}