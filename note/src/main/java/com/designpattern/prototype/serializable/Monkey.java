package com.designpattern.prototype.serializable;

import java.io.*;
import java.util.Date;

public class Monkey implements Cloneable,Serializable {
    private int height;
    private int weight;
    private Date birthDate;
    private GoldRingedStaff staff;

    public Monkey() {
        this.birthDate = new Date();
        this.staff = new GoldRingedStaff();
    }

    public Object clone(){
        Monkey temp = null;
        try {
            temp = (Monkey) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }finally {
            return temp;
        }
    }

    public Object deepClone() throws IOException, ClassNotFoundException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oss = new ObjectOutputStream(bos);
        oss.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public GoldRingedStaff getStaff() {
        return staff;
    }

    public void setStaff(GoldRingedStaff staff) {
        this.staff = staff;
    }
}
