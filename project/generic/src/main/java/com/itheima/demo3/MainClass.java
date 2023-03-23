package com.itheima.demo3;

public class MainClass {
    public static void main(String[] args) {
        //创建抽奖器对象，指定数据类型
        ProductGetter<String> stringProductGetter = new ProductGetter<>();
        String[] strProducts = {"苹果手机","华为手机","扫地机器人","咖啡机"};
        //给抽奖器中，填充奖品
        for (int i = 0; i < strProducts.length; i++) {
            stringProductGetter.addProduct(strProducts[i]);
        }
        //抽奖
        String product1 = stringProductGetter.getProduct();
        System.out.println("恭喜您，你抽中了:" + product1);

        System.out.println("----------------------------------------");
        ProductGetter<Integer> integerProductGetter = new ProductGetter<>();
        int[] intProducts = {10000,5000,3000,500,300000};
        for (int i = 0; i < intProducts.length; i++) {
            integerProductGetter.addProduct(intProducts[i]);
        }

        Integer product2 = integerProductGetter.getProduct();
        System.out.println("恭喜您，你抽中了:" + product2);
    }
}
