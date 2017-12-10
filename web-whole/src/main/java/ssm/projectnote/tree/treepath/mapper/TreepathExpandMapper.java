package ssm.projectnote.tree.treepath.mapper;

import ssm.projectnote.tree.treepath.TreepathExpand;

import java.util.List;

public interface TreepathExpandMapper {
    List<TreepathExpand> queryByPath(String path);
}