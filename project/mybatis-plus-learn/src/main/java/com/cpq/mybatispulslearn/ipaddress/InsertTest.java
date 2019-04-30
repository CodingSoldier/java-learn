package com.cpq.mybatispulslearn.ipaddress;


import com.cpq.mybatispulslearn.ipaddress.service.IpAddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertTest {

    @Autowired
    IpAddressService ipAddressService;

    @Test
    public void insert() {
        ipAddressService.loadIpAddressToRedis();
    }



}
