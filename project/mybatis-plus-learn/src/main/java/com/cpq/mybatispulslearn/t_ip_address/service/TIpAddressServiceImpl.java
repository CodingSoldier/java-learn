package com.cpq.mybatispulslearn.t_ip_address.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpq.mybatispulslearn.ip_1.entity.Ip1;
import com.cpq.mybatispulslearn.ip_1.service.Ip1Service;
import com.cpq.mybatispulslearn.t_ip_address.entity.TIpAddress;
import com.cpq.mybatispulslearn.t_ip_address.mapper.TIpAddressMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cpq
 * @since 2019-04-24
 */
@Service
public class TIpAddressServiceImpl extends ServiceImpl<TIpAddressMapper, TIpAddress> implements TIpAddressService {

    @Autowired
    Ip1Service ip1Service;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertIpZset() {

        List<Ip1> ip1List = ip1Service.list(new QueryWrapper<>());

        for (Ip1 ip1Obj:ip1List){
            if (ip1Obj != null){
                String stringAll = ip1Obj.getStringAll();
                if (StringUtils.isNotBlank(stringAll)){
                    String stringAllTempl = stringAll.replaceAll(" +", " ");
                    String[] stringArr = StringUtils.split(stringAllTempl, " ", 4);

                    TIpAddress tIpAddress = new TIpAddress();
                    tIpAddress.setIpStart(stringArr[0].trim());
                    tIpAddress.setIpEnd(stringArr[1].trim());
                    tIpAddress.setIp10dStart(ipToLong(stringArr[0].trim()));
                    tIpAddress.setIp10dEnd(ipToLong(stringArr[1].trim()));
                    tIpAddress.setAddress(stringArr[2].trim());

                    save(tIpAddress);
                }
            }
        }
    }

    public long ipToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }

}
