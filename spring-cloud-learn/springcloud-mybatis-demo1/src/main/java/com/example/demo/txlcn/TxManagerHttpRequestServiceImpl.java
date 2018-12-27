package com.example.demo.txlcn;

import com.codingapi.tx.netty.service.TxManagerHttpRequestService;
import com.lorne.core.framework.utils.http.HttpUtils;
import org.springframework.stereotype.Service;

@Service
public class TxManagerHttpRequestServiceImpl implements TxManagerHttpRequestService{

    @Override
    public String httpGet(String url) {
        return HttpUtils.get(url);
    }

    @Override
    public String httpPost(String url, String params) {
        return HttpUtils.post(url,params);
    }
}
