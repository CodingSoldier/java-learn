package com.aliyun.alink.devicesdk.demo;

import com.aliyun.alink.apiclient.utils.StringUtils;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.api.InputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.TextUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class ThingSample extends BaseSample {

    private static final String TAG = "ThingSample";

    private final static String SERVICE_SET = "set";
    private final static String SERVICE_GET = "get";
    private final static String CONNECT_ID = "LINK_PERSISTENT";

    public ThingSample(String pk, String dn) {
        super(pk, dn);
    }

    /*  上报属性  */
    public void reportDemoProperty(){
        /**
         * TODO 用户根据实际情况填写 仅做参考
         *
         *  我们在控制台-->产品-->功能定义一栏中，创建标识符为LightSwitch的自定义属性，类型为整形；
         *  我们以该属性为例，演示属性的更新
         */

        String identity = "LightSwitch";
        ValueWrapper intWrapper = new ValueWrapper.IntValueWrapper(1);
        String lightCurrent = "LightCurrent";
        ValueWrapper lightCurrentW = new ValueWrapper.DoubleValueWrapper(1.2);

        Map<String, ValueWrapper> reportData = new HashMap<String, ValueWrapper>();
        reportData.put(identity, intWrapper);
        reportData.put(lightCurrent, lightCurrentW);

        LinkKit.getInstance().getDeviceThing().thingPropertyPost(reportData, new IPublishResourceListener() {

            public void onSuccess(String s, Object o) {
                // 属性上报成功
                ALog.d(TAG, "上报成功 onSuccess() called with: s = [" + s + "], o = [" + o + "]");
            }

            public void onError(String s, AError aError) {
                // 属性上报失败
                ALog.d(TAG, "上报失败onError() called with: s = [" + s + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     *上报事件
     */
    public void reportDemoEvent() {

         /**
          * TODO 用户根据实际情况填写 仅做参考
          *
          *  我们在控制台-->产品-->功能定义一栏中，创建标识符为ErrorEvent的自定义事件，该事件有一个参数ErrorCode，类型为整形；
          *  另一个参数ErrorDesc，类型为text类型
          *  我们以该事件为例，演示用1条事件上报的消息更新2个参数
          */

        String identity = "ErrorEvent";

        HashMap<String, ValueWrapper> valueWrapperMap = new HashMap<String, ValueWrapper>();
        /* 为参数ErrorCode赋值 */
        ValueWrapper intWrapper = new ValueWrapper.IntValueWrapper(1);
        valueWrapperMap.put("ErrorCode", intWrapper);
        /* 为参数ErrorDesc赋值 */
        ValueWrapper StringWrapper = new ValueWrapper.StringValueWrapper("hello123");
        valueWrapperMap.put("ErrorDesc", StringWrapper);

        OutputParams params = new OutputParams(valueWrapperMap);

        LinkKit.getInstance().getDeviceThing().thingEventPost(identity, params, new IPublishResourceListener() {
            public void onSuccess(String resId, Object o) {
                // 事件上报成功
                ALog.d(TAG, "onSuccess() called with: s = [" + resId + "], o = [" + o + "]");
            }

            public void onError(String resId, AError aError) {
                // 事件上报失败
                ALog.w(TAG, "onError() called with: s = [" + resId + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     * 设备端接收服务端的属性下发和服务下发的消息，并作出反馈
     */
    public void setServiceHandler() {
        ALog.d(TAG, "setServiceHandler() called");
        List<Service> srviceList = LinkKit.getInstance().getDeviceThing().getServices();
        for (int i = 0; srviceList != null && i < srviceList.size(); i++) {
            Service service = srviceList.get(i);
            LinkKit.getInstance().getDeviceThing().setServiceHandler(service.getIdentifier(), mCommonHandler);
        }
        LinkKit.getInstance().registerOnNotifyListener(connectNotifyListener);
    }

    private ITResRequestHandler mCommonHandler = new ITResRequestHandler() {
        public void onProcess(String identify, Object result, ITResResponseCallback itResResponseCallback) {
            ALog.d(TAG, "onProcess() called with: s = [" + identify + "], o = [" + result + "], itResResponseCallback = [" + itResResponseCallback + "]");
            try {
                if (SERVICE_SET.equals(identify)) {

                    /** 云端下发属性，SDK收到后触发的回调
                     *
                     * TODO: 用户需要将下发的属性值，设置到真实设备里面。
                     * 若设置成功，需要将isSetPropertySuccess写为true，
                     * demo将通过itResResponseCallback这个回调，将设备本地更新后的属性值写到云平台，
                     * 云平台的设备详情的物模型数据一栏属性值将会刷新
                     * 若设置失败，需要将isSetPropertySuccess写为false, demo将不更新云平台中的属性值
                     *
                     *  这里假定用户已经将属性设置到真实设备里面，将isSetPropertySuccess写为true
                     */
                    boolean isSetPropertySuccess = true;

                    if (isSetPropertySuccess) {
                        if (result instanceof InputParams) {
                            Map<String, ValueWrapper> data = (Map<String, ValueWrapper>) ((InputParams) result).getData();
                            // 如果控制台下发了属性OverTiltEnable， 可以通过data.get("OverTiltEnable")来获取相应的属性值
                            ALog.d(TAG, "收到下行数据 " + data);

                            /**
                             * 读取属性的值
                             *
                             * 假设用户物模型中有OverCurrentEnable这个属性，并且用户在控制台对OverCurrentEnable进行了下发属性的操作
                             * 我们下面示例代码演示如何从中读取到属性的值
                             *
                             *
                             * TODO:用户需要根据自己的物模型进行适配
                             */

                             // ValueWrapper.IntValueWrapper intValue = (ValueWrapper.IntValueWrapper) data.get("OverCurrentEnable");
                             // if (null != intValue) {
                             // ALog.d(TAG, "收到下行数据 " + intValue.getValue());
                             // }
                        }

                        /**
                         * 向云端上报数据
                         *
                         * errorInfo为空，表示接收数据成功，itResResponseCallback.onComplete回调将
                         * 回复/sys/${productKey}/${deviceName}/thing/service/property/set_reply给云端
                         * 同时，该回调会再通过/sys/${productKey}/${deviceName}/thing/service/property/post将更新后的属性上报到云端
                         * 表示设备端更新该属性成功
                         */
                        itResResponseCallback.onComplete(identify, null, null);

                    } else {
                        AError error = new AError();
                        error.setCode(100);
                        error.setMsg("setPropertyFailed.");
                        itResResponseCallback.onComplete(identify, new ErrorInfo(error), null);
                    }

                } else if (SERVICE_GET.equals(identify)) {
                    //  初始化的时候将默认值初始化传进来，物模型内部会直接返回云端缓存的值

                } else {
                    /**
                     *  异步服务下行处理
                     */
                    ALog.d(TAG, "用户根据真实的服务返回服务的值，请参照set示例");
                    OutputParams outputParams = new OutputParams();
                    // outputParams.put("op", new ValueWrapper.IntValueWrapper(20));
                    /**
                     * 设备端接收到服务，并返回响应数据给服务端
                     */
                    itResResponseCallback.onComplete(identify, null, outputParams);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ALog.d(TAG, "TMP 返回数据格式异常");
            }
        }

        public void onSuccess(Object o, OutputParams outputParams) {
            ALog.d(TAG, "onSuccess() called with: o = [" + o + "], outputParams = [" + outputParams + "]");
            ALog.d(TAG, "注册服务成功");
        }

        public void onFail(Object o, ErrorInfo errorInfo) {
            ALog.d(TAG, "onFail() called with: o = [" + o + "], errorInfo = [" + errorInfo + "]");
            ALog.d(TAG, "注册服务失败");
        }
    };

    /**
     * 同步服务回调处理函数
     * 同步服务下行方式包括云端系统RRPC下行和用户自定义RRPC下行两种，在该函数中都分别进行处理
     * 设备收到同步服务后，需要通过LinkKit.getInstance().getMqttClient().publish接口进行及时回复，否则控制台会显示调用超时失败
     */
    private IConnectNotifyListener connectNotifyListener = new IConnectNotifyListener() {
        public void onNotify(String connectId, String topic, AMessage aMessage) {
            ALog.d(TAG, "onNotify() called with: connectId = [" + connectId + "], topic = [" + topic + "], aMessage = [" + printAMessage(aMessage) + "]");
            try {
                if (CONNECT_ID.equals(connectId) && !StringUtils.isEmptyString(topic) &&
                        topic.startsWith("/sys/" + productKey + "/" + deviceName + "/rrpc/request")) {
                    ALog.d(TAG, "收到云端系统RRPC下行" + printAMessage(aMessage));
                    // ALog.d(TAG, "receice Message=" + new String((byte[]) aMessage.data));
                    // 服务端返回数据示例  {"method":"thing.service.test_service","id":"123374967","params":{"vv":60},"version":"1.0.0"}
                    MqttPublishRequest request = new MqttPublishRequest();
                    request.isRPC = false;
                    request.topic = topic.replace("request", "response");
                    String resId = topic.substring(topic.indexOf("rrpc/request/") + 13);
                    request.msgId = resId;
                    // TODO 用户根据实际情况填写 仅做参考
                    request.payloadObj = "{\"id\":\"" + resId + "\", \"code\":\"200\"" + ",\"data\":{} }";
                    // aResponse.data =
                    LinkKit.getInstance().getMqttClient().publish(request, new IConnectSendListener() {
                        public void onResponse(ARequest aRequest, AResponse aResponse) {
                            ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + aResponse + "]");
                        }

                        public void onFailure(ARequest aRequest, AError aError) {
                            ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
                        }
                    });
                }
                else if (CONNECT_ID.equals(connectId) && !TextUtils.isEmpty(topic) &&
                        topic.startsWith("/ext/rrpc/")) {
                    ALog.d(TAG, "收到云端自定义RRPC下行");
                    // ALog.d(TAG, "receice Message=" + new String((byte[]) aMessage.data));
                    // 服务端返回数据示例  {"method":"thing.service.test_service","id":"123374967","params":{"vv":60},"version":"1.0.0"}
                    MqttPublishRequest request = new MqttPublishRequest();
                    // 支持 0 和 1， 默认0
                    // request.qos = 0;
                    request.isRPC = false;
                    request.topic = topic.replace("request", "response");
                    String[] array = topic.split("/");
                    String resId = array[3];
                    request.msgId = resId;
                    // TODO 用户根据实际情况填写 仅做参考
                    request.payloadObj = "{\"id\":\"" + resId + "\", \"code\":\"200\"" + ",\"data\":{} }";
                    // aResponse.data =
                    LinkKit.getInstance().publish(request, new IConnectSendListener() {
                        @Override
                        public void onResponse(ARequest aRequest, AResponse aResponse) {
                            ALog.d(TAG, "onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + aResponse + "]");
                        }

                        @Override
                        public void onFailure(ARequest aRequest, AError aError) {
                            ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + aError + "]");
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean shouldHandle(String s, String s1) {
            return true;
        }

        public void onConnectStateChange(String s, ConnectState connectState) {
        }
    };

    private String printAMessage(AMessage aMessage) {
        return (aMessage == null || aMessage.data == null) ? "" : new String((byte[]) aMessage.data);
    }
}
