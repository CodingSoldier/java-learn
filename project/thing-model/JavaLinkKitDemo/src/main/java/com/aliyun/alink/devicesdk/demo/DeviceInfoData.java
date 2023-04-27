package com.aliyun.alink.devicesdk.demo;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.dm.api.BaseInfo;
import com.aliyun.alink.dm.api.DeviceInfo;

import java.util.List;

/*
 * Copyright (c) 2014-2016 Alibaba Group. All rights reserved.
 * License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

public class DeviceInfoData extends DeviceInfo {

    /**
     * 区域
     */
    public String region = "cn-shanghai";
    /**
     * 与网关关联的子设备信息
     * 后续网关测试demo 会 添加子设备 删除子设备 建立 topo关系 子设备上下线等
     */
    public List<BaseInfo> subDevice = null;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 实例id
     */
    public String instanceId = "";
}
