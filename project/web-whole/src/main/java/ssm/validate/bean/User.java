package ssm.validate.bean;

import java.math.BigDecimal;
import java.util.Date;

public class User {
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
