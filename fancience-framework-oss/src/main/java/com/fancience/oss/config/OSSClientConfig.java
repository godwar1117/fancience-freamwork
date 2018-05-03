package com.fancience.oss.config;

/**
 * @Package com.fancience.oss.config
 * @Description:
 * @author: shoucai.wang
 * @date: 23/01/2018 17:39
 */
public class OSSClientConfig {
  private String endpoint;
  private String accessKeyId;
  private String accessKeySecret;
  private String bucketName;
  private String firstKey;
  private String baseUrl;

  public OSSClientConfig(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String firstKey, String baseUrl) {
    this.endpoint = endpoint;
    this.accessKeyId = accessKeyId;
    this.accessKeySecret = accessKeySecret;
    this.bucketName = bucketName;
    this.firstKey = firstKey;
    this.baseUrl = baseUrl;
  }

  public String getEndpoint() {
    return this.endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getAccessKeyId() {
    return this.accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
    return this.accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }

  public String getBucketName() {
    return this.bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getFirstKey() {
    return this.firstKey;
  }

  public void setFirstKey(String firstKey) {
    this.firstKey = firstKey;
  }

  public String getBaseUrl() {
    return this.baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
