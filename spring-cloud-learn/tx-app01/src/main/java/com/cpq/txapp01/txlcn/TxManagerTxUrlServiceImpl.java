package com.cpq.txapp01.txlcn;

import com.codingapi.tx.config.service.TxManagerTxUrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TxManagerTxUrlServiceImpl implements TxManagerTxUrlService{
    @Value("${tm.manager.url}")
    private String url;

    @Override
    public String getTxUrl() {
        return url;
    }
}
