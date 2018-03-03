package projectnote.tree.treepath;

import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/9
 */
public class TreepathExpand extends Treepath{
    private List<TreepathExpand> childrenList;

    public List<TreepathExpand> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<TreepathExpand> childrenList) {
        this.childrenList = childrenList;
    }
}
