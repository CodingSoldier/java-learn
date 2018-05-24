package com.tree.treepath.model;

import java.util.List;

public class TreepathExpand extends Treepath{
    private List<TreepathExpand> childrenList;

    public List<TreepathExpand> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<TreepathExpand> childrenList) {
        this.childrenList = childrenList;
    }
}
