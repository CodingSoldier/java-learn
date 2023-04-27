package com.aliyun.alink.devicesdk.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.apiclient.threadpool.ThreadPool;
import com.aliyun.alink.dm.api.IShadowRRPC;
import com.aliyun.alink.dm.shadow.ShadowResponse;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;


/**
 * 设备影子使用示例
 */
public class DeviceShadowSample {
    private static final String TAG = "DeviceShadowSample";

    private int version = 1;


    // 更新设备影子 需要根据获得到的设备影子读取返回的 version值，在更新的时候 {ver} 替换为version+1
    private String shadowUpdate = "{" + "\"method\": \"update\"," + "\"state\": {" + "\"reported\": {" +
            "\"color\": \"red\"" + ",\"mode\": \"1\"" + "}" + "}," + "\"version\": {ver}" + "}";

    // 获取设备影子
    private String shadowGet = "{" + "\"method\": \"get\"" + "}";

    //删除设备影子 color 属性  {ver}需要替换
    private String shadowDelete = "{" + "\"method\": \"delete\"," + "\"state\": {" + "\"reported\": {" +
            "\"color\": \"null\"" + "}" + "}," + "\"version\": {ver}" + "}";

    //删除设备影子所有属性  {ver}需要替换
    private String shadowDeleteAll = "{" + "\"method\": \"delete\"," + "\"state\": {" +
            "\"reported\":\"null\"" + "}," + "\"version\": {ver}" + "}";

    /**
     * 获取设备影子
     */
    public void shadowGet() {
        ALog.d(TAG, "shadowGet");
        shadowUpstream(shadowGet);
    }

    /**
     * 设备影子更新
     */
    public void shadowUpdate() {
        ALog.d(TAG, "shadowUpdate");
        version++;
        shadowUpstream(shadowUpdate.replace("{ver}", String.valueOf(version)));
    }

    /**
     * 删除设备影子
     */
    public void shadowDelete() {
        ALog.d(TAG, "shadowDelete");
        version++;
        shadowUpstream(shadowDelete.replace("{ver}", String.valueOf(version)));
    }

    /**
     * 删除所有设备影子
     */
    public void shadowDeleteAll() {
        ALog.d(TAG, "shadowDeleteAll");
        version++;
        shadowUpstream(shadowDeleteAll.replace("{ver}", String.valueOf(version)));
    }

    /**
     * 先订阅设备影子的更新 topic
     * 云端下发设备影子数据之后
     */
    public void listenDownStream() {

        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                LinkKit.getInstance().getDeviceShadow().setShadowChangeListener(new IShadowRRPC() {
                    @Override
                    public void onSubscribeSuccess(ARequest aRequest) {
                        ALog.d(TAG, "设备影子下行订阅成功");
                        ALog.d(TAG, "onSubscribeSuccess() called with: aRequest = [" + aRequest + "]");
                    }

                    @Override
                    public void onSubscribeFailed(ARequest aRequest, AError aError) {
                        ALog.d(TAG, "设备影子下行订阅失败");
                        ALog.d(TAG, "onSubscribeFailed() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
                    }

                    @Override
                    public void onReceived(ARequest aRequest, AResponse aResponse, IConnectRrpcHandle iConnectRrpcHandle) {
                        ALog.d(TAG, "onReceived() called with: aRequest = [" + aRequest + "], iConnectRrpcHandle = [" + iConnectRrpcHandle + "]");
                        // TODO user logic
                        ALog.d(TAG, "收到设备影子下行指令");
                        try {
                            if (aRequest != null) {
                                String dataStr = null;
                                if (aResponse.data instanceof byte[]) {
                                    dataStr = new String((byte[]) aResponse.data, "UTF-8");
                                } else if (aResponse.data instanceof String) {
                                    dataStr = (String) aResponse.data;
                                } else {
                                    dataStr = String.valueOf(aResponse.data);
                                }
                                ALog.d(TAG, "dataStr = " + dataStr);
                                // 返回数据示例
                                //{"method":"control","payload":{"state":{"desired":{"mode":2,"color":"white"},"reported":{"mode":"1","color":"red"}},"metadata":{"desired":{"mode":{"timestamp":1547642408},"color":{"timestamp":1547642408}},"reported":{"mode":{"timestamp":1547642408},"color":{"timestamp":1547642408}}}},"timestamp":1547642408,"version":12}
                                // 仅供参考
                                ShadowResponse<String> shadowResponse = JSONObject.parseObject(dataStr, new TypeReference<ShadowResponse<String>>() {
                                }.getType());
                                if (shadowResponse != null && shadowResponse.version != null) {
                                    version = Integer.valueOf(shadowResponse.version);
                                }

                                AResponse response = new AResponse();
                                // TODO 用户实现控制设备
                                // 用户控制设备之后 上报影子的值到云端
                                // 上报设置之后的值到云端
                                // 根据当前实际值上报
                                response.data = shadowUpdate.replace("{ver}", String.valueOf(++version));
                                // 第一个值 replyTopic 有默认值 用户不需要设置
                                iConnectRrpcHandle.onRrpcResponse(null, response);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onResponseSuccess(ARequest aRequest) {
                        ALog.d(TAG, "onResponseSuccess() called with: aRequest = [" + aRequest + "]");
                    }

                    @Override
                    public void onResponseFailed(ARequest aRequest, AError aError) {
                        ALog.w(TAG, "onResponseFailed() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
                    }
                });
            }
        });
    }

    private void shadowUpstream(String requestData) {
        if (requestData == null) {
            ALog.e(TAG, "shadowUpstream error requestData=null.");
            return;
        }
        LinkKit.getInstance().getDeviceShadow().shadowUpload(requestData, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? null : aResponse.data) + "]");
                try {
                    if (aRequest instanceof MqttPublishRequest && aResponse != null) {
                        String dataStr = null;
                        if (aResponse.data instanceof byte[]) {
                            dataStr = new String((byte[]) aResponse.data, "UTF-8");
                        } else if (aResponse.data instanceof String) {
                            dataStr = (String) aResponse.data;
                        } else {
                            dataStr = String.valueOf(aResponse.data);
                        }
                        ALog.d(TAG, "dataStr = " + dataStr);
                        // {"method":"reply","payload":{"status":"success","state":{"reported":{}},"metadata":{"reported":{}}},"timestamp":1547641855,"version":7,"clientToken":"null"}
                        ShadowResponse<String> response = JSONObject.parseObject(dataStr, new TypeReference<ShadowResponse<String>>() {
                        }.getType());
                        if (response != null && response.version != null) {
                            version = Integer.valueOf(response.version);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ALog.e(TAG, "update version failed.");
                } catch (Exception e) {
                    ALog.e(TAG, "update response parse exception.");
                }
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
            }
        });
    }

}
