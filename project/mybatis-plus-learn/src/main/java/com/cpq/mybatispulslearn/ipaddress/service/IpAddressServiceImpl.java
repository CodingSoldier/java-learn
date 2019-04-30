package com.cpq.mybatispulslearn.ipaddress.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpq.mybatispulslearn.ipaddress.Const;
import com.cpq.mybatispulslearn.RedisUtil;
import com.cpq.mybatispulslearn.ipaddress.entity.IpAddress;
import com.cpq.mybatispulslearn.ipaddress.mapper.IpAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * ip网段对应地址 服务实现类
 * </p>
 * @author chenpiqian
 * @since 2019-04-24
 */
@Service
public class IpAddressServiceImpl extends ServiceImpl<IpAddressMapper, IpAddress> implements IpAddressService {

    @Autowired
    RedisUtil redisUtil;

    //加载t_ip_address数据到redis缓存
    @Override
    public void loadIpAddressToRedis() {
        redisUtil.delete(Const.REDIS_IP10_ADDRESS_ZSET);
        int total = count(new QueryWrapper<>());
        int pageSize = 50000;
        double pageNum = Math.ceil(total/pageSize);
        //ip_address表格循环
        for (int i=1; i<=pageNum; i++){
            Page page = new Page(i, pageSize);
            QueryWrapper qw = new QueryWrapper();
            qw.orderByAsc("ip10d_start");
            IPage<IpAddress> pageResult = page(page, qw);
            List<IpAddress> list = pageResult.getRecords();

            //ip_address page循环
            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
            for (IpAddress ipAddress:list){
                ZSetOperations.TypedTuple<String> typedTupleStart =
                        new DefaultTypedTuple<>(ipAddress.getAddress()+ipAddress.getIp10dStart(), ipAddress.getIp10dStart().doubleValue());
                typedTupleSet.add(typedTupleStart);

                ZSetOperations.TypedTuple<String> typedTupleEnd =
                        new DefaultTypedTuple<>(ipAddress.getAddress()+ipAddress.getIp10dEnd(), ipAddress.getIp10dEnd().doubleValue());
                typedTupleSet.add(typedTupleEnd);
            }
            redisUtil.addZSet(Const.REDIS_IP10_ADDRESS_ZSET, typedTupleSet);
        }
    }


}
