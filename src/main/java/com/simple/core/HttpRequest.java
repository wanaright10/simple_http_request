package com.simple.core;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dong Wang.
 * Created on 15/6/5 14:58
 * <p/>
 * 发送http请求的类
 */
public class HttpRequest {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String data;

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getData() {
        return data;
    }

    private HttpRequest() {
        data = "";
        headers = new HashMap<>();
    }

    /**
     * 创建请求实例
     *
     * @return HttpRequest 实例
     */
    public static HttpRequest create() {
        return new HttpRequest();
    }

    /**
     * 添加一个响应头
     *
     * @param key   响应头的key
     * @param value 响应头的value
     * @return HttpRequest 实例
     */
    public HttpRequest addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    /**
     * 添加多个响应头
     *
     * @param headers 响应头map
     * @return HttpRequest 实例
     */
    public HttpRequest addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    /**
     * 发起GET请求
     *
     * @param url 请求的url
     * @return HttpRequest 实例
     */
    public HttpRequest get(String url) {
        this.url = url;
        method = "GET";
        return this;
    }

    /**
     * 发起GET请求 URL不加密
     *
     * @param url   请求的url
     * @param param 请求的表单参数
     * @return HttpRequest 实例
     */
    public HttpRequest get(String url, Map<String, String> param) {
        return get(url, param, false);
    }

    /**
     * 发起GET请求 URL不加密
     *
     * @param url    请求的url
     * @param param  请求的表单参数
     * @param encode 是否URL加密
     * @return HttpRequest 实例
     */
    public HttpRequest get(String url, Map<String, String> param, boolean encode) {
        String paramStr = mapParamToString(param, encode);

        url += ("?" + paramStr);

        this.url = url;
        method = "GET";
        return this;
    }

    /**
     * 发起POST请求 URL不加密
     *
     * @param url   请求的url
     * @param param 请求的表单参数
     * @return HttpRequest 实例
     */
    public HttpRequest post(String url, Map<String, String> param) {
        return post(url, param, false);
    }

    /**
     * 发起POST请求 URL不加密
     *
     * @param url    请求的url
     * @param param  请求的表单参数
     * @param encode 是否URL加密
     * @return HttpRequest 实例
     */
    public HttpRequest post(String url, Map<String, String> param, boolean encode) {
        data = mapParamToString(param, encode);
        this.url = url;
        method = "POST";
        return this;
    }

    /**
     * 正式发起请求
     *
     * @return HttpResponse 实例
     */
    public HttpResponse execute() {
        checkParam();
        HttpResponse result = new HttpResponse();

        try {
            URL localURL = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) localURL.openConnection();

            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(method.toUpperCase());

            headers.forEach(httpURLConnection::setRequestProperty);
            if (!data.isEmpty()) {
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
            }

            if (method.equals("POST")) {
                try (OutputStream outputStream = httpURLConnection.getOutputStream();
                     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream)) {
                    if (!data.isEmpty()) {
                        outputStreamWriter.write(data);
                        outputStreamWriter.flush();
                    }
                }
            }

            String responseBody = "";
            String responseLine;

            result.setCode(httpURLConnection.getResponseCode());
            if (result.getCode() < 300) {
                try (InputStream inputStream = httpURLConnection.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    while ((responseLine = reader.readLine()) != null) {
                        responseBody += responseLine;
                    }

                    result.setBody(responseBody);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 检验参数的合法性
     */
    private void checkParam() {
        if (url.isEmpty() || !url.startsWith("http")) {
            throw new IllegalArgumentException("url不合法");
        }

        if (method.isEmpty()) {
            throw new IllegalArgumentException("method不合法");
        }
    }

    /**
     * 将map里面的参数，转换为一行String
     *
     * @param param  表单参数
     * @param encode 是否加密
     * @return 一行的参数字符串
     */
    private String mapParamToString(Map<String, String> param, boolean encode) {
        String paramStr = "";
        if (param != null && param.size() != 0) {
            for (Map.Entry<String, String> item : param.entrySet()) {
                paramStr += ("&" + item.getKey() + "=" + item.getValue());
            }
        }

        if (!paramStr.isEmpty()) {
            paramStr = paramStr.substring(1);
        }

        if (encode) {
            try {
                paramStr = URLEncoder.encode(paramStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return paramStr;
    }
}
