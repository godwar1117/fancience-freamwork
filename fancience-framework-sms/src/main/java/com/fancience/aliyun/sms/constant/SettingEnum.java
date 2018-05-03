package com.fancience.aliyun.sms.constant;

/**
 * @Author: Jonah.Chen
 * @Date: 15:01 30/03/2018
 * @Desc: 设置文件枚举（更改配置文件后需要在此更新）
 */
public enum SettingEnum {

    ACCESS_KEY_ID("access_key_id", "access_key_id"),
    ACCESS_KEY_SECRET("access_key_secret", "access_key_secret"),
    SIGN_NAME_GENERAL("sign_name_general", "凡学教育通用短信签名"),
    TEMPLATE_CODE_REGISTER("template_code_register", "注册专用短信模板code"),
    TEMPLATE_CODE_CHANGEPWD("template_code_changepwd", "修改密码专用短信模板code");

    private String key;
    private String name;

    SettingEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
