package com.aliyun.alink.devicesdk.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.api.*;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttRrpcRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcHandle;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GatewaySample extends BaseSample {
    private List<DeviceInfo> userSubDev = null;
    private List<BaseInfo> inputSubDev = null;

    private String testPublishTopic = "/sys/{productKey}/{deviceName}/thing/event/property/post";
    private String testSubscribePropertyService = "/sys/{productKey}/{deviceName}/thing/service/property/set";


    public GatewaySample(String pk, String dn, List<BaseInfo> subDevice) {
        super(pk, dn);
        inputSubDev = subDevice;
    }

    /**
     * 子设备动态注册
     * 云端安全策略问题  需要先在云端创建 子设备
     */
    public void subdevRegister() {
        ALog.d(TAG, "subdevRegister");
        LinkKit.getInstance().getGateway().gatewaySubDevicRegister(inputSubDev, new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "subdevRegister onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "null" : aResponse.data) + "]");
                try {
                    ResponseModel<List<DeviceInfo>> response = JSONObject.parseObject(aResponse.data.toString(), new TypeReference<ResponseModel<List<DeviceInfo>>>() {
                    }.getType());
                    //TODO 保存子设备的三元组信息
                    // for test
                    userSubDev = response.data;
                    addSubDevice(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "subdevRegister onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     * 获取当前网关的子设备列表
     * 需要先添加子设备到网关
     */
    public void getSubDevices() {
        ALog.d(TAG, "getSubDevices");
        LinkKit.getInstance().getGateway().gatewayGetSubDevices(new IConnectSendListener() {
            @Override
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(TAG, "getSubDevices onResponse() called with: aRequest = [" + aRequest + "], aResponse = [" + (aResponse == null ? "null" : aResponse.data) + "]");
            }

            @Override
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.d(TAG, "onFailure() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     * 添加子设备到网关
     * 子设备动态注册之后　可以拿到子设备的 deviceSecret 信息，签名的时候需要使用到
     * 签名方式 sign = hmac_md5(deviceSecret, clientId123deviceNametestproductKey123timestamp1524448722000)
     */
    private void addSubDevice(final int index) {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，添加失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(index);
        LinkKit.getInstance().getGateway().gatewayAddSubDevice(info, new ISubDeviceConnectListener() {
            @Override
            public String getSignMethod() {
                ALog.d(TAG, "getSignMethod() called");
                return "hmacsha1";
            }

            @Override
            public String getSignValue() {
                ALog.d(TAG, "getSignValue() called");
                Map<String, String> signMap = new HashMap<String, String>();
                signMap.put("productKey", info.productKey);
                signMap.put("deviceName", info.deviceName);
//                signMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
                signMap.put("clientId", getClientId());
                return SignUtils.hmacSign(signMap, info.deviceSecret);
            }

            @Override
            public String getClientId() {
                ALog.d(TAG, "getClientId() called");
                return "id";
            }

            @Override
            public Map<String, Object> getSignExtraData() {
                return null;
            }

            @Override
            public void onConnectResult(boolean isSuccess, ISubDeviceChannel iSubDeviceChannel, AError aError) {
                ALog.d(TAG, "onConnectResult() called with: isSuccess = [" + isSuccess + "], iSubDeviceChannel = [" + iSubDeviceChannel + "], aError = [" + getError(aError) + "]");
                if (isSuccess) {
                    ALog.d(TAG, "子设备添加成功 " + getPkDn(info));
                    // for test
                    // 添加成功之后调用子设备登录
                    subDevOnline(index);
                }
            }

            @Override
            public void onDataPush(String s, AMessage message) {
                // new String((byte[]) message.getData())
                // {"method":"thing.service.property.set","id":"184220091","params":{"test":2},"version":"1.0.0"} 示例
                ALog.d(TAG, "收到子设备下行数据  onDataPush() called with: topic = [" + s + "], message = [" + getMessage(message) + "]");
            }
        });
    }

    /**
     * 删除子设备
     */
    private void deleteSubDevice() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        LinkKit.getInstance().getGateway().gatewayDeleteSubDevice(info, new ISubDeviceRemoveListener() {
            @Override
            public void onSuceess() {
                ALog.d(TAG, "deleteSubDevice onSuceess() called " + getPkDn(info));
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "deleteSubDevice onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }

    /**
     * 网关添加子设备之后才能代理子设备上线
     */
    private void subDevOnline(int index) {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(index);
        LinkKit.getInstance().getGateway().gatewaySubDeviceLogin(info, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                ALog.d(TAG, "subDevOnline onSuccess() called " + getPkDn(info));
//                subDevDisable();
//                subDevDelete();
                // 测试子设备物模型
//                testSubdevThing();
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "subDevOnline onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }

    /**
     * 网关添加子设备之后才能代理子设备下线
     */
    public void subDevOffline() {
        ALog.d(TAG, "subDevOffline");
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        LinkKit.getInstance().getGateway().gatewaySubDeviceLogout(info, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                ALog.d(TAG, "subDevOffline onSuccess() called " + getPkDn(info));
                deleteSubDevice();
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "subDevOffline onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }

    /**
     * 代理子设备订阅
     */
    public void subDevSubscribe() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        String topic = testSubscribePropertyService;

        final String tempTopic = topic.replace("{deviceName}", info.deviceName)
                .replace("{productKey}", info.productKey);

        LinkKit.getInstance().getGateway().gatewaySubDeviceSubscribe(tempTopic, info, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                ALog.d(TAG, "subDevSubscribe onSuccess() called " + getPkDn(info));
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "subDevSubscribe onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }

    /**
     * 代理子设备发布
     */
    public void subDevPublish() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        String topic = testPublishTopic.replace("{deviceName}", info.deviceName)
                .replace("{productKey}", info.productKey);
        String data = "";//TODO add by user
        LinkKit.getInstance().getGateway().gatewaySubDevicePublish(topic, data, info, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                ALog.d(TAG, "subDevPublish onSuccess() called " + getPkDn(info));
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "subDevPublish onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }

    /**
     * 代理子设备取消订阅
     */
    public void subDevUnsubscribe() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        String topic = testSubscribePropertyService.replace("{deviceName}", info.deviceName)
                .replace("{productKey}", info.productKey);
        LinkKit.getInstance().getGateway().gatewaySubDeviceUnsubscribe(topic, info, new ISubDeviceActionListener() {
            @Override
            public void onSuccess() {
                ALog.d(TAG, "subDevUnsubscribe onSuccess() called " + getPkDn(info));
            }

            @Override
            public void onFailed(AError aError) {
                ALog.d(TAG, "subDevUnsubscribe onFailed() called with: aError = [" + getError(aError) + "]" + getPkDn(info));
            }
        });
    }


    /**
     * 子设备禁用监听
     */
    private void subDevDisable() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        LinkKit.getInstance().getGateway().gatewaySetSubDeviceDisableListener(info, new IConnectRrpcListener() {
            @Override
            public void onSubscribeSuccess(ARequest aRequest) {
                ALog.d(TAG, "订阅禁用下行成功");
            }

            @Override
            public void onSubscribeFailed(ARequest aRequest, AError aError) {
                ALog.d(TAG, "订阅禁用下行失败 " + getError(aError));
            }

            @Override
            public void onReceived(ARequest aRequest, IConnectRrpcHandle iConnectRrpcHandle) {
                ALog.d(TAG, "== onReceived() called with: aRequest = [" + aRequest + "], iConnectRrpcHandle = [" + iConnectRrpcHandle + "]" + getPkDn(info));
                AResponse response = new AResponse();
                // 回复示例
                response.data = "{\"id\":\"123\", \"code\":\"200\"" + ",\"data\":{} }";
                //TODO
                if (aRequest instanceof MqttRrpcRequest) {
                    String receivedData = new String((byte[]) ((MqttRrpcRequest) aRequest).payloadObj);
                    //{"method":"thing.disable","id":"123643484","params":{},"version":"1.0.0"}  参考数据
                    // TODO 数据解析处理
                    iConnectRrpcHandle.onRrpcResponse(((MqttRrpcRequest) aRequest).replyTopic, response);
                }
            }

            @Override
            public void onResponseSuccess(ARequest aRequest) {
                ALog.d(TAG, "subDevDisable onResponseSuccess() called with: aRequest = [" + aRequest + "]");
            }

            @Override
            public void onResponseFailed(ARequest aRequest, AError aError) {
                ALog.d(TAG, "subDevDisable onResponseFailed() called with: aRequest = [" + aRequest + "], aError = [" + getError(aError) + "]");
            }
        });
    }

    /**
     * 测试子设备物模型
     * 先获取子设备物模型
     */
    public void testSubdevThing() {
        if (userSubDev == null || userSubDev.size() < 1) {
            ALog.e(TAG, "无有效已动态注册的设备，删除失败");
            return;
        }
        final DeviceInfo info = userSubDev.get(0);
        Map<String, ValueWrapper> subDevInitState = new HashMap<String, ValueWrapper>();
//        subDevInitState.put(); //TODO
        LinkKit.getInstance().getGateway().initSubDeviceThing(null, info, subDevInitState, new IDMCallback<InitResult>() {
            @Override
            public void onSuccess(InitResult initResult) {
                ALog.d(TAG, "initSubDeviceThing onSuccess() called with: initResult = [" + initResult + "]");
                testSubDevThing(info);
            }

            @Override
            public void onFailure(AError aError) {
                ALog.d(TAG, "initSubDeviceThing onFailure() called with " + getError(aError));
            }
        });
    }

    /**
     * 测试子设备物模型
     * @param info 子设备信息
     */
    private void testSubDevThing(DeviceInfo info) {
        SubThingSample sample = new SubThingSample(info.productKey, info.deviceName);
        sample.readData(System.getProperty("user.dir") + "/test_sub_case.json");
        sample.setServiceHandler();
        sample.report();
    }
}
