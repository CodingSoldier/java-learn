package com.pbteach.dtx.tccdemo.bank1.service.impl;

import com.pbteach.dtx.tccdemo.bank1.dao.AccountInfoDao;
import com.pbteach.dtx.tccdemo.bank1.service.AccountInfoService;
import com.pbteach.dtx.tccdemo.bank1.spring.Bank2Client;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
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

    // 账户扣款，就是tcc的try方法

    /**
     * 	try幂等校验
     * 	try悬挂处理
     * 	检查余额是够扣减金额
     * 	扣减金额
     * @param accountNo
     * @param amount
     */
    @Override
    @Transactional
    //只要标记@Hmily就是try方法，在注解中指定confirm、cancel两个方法的名字
    @Hmily(confirmMethod="commit",cancelMethod="rollback")
    public void updateAccountBalance(String accountNo, Double amount) {
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 try begin 开始执行...xid:{}",transId);

        /**
         * 幂等判断
         * 在try之后加一条try-modify日志，日志记录全局事务ID
         * 在try之前判断日志是否存在，如果已经存在，则证明已经执行try-modify，不再往下执行
         * 被调用端必须也用这个判断
         */
        //幂等判断 判断local_try_log表中是否有try日志记录，如果有则不再执行
        if(accountInfoDao.isExistTry(transId)>0){
            log.info("bank1 try 已经执行，无需重复执行,xid:{}",transId);
            return ;
        }

        /**
         * 悬挂就是对于一个分布式事务，其二阶段 Cancel 接口比 Try 接口先执行。
         * 解决办法同样是在try之前判断Cancel是否已经执行
         */
        // try悬挂处理，如果cancel、confirm有一个已经执行了，try不再执行
        // 本案例中confirm为空，所以isExistConfirm(transId)在本案例中是没必要的
        if(accountInfoDao.isExistConfirm(transId)>0 || accountInfoDao.isExistCancel(transId)>0){
            log.info("bank1 try悬挂处理  cancel或confirm已经执行，不允许执行try,xid:{}",transId);
            return ;
        }

        /**
         * 空回滚
         *
         * 解决办法，在修改操作后，加一行try-modify日志。
         * cancel方法判断try-modify日志是否存在，如果存在，cancel才执行回滚修改
         */
        if(amount == 0){
            throw new RuntimeException("没执行try数据库修改操作就抛出异常，cancel方法会执行，形成空回滚，xid:{}"+transId);
        }

        //扣减金额
        if(accountInfoDao.subtractAccountBalance(accountNo, amount)<=0){
            //扣减失败
            throw new RuntimeException("bank1 try 扣减金额失败,xid:{}"+transId);
        }
        //插入try执行记录,用于幂等判断
        accountInfoDao.addTry(transId);

        //远程调用李四，转账
        if(!bank2Client.transfer(amount)){
            throw new RuntimeException("bank1 远程调用李四微服务失败,xid:{}"+transId);
        }
        if(amount == 2){
            throw new RuntimeException("人为制造异常,xid:{}"+transId);
        }
        log.info("bank1 try end 结束执行...xid:{}",transId);
    }

    //confirm方法
    @Transactional
    public void commit(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 confirm begin 开始执行...xid:{},accountNo:{},amount:{}",transId,accountNo,amount);
    }



    /** cancel方法
     * 	cancel幂等校验
     * 	cancel空回滚处理
     * 	增加可用余额
     * @param accountNo
     * @param amount
     */
    @Transactional
    public void rollback(String accountNo, Double amount){
        //获取全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 cancel begin 开始执行...xid:{}",transId);
        //	cancel幂等校验
        if(accountInfoDao.isExistCancel(transId)>0){
            log.info("bank1 cancel 已经执行，无需重复执行,xid:{}",transId);
            return ;
        }
        //cancel空回滚处理，如果try没有执行，cancel不允许执行
        if(accountInfoDao.isExistTry(transId)<=0){
            log.info("bank1 空回滚处理，try没有执行，不允许cancel执行,xid:{}",transId);
            return ;
        }
        //	增加可用余额
        accountInfoDao.addAccountBalance(accountNo,amount);
        //插入一条cancel的执行记录
        accountInfoDao.addCancel(transId);
        log.info("bank1 cancel end 结束执行...xid:{}",transId);

    }

}
