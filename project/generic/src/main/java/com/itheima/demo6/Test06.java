package com.itheima.demo6;

import com.itheima.demo3.ProductGetter;

import java.util.ArrayList;

/**
 * 泛型方法的使用
 */
public class Test06 {
    public static void main(String[] args) {
        ProductGetter<Integer> productGetter = new ProductGetter<>();
        int[] products = {100,200,400};
        for (int i = 0; i < products.length; i++) {
            productGetter.addProduct(products[i]);
        }
        //泛型类的成员方法的调用
        Integer product = productGetter.getProduct();
        System.out.println(product + "\t" + product.getClass().getSimpleName());
        System.out.println("---------------------------------------------------");
        ArrayList<String> strList = new ArrayList<>();
        strList.add("笔记本电脑");
        strList.add("苹果手机");
        strList.add("扫地机器人");
        //泛型方法的调用，类型是通过调用方法的时候来指定。
        String product1 = productGetter.getProduct(strList);
        System.out.println(product1 + "\t" + product1.getClass().getSimpleName());
        System.out.println("---------------------------------------------------");
        ArrayList<Integer> intList = new ArrayList<>();
        intList.add(1000);
        intList.add(5000);
        intList.add(3000);
        Integer product2 = productGetter.getProduct(intList);
        System.out.println(product2 + "\t" + product2.getClass().getSimpleName());
        System.out.println("---------------------------------------------------");
        //调用多个泛型类型的静态泛型方法
        ProductGetter.printType(100,"java",true);
        ProductGetter.printType(false,false,true);
        System.out.println("---------------------------------------------------");
        //可变参数的泛型方法的调用
        ProductGetter.print(1,2,3,4,5);
        System.out.println("---------------------------------------------------");
        ProductGetter.print("a","b","c");

    }
}
