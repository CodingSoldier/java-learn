package com.designpattern.builder;

public class ConcreteBuilder implements Builder {
    private Product product = new Product();

    @Override
    public void builderPart1() {
        product.setPart1("编号：9567");
    }

    @Override
    public void builderPart2() {
        product.setPart2("编号：9568");
    }

    @Override
    public Product retrieveResult() {
        return product;
    }
}
