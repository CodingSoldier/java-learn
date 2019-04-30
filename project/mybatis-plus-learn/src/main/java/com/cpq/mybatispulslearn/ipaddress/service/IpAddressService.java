package com.cpq.mybatispulslearn.ipaddress.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cpq.mybatispulslearn.ipaddress.entity.IpAddress;

/**
 * <p>
 * ip网段对应地址 服务类
 * </p>
 *
 * @author chenpiqian
 * @since 2019-04-24
 */
public interface IpAddressService extends IService<IpAddress> {

    void loadIpAddressToRedis();

    //String getIpAddress(String ip);

}
