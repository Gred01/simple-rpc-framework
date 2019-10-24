package com.github.liyue2008.rpc.hello.entity;

import java.io.Serializable;

public class HelloRequest implements Serializable {

    private Object param;
    private String timestamp;

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HelloRequest{" +
                "param=" + param +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
