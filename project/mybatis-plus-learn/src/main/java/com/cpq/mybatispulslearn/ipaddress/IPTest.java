//package com.cpq.mybatispulslearn;
//
//
//import com.cpq.mybatispulslearn.t_ip_address.service.TIpAddressService;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.Arrays;
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
//public class IPTest {
//
//    @Autowired
//    TIpAddressService tIpAddressService;
//
//    @Test
//    public void insert() {
//        tIpAddressService.insertIpZset();
//    }
//
//    @Test
//    public void insert1() {
//        Long l1 = 4278190080L;
//        Double d1 = l1.doubleValue();
//        System.out.println(d1);
//        System.out.println(d1.longValue());
//
//        Long l2 = 4294967039L;
//        Double d2 = l2.doubleValue();
//        System.out.println(d2);
//        System.out.println(d2.longValue());
//    }
//
//    @Test
//    public void test1() {
//        String str = "1.62.29.0       1.62.29.255     黑龙 江省  哈尔  滨市 双城区 联通";
//        String str2 = str.replaceAll(" +", " ");
//        String[] stringArr = StringUtils.split(str2, " ", 3);
//        System.out.println(Arrays.asList(stringArr).toString());
//
//        System.out.println(ipToLong(stringArr[0]));
//        System.out.println(ipToLong(stringArr[1]));
//    }
//
//    public long ipToLong(String ipAddress) {
//        String[] ipAddressInArray = ipAddress.split("\\.");
//        long result = 0;
//        for (int i = 0; i < ipAddressInArray.length; i++) {
//            int power = 3 - i;
//            int ip = Integer.parseInt(ipAddressInArray[i]);
//            result += ip * Math.pow(256, power);
//        }
//        return result;
//    }
//
//
//}
