package com.commonsjar.io.basics;

import org.junit.Test;

import java.io.File;

//  http://blog.csdn.net/zhaoyanjun6/article/details/54581478
//  http://www.runoob.com/java/java-file.html
public class B_File {
    /*File类以抽象的方式代表文件名和目录路径名，File类封装了对用户机器文件系统进行操作的功能，可用于文件、目录创建，文件的查找、删除
    * File的public构造函数有4个*/
    @Test
    public void file1() throws Exception{
        //构造函数1，通过文件路径创建File
        File file = new File("E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\b_file-img-r01.png");
        System.out.println(file);
    }

    @Test
    public void file2() throws Exception{
        //构造函数2，通过new出文件夹的File对象，再new出文件夹中文件的File对象。
        File parentFile = new File("E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\bfile\\a");
        //parentFile.mkdir();  //目录： D:/a/b 若D:/a存在，则创建b，若D:/a不存在，无法创建b
        parentFile.mkdirs();    //目录： D:/a/b 若D:/a不存在，也可以创建D:/a/b
        File file = new File(parentFile, "b_file-img-r01.png");
        File file2 = new File(parentFile, "file-img-r012.png");
        System.out.println("文件是否存在： "+file.exists());
        System.out.println("文件是否存在： "+file2.exists());
    }

    @Test
    public void file3() throws Exception{
        //通过文件夹路径、文件名的形式创建File实例
        //String parentFilePath = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\bfile\\a\\文件夹不存在";
        String parentFilePath = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\bfile\\a";
        File file = new File(parentFilePath, "b_file-img-r01.png");
        System.out.println("文件是否存在： "+file.exists());
    }

    @Test
    public void method() throws Exception{

        String path = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io";
        File file = new File(path);
        //获取文件名
        String[] fileNames = file.list();  //如果file.exists()为false，文件不存在，这file.list()返回null
        if(fileNames != null){
            for (String name: fileNames){
                System.out.println(name);
            }
        }

        //获取目录中的文件
        File[] files = file.listFiles();
        if (files != null){
            for (File f:files){
                System.out.println(f);
            }
        }
    }

}













