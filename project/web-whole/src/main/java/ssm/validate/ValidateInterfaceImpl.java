package ssm.validate;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.github.codingsoldier.paramsvalidate.PvUtil;
import com.github.codingsoldier.paramsvalidate.ValidateInterfaceAdapter;
import com.github.codingsoldier.paramsvalidate.bean.Parser;
import com.github.codingsoldier.paramsvalidate.bean.PvConst;
import com.github.codingsoldier.paramsvalidate.bean.ResultValidate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ValidateInterfaceImpl extends ValidateInterfaceAdapter {


    /**
     * json解析器
     * 1、使用默认解析器jackson，可不覆盖此方法
     * 2、使用gson，请返回 new Parser(Gson.class);
     * 3、使用fastjson，请返回new Parser(JSON.class, Feature[].class)
     * 为了支持fastjson，搞得好坑爹
     */
    public Parser getParser(){
        //return new Parser(Gson.class);
        return new Parser(JSON.class, Feature[].class);
    }

    /**
     * 参数校验未通过, 返回自定义数据给客户端的数据
     * 我覆盖此方法，仅仅是为了方便自动化测试。
     * 此方法的代码和父类ValidateInterfaceAdapter的validateNotPass()代码几乎一样
     */
    @Override
    public Object validateNotPass(ResultValidate resultValidate) {
        List<Map<String, String>> msgList = resultValidate.getMsgList();
        /**
         * 父类ValidateInterfaceAdapter是new HashMap<>()
         * 我new TreeMap<>()是为了让返回结果有序，方便自动化测试
         */
        Map<String, String> data = new TreeMap<>();
        for (Map<String, String> elemMap:msgList){
            if (elemMap != null){
                Boolean requestVal = Boolean.parseBoolean(elemMap.get(PvConst.REQUEST));
                String minVal = elemMap.get(PvConst.MIN_VALUE);
                String maxVal = elemMap.get(PvConst.MAX_VALUE);
                String minLen = elemMap.get(PvConst.MIN_LENGTH);
                String maxLen = elemMap.get(PvConst.MAX_LENGTH);
                String jsonMsg = elemMap.get(PvConst.MESSAGE);

                String message = "";
                message = PvUtil.isNotBlankObj(jsonMsg) ? (message+jsonMsg+"，") : message;
                message = Boolean.TRUE.equals(requestVal) ? (message+"必填，") : message;
                message = PvUtil.isNotBlankObj(minVal) ? (message+"最小值"+minVal+"，") : message;
                message = PvUtil.isNotBlankObj(maxVal) ? (message+"最大值"+maxVal+"，") : message;
                message = PvUtil.isNotBlankObj(minLen) ? (message+"最小长度"+minLen+"，") : message;
                message = PvUtil.isNotBlankObj(maxLen) ? (message+"最大长度"+maxLen+"，") : message;
                message = "".equals(message) ? "未通过校验，" : message;
                message = message.substring(0, message.length()-1);

                String name = elemMap.get(PvConst.NAME);
                data.put(name, message);
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("code", resultValidate.isPass() ? 0 : 101);
        r.put("data", data);
        return r;
    }


}
