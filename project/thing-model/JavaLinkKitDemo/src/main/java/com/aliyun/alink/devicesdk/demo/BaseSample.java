package com.aliyun.alink.devicesdk.demo;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tools.AError;

public class BaseSample {

    protected String TAG = getClass().getSimpleName();
    protected String productKey = null;
    protected String deviceName = null;

    public BaseSample(String pk, String dn) {
        productKey = pk;
        deviceName = dn;
    }

    protected String getMessage(AMessage message) {
        if (message == null) {
            return null;
        }
        if (message.data instanceof byte[]) {
            return new String((byte[]) message.data);
        }
        return String.valueOf(message.data);
    }

    protected String getError(AError error) {
        if (error == null) {
            return null;
        }
        return "[code=" + error.getCode() + ",msg=" + error.getMsg() + ",subCode=" + error.getSubCode() + ",subMsg=" + error.getSubMsg() + "]";
    }


    protected String getPkDn(DeviceInfo info) {
        if (info == null) {
            return null;
        }
        return "[pk=" + info.productKey + ",dn=" + info.deviceName + "]";
    }
}
