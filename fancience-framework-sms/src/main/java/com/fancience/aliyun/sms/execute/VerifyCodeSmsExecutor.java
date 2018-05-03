package com.fancience.aliyun.sms.execute;


import cn.hutool.core.util.ObjectUtil;
import com.fancience.aliyun.sms.SmsClient;
import com.fancience.aliyun.sms.constant.SmsConstant;
import com.fancience.aliyun.sms.template.SmsTemplate;

public class VerifyCodeSmsExecutor {

    /**
     * @Author: Jonah.Chen
     * @Date: 16:04 30/03/2018
     * @Desc: 发送注册短信
     */
    public static boolean sendRegisterSms(String mobile, String vCode) {
        return sendSms(mobile, vCode, SmsConstant.TEMPLATE_CODE_REGISTER);
    }

    /**
     * @Author: Jonah.Chen
     * @Date: 16:04 30/03/2018
     * @Desc: 发送注册短信
     */
    public static boolean sendChangePwdSms(String mobile, String vCode) {
        return sendSms(mobile, vCode, SmsConstant.TEMPLATE_CODE_CHANGEPWD);
    }

    /**
     * @Author: Jonah.Chen
     * @Date: 16:04 30/03/2018
     * @Desc: 发送验证码短信
     */
    public static boolean sendSms(String mobile, String vCode, String templeteCode) {
        if (ObjectUtil.isNull(mobile) || ObjectUtil.isNull(vCode)) {
            return false;
        }
        SmsClient smsClient = new SmsClient(
                SmsConstant.ACCESS_KET_ID, SmsConstant.ACCESS_KEY_SECRET);
        SmsTemplate smsTemplate = new SmsTemplate(
                SmsConstant.SIGN_NAME_GENERAL,
                templeteCode)
                .addTemplateParam("code", vCode)
                .addPhoneNum(mobile);
        smsClient.send(smsTemplate);
        return true;
    }

    public static void main(String[] args) {
        VerifyCodeSmsExecutor.sendChangePwdSms("17621321019", "1234");
    }
}
