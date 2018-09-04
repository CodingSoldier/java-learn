package com.demo.testvalidate.bean;

public class BaoBao {
    private String brand;
    private double price;
    private InnerThing innerThing;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public InnerThing getInnerThing() {
        return innerThing;
    }

    public void setInnerThing(InnerThing innerThing) {
        this.innerThing = innerThing;
    }
}
