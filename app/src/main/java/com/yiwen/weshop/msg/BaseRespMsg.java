package com.yiwen.weshop.msg;



public class BaseRespMsg {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 0;
    public static final String MSG_SUCCESS = "success";

    protected int status = STATUS_SUCCESS;
    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
