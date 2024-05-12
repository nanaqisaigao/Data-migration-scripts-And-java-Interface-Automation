package com.hellobike.pmo.cockpit.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * @author renmengxiwb304
 * @date 2024/3/6
 */
public class ParseDataUtil {
    //将一个包含GET参数的字符串按照"&"分割成多个键值对，然后分别按照"="分割成键和值，最后将所有键值对存入一个HashMap中。
    public static Map<String,String> parseGetParams(String params){
        String[] paramsList = params.split("&");
        Map<String, String> parameters = new HashMap<>();
        for (String pathValuePair : paramsList) {
            String[] keyValue = pathValuePair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                parameters.put(key, value);
            }
        }
        return parameters;
       /*
       注意事项：
        输入的params字符串不能为空，否则会抛出异常。
        字符串分割操作可能会受到URL编码的影响，需要确保params字符串已经进行了正确的URL编码。
        由于使用了HashMap，对于重复的键值对，后面的值会覆盖前面的值。如果有需要，可以考虑使用
      其他映射类（如ConcurrentHashMap）来避免并发问题。
      */
    }

    public static Map<String, Object> parsePostParams(String params) {
        JSONObject paramsObject = JSONObject.parseObject(params);
        Map<String, Object> resultMap = new HashMap<>();
        parseJsonHelper(paramsObject, "", resultMap);
        return resultMap;
    }

    private static void parseJsonHelper(JSONObject jsonObject, String prefix, Map<String, Object> resultMap) {
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof JSONObject) {
                // 如果值是JSONObject，则递归解析
                JSONObject nestedObject = (JSONObject) value;
                parseJsonHelper(nestedObject, prefix + key + ".", resultMap);
            } else if (value instanceof JSONArray) {
                // 如果值是JSONArray，则递归解析每个元素
                JSONArray jsonArray = (JSONArray) value;
                for (int i = 0; i < jsonArray.size(); i++) {
                    Object arrayElement = jsonArray.get(i);
                    if (arrayElement instanceof JSONObject) {
                        parseJsonHelper((JSONObject) arrayElement, prefix + key + "[" + i + "].", resultMap);
                    } else {
                        // 如果值是字符串，则直接添加到结果Map中
                        resultMap.put(prefix + key + "[" + i + "]", arrayElement);
                    }
                }
                // 添加一个额外的字段用来存储数组长度
                resultMap.put(prefix + key + ".length", jsonArray.size());
            } else {
                // 如果值不是JSONObject或JSONArray，则直接添加到结果Map中
                resultMap.put(prefix + key, value);
            }
        }
    }

    public static void main(String[] args) {
        String params = "{\n" +
                "    \"buId\": \"1\",\n" +
                "    \"buName\": \"软件研发中心\",\n" +
                "    \"deptId\": [7, 9],\n" +
                "    \"deptName\": \"软件研发中心\",\n" +
                "    \"analysisType\": 1,\n" +
                "    \"analysisDate\": [\"2024\", \"2024\"]\n" +
                "}\n";

        Map<String, Object> resultMap = parsePostParams(params);
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

}
