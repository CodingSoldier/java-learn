package com.aliyun.alink.devicesdk.demo;

import com.aliyun.alink.dm.model.RequestModel;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

import java.util.HashMap;
import java.util.Map;

public class COTASample extends BaseSample {


    public COTASample(String pk, String dn) {
        super(pk, dn);
    }

    /**
     * 设置远程配置下行监听器
     * 会有云端请求，先去订阅下行的 topic
     */
    public void setCOTAChangeListener() {
        LinkKit.getInstance().getDeviceCOTA().setCOTAChangeListener(new IConnectRrpcListener() {
            @Override
            public void onSubscribeSuccess(ARequest aRequest) {
                ALog.d(TAG, "onSubscribeSuccess() called with: aRequest = [" + aRequest + "]");
            }

            @Override
            public void onSubscribeFailed(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onSubscribeFailed() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }

            @Override
            public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
                ALog.d(TAG, "onReceived() called with: aRequest = [" + aRequest + "], iConnectRrpcHandle = [" + iConnectRrpcHandle + "]");
                if (aRequest instanceof MqttRrpcRequest) {
                    // 云端下行数据 拿到
                    String cotaData = new String((byte[]) ((MqttRrpcRequest) aRequest).payloadObj);
                    ALog.d(TAG, "received data=" + cotaData);
//                     ((MqttRrpcRequest) aRequest).payloadObj;
//                    ResponseModel<Map<String, String>> responseModel = JSONObject.parseObject(((MqttRrpcRequest) aRequest).payloadObj, new TypeReference<ResponseModel<Map<String, String>>>(){}.getType());

                }
                // 返回数据示例
                    /*{
                        "id": "123",
                        "version": "1.0",
                        "code": 200,
                        "data": {
                        "configId": "123dagdah",
                            "configSize": 1234565,
                            "sign": "123214adfadgadg",
                            "signMethod": "Sha256",
                            "url": "xxx",
                            "getType": "file"
                        }
                    }*/

            }

            @Override
            public void onResponseSuccess(ARequest aRequest) {
                ALog.d(TAG, "onResponseSuccess() called with: aRequest = [" + aRequest + "]");
            }

            @Override
            public void onResponseFailed(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onResponseFailed() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     * 获取云端当前的配置
     */
    public void cOTAGet() {
        RequestModel<Map> requestModel = new RequestModel<Map>();
        requestModel.id = "123";
        requestModel.method = "thing.config.get";
        requestModel.version = "1.0";
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("configScope", "product");
        paramsMap.put("getType", "file");
        requestModel.params = paramsMap;

        LinkKit.getInstance().getDeviceCOTA().COTAGet(requestModel, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? null : aResponse.data) + "]");
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }
}
