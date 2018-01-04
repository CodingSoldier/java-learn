package com.tree.treepath.mapper;

import com.tree.treepath.model.TreepathExpand;

import java.util.List;

public interface TreepathExpandMapper {
    List<TreepathExpand> queryByPath(String path);
}