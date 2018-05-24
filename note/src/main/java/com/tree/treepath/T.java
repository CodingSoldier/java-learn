package com.tree.treepath;

import com.alibaba.fastjson.JSON;
import com.tree.treepath.mapper.TreepathExpandMapper;
import com.tree.treepath.model.TreepathExpand;
import com.utils.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        Iterator<TreepathExpand> it = expands.iterator();  //迭代器也可以有泛型
        while (it.hasNext()){
            TreepathExpand treepathExpand = it.next();
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








//p236 练习18
class E18_MapOrder {
    public static void main(String[] args) {
        String str = "{\n" +
                "    'ALGERIA': 'Algiers',\n" +
                "    'ANGOLA': 'Luanda',\n" +
                "    'BENIN': 'Porto-Novo',\n" +
                "    'BISSAU': 'Bissau',\n" +
                "    'BOTSWANA': 'Gaberone',\n" +
                "    'BURKINAFASO': 'Ouagadougou',\n" +
                "    'BURUNDI': 'Bujumbura',\n" +
                "    'CAMEROON': 'Yaounde',\n" +
                "    'CAPEVERDE': 'Praia',\n" +
                "    'CENTRALAFRICANREPUBLIC': 'Bangui',\n" +
                "    'CHAD': 'Ndjamena',\n" +
                "    'COMOROS': 'Moroni',\n" +
                "    'CONGO': 'Brazzaville',\n" +
                "    'DJIBOUTI': 'Dijibouti',\n" +
                "    'EGYPT': 'Cairo',\n" +
                "    'EQUATORIALGUINEA': 'Malabo',\n" +
                "    'ERITREA': 'Asmara',\n" +
                "    'ETHIOPIA': 'AddisAbaba',\n" +
                "    'GABON': 'Libreville',\n" +
                "    'GHANA': 'Accra',\n" +
                "    'GUINEA': 'Conakry',\n" +
                "    'KENYA': 'Nairobi',\n" +
                "    'LESOTHO': 'Maseru',\n" +
                "    'THEGAMBIA': 'Banjul'\n" +
                "}";
        Map<String,String> m1 = JSON.parseObject(str, Map.class);
        System.out.println(m1);

        String[] keys = m1.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        System.out.println(keys.toString());

        Map<String,String> m2 = new LinkedHashMap<String,String>();
        for(String key : keys)
            m2.put(key, m1.get(key));
        System.out.println(m2);
    }
}






















