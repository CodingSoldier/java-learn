package com.pbteach.dtx.seatademo.bank1.service.impl;

import com.pbteach.dtx.seatademo.bank1.dao.AccountInfoDao;
import com.pbteach.dtx.seatademo.bank1.service.AccountInfoService;
import com.pbteach.dtx.seatademo.bank1.spring.Bank2Client;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @version 1.0
 **/
@Service
@Slf4j
public class AccountInfoServiceImpl implements AccountInfoService {

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    Bank2Client bank2Client;

    @Transactional
    @GlobalTransactional//开启全局事务
    @Override
    public void updateAccountBalance(String accountNo, Double amount) {
        log.info("bank1 service begin,XID：{}", RootContext.getXID());
        //扣减张三的金额
        accountInfoDao.updateAccountBalance(accountNo,amount *-1);
        //调用李四微服务，转账
        String transfer = bank2Client.transfer(amount);
        if("fallback".equals(transfer)){
            //调用李四微服务异常
            throw new RuntimeException("调用李四微服务异常");
        }

        /**
         * amount = 2
         * 在这里打断点，Bank2已经新增了2块钱，Bank2的事务已经提交了。
         * 等Bank1抛出异常后，Bank2会执行反向操作，把Bank2减少2块钱。
         *
         * 可能会存在Bank2本来是0元，Bank2新增为10元。
         * 一个新的请求将Bank2减为5元。
         * Bank1服务出错，Bank2需要执行反向操作，即减10元，导致Bank2为-5元
         * 解决思路是使用select for update
         */
        if(amount == 2){
            //人为制造异常
            throw new RuntimeException("bank1 make exception..");
        }
    }
}
