package com.my.shirospringboot.shiro.core.base;



/**
 * @author Gzy
 * @version 1.0
 * @Description
 */

public class BaseResponse {
    private Integer code;
    private String msg;
    private String date;
    private String info;

    private static final long serialVersionUID = -1;

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Integer code, String msg, String info) {
        this.code = code;
        this.msg = msg;
        this.info = info;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
