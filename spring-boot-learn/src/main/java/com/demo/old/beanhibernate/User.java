package com.demo.old.beanhibernate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

public class User {
    @NotNull
    @Pattern(regexp = "^[u4e00-u9fa5a-zA-Z-z0-9]+$", message = "文字")
    private String id;
    private String name;
    private Date birthday;
    private Boolean single;
    private Float salaryNum;
    private BigDecimal bigNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public Float getSalaryNum() {
        return salaryNum;
    }

    public void setSalaryNum(Float salaryNum) {
        this.salaryNum = salaryNum;
    }

    public BigDecimal getBigNum() {
        return bigNum;
    }

    public void setBigNum(BigDecimal bigNum) {
        this.bigNum = bigNum;
    }
}
