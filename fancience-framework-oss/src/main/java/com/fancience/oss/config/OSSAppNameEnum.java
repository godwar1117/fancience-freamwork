package com.fancience.oss.config;

/**
 * @Package com.fancience.oss.config
 * @Description:
 * @author: shoucai.wang
 * @date: 12/01/2018 17:20
 */
public enum OSSAppNameEnum {
    FANCIENCE_DEV("FANCIENCE_DEV"),
    FANCIENCE_TEST("FANCIENCE_TEST"),
    FANCIENCE("FANCIENCE");

    private String appName;

    OSSAppNameEnum(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
