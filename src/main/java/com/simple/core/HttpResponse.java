package com.simple.core;

/**
 * Created by Dong Wang.
 * Created on 15/6/5 15:15
 * <p/>
 * 响应实例
 */
public class HttpResponse {
    private int code;
    private String body;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return code == that.code && !(body != null ? !body.equals(that.body) : that.body != null);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\":" + code +
                ", \"body\":\"" + body + "\"" +
                "}";
    }
}
