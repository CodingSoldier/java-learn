package com.cpq.mybatispulslearn.t_ip_address.service;

import com.cpq.mybatispulslearn.t_ip_address.entity.TIpAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cpq
 * @since 2019-04-24
 */
public interface TIpAddressService extends IService<TIpAddress> {
    void insertIpZset();
}
