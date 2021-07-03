package com.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 数据实体类接口
 */
public interface TreeChildrenInterface<E> {

    /**
     * 获取ID
     *
     * @return
     */
    Serializable getId();

    /**
     * 获取父级ID
     *
     * @return
     */
    Serializable getParentId();

    /**
     * 赋值子类
     *
     * @param children
     * @return
     */
    void setChildren(List<E> children);

}
