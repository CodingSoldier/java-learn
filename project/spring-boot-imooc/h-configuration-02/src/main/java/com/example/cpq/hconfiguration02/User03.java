package com.example.cpq.hconfiguration02;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
public class User03 {

    private Long id;
    private String name;
    private Integer age;

    //嵌套属性
    private City city=new City();

    public class City{
        private Integer postCode;

        /**
         * 校验属性name不能为null
         * 1、pom.xml导入依赖
         * 2、类上加注解@Validated
         * 3、属性加注解@NotNull
         */
        @NotEmpty
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPostCode() {
            return postCode;
        }

        public void setPostCode(Integer postCode) {
            this.postCode = postCode;
        }

        @Override
        public String toString() {
            return "City{" +
                    "postCode=" + postCode +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "User03{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", city=" + city +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
