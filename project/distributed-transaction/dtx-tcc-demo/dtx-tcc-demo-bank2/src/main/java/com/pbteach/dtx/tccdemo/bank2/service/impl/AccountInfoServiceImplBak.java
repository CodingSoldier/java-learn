package com.pbteach.dtx.tccdemo.bank2.service.impl;

import com.pbteach.dtx.tccdemo.bank2.dao.AccountInfoDao;
import com.pbteach.dtx.tccdemo.bank2.dao.HmilyLogDao;
import com.pbteach.dtx.tccdemo.bank2.service.AccountInfoService;
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
public class AccountInfoServiceImplBak implements AccountInfoService {

    @Autowired
    AccountInfoDao accountInfoDao;

    @Autowired
    HmilyLogDao hmilyLogDao;

    @Override
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public void updateAccountBalance(String msg, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 try 开始执行，transId:{}",transId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirm(String msg, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 confirm 开始执行，transId:{}",transId);

        int existConfirm = hmilyLogDao.isExistConfirm(transId);
        if (existConfirm > 0) {
            log.info("bank2 confirm 已经执行过，无需再次执行，transId", transId);
            return;
        }

        // bank2加钱
        accountInfoDao.addAccountBalance("2", amount);

        hmilyLogDao.addConfirm(transId);

        // bank2 confirm，抛出异常，会重试
        if (StringUtils.equals("confirm抛出异常会重试", msg)) {
            throw new RuntimeException("confirm抛出异常会重试，transId=" + transId);
        }
    }


    public void cancel(String msg, Double amount) {
        String transId = HmilyTransactionContextLocal.getInstance().get().getTransId();
        log.info("bank2 cancel 开始执行，transId:{}",transId);
    }
}
