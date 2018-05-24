package com.demo.paramsvalidate.bean;

public class Parser {
    private Class parserClass;
    private Class featureArrClass;

    public Parser() {

    }
    public Parser(Class parserClass) {
        this.parserClass = parserClass;
    }

    public Parser(Class parserClass, Class featureArrClass) {
        this.parserClass = parserClass;
        this.featureArrClass = featureArrClass;
    }

    public Class getParserClass() {
        return parserClass;
    }

    public void setParserClass(Class parserClass) {
        this.parserClass = parserClass;
    }

    public Class getFeatureArrClass() {
        return featureArrClass;
    }

    public void setFeatureArrClass(Class featureArrClass) {
        this.featureArrClass = featureArrClass;
    }
}
