package com.example.bspringboot.i_exception_report.exception;

public class ExceptionTest {

    public static void main(String[] args) {
        /**
         org.springframework.boot.diagnostics.AbstractFailureAnalyzer#analyze(java.lang.Throwable)
         获取exception堆栈
         */
        try {
            throw new Cexception(new Bexception(new Aexception(new Exception("Exception"))));
        }catch (Throwable t){
            while (t != null){
                System.out.println(t.getClass());
                t = t.getCause();
            }
        }
    }

}
