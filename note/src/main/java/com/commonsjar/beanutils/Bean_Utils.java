package com.commonsjar.beanutils;

import com.utils.BaseTest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * http://m.blog.csdn.net/u012426327/article/details/77744415
 * 第六十篇：commons-beanutils使用介绍
 */
public class Bean_Utils extends BaseTest {

    @Test
    public void T_MethodUtils() throws Exception{
        User user = new User();
        Method method = MethodUtils.getAccessibleMethod(User.class, "setName",String.class);
        method.invoke(user, "不能调用私有方法，没什么用");
        System.out.println(user.toString());

        MethodUtils.invokeExactMethod(user, "setName", "不能调用私有方法，没什么用");
        System.out.println(user.toString());
    }

    @Test
    public void T_BeanUtils() throws Exception{

        // 复制属性
        User user0 = new User();
        user0.setId("init");
        User user1 = new User();
        user1.setId("属性值会被覆盖");
        BeanUtils.copyProperties(user1, user0);
        System.out.println(user1);

        //拷贝map到bean，BeanUtils能转换类型
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id", 111);
        map1.put("birthday", new Date());
        BeanUtils.copyProperties(user0, map1);
        System.out.println(user0.toString());

        //拷贝一个bean
        //User user2 = (User) BeanUtils.cloneBean(map1);  报错
        User user3 = (User) BeanUtils.cloneBean(user1);
        user3.setId("拷贝bean后修改id属性，不影响被被拷贝的bean");
        System.out.println(user3);

        //获取bean描述，拷贝map到bean，BeanUtils能转换类型，map中多了一个key "class"
        Map<String, String> map2 = BeanUtils.describe(user0);
        System.out.println(map2.toString());

    }

}
