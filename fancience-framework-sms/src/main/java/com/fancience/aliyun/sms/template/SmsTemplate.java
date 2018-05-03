package com.fancience.aliyun.sms.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsTemplate {
    private String signName;
    private String templateCode;
    private Map<String, String> templateParam;
    private List<String> phoneNumbers;

    /**
     * 添加短信模板参数.
     */
    public SmsTemplate addTemplateParam(final String key, final String value) {
        if (null == this.templateParam) {
            this.templateParam = new HashMap<>(3);
        }
        this.templateParam.put(key, value);
        return this;
    }

    public SmsTemplate addPhoneNum(final String phoneNum) {
        if (null == this.phoneNumbers) {
            this.phoneNumbers = new ArrayList<>();
        }
        this.phoneNumbers.add(phoneNum);
        return this;
    }


    public SmsTemplate() {
    }

    public SmsTemplate(String signName, String templateCode) {
        this.signName = signName;
        this.templateCode = templateCode;
    }

    public SmsTemplate(String signName, String templateCode, List<String> phoneNumbers) {
        this.signName = signName;
        this.templateCode = templateCode;
        this.phoneNumbers = phoneNumbers;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public Map<String, String> getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(Map<String, String> templateParam) {
        this.templateParam = templateParam;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

}
