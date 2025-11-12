package io.github.hkx419.casdoor;

import lombok.Data;

@Data
public class Response<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Response<T> ok(T data, String msg) {
        Response<T> resp = new Response<>();
        resp.setCode(0);
        resp.setMsg(msg);
        resp.setData(data);
        return resp;
    }

    public static <T> Response<T> error(String msg) {
        Response<T> resp = new Response<>();
        resp.setCode(-1);
        resp.setMsg(msg);
        resp.setData(null);
        return resp;
    }
}