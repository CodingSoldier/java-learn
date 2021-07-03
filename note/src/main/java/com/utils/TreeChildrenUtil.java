package com.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 树工具类
 */
public class TreeChildrenUtil {

    /**
     * 获取树结构集合
     *
     * @param parentId
     * @param entityList
     * @return
     */
    public static <E extends TreeChildrenInterface<E>> List<E> getTreeList(Serializable parentId, List<E> entityList) {
        List<E> treeList = new ArrayList<>();
        Iterator it = entityList.iterator();
        while (it.hasNext()) {
            E elem = (E) it.next();
            if (objToStr(parentId).equals(objToStr(elem.getParentId()))) {
                treeList.add(elem);
                //使用Iterator，以便在迭代时把entityList中已经添加到treeList的数据删除，减少迭代次数
                it.remove();
            }
        }
        for (E elem : treeList) {
            elem.setChildren(getTreeList(elem.getId(), entityList));
        }
        return treeList;
    }

    public static String objToStr(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }

}