package com.cpq.paramsvalidateboot;

import com.cpq.paramsvalidateboot.bean.Dream;
import com.cpq.paramsvalidateboot.bean.Girl;
import com.cpq.paramsvalidateboot.bean.UserVo;
import com.cpq.paramsvalidateboot.bean2.ResultCheckVo;
import com.cpq.paramsvalidateboot.validate.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.Null;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void t11(){
        redisTemplate.opsForValue().set("a:b:c", "资质");
        System.out.println(redisTemplate.opsForValue().get("a:b:c"));
    }

    @Test
    public void d22() throws Exception{
        ResultCheckVo vo = new ResultCheckVo();
        vo.setA("aaaaaaaa");
        vo.setBb("bbbbbb");
        vo.setPass(false);
        HashSet<String> set = new HashSet<String>();
        set.add("sss11");
        set.add("ss2222");
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer(Object.class));
        String jsonstr = new ObjectMapper().writeValueAsString(vo);
        //redisTemplate.opsForValue().set("bean1", jsonstr);
        redisTemplate.opsForValue().set("bean1", jsonstr);
    }
    @Test
    public void d223(){
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1,2,"1sd"));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer(Object.class));
        redisTemplate.opsForValue().set("bean1", map1);
    }

    public static String trimBegin(String args, char beTrim) {
        if (Utils.isEmpty(args) || Utils.isEmpty(beTrim)){
            return "";
        }

        int st = 0;
        int len = args.length();
        char[] val = args.toCharArray();
        char sbeTrim = beTrim;
        while ((st < len) && (val[st] <= sbeTrim)) {
            st++;
        }
        //while ((st < len) && (val[len - 1] <= sbeTrim)) {
        //    len--;
        //}
        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }


    @Test
    public void test1() {
        Double d = 12345678901.123456d;
        System.out.println(null == d);
    }

    @Test
    public void test2() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1,2,"1sd"));
        //if (map1.get("124235345") instanceof Map){
            Map<String, Object> a = (Map<String, Object>)map1.get("124235345");
        //}

        System.out.println(Boolean.parseBoolean(null));

        Set<String> msgSet = new TreeSet<>();

    }

    @Test
    public void testVo() {
        ResultCheckVo vo = new ResultCheckVo();
        vo.setA("aaaaaaaa");
        vo.setBb("bbbbbb");
        vo.setPass(false);
        HashSet<String> set = new HashSet<String>();
        set.add("sss11");
        set.add("ss2222");

        //new BigDecimal()

    }

    @Test
    public void testVo11() throws Exception{
        String filePath = "/config/validate/json/test.json";
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = this.getClass().getResourceAsStream(filePath);
        Map<String, Object> map = mapper.readValue(is, Map.class);
        System.out.println(Double.parseDouble(""));
    }

    @Test
    public void w22() throws Exception{
        boolean matcher = Pattern.matches("\\d+", "11242");
        System.out.println(matcher);
    }

    @Test
    public void testVo22() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        ResultCheckVo vo = new ResultCheckVo();
        vo.setA("aaaaaaaa");
        vo.setBb("bbbbbb");
        //vo.setPass(true);
        vo.setName("名字");


        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = ResultCheckVo.class;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        for (int i = 0, len = fieldList.size(); i < len; i++) {
            String varName = fieldList.get(i).getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fieldList.get(i).isAccessible();
                // 修改访问控制权限
                fieldList.get(i).setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Field field = fieldList.get(i);
                Object o = field.get(vo);
                System.out.println(o);
                if (o != null)  //子类属性覆盖父类后，父类属性的值就变成了null
                    map.put(varName, o);

            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(map);

            Set<Field> fieldSet = new LinkedHashSet<>();
            Class tempClass1 = vo.getClass();
            while (tempClass1 != null){
                Collections.addAll(fieldSet, tempClass1.getDeclaredFields());
                tempClass1 = tempClass1.getSuperclass();

            }
        System.out.println(fieldSet);
    }

    @Test
    public void testMap() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1,2,"1sd"));

        System.out.println(null instanceof Null);

        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = Map.class;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }

        for (int i = 0, len = fieldList.size(); i < len; i++) {
            String varName = fieldList.get(i).getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fieldList.get(i).isAccessible();
                // 修改访问控制权限
                fieldList.get(i).setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fieldList.get(i).get(map1);
                if (o != null)
                    map.put(varName, o);


            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(map);
    }

    @Test
    public void regex() throws Exception{
        String content = "/get/path/{id}/sav/{asdfjadf}";
        //Set<String> param
        Matcher m = Pattern.compile("(?<=\\{)[^\\}]+").matcher(content);
        while (m.find()) {
            System.out.println("Found value: " + m.group() );
        }
    }

    @Test
    public void isBean() throws Exception{
        int obj = 1;
        String v = "v";
        Date date = new Date();
        char[] charArray ={ 'a', 'b', 'c', 'd', 'e' };
        //System.out.println(isBaseTypeNull(date));
    }

    public boolean isSingleType (Object obj) {
        return obj == null || obj instanceof Number
                || obj instanceof CharSequence || obj instanceof Character
                || obj instanceof Date;
    }

    public void objToMap(Object obj, Map<String, Object> result){

        if (obj instanceof Map){
            mergeMap((Map<String, Object>)obj, result);
        }
    }

    public void mergeMap(Map<String, Object> obj, Map<String, Object> result){
        if (obj instanceof Map){
            for (String key:obj.keySet()){

            }
        }

    }

    private Map<String, Object> beanToMap(Object obj){
        if (obj == null){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        Set<Field> fieldSet = new HashSet<>();
        Class tempClass = obj.getClass();
        while (tempClass != null){
            Collections.addAll(fieldSet, tempClass.getDeclaredFields());
            tempClass = tempClass.getSuperclass();
        }
        String name = null;
        Object val = null;
        for (Field field:fieldSet){
            try {
                name = field.getName();
                field.setAccessible(true);
                val = field.get(obj);
                //if ()
            }catch (Exception e){
                name = null;
                val = null;
            }
            if (name != null && val != null){
                result.put(name, val);
            }
        }
        return result;
    }

    @Test
    public void test11() throws Exception{

        Dream dream = new Dream();
        dream.setExe("改变世界");
        dream.setMoney(123342234.123123);
        Dream dream1 = new Dream();
        dream1.setExe("改变世界");
        dream1.setMoney(123342234.123123);

        Girl girl = new Girl();
        girl.setMarry(true);
        girl.setName("girl名字");

        UserVo userVo = new UserVo();
        userVo.setGirl(girl);
        userVo.setDreamList(Arrays.asList(dream, dream1));
        userVo.setBirthday(new Date());
        userVo.setId(12324354);
        userVo.setSalaryNum(123123);

        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(null);
        Map<String, Object> map = objectMapper.readValue(str, Map.class);
        System.out.println(map);

    }
}











