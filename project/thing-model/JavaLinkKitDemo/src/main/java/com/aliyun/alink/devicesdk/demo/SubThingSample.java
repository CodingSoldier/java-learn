package com.aliyun.alink.devicesdk.demo;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.apiclient.utils.StringUtils;
import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.IThing;
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
import com.aliyun.alink.linksdk.tmp.devicemodel.Arg;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.devicemodel.Property;
import com.aliyun.alink.linksdk.tmp.devicemodel.Service;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SubThingSample extends BaseSample{
    private static final String TAG = "SubThingSample";

    private final static String SERVICE_SET = "set";
    private final static String SERVICE_GET = "get";
    private final static String CONNECT_ID = "LINK_PERSISTENT";
    final static Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");

    private final static int DEF_VALUE = Integer.MIN_VALUE;

    private String identity = null;
    private String value = null;
    private ValueWrapper valueWrapper = null;
    private HashMap<String, ValueWrapper> valueWrapperMap = null;
    private ThingData mThingData = null;
    private Gson mGson = new Gson();

    private boolean isEvent = false;

    private BaseInfo baseInfo = null;
    private IThing subdevThing = null;

    public SubThingSample(String pk, String dn) {
        super(pk, dn);
        baseInfo = new BaseInfo();
        baseInfo.productKey = pk;
        baseInfo.deviceName = dn;

        subdevThing = LinkKit.getInstance().getGateway().getSubDeviceThing(baseInfo).first;
    }

    public void readData(String path) {
        String data = FileUtils.readFile(path);
        mThingData = mGson.fromJson(data, ThingData.class);
        if (mThingData == null) {
            ALog.e(TAG, "数据格式错误");
            return;
        }
        identity = mThingData.identifier;
        value = mThingData.value;
        if ("event".equals(mThingData.type)) {
            isEvent = true;
            getPostEvent();
        } else {
            isEvent = false;
            getPost(identity, value);
        }
    }

    private void getPost(String identity, String value) {
        try {
            if (StringUtils.isEmptyString(identity)) {
                ALog.w(TAG, "属性错误");
                return;
            }
            List<Property> propertyList = subdevThing.getProperties();
            if (propertyList == null) {
                ALog.w(TAG, "选择的产品property列表为空");
                return;
            }
            Property property = null;
            for (int i = 0; i < propertyList.size(); i++) {
                property = propertyList.get(i);
                if (property == null) {
                    continue;
                }
                if (identity.equals(property.getIdentifier())) {
                    break;
                }
                property = null;
            }
            if (property == null) {
                ALog.w(TAG, "属性不存在");
                return;
            }
            if (TmpConstant.TYPE_VALUE_INTEGER.equals(property.getDataType().getType())) {
                int parseData = getInt(value);
                if (parseData != DEF_VALUE) {
                    updateCache(property.getIdentifier(), new ValueWrapper.IntValueWrapper(parseData));
                } else {
                    ALog.w(TAG, "数据格式不对");
                }
                return;
            }
            if (TmpConstant.TYPE_VALUE_FLOAT.equals(property.getDataType().getType())) {
                Double parseData = getDouble(value);
                if (parseData != null) {
                    updateCache(property.getIdentifier(), new ValueWrapper.DoubleValueWrapper(parseData));
                } else {
                    ALog.w(TAG, "数据格式不对");
                }
                return;
            }
            if (TmpConstant.TYPE_VALUE_DOUBLE.equals(property.getDataType().getType())) {
                Double parseData = getDouble(value);
                if (parseData != null) {
                    updateCache(property.getIdentifier(), new ValueWrapper.DoubleValueWrapper(parseData));
                } else {
                    ALog.w(TAG, "数据格式不对");
                }
                return;
            }
            if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(property.getDataType().getType())) {
                int parseData = getInt(value);
                if (parseData == 0 || parseData == 1) {
                    updateCache(property.getIdentifier(), new ValueWrapper.BooleanValueWrapper(parseData));
                } else {
                    ALog.w(TAG, "数据格式不对");
                }
                return;
            }
            if (TmpConstant.TYPE_VALUE_TEXT.equals(property.getDataType().getType())) {
                updateCache(property.getIdentifier(), new ValueWrapper.StringValueWrapper(value));
                return;
            }
            if (TmpConstant.TYPE_VALUE_DATE.equals(property.getDataType().getType())) {
                updateCache(property.getIdentifier(), new ValueWrapper.DateValueWrapper(value));
                return;
            }
            if (TmpConstant.TYPE_VALUE_ENUM.equalsIgnoreCase(property.getDataType().getType())) {
                updateCache(property.getIdentifier(), new ValueWrapper.EnumValueWrapper(getInt(value)));
                return;
            }
            if (TmpConstant.TYPE_VALUE_ARRAY.equalsIgnoreCase(property.getDataType().getType())) {
                ValueWrapper.ArrayValueWrapper arrayValueWrapper = GsonUtils.fromJson(value, new TypeToken<ValueWrapper>() {
                }.getType());
                updateCache(property.getIdentifier(), arrayValueWrapper);
                return;
            }
            // 结构体数据解析  结构体不支持嵌套结构体和数组
            if (TmpConstant.TYPE_VALUE_STRUCT.equals(property.getDataType().getType())) {
                try {
                    List<Map<String, Object>> specsList = (List<Map<String, Object>>) property.getDataType().getSpecs();
                    if (specsList == null || specsList.size() == 0) {
                        ALog.w(TAG, "云端创建的struct结构为空，不上传任何值。");
                        return;
                    }
                    Gson gson = new Gson();
                    JsonObject dataJson = gson.fromJson(value, JsonObject.class);
                    Map<String, ValueWrapper> dataMap = new HashMap<String, ValueWrapper>();
                    Map<String, Object> specsItem = null;
                    for (int i = 0; i < specsList.size(); i++) {
                        specsItem = specsList.get(i);
                        if (specsItem == null) {
                            continue;
                        }
                        String idKey = (String) specsItem.get("identifier");
                        String dataType = (String) ((Map) specsItem.get("dataType")).get("type");
                        if (idKey != null && dataJson.has(idKey) && dataType != null) {
                            ValueWrapper valueItem = null;
                            if ("int".equals(dataType)) {
                                valueItem = new ValueWrapper.IntValueWrapper(getInt(String.valueOf(dataJson.get(idKey))));
                            } else if ("text".equals(dataType)) {
                                valueItem = new ValueWrapper.StringValueWrapper(dataJson.get(idKey).getAsString());
                            } else if ("float".equals(dataType) || "double".equals(dataType)) {
                                valueItem = new ValueWrapper.DoubleValueWrapper(getDouble(String.valueOf(dataJson.get(idKey))));
                            } else if ("bool".equals(dataType)) {
                                valueItem = new ValueWrapper.BooleanValueWrapper(getInt(String.valueOf(dataJson.get(idKey))));
                            } else if ("date".equals(dataType)) {
                                if (isValidInt(String.valueOf(dataJson.get(idKey)))) {
                                    valueItem = new ValueWrapper.DateValueWrapper(String.valueOf(dataJson.get(idKey)));
                                } else {
                                    ALog.w(TAG, "数据格式不对");
                                }
                            } else if ("enum".equals(dataType)) {
                                valueItem = new ValueWrapper.EnumValueWrapper(getInt(String.valueOf(dataJson.get(idKey))));
                            } else {
                                ALog.w(TAG, "数据格式不支持");
                            }
                            if (valueItem != null) {
                                dataMap.put(idKey, valueItem);
                            }
                        }
                    }

                    updateCache(property.getIdentifier(), new ValueWrapper.StructValueWrapper(dataMap));
                } catch (Exception e) {
                    ALog.e(TAG, "数据格式不正确");
                }
                return;
            }
            ALog.w(TAG, "该类型Demo暂不支持，用户可参照其他类型代码示例开发支持。");
        } catch (Exception e) {
            ALog.e(TAG, "数据格式不对");
            e.printStackTrace();
        }
    }

    private void getPostEvent() {
        if (StringUtils.isEmptyString(identity)) {
            ALog.w(TAG, "事件identifier错误");
            return;
        }
        List<Event> propertyList = subdevThing.getEvents();
        if (propertyList == null) {
            ALog.w(TAG, "选择的产品 event列表为空");
            return;
        }
        Event event = null;
        for (int i = 0; i < propertyList.size(); i++) {
            event = propertyList.get(i);
            if (event == null) {
                continue;
            }
            if (identity.equals(event.getIdentifier())) {
                break;
            }
            event = null;
        }
        if (event == null) {
            ALog.w(TAG, "事件不存在");
            return;
        }

        HashMap<String, ValueWrapper> hashMap = new HashMap<String, ValueWrapper>();
        try {
            JSONObject object = JSONObject.parseObject(value);
            if (object == null) {
                ALog.d(TAG, "参数不能为空");
                return;
            }
            if (event.getOutputData() != null) {
                for (int i = 0; i < event.getOutputData().size(); i++) {
                    Arg arg = event.getOutputData().get(i);
                    if (arg == null || arg.getDataType() == null || arg.getIdentifier() == null) {
                        continue;
                    }
                    String idnValue = String.valueOf(object.get(arg.getIdentifier()));
                    if (idnValue == null || object.get(arg.getIdentifier()) == null) {
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_INTEGER.equals(arg.getDataType().getType())) {
                        int parseData = getInt(idnValue);
                        if (parseData != DEF_VALUE) {
                            hashMap.put(arg.getIdentifier(), new ValueWrapper.IntValueWrapper(parseData));
                        } else {
                            ALog.d(TAG, "数据格式不对");
                            break;
                        }
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_FLOAT.equals(arg.getDataType().getType())) {
                        Double parseData = getDouble(idnValue);
                        if (parseData != null) {
                            hashMap.put(arg.getIdentifier(), new ValueWrapper.DoubleValueWrapper(parseData));
                        } else {
                            ALog.d(TAG, "数据格式不对");
                            break;
                        }
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_DOUBLE.equals(arg.getDataType().getType())) {
                        Double parseData = getDouble(idnValue);
                        if (parseData != null) {
                            hashMap.put(arg.getIdentifier(), new ValueWrapper.DoubleValueWrapper(parseData));
                        } else {
                            ALog.d(TAG, "数据格式不对");
                            break;
                        }
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_BOOLEAN.equals(arg.getDataType().getType())) {
                        int parseData = getInt(idnValue);
                        if (parseData == 0 || parseData == 1) {
                            hashMap.put(arg.getIdentifier(), new ValueWrapper.BooleanValueWrapper(parseData));
                        } else {
                            ALog.d(TAG, "数据格式不对");
                            break;
                        }
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_TEXT.equals(arg.getDataType().getType())) {
                        hashMap.put(arg.getIdentifier(), new ValueWrapper.StringValueWrapper(idnValue));
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_DATE.equals(arg.getDataType().getType())) {
                        hashMap.put(arg.getIdentifier(), new ValueWrapper.DateValueWrapper(idnValue));
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_ENUM.equalsIgnoreCase(arg.getDataType().getType())) {
                        hashMap.put(arg.getIdentifier(), new ValueWrapper.EnumValueWrapper(getInt(idnValue)));
                        continue;
                    }
                    if (TmpConstant.TYPE_VALUE_ARRAY.equalsIgnoreCase(arg.getDataType().getType())) {
                        ValueWrapper.ArrayValueWrapper arrayValueWrapper = GsonUtils.fromJson(idnValue, new TypeToken<ValueWrapper>() {
                        }.getType());
                        hashMap.put(arg.getIdentifier(), arrayValueWrapper);
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(TAG, "数据格式错误");
            return;
        }
        valueWrapperMap = hashMap;
    }

    private void reportEvent() {
        OutputParams params = new OutputParams(valueWrapperMap);
        subdevThing.thingEventPost(identity, params, new IPublishResourceListener() {
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
     * 上报灯的状态
     */
    private void updateCache(String identifier, ValueWrapper valueWrapper) {
        identity = identifier;
        this.valueWrapper = valueWrapper;
    }

    public void report() {
        if (isEvent) {
            ALog.d(TAG, "上报事件" + identity);
            reportEvent();
        } else {
            ALog.d(TAG, "上报属性" + identity);
            reportProperty();
        }
    }

    private void reportProperty(){
        if (StringUtils.isEmptyString(identity) || valueWrapper == null) {
            ALog.e(TAG, "数据格式错误");
            return;
        }

        ALog.d(TAG, "上报 属性identity=" + identity);

        Map<String, ValueWrapper> reportData = new HashMap<String, ValueWrapper>();
        reportData.put(identity, valueWrapper);
        subdevThing.thingPropertyPost(reportData, new IPublishResourceListener() {

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
     * 云端调用设备的某项服务的时候，设备端需要响应该服务并回复。
     * 设备端事件触发的时候需要调用这个接口上报事件，如事件告警等
     * 需要用户在云端定义不同的 Error 的类型
     */
    public void setServiceHandler() {
        ALog.d(TAG, "setServiceHandler() called");
        List<Service> srviceList = subdevThing.getServices();
        for (int i = 0; srviceList != null && i < srviceList.size(); i++) {
            Service service = srviceList.get(i);
            subdevThing.setServiceHandler(service.getIdentifier(), mCommonHandler);
        }
        LinkKit.getInstance().registerOnNotifyListener(connectNotifyListener);
        //
    }

    private String printAMessage(AMessage aMessage) {
        return (aMessage == null || aMessage.data == null) ? "" : new String((byte[]) aMessage.data);
    }

    private IConnectNotifyListener connectNotifyListener = new IConnectNotifyListener() {
        public void onNotify(String connectId, String topic, AMessage aMessage) {
            ALog.d(TAG, "onNotify() called with: connectId = [" + connectId + "], topic = [" + topic + "], aMessage = [" + printAMessage(aMessage) + "]");
            if (CONNECT_ID.equals(connectId) && !StringUtils.isEmptyString(topic) &&
                    topic.startsWith("/sys/" + productKey + "/" + deviceName + "/rrpc/request")) {
                ALog.d(TAG, "收到云端同步下行" + printAMessage(aMessage));
//                    ALog.d(TAG, "receice Message=" + new String((byte[]) aMessage.data));
                // 服务端返回数据示例  {"method":"thing.service.test_service","id":"123374967","params":{"vv":60},"version":"1.0.0"}
                MqttPublishRequest request = new MqttPublishRequest();
                request.isRPC = false;
                request.topic = topic.replace("request", "response");
                String resId = topic.substring(topic.indexOf("rrpc/request/") + 13);
                request.msgId = resId;
                // TODO 用户根据实际情况填写 仅做参考
                request.payloadObj = "{\"id\":\"" + resId + "\", \"code\":\"200\"" + ",\"data\":{} }";
//                    aResponse.data =
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
                //                    ALog.d(TAG, "receice Message=" + new String((byte[]) aMessage.data));
                // 服务端返回数据示例  {"method":"thing.service.test_service","id":"123374967","params":{"vv":60},"version":"1.0.0"}
                MqttPublishRequest request = new MqttPublishRequest();
                // 支持 0 和 1， 默认0
                //                request.qos = 0;
                request.isRPC = false;
                request.topic = topic.replace("request", "response");
                String[] array = topic.split("/");
                String resId = array[3];
                request.msgId = resId;
                // TODO 用户根据实际情况填写 仅做参考
                request.payloadObj = "{\"id\":\"" + resId + "\", \"code\":\"200\"" + ",\"data\":{} }";
                //                    aResponse.data =
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
        }

        public boolean shouldHandle(String s, String s1) {
            return true;
        }

        public void onConnectStateChange(String s, ConnectState connectState) {

        }
    };

    private ITResRequestHandler mCommonHandler = new ITResRequestHandler() {
        public void onProcess(String identify, Object result, ITResResponseCallback itResResponseCallback) {
            ALog.d(TAG, "onProcess() called with: s = [" + identify + "], o = [" + result + "], itResResponseCallback = [" + itResResponseCallback + "]");
            ALog.d(TAG, "收到云端异步服务调用 " + identify);
            try {
                if (SERVICE_SET.equals(identify)) {
                    // TODO  用户按照真实设备的接口调用  设置设备的属性
                    // 设置完真实设备属性之后，上报设置完成的属性值
                    // 用户根据实际情况判断属性是否设置成功 这里测试直接返回成功
                    boolean isSetPropertySuccess = true;
                    if (isSetPropertySuccess) {
                        if (result instanceof InputParams) {
                            Map<String, ValueWrapper> data = (Map<String, ValueWrapper>) ((InputParams) result).getData();
//                        data.get()
                            ALog.d(TAG, "收到异步下行数据 " + data);
                            // 响应云端 接收数据成功
                            itResResponseCallback.onComplete(identify, null, null);
                        } else {
                            itResResponseCallback.onComplete(identify, null, null);
                        }
                    } else {
                        AError error = new AError();
                        error.setCode(100);
                        error.setMsg("setPropertyFailed.");
                        itResResponseCallback.onComplete(identify, new ErrorInfo(error), null);
                    }

                } else if (SERVICE_GET.equals(identify)) {
                    //  初始化的时候将默认值初始化传进来，物模型内部会直接返回云端缓存的值

                } else {
                    // 根据不同的服务做不同的处理，跟具体的服务有关系
                    ALog.d(TAG, "用户根据真实的服务返回服务的值，请参照set示例");
                    OutputParams outputParams = new OutputParams();
//                    outputParams.put("op", new ValueWrapper.IntValueWrapper(20));
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

    private boolean isValidDouble(String value) {
        if (StringUtils.isEmptyString(value)) {
            return false;
        }
        try {
            if (pattern != null && pattern.matcher(value) != null) {
                if (pattern.matcher(value).matches()) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Double getDouble(String value) {
        if (isValidDouble(value)) {
            return Double.parseDouble(value);
        }
        return null;
    }

    private boolean isValidInt(String value) {
        if (!StringUtils.isEmptyString(value)) {
            return true;
        }
        return false;
    }


    private int getInt(String value) {
        if (isValidInt(value)) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return DEF_VALUE;
    }
}
