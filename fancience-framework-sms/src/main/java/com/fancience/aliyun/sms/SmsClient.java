package com.fancience.aliyun.sms;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fancience.aliyun.sms.exception.SmsException;
import com.fancience.aliyun.sms.template.SmsTemplate;
import com.fancience.aliyun.sms.util.Utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fancience.aliyun.sms.util.Utils.checkNotEmpty;
import static com.fancience.aliyun.sms.util.Utils.checkPhoneNumber;
import static com.fancience.aliyun.sms.util.Utils.checkSmsResponse;

public class SmsClient {

    private final IAcsClient acsClient;
    private final String product = "Dysmsapi";
    private final String domain = "dysmsapi.aliyuncs.com";
    private final String region = "cn-hangzhou";
    private final String endpointName = "cn-hangzhou";
    private final Map<String, SmsTemplate> smsTemplates;

    Log log = LogFactory.get();

    /**
     * Instantiates a new SmsClient.
     *
     * @param accessKeyId 阿里云短信 accessKeyId
     * @param accessKeySecret 阿里云短信 accessKeySecret
     */
    public SmsClient(final String accessKeyId, final String accessKeySecret) {
        this(accessKeyId, accessKeySecret, Collections.emptyMap());
    }

    /**
     * Instantiates a new SmsClient.
     *
     * @param accessKeyId 阿里云短信 accessKeyId
     * @param accessKeySecret 阿里云短信 accessKeySecret
     * @param smsTemplates 预置短信模板
     */
    public SmsClient(final String accessKeyId,
                     final String accessKeySecret,
                     final Map<String, SmsTemplate> smsTemplates) {
        if (ObjectUtil.isNull(accessKeyId)) {
            log.error("AccessKeyId不能为空");
            throw new SmsException("找不到 AccessKeyId！");
        }
        checkNotEmpty(accessKeyId, "'accessKeyId' must be not empty");
        checkNotEmpty(accessKeySecret, "'accessKeySecret' must be not empty");

        final IClientProfile clientProfile = DefaultProfile.getProfile(
                this.region, accessKeyId, accessKeySecret);
        Utils.tryChecked(() -> DefaultProfile.addEndpoint(this.endpointName, this.region, this.product, this.domain));
        this.acsClient = new DefaultAcsClient(clientProfile);
        this.smsTemplates = smsTemplates;
    }

    /**
     * 发送短信.
     *
     * @param smsTemplateKey 预置短信模板 key
     */
    public void send(final String smsTemplateKey) {
        final SmsTemplate smsTemplate = this.smsTemplates.get(smsTemplateKey);
        Objects.requireNonNull(smsTemplate, () -> "SmsTemplate must be not null, key:" + smsTemplateKey);

        send(smsTemplate);
    }

    /**
     * 发送短信.
     *
     * @param smsTemplateKey 预置短信模板 key
     * @param phoneNumbers 手机号码，优先于预置短信模板中配置的手机号码
     */
    public void send(final String smsTemplateKey, final String... phoneNumbers) {
        final SmsTemplate smsTemplate = this.smsTemplates.get(smsTemplateKey);
        Objects.requireNonNull(smsTemplate, () -> "SmsTemplate must be not null, key:" + smsTemplateKey);

        smsTemplate.setPhoneNumbers(Arrays.asList(phoneNumbers));
        send(smsTemplate);
    }

    /**
     * 发送短信.
     *
     * @param smsTemplate 短信模板
     */
    public void send(final SmsTemplate smsTemplate) {
        Objects.requireNonNull(smsTemplate);

        final SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(smsTemplate.getPhoneNumbers().stream().collect(Collectors.joining(",")));
        request.setSignName(smsTemplate.getSignName());
        request.setTemplateCode(smsTemplate.getTemplateCode());
        request.setTemplateParam(JSON.toJSONString(smsTemplate.getTemplateParam()));
        final SendSmsResponse response = Utils.tryChecked(() -> this.acsClient.getAcsResponse(request));
        checkSmsResponse(response);
    }
}
