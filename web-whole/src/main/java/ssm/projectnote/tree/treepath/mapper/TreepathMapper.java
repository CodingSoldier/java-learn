package ssm.projectnote.tree.treepath.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import ssm.projectnote.tree.treepath.Treepath;
import ssm.projectnote.tree.treepath.TreepathExample;

public interface TreepathMapper {
    long countByExample(TreepathExample example);

    int deleteByExample(TreepathExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Treepath record);

    int insertSelective(Treepath record);

    List<Treepath> selectByExample(TreepathExample example);

    Treepath selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Treepath record, @Param("example") TreepathExample example);

    int updateByExample(@Param("record") Treepath record, @Param("example") TreepathExample example);

    int updateByPrimaryKeySelective(Treepath record);

    int updateByPrimaryKey(Treepath record);
}