package com.designpattern.prototype;

import java.io.*;

public class Serializable01 implements Serializable {

    public Object deepClone() throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }
    public static void main(String[]args) throws Exception{

    }
}
