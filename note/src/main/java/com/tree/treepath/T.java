package com.tree.treepath;

import com.tree.treepath.mapper.TreepathExpandMapper;
import com.tree.treepath.model.TreepathExpand;
import com.utils.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/9
 */
public class T extends BaseTest {
    @Autowired
    TreepathExpandMapper mapper;

    @Test
    public void test1() throws Exception{
        List<TreepathExpand> qList = mapper.queryByPath("0001");
        List<TreepathExpand> rList = recursion(null, qList);
        System.out.println(rList);
    }

    public List<TreepathExpand> recursion(Integer id, List<TreepathExpand> expands) throws Exception{
        List<TreepathExpand> rList = new ArrayList<TreepathExpand>();
        Iterator it = expands.iterator();
        while (it.hasNext()){
            TreepathExpand treepathExpand = (TreepathExpand)it.next();
            if (treepathExpand.getParentId() == id){
                rList.add(treepathExpand);
                it.remove();
            }
        }
        for(TreepathExpand expand:rList){
            expand.setChildrenList(recursion(expand.getId(), expands));
        }
        return rList;
    }
}
