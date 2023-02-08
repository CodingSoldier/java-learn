package com.pbteach.dtx.tccdemo.bank2.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface AccountInfoDao {

    @Update("update account_info set account_balance=account_balance + #{amount} where  account_no=#{accountNo} ")
    int addAccountBalance(@Param("accountNo") String accountNo, @Param("amount") Double amount);

}
