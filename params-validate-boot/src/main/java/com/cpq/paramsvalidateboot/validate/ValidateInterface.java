package com.cpq.paramsvalidateboot.validate;

import com.cpq.paramsvalidateboot.validate.bean.ResultValidate;

public interface ValidateInterface {
    /**
     * 返回json文件基础路径。regex-common-json.json文件必须放在此目录下
     * @return json文件基础路径
     */
    String basePath();

    /**
     * 参数校验未通过
     * @param resultValidate
     * @return 返回给客户端的数据
     */
    Object validateNotPass(ResultValidate resultValidate);

}
