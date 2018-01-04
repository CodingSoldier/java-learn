package com.tree.treepath;

import com.tree.treepath.mapper.TreepathExpandMapper;
import com.tree.treepath.model.TreepathExpand;
import com.utils.BaseTest;
import com.utils.Utils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

        List<TreepathExpand> rList = new ArrayList<TreepathExpand>();

        for(TreepathExpand expand:qList){
            if(expand.getParentId() == null){
                rList.add(expand);
            }
        }

        for(TreepathExpand expand: rList){
           expand.setChildrenList(recursion(expand.getId(),qList));
        }

        System.out.println(rList);

    }

    public List<TreepathExpand> recursion(int id, List<TreepathExpand> expands) throws Exception{

        List<TreepathExpand> list = new ArrayList<TreepathExpand>();
        for(TreepathExpand expand:expands){
            if( Utils.numValEqual(id, expand.getParentId())){
                list.add(expand);
            }
        }
        for(TreepathExpand expand:list){
            expand.setChildrenList(recursion(expand.getId(), expands));
        }

        return list;
    }
}
