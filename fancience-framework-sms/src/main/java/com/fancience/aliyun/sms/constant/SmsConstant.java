package com.fancience.aliyun.sms.constant;

import cn.hutool.setting.Setting;

/**
 * @Author: Jonah.Chen
 * @Date: 14:59 30/03/2018
 * @Desc: 短信相关常量类（新增模板在此添加相关设置常量）
 */
public class SmsConstant {

    public static final String ALIYUN_SMS_SETTING_PATH = "aliyunSms.setting";

    private static final Setting aliyunSmsSetting = new Setting(ALIYUN_SMS_SETTING_PATH);

    public static final String ACCESS_KET_ID = aliyunSmsSetting.getStr(SettingEnum.ACCESS_KEY_ID.getKey());

    public static final String ACCESS_KEY_SECRET = aliyunSmsSetting.getStr(SettingEnum.ACCESS_KEY_SECRET.getKey());

    public static final String SIGN_NAME_GENERAL = aliyunSmsSetting.getStr(SettingEnum.SIGN_NAME_GENERAL.getKey());

    public static final String TEMPLATE_CODE_REGISTER = aliyunSmsSetting.getStr(SettingEnum.TEMPLATE_CODE_REGISTER.getKey());

    public static final String TEMPLATE_CODE_CHANGEPWD = aliyunSmsSetting.getStr(SettingEnum.TEMPLATE_CODE_CHANGEPWD.getKey());

    public static void main(String[] args) {
        System.out.println(ACCESS_KET_ID);
        System.out.println(ACCESS_KEY_SECRET);
        System.out.println(SIGN_NAME_GENERAL);
        System.out.println(TEMPLATE_CODE_REGISTER);
    }

}
