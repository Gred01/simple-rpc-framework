package com.github.liyue2008.rpc.hello.entity;

import java.io.Serializable;

public class HelloResult implements Serializable {

    private String resultCode;
    private boolean isSuccess;
    private Object result;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "HelloResult{" +
                "resultCode='" + resultCode + '\'' +
                ", isSuccess=" + isSuccess +
                ", result=" + result +
                '}';
    }
}
