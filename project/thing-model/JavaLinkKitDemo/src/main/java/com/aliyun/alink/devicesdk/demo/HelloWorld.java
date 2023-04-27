package com.aliyun.alink.devicesdk.demo;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.apiclient.CommonRequest;
import com.aliyun.alink.apiclient.CommonResponse;
import com.aliyun.alink.apiclient.IoTCallback;
import com.aliyun.alink.apiclient.utils.StringUtils;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.InitResult;
import com.aliyun.alink.dm.api.IoTApiClientConfig;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.channel.core.base.ARequest;
import com.aliyun.alink.linksdk.channel.core.base.IOnCallListener;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttInitParams;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HelloWorld {
    private static final String TAG = "HelloWorld";

    private String pk, dn;
    private ThingSample thingTestManager = null;

    public static void main(String[] args) {
        ALog.d(TAG, "Hello world!");
        ALog.setLevel(ALog.LEVEL_DEBUG);
        HelloWorld manager = new HelloWorld();
        ALog.d(TAG, "args=" + Arrays.toString(args));
        System.out.println(System.getProperty("user.dir"));
        String diPath = System.getProperty("user.dir") + "/device_id.json";
        // TODO
        String deviceInfo = FileUtils.readFile("D:\\mycode\\java-learn\\project\\JavaLinkKitDemo\\device_id.json");
        if (deviceInfo == null) {
            ALog.e(TAG, "main - need device info path.");
            return;
        }
        Gson mGson = new Gson();
        DeviceInfoData deviceInfoData = mGson.fromJson(deviceInfo, DeviceInfoData.class);
        if (deviceInfoData == null) {
            ALog.e(TAG, "main - deviceInfo format error.");
            return;
        }

        // 如果device_id.json中没有设置deviceSecret, demo默认先走动态注册方式获取秘钥
        if (StringUtils.isEmptyString(deviceInfoData.deviceSecret)) {
            manager.deviceRegister(deviceInfoData);
            ALog.d(TAG, "测试一型一密动态注册，只测试动态注册");
            ALog.d(TAG, "请将获取到的deviceSecret填入到deviceId.json文件中继续一型一密的流程");
            return;
        }

        ALog.d(TAG, "测试一机一密和物模型");
        manager.init(deviceInfoData);
    }


    public void init(final DeviceInfoData deviceInfoData) {
        this.pk = deviceInfoData.productKey;
        this.dn = deviceInfoData.deviceName;
        LinkKitInitParams params = new LinkKitInitParams();
        /**
         * 设置 Mqtt 初始化参数
         */
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = deviceInfoData.productKey;
        config.deviceName = deviceInfoData.deviceName;
        config.deviceSecret = deviceInfoData.deviceSecret;

        if(!deviceInfoData.instanceId.isEmpty()) {
            //如果实例详情页面有实例的id, 建议开发者填入实例id. 推荐的做法
            config.channelHost = "ssl://" + deviceInfoData.instanceId + ".mqtt.iothub.aliyuncs.com:443";
        } else {
            //如果实例详情页面没有实例的id, 建议开发者填入实例所在的region. 注：该用法不支持深圳和北京两个region
            config.channelHost = deviceInfoData.productKey + ".iot-as-mqtt." + deviceInfoData.region + ".aliyuncs.com:443";
        }

        /**
         * 是否接受离线消息
         * 对应 mqtt 的 cleanSession 字段
         */
        config.receiveOfflineMsg = false;
        params.mqttClientConfig = config;


        /**
         * 设置初始化三元组信息，用户传入
         */
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.productKey = pk;
        deviceInfo.deviceName = dn;
        deviceInfo.deviceSecret = deviceInfoData.deviceSecret;

        params.deviceInfo = deviceInfo;

        /**
         * 设置设备当前的初始状态值，属性需要和云端创建的物模型属性一致
         * 如果这里什么属性都不填，物模型就没有当前设备相关属性的初始值。
         * 用户调用物模型上报接口之后，物模型会有相关数据缓存。
         */
        Map<String, ValueWrapper> propertyValues = new HashMap<String, ValueWrapper>();
        params.propertyValues = propertyValues;
        params.fmVersion = "1.0.2";

        /**
         * 设备进行初始化，并连云
         */
        LinkKit.getInstance().init(params, new ILinkKitConnectListener() {
            public void onError(AError aError) {
                ALog.e(TAG, "Init Error error=" + aError);
            }

            public void onInitDone(InitResult initResult) {
                ALog.i(TAG, "onInitDone result=" + initResult);
                executeScheduler(deviceInfoData);
            }
        });
    }

    /**
     * 定时执行
     * @param deviceInfoData
     */
    public void executeScheduler(DeviceInfoData deviceInfoData) {

       /**
        * 测试物模型，请参照testDeviceModel函数中的TODO注释将物模型字段替换为当前产品的物模型数据
        */
       testDeviceModel();

       /** 测试单纯mqtt通信能力
        * 物模型用例json的格式，用户可以通过自定义的topic使用自定义的格式, 用户可以使用SDK中单纯的mqtt通信能力满足这方面的开发需求
        */
        // testMqtt();

       /**
        * 测试设备标签
        */
       // testLabel();

       /**
        * 测试远程配置
        */
        // testCota();

       /**
        * 测试网关子设备管理功能，高级版功能
        */
       // testGateway(deviceInfoData);

       /**
        *  测试获取设备影子
        */
       // testDeviceShadow();
    }

    /**
     *  物模型测试代码
     */
    private void testDeviceModel(){
        thingTestManager = new ThingSample(pk, dn);
        /* 创建下行消息处理回调 */
        thingTestManager.setServiceHandler();
        /* 上报属性 */
        thingTestManager.reportDemoProperty();
        /* 上报事件 */
        thingTestManager.reportDemoEvent();
    }

    /**
     *  设备影子测试代码
     */
    private void testDeviceShadow() {
        DeviceShadowSample sample = new DeviceShadowSample();
        try {
            sample.listenDownStream();
            sample.shadowGet();
            try {
                Thread.sleep(5*1000);
            } catch (Exception e){

            }
            // 异步操作，注意别和删除操作一起执行，不能保持时序
            sample.shadowUpdate();

            // 异步操作，注意别和更新一起执行
            // sample.shadowDelete();
            // 异步操作，
            // sample.shadowDeleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
            //sample.shadowDelete();
    }


    /**
     * 动态注册示例代码，适用于所有region
     * 1.现在云端创建产品和设备；
     * 2.在云端开启动态注册；
     * 3.填入pk、dn、ps；
     * 4.调用该方法；
     * 5.拿到deviceSecret返回之后 调初始化建联；
     */
    public void deviceRegister( DeviceInfoData deviceInfoData) {

        //动态注册step1: 确定一型一密的类型（免预注册, 还是非免预注册）
        //case 1: 如果registerType里面填写了regnwl, 表明设备的一型一密方式为免预注册（即无需创建设备）
        //case 2: 如果这个字段为空, 则表示为需要预注册的一型一密（需要实现创建设备）
        String registerType = "register";

        //动态注册step2: 设置动态注册的注册接入点域名
        if(!deviceInfoData.instanceId.isEmpty()) {
            //如果实例详情页面有实例的id, 建议开发者填入实例id. 推荐的做法
            MqttConfigure.mqttHost = "ssl://" + deviceInfoData.instanceId + ".mqtt.iothub.aliyuncs.com:443";
        } else {
            //如果实例详情页面没有实例的id, 建议开发者填入实例所在的region. 注：该用法不支持深圳和北京两个region
            MqttConfigure.mqttHost = deviceInfoData.productKey + ".iot-as-mqtt." + deviceInfoData.region + ".aliyuncs.com:443";
        }

        final MqttInitParams initParams = new MqttInitParams(deviceInfoData.productKey, deviceInfoData.productSecret, deviceInfoData.deviceName, "",registerType);

        //动态注册step3: 如果用户所用的实例为新版本的公共实例或者企业实例（控制台中有实例详情的页面）, 需设置动态注册的实例id
        initParams.instanceId = deviceInfoData.instanceId;

        final Object lock = new Object();
        LinkKit.getInstance().deviceDynamicRegister(initParams, new IOnCallListener() {
            @Override
            public void onSuccess(com.aliyun.alink.linksdk.channel.core.base.ARequest request, com.aliyun.alink.linksdk.channel.core.base.AResponse response) {
                try {
                    String responseData = new String((byte[]) response.data);
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    // 一型一密免预注册返回
                    String clientId = jsonObject.getString("clientId");
                    String deviceToken = jsonObject.getString("deviceToken");
                    // 一型一密预注册返回
                    String deviceSecret = jsonObject.getString("deviceSecret");
                    ALog.e(TAG, "mqtt dynamic registration succeed，deviceSecret:" + deviceSecret);
                    synchronized (lock){
                        lock.notify();
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailed(ARequest aRequest, com.aliyun.alink.linksdk.channel.core.base.AError aError) {
                ALog.e(TAG, "mqtt dynamic registration failed");
                synchronized (lock){
                    lock.notify();
                }
            }

            @Override
            public boolean needUISafety() {
                return false;
            }
        });

        try {
            //等待服务端下行报文，一般1s内就会返回
            synchronized (lock) {
                lock.wait(3000);
            }

            //动态注册step4: 关闭动态注册的实例.
            // 注意：该接口不能在LinkKit.getInstance().deviceDynamicRegister的onSuccess/onFailed回调中执行，否则会报错
            LinkKit.getInstance().stopDeviceDynamicRegister(2000, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    ALog.e(TAG,"mqtt dynamic registration success");
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    ALog.e(TAG,"mqtt dynamic registration failed");
                }
            });

        } catch (Exception e) {
        }
    }

    /**
     * 动态注册示例代码(仅上海region支持，不建议使用）
     * 1.现在云端创建产品和设备；
     * 2.在云端开启动态注册；
     * 3.填入pk、dn、ps；
     * 4.调用该方法；
     * 5.拿到deviceSecret返回之后 调初始化建联；
     */
    public void deviceRegisterDeprecated(DeviceInfo deviceInfo) {
        LinkKitInitParams params = new LinkKitInitParams();
        IoTMqttClientConfig config = new IoTMqttClientConfig();
        config.productKey = deviceInfo.productKey;
        config.deviceName = deviceInfo.deviceName;

        params.mqttClientConfig = config;
        params.connectConfig = new IoTApiClientConfig();

        params.deviceInfo = deviceInfo;

        final CommonRequest request = new CommonRequest();
        request.setPath("/auth/register/device");
        LinkKit.getInstance().deviceRegister(params, request, new IoTCallback() {
            public void onFailure(CommonRequest commonRequest, Exception e) {
                ALog.e(TAG, "动态注册失败 " + e);
            }

            public void onResponse(CommonRequest commonRequest, CommonResponse commonResponse) {
                if (commonResponse == null || StringUtils.isEmptyString(commonResponse.getData())) {
                    ALog.e(TAG, "动态注册失败 response=null");
                    return;
                }
                try {
                    ResponseModel<Map<String, String>> response = new Gson().fromJson(commonResponse.getData(), new TypeToken<ResponseModel<Map<String, String>>>() {
                    }.getType());
                    if (response != null && "200".equals(response.code)) {
                        ALog.d(TAG, "register success " + (commonResponse == null ? "" : commonResponse.getData()));
                        /**  获取 deviceSecret, 存储到本地，然后执行初始化建联
                         * 这个流程只能走一次，获取到 secret 之后，下次启动需要读取本地存储的三元组，
                         * 直接执行初始化建联，不可以再走动态初始化
                         */
                        // deviceSecret = response.data.get("deviceSecret");
                        // init(pk,dn,ds);
                        return;
                    }
                } catch (Exception e) {

                }
                ALog.d(TAG, "register fail " + commonResponse.getData());
            }
        });
    }



    private void deinit(){
        LinkKit.getInstance().deinit();
    }

    /**
     * 测试 Mqtt 基础topic封装
     * 发布
     * 订阅
     * 取消订阅
     * 注册资源监听，一般用于服务
     */
    private void testMqtt(){
        MqttSample sample = new MqttSample(pk, dn);
        sample.publish();
        sample.subscribe();
        sample.unSubscribe();
        sample.registerResource();
    }

    /**
     * 测试 COTA 远程配置
     */
    private void testCota() {
        COTASample sample = new COTASample(pk, dn);
        // 监听云端 COTA 下行数据更新
        sample.setCOTAChangeListener();
        // 获取 COTA 更新
        sample.cOTAGet();
    }

    /**
     * 标签测试
     */
    private void testLabel() {
        LabelSample sample = new LabelSample(pk, dn);
        // 测试标签更新
        sample.labelUpdate();
        // 测试标签删除
//        sample.labelDelete();
    }

    /**
     * @param deviceInfoData
     * 网关测试
     */
    private void testGateway(DeviceInfoData deviceInfoData) {
        GatewaySample sample = new GatewaySample(pk, dn, deviceInfoData.subDevice);
        sample.getSubDevices();
        // 注册 + 添加 + 登录 + 上报
        sample.subdevRegister();

//        try {
//            Thread.sleep(10*1000);
//            // 测试下线 + 删除
//            sample.subDevOffline();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }
}
