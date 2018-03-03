package projectnote.commonsjar.collections4;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/22
 */
public class Collections4_Learn {

    //collections4提供了泛型，collection没提供
    @SuppressWarnings("unchecked")
    @Test
    public void t() throws Exception{

        List<ExampleBean> list = new ArrayList<ExampleBean>();
        ExampleBean eb1 = new ExampleBean();
        eb1.setAge(1);
        list.add(eb1);
        ExampleBean eb2 = new ExampleBean();
        eb2.setAge(11);
        list.add(eb2);
        ExampleBean eb3 = new ExampleBean();
        eb3.setAge(111);
        list.add(eb3);

        //找出满足Predicate条件的第一个元素，并返回
        ExampleBean e = IterableUtils.find(list, new Predicate<ExampleBean>() {
            @Override
            public boolean evaluate(ExampleBean exampleBean) {
                return exampleBean.getAge() > 1;
            }
        });
        System.out.println(e);

        //工具类方法中已经对list做了非空判断，list为null也不报错
        long num = IterableUtils.countMatches(list, new Predicate<ExampleBean>() {
            @Override
            public boolean evaluate(ExampleBean exampleBean) {
                return exampleBean.getAge() > 2;
            }
        });
        System.out.println(num);

        //找出满足Predicate条件的集合，并返回
        Collection<ExampleBean> list1 = CollectionUtils.select(list, new Predicate<ExampleBean>() {
            @Override
            public boolean evaluate(ExampleBean exampleBean) {
                return exampleBean.getAge() > 10;
            }
        });
        list1 = (List<ExampleBean>) list1;
        System.out.println(list1.toString());

        //找出list中满足Predicate的元素添加到list2中
        List<ExampleBean> list2 = new ArrayList<ExampleBean>();
        CollectionUtils.select(list, new Predicate<ExampleBean>() {
            @Override
            public boolean evaluate(ExampleBean exampleBean) {
                return exampleBean.getAge() < 100;
            }
        },list2);
        System.out.println(list2.toString());


        //过滤，list仅保留满足Predicate条件的元素
        CollectionUtils.filter(list, new Predicate<ExampleBean>() {
            @Override
            public boolean evaluate(ExampleBean exampleBean) {
                return exampleBean.getAge() > 2;
            }
        });
        System.out.println(list);

        //判断集合是否为空，做了null判断
        System.out.println(CollectionUtils.isEmpty(null));
        System.out.println(CollectionUtils.isEmpty(new ArrayList<>()));
        System.out.println(CollectionUtils.isEmpty(new HashSet<>()));

    }

    @Test
    public void tt() throws Exception{

        List<ExampleBean> list = new ArrayList<ExampleBean>();
        ExampleBean eb1 = new ExampleBean();
        eb1.setAge(1);
        list.add(eb1);
        ExampleBean eb2 = new ExampleBean();
        eb2.setAge(11);
        list.add(eb2);
        ExampleBean eb3 = new ExampleBean();
        eb3.setAge(111);
        list.add(eb3);

        List<ExampleBean> list2 = new ArrayList<ExampleBean>();
        ExampleBean e = new ExampleBean();
        e.setAge(111);
        list2.add(e);

        list2.add(eb1);

        //去交集，取list中内存相同的bean，得eb1
        List<ExampleBean> list12 = ListUtils.intersection(list,list2);
        System.out.println(list12.toString());

        List<String> l1 = new ArrayList<String>();
        l1.add("a");
        l1.add("a");
        l1.add("aa");
        List<String> l2 = new ArrayList<String>();
        l2.add("a");
        l2.add("bb");

        //List交集
        List<String> l12 = ListUtils.intersection(l1, l2);
        System.out.println(l12.toString());

        //l1减去l2中存在的元素，l1["a","a","aa"] - l2["a","bb"] = l12_subtract["a","aa"]
        List<String> l12_subtract = ListUtils.subtract(l1,l2);
        System.out.println(l12_subtract);
        //l1删除l2中存在的元素，l1["a","a","aa"] 删除 l2["a","bb"] = l12_remove["aa"]
        List<String> l12_remove = ListUtils.removeAll(l1,l2);
        System.out.println(l12_remove);

        //l1,l2合并， ["a","a","aa","a","aa"] 相同的元素不会合并为一个
        List<String> l12_union = ListUtils.union(l1,l2);
        System.out.println(l12_union);



    }

}
