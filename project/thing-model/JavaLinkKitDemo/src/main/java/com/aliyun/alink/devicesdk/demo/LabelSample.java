package com.aliyun.alink.devicesdk.demo;

import com.aliyun.alink.dm.model.RequestModel;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabelSample extends BaseSample {

    public LabelSample(String pk, String dn) {
        super(pk, dn);
    }

    public void labelDelete() {
        RequestModel<List<Map<String, String>>> requestModel = new RequestModel<List<Map<String, String>>>();
        requestModel.id = "123";
        requestModel.method = "thing.deviceinfo.delete";
        requestModel.version = "1.0";
        List<Map<String, String>> paramsList = new ArrayList<Map<String, String>>();

        Map<String, String> listItemMap = new HashMap<String, String>();
        listItemMap.put("attrKey", "Temperature");

        paramsList.add(listItemMap);
        requestModel.params = paramsList;

        LinkKit.getInstance().getDeviceLabel().labelDelete(requestModel, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "" : aResponse.data) + "]");
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    public void labelUpdate() {
        RequestModel<List<Map<String, String>>> requestModel = new RequestModel<List<Map<String, String>>>();
        requestModel.id = "123";
        requestModel.method = "thing.deviceinfo.update";
        requestModel.version = "1.0";
        List<Map<String, String>> paramsList = new ArrayList<Map<String, String>>();

        Map<String, String> listItemMap = new HashMap<String, String>();
        listItemMap.put("attrKey", "Temperature");
        listItemMap.put("attrValue", "56.8");

        paramsList.add(listItemMap);
        requestModel.params = paramsList;
        LinkKit.getInstance().getDeviceLabel().labelUpdate(requestModel, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "" : aResponse.data) + "]");
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }
}
