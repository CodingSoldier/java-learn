package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.demo.old.boy.mapper.BoyExpandMapper;
import com.demo.old.boy.mapper.BoyMapper;
import com.demo.old.boy.model.Boy;
import com.demo.old.boy.model.BoyExample;
import com.demo.config.AppProp;
import com.demo.old.girl.mapper.GirlExpandMapper;
import com.demo.old.girl.mapper.GirlMapper;
import com.demo.paramsvalidate.ValidateUtils;
import com.demo.old.sysresource.mapper.SysResourceMapper;
import com.demo.old.sysresource.model.SysResource;
import com.demo.old.sysresource.model.SysResourceExample;
import com.demo.old.sysrole.mapper.SysRoleMapper;
import com.demo.old.sysuser.mapper.SysUserExpandMapper;
import com.demo.old.sysuser.mapper.SysUserMapper;
import com.demo.old.sysuser.model.SysUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest
public class Te {

    @Autowired
    AppProp appProp;
    @Autowired
    SysUserExpandMapper sysUserExpandMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysResourceMapper sysResourceMapper;
    @Autowired
    BoyMapper boyMapper;
    @Autowired
    BoyExpandMapper boyExpandMapper;
    @Autowired
    GirlMapper girlMapper;
    @Autowired
    GirlExpandMapper girlExpandMapper;

    @Test
    public void selectBoth1(){
        List<Map<String, Object>> l = girlExpandMapper.selectBoth(60f);
        System.out.println(l.toString());

        Boy boy = new Boy();
        boy.setLoyalty(0);
        BoyExample example = new BoyExample();
        BoyExample.Criteria criteria = example.createCriteria();
        criteria.andBigNameEqualTo("野猪佩奇");
        boyMapper.updateByExampleSelective(boy, example);

        l =  girlExpandMapper.selectBoth(60f);
        System.out.println(l.toString());

    }

    @Test
    public void changLoyaltyByName(){

        Boy boy = new Boy();
        boy.setLoyalty(11);
        BoyExample example = new BoyExample();
        BoyExample.Criteria criteria = example.createCriteria();
        criteria.andBigNameEqualTo("野猪佩奇");
        //criteria.andSmallNameEqualTo("佩奇");

        boyMapper.updateByExampleSelective(boy, example);

    }

    @Test
    public void test22(){
        //List<Map<String, Object>> l = sysUserExpandMapper.selectUserRole(0002);
        //System.out.println(l.toString());
    }

    @Test
    public void test44(){
        //List<Map<String, Object>> l = sysUserExpandMapper.selectUserRole(0002);
        //System.out.println(l.toString());
    }


    @Test
    public void test33(){
        SysUser sysUser = new SysUser();
        sysUser.setPassword("ppppppppppp");
        sysUser.setId("GASY04");
        sysUserMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Test
    public void test3344(){
        SysUser sysUser = new SysUser();
        sysUser.setPassword("ppppppppppp");
        sysUser.setId("GASY0411111111");
        sysUserMapper.insertSelective(sysUser);
    }

    //缓存失效测试
    @Test
    public void t55555(){
        SysResource sysResource = new SysResource();
        sysResource.setpId("1");
        SysResourceExample example = new SysResourceExample();
        SysResourceExample.Criteria criteria = example.createCriteria();
        criteria.andSysTypeEqualTo(1111);
        sysResourceMapper.updateByExampleSelective(sysResource, example);

        //List<Map<String, Object>> l = sysUserExpandMapper.selectUserRole("0002");
        //System.out.println(l.toString());

    }
    //缓存失效测试
    @Test
    public void t666(){

        List<Map<String, Object>> l = sysUserExpandMapper.selectUserRole("0002");
        System.out.println(l.toString());

        //SysRole sysRole = new SysRole();
        //sysRole.setId("0001");
        ////sysRole.setSysType(2);
        //sysRole.setRemark("2");
        //sysRoleMapper.updateByPrimaryKey(sysRole);

    }

    @Test
    public void test() {
        System.out.println(appProp.getA());
    }

    @Test
    public void jackson() throws IOException{
        String path = "config/validate/json-post.json";
        InputStream is = ValidateUtils.class.getClassLoader().getResourceAsStream(path);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map = mapper.readValue(is, Map.class);
        System.out.println(map);
    }
    @Test
    public void gson() throws Exception{
        String path = "config/validate/test-gson.json";
        InputStream is = ValidateUtils.class.getClassLoader().getResourceAsStream(path);
        Map<String, Object> map = new HashMap<>();
        Object gson = Gson.class.newInstance();
        Method method = Gson.class.getMethod("fromJson", Reader.class, Class.class);
        map = (Map<String, Object>)method.invoke(gson, new BufferedReader(new InputStreamReader(is)), Map.class );
        Gson gson1 = new Gson();
        map = gson1.fromJson(new InputStreamReader(is), Map.class);

        System.out.println(map);
        System.out.println(Gson.class.getName());

    }
    @Test
    public void fastjson() throws Exception{
        Class[] arr = {JSON.class, Feature[].class};
        String path = "config/validate/test-fast-json.json";
        InputStream is = ValidateUtils.class.getClassLoader().getResourceAsStream(path);
        Map<String, Object> map = new HashMap<>();
        Method method = arr[0].getMethod("parseObject", InputStream.class, Type.class, arr[1]);
        map = (Map<String, Object>)method.invoke(null, is, Map.class, null);

        //map = JSON.parseObject(is, Map.class);
        System.out.println(map);
    }

    public void ttt1(String... str){
        System.out.println("sdfahkfjhakfjha");
    }

    @Test
    public void test2() {
        ttt1();

    }

    @Test
    public void test3() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1, 2, "1sd"));

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map1));

        System.out.println(jsonObject);
    }



}

