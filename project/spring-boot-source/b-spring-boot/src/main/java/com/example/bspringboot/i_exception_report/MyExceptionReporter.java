package com.example.bspringboot.i_exception_report;

import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.SpringBootExceptionReporter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 自定义异常报告器
spring.factories添加
 org.springframework.boot.SpringBootExceptionReporter=com.example.bspringboot.i_exception_report.MyExceptionReporter
 */
public class MyExceptionReporter implements SpringBootExceptionReporter {

    private ConfigurableApplicationContext context;

    public MyExceptionReporter(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean reportException(Throwable failure) {
        if (failure instanceof UnsatisfiedDependencyException){
            UnsatisfiedDependencyException exception = (UnsatisfiedDependencyException)failure;
            System.out.println("#####no such bean: "+exception.getInjectionPoint().getField().getName());
        }
        return false;
    }
}
