package com.hellobike.pmo.cockpit.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.hellobike.pmo.cockpit.model.common.Proto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author renmengxiwb304
 * @date 2024/2/16
 */
@Slf4j
public class HttpUtil {

    public static Map<String,String> headers(){
        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return  headersMap;
    }

    /**
     * @param url url路径
     * @param headers header头，参数以map格式传递
     * @param params url参数，以map格式传递
     */
    public static String doGet(String url, Map<String,String> headers, String params) {
        String result = StringUtils.EMPTY;
        if (StringUtils.isEmpty(url)) {
            return result;
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
//            url = buildGetUrl(url, params);
            url = url + "?" + params;
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            URIBuilder uriBuilder = new URIBuilder(url);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            // 设置请求头信息，鉴权
            setHeader(httpGet, headers);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(5000)// 请求超时时间
                    .setSocketTimeout(5000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            if (response.getStatusLine().getStatusCode() != 200) {
                result = JSON.toJSONString(Proto.fail(null, EntityUtils.toString(entity)));
            } else {
                result = EntityUtils.toString(entity);
            }
        } catch (URISyntaxException e) {
            log.error("doGet url:"+ url +" params:"+ JSON.toJSONString(params)+" URISyntaxException:" + e.getMessage());
            result = JSON.toJSONString(Proto.fail(null, e.getMessage()));
        } catch (ClientProtocolException e) {
            log.error("doGet url:"+ url +" params:"+ JSON.toJSONString(params)+" ClientProtocolException:" + e.getMessage());
        } catch (IOException e) {
            log.error("doGet url:"+ url +" params:"+ JSON.toJSONString(params)+" IOException:" + e.getMessage());
            result = JSON.toJSONString(Proto.fail(null, e.getMessage()));
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String buildGetUrl(String url, Map<String, String> params) {
        if (StringUtils.isEmpty(url)) {
            return StringUtils.EMPTY;
        }
        if (params == null || params.size() < 1) {
            return url;
        }
        StringBuilder str = new StringBuilder();
        str.append(url);
        str.append("?");
        int size = 0;
        for (Map.Entry<String, String> entrySet : params.entrySet()) {
            if (entrySet.getKey() == null || entrySet.getValue() == null) {
                continue;
            }
            str.append(entrySet.getKey());
            str.append("=");
            str.append(entrySet.getValue());
            size++;
            if (size < params.entrySet().size()) {
                str.append("&");
            }
        }
        return str.toString();
    }


    private static void setHeader(HttpGet httpGet, Map<String,String> headers) {
        if (headers == null || headers.size() < 1) {
            return;
        }
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            if (entrySet.getKey() == null || entrySet.getValue() == null) {
                continue;
            }
            httpGet.setHeader(entrySet.getKey(), entrySet.getValue());
        }
    }

    /**
     * post body提交
     * @param url
     * @param params 参数
     * @return
     */
    public static String doPost(String url, Map<String,String> headers,String params) {
        String result = StringUtils.EMPTY;
        if (StringUtils.isEmpty(url)) {
            return result;
        }
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(5000)// 设置连接请求超时时间
                .setSocketTimeout(5000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        setPostHeader(httpPost, headers);
        // 封装post请求参数
        setEntity(httpPost, params);
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                result = JSON.toJSONString(Proto.fail(null, EntityUtils.toString(entity)));
            } else {
                result = EntityUtils.toString(entity);
            }
        } catch (ClientProtocolException e) {
            log.error("doPost url:"+ url +" params:"+ JSON.toJSONString(params)+" ClientProtocolException:" + e.getMessage());
            result = JSON.toJSONString(Proto.fail(null, e.getMessage()));
        } catch (IOException e) {
            log.error("doPost url:"+ url +" params:"+ JSON.toJSONString(params)+" IOException:" + e.getMessage());
            result = JSON.toJSONString(Proto.fail(null, e.getMessage()));
        } finally {
            // 关闭资源
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    private static void setPostHeader(HttpPost httpPost, Map<String,String> headers) {
        if (headers == null || headers.size() < 1) {
            return;
        }
        for (Map.Entry<String, String> entrySet : headers.entrySet()) {
            if (entrySet.getKey() == null || entrySet.getValue() == null) {
                continue;
            }
            httpPost.setHeader(entrySet.getKey(), entrySet.getValue());
        }
    }


    private static void setEntity(HttpPost httpPost, String params) {
        JSONObject jsonParams = JSON.parseObject(params);
        httpPost.setEntity(new StringEntity(jsonParams.toString(), "UTF-8"));
    }
    private String buildURL(String u, String path){
        String url = u+path;
        return url;
    }

    private String getPostResponse(String url,String param,String token){
        Map<String, String> headers = HttpUtil.headers();
        headers.put("token",token);
        String response = HttpUtil.doPost(url,headers,param);
        return response;
    }
}
