package com.example.cpq.hconfiguration02.a_import;

public class RunImport2 {

    public static void main(String[] args) throws Exception {
        User2 user2 = cteateBean("com.example.cpq.hconfiguration02.a_import.User2");
        user2.setId(11L);
        user2.setName("名称");
        System.out.println(user2.toString());

    }

    public static <T> T cteateBean(String classpath) throws Exception{
        Class onwClass = Class.forName(classpath);
        Object o = onwClass.newInstance();
        //System.out.println(requestType.isInstance(o));
        return (T)o;
    }

}
