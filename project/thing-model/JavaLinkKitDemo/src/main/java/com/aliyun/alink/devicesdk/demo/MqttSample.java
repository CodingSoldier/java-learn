package com.aliyun.alink.devicesdk.demo;

import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.api.CommonResource;
import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttResource;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.*;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

public class MqttSample extends BaseSample {
    final static String TAG = "MqttSample";

    public MqttSample(String pk, String dn) {
        super(pk, dn);
    }

    /**
     * 发布接口示例
     */
    public void publish() {
        MqttPublishRequest request = new MqttPublishRequest();
        // topic 用户根据实际场景填写
        request.topic = "/sys/" + productKey + "/" + deviceName + "/thing/deviceinfo/update";
        /**
         * 订阅回复的 replyTopic
         * 如果业务有相应的响应需求，可以设置 replyTopic，且 isRPC=true
         */
//        request.replyTopic = request.topic + "_reply";
        /**
         * isRPC = true; 表示先订阅 replyTopic，然后再发布；
         * isRPC = false; 不会订阅回复
         */
//        request.isRPC = true;

        /**
         * 设置请求的 qos
         */
        request.qos = 0;
        // 更新标签 仅做测试
        request.payloadObj = "{\"id\":2, \"params\":{\"version\":\"1.0.0\"}}";
        LinkKit.getInstance().publish(request, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                // publish 结果
                ALog.d(TAG, "onResponse " + (aResponse == null ? "" : aResponse.data));
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                // publish 失败
                ALog.d(TAG, "onFailure " + (aError == null ? "" : (aError.getCode() + aError.getMsg())));
            }
        });
    }

    /**
     * 订阅接口示例
     */
    public void subscribe() {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        // topic 用户根据实际场景填写
        request.topic = "/sys/" + productKey + "/" + deviceName + "/thing/deviceinfo/update";
        request.isSubscribe = true;
        LinkKit.getInstance().subscribe(request, new IConnectSubscribeListener() {
            @Override
            public void onSuccess() {
                // 订阅成功
                ALog.d(TAG, "onSuccess ");
            }

            @Override
            public void onFailure(AError aError) {
                // 订阅失败
                ALog.d(TAG, "onFailure " + getError(aError));
            }
        });
    }

    /**
     * 取消订阅接口示例
     */
    public void unSubscribe() {
        MqttSubscribeRequest request = new MqttSubscribeRequest();
        // topic 用户根据实际场景填写
        request.topic = "/sys/" + productKey + "/" + deviceName + "/thing/deviceinfo/update";
        request.isSubscribe = false;
        LinkKit.getInstance().unsubscribe(request, new IConnectUnscribeListener() {
            @Override
            public void onSuccess() {
                // 取消订阅成功
                ALog.d(TAG, "onSuccess ");
            }

            @Override
            public void onFailure(AError aError) {
                // 取消订阅失败
                ALog.d(TAG, "onFailure " + getError(aError));
            }
        });
    }


    /**
     * 注册资源 接口示例
     * 1.先订阅下行的 topic
     * 2.云端通过该 topic 下行，发送指令；
     * 3.收到数据并相应。
     */
    public void registerResource() {
        final CommonResource resource = new CommonResource();
        resource.topic = "/ext/rrpc/+/" + productKey + "/" + deviceName + "/get";
        resource.replyTopic = resource.topic;

        LinkKit.getInstance().registerResource(resource, new IResourceRequestListener() {
            @Override
            public void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener) {
                // 收到云端数据下行
                ALog.d(TAG, "onHandleRequest aResource=" + aResource + ", resourceRequest=" + resourceRequest + ", iResourceResponseListener=" + iResourceResponseListener);
                // 下行数据解析示例
//                String downstreamData = new String((byte[]) resourceRequest.payloadObj);
                // 示例 {"id":"269297015","version":"1.0","method":"thing.event.property.post","params":{"lightData":{"vv":12}}}

                // 如果数据是json，且包含id字段，格式可以按照如下示例回复，传输数据请根据实际情况定制
//                if (aResource instanceof  CommonResource) {
//                    ((CommonResource) aResource).replyTopic = resourceRequest.topic;
//                }
//                if (iResourceResponseListener != null) {
//                    AResponse response = new AResponse();
//
//                    response.data = "{\"id\":\"123\", \"code\":\"200\"" + ",\"data\":{} }";
//                    iResourceResponseListener.onResponse(aResource, resourceRequest, response);
//                }
                // 如果不一定是json格式，可以参考如下方式回复
                MqttPublishRequest rrpcResponse = new MqttPublishRequest();
                rrpcResponse.topic = resourceRequest.topic;
                rrpcResponse.payloadObj ="xxx";

                LinkKit.getInstance().publish(rrpcResponse,null);
            }

            @Override
            public void onSuccess() {
                // 注册资源成功
                ALog.d(TAG, "onSuccess ");
            }

            @Override
            public void onFailure(AError aError) {
                // 注册资源失败
                ALog.d(TAG, "onFailure " + getError(aError));
            }
        });
    }
}
