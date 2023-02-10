package com.pbteach.dtx.tccdemo.bank1.service.impl;

import com.pbteach.dtx.tccdemo.bank1.dao.AccountInfoDao;
import com.pbteach.dtx.tccdemo.bank1.dao.HmilyLogDao;
import com.pbteach.dtx.tccdemo.bank1.service.Bank1Service;
import com.pbteach.dtx.tccdemo.bank1.spring.Bank2Client;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class Bank1ServiceImpl implements Bank1Service {

    @Autowired
    AccountInfoDao accountInfoDao;
    @Autowired
    HmilyLogDao hmilyLogDao;

    @Autowired
    Bank2Client bank2Client;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public void updateAccountBalance(String msg, Double amount) {
        // 全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 try 开始，transId={}", transId);

        // 幂等判断
        int existTry = hmilyLogDao.isExistTry(transId);
        // 通故全局事务id查找到try日志，表明已经只执行过try
        if (existTry > 0) {
            log.info("已经执行过try，无需重复执行try，transId={}", transId);
            return;
        }

        // 悬挂处理
        int existConfirm = hmilyLogDao.isExistConfirm(transId);
        int existCancel = hmilyLogDao.isExistCancel(transId);
        // 通故全局事务id查找到confirm、cancel日志，表明已经只执行过confirm、cancel
        if (existConfirm > 0 || existCancel > 0) {
            log.info("confirm，cancel有一个已经执行过，try不能再次执行，transId={}", transId);
            return;
        }

        // 制造空回滚
        if (StringUtils.equals("制造空回滚", msg)) {
            throw new RuntimeException("try方法没修改数据库就抛出异常，cancel方法会执行，形成空回滚，transId=" + transId);
        }

        // blank1减金额
        accountInfoDao.subtractAccountBalance("1", amount);

        // 添加try日志记录，try日志和扣减余额在同一个本地事务中，要么都成功，要么都失败
        // 日志的组件id必须是全局事务id，如果同一个事物重复调用try，到这一步会报主键重复
        hmilyLogDao.addTry(transId);

        // 远程调用
        Boolean result = bank2Client.transfer(msg, amount);
        if (!result) {
            throw new RuntimeException("调用bank2失败");
        }

        // bank1调用bank2成功后，发生异常，模拟回滚
        if (StringUtils.equals("bank1调用bank2成功后，发生异常，模拟回滚", msg)) {
            throw new RuntimeException("bank1调用bank2成功后，发生异常，模拟回滚，transId=" + transId);
        }
    }

    public void confirm(String accountNo, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 confirm 开始执行，transId={}", transId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String msg, Double amount) {
        // 全局事务id
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank1 cancel 开始执行，transId={}", transId);

        // 幂等判断
        int existCancel = hmilyLogDao.isExistCancel(transId);
        if (existCancel > 0) {
            log.info("cancel已经执行过，无需重复执行，transId={}", transId);
            return;
        }

        // 处理空回滚
        int existTry = hmilyLogDao.isExistTry(transId);
        if (existTry == 0) {
            log.info("try未执行过，不能执行cancel，transId={}", transId);
            return;
        }

        // bank1回滚，加钱
        accountInfoDao.addAccountBalance(msg, amount);
        // 添加日志
        hmilyLogDao.addCancel(transId);
    }

}
