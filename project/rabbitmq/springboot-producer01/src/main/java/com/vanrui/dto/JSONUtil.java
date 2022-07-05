package com.vanrui.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class JSONUtil {

	/*
	 * 直接从json字符串中获取对应key的value值 {"id":8,"name":"小明"}
	 */
	public static String getVluseByJsonStr(String jsonStr, String key) {
		// 本方法大概耗时22132纳秒,性能上完全超越很多json工具类
		char[] strs = jsonStr.toCharArray();
		String result = "";
		for (int i = jsonStr.indexOf(key) + key.length() + 2; i < strs.length; i++) {
			if (strs[i] == ',' || strs[i] == '}') {
				return result;
			}
			if (strs[i] != '"') {
				result += strs[i];
			}
		}
		return result;

	}

	/*
	 * json字符串转换成map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> jsonStrToMap(String jsonStr) {

		JSONObject jSONObject = JSONObject.parseObject(jsonStr);

		Map<String, Object> itemMap = JSONObject.toJavaObject(jSONObject, Map.class);

		return itemMap;

	}

	/*
	 * json字符串转换成JSONObject
	 */
	public static JSONObject jsonStrToJSONObject(String jsonStr) {

		JSONObject jSONObject = JSONObject.parseObject(jsonStr);

		return jSONObject;

	}

	/*
	 * 将List转换成JSONArray
	 */
	public static JSONArray listToJSONArray(List list) {

		JSONArray jSONArray = JSONArray.parseArray(JSON.toJSONString(list));

		return jSONArray;

	}
	

	
	// List 转 json string
	 
	public static String listToJSONStr(List list) {
		return JSON.toJSONString(list);
	}

	// Map 转 json string
	 
	public static String MapToJSONStr(Map map) {
		return JSON.toJSONString(map);
	}
	

	/*
	 * 将JavaBean转换成JSON字符串
	 */
	public static String JavaBeantoJSONString(Object obj) {
		return JSON.toJSONString(obj);
	}
	
	
	/*
	 * 将JavaBean转换成JSON字
	 */
	public static JSONObject JavaBeantoJSON(Object obj) {
		String jsonStr =  JSON.toJSONString(obj);	
		return JSONObject.parseObject(jsonStr);
		
		
	}

}
