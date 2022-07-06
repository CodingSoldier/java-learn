package com.example.reactor0._01;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * https://zhuanlan.zhihu.com/p/103686124
 */
public class MapFlatMap {
    List<Integer> numList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    @Test
    public void test01(){
        List<String> strList = numList.stream()
                // 对元素进行转换，将整型转换为字符串
                .map(e -> String.valueOf(e))
                .collect(Collectors.toList());
        System.out.println(strList.toString());

    }


    private static class Klass {
        private int field;

        public Klass(int field) {
            this.field = field;
        }

        @Override
        public String toString() {
            return "field=" + field;
        }
    }

    private static class KlassGroup {
        private List<Klass> group = new ArrayList<>();

        public KlassGroup(Klass... objList) {
            for (Klass item : objList) {
                this.group.add(item);
            }
        }

        public List<Klass> getKlassList() {
            return group;
        }
    }

    List<KlassGroup> groupList = Arrays.asList(
            new KlassGroup(new Klass(1), new Klass(2), new Klass(3)),
            new KlassGroup(new Klass(4), new Klass(5), new Klass(6)),
            new KlassGroup(new Klass(7), new Klass(8), new Klass(9)),
            new KlassGroup(new Klass(10))
    );

    @Test
    public void test03(){
        List<List<Klass>> ll = groupList.stream()
                .map(e -> e.getKlassList())
                .collect(Collectors.toList());

        List<Klass> l = groupList.stream()
                .flatMap(e -> e.getKlassList().stream())
                .collect(Collectors.toList());


    }

}
