package com.fancience.oss.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.setting.Setting;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.PutObjectResult;
import com.fancience.oss.config.OSSClientConfig;
import com.fancience.oss.exception.AliyunOSSException;
import com.fancience.oss.protocol.PutObjectResultData;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Package com.fancience.oss.client
 * @Description:
 * @author: shoucai.wang
 * @date: 23/01/2018 17:23
 */
public class AliyunOSS {
    private static OSSClient ossClient;
    private static volatile AliyunOSS instance = null;
    private static OSSClientConfig ossClientConfig = null;

    public static AliyunOSS getInstance(String appName) throws AliyunOSSException {
        if (instance == null) {
            Class var1 = AliyunOSS.class;
            synchronized(AliyunOSS.class) {
                if (instance == null) {
                    instance = new AliyunOSS(appName);
                }
            }
        }

        return instance;
    }

    private AliyunOSS(String appName) throws AliyunOSSException {
        ossClientConfig = this.initConfig(appName);
        ossClient = new OSSClient(ossClientConfig.getEndpoint(), ossClientConfig.getAccessKeyId(), ossClientConfig.getAccessKeySecret());
    }

    public Boolean doesBucketExist() {
        return ossClient.doesBucketExist(ossClientConfig.getBucketName());
    }

    public BucketInfo createBucket() {
        if (!this.doesBucketExist()) {
            ossClient.createBucket(ossClientConfig.getBucketName());
        }

        return ossClient.getBucketInfo(ossClientConfig.getBucketName());
    }

    private OSSClientConfig initConfig(String appName) throws AliyunOSSException {
        if (StrUtil.isEmpty(appName)) {
            throw new AliyunOSSException("appName is null!");
        } else {
            Setting setting = new Setting("oss.setting", true);
            String endpoint = setting.getByGroup("END_POINT", appName);
            String accessKeyId = setting.getByGroup("ACCESS_KEY_ID", appName);
            String accessKeySecret = setting.getByGroup("ACCESS_KEY_SECRET", appName);
            String bucketName = setting.getByGroup("BUCKET_NAME", appName);
            String firstKey = setting.getByGroup("FIRSTKEY", appName);
            String baseUrl = setting.getByGroup("BASE_URL", appName);
            if (StrUtil.hasBlank(new CharSequence[]{endpoint, accessKeyId, accessKeySecret, bucketName, firstKey, baseUrl})) {
                throw new AliyunOSSException("Please config oss.setting");
            } else {
                return new OSSClientConfig(endpoint, accessKeyId, accessKeySecret, bucketName, firstKey, baseUrl);
            }
        }
    }

    public PutObjectResultData uploadByte(byte[] content, String objectKey) throws AliyunOSSException {
        PutObjectResult putObjectResult = null;

        try {
            putObjectResult = ossClient.putObject(ossClientConfig.getBucketName(), ossClientConfig.getFirstKey() + objectKey, new ByteArrayInputStream(content));
        } catch (OSSException var5) {
            StaticLog.error("Error Message: " + var5.getErrorCode(), new Object[0]);
            StaticLog.error("Error Code:       " + var5.getErrorCode(), new Object[0]);
            StaticLog.error("Request ID:      " + var5.getRequestId(), new Object[0]);
            StaticLog.error("Host ID:           " + var5.getHostId(), new Object[0]);
            throw new AliyunOSSException("uploadByte Caught an OSSException!", var5);
        } catch (ClientException var6) {
            StaticLog.error("Error Message: " + var6.getErrorCode(), new Object[0]);
            throw new AliyunOSSException("uploadByte Caught an ClientException!", var6);
        }

        if (ObjectUtil.isNull(putObjectResult)) {
            throw new AliyunOSSException("ossClient putobject exception,PutObjectResult is null!");
        } else {
            PutObjectResultData putObjectResultData = new PutObjectResultData();
            BeanUtil.copyProperties(putObjectResult, putObjectResultData);
            return putObjectResultData;
        }
    }

    public PutObjectResultData uploadInputStream(InputStream inputStream, String objectKey) throws IOException, AliyunOSSException {
        PutObjectResult putObjectResult = null;

        try {
            putObjectResult = ossClient.putObject(ossClientConfig.getBucketName(), ossClientConfig.getFirstKey() + objectKey, inputStream);
        } catch (OSSException var5) {
            StaticLog.error("Error Message: " + var5.getErrorCode(), new Object[0]);
            StaticLog.error("Error Code:       " + var5.getErrorCode(), new Object[0]);
            StaticLog.error("Request ID:      " + var5.getRequestId(), new Object[0]);
            StaticLog.error("Host ID:           " + var5.getHostId(), new Object[0]);
            throw new AliyunOSSException("uploadInputStream Caught an in OSSException!", var5);
        } catch (ClientException var6) {
            StaticLog.error("Error Message: " + var6.getErrorCode(), new Object[0]);
            throw new AliyunOSSException("uploadInputStream Caught an ClientException!", var6);
        }

        if (ObjectUtil.isNull(putObjectResult)) {
            throw new AliyunOSSException("ossClient putobject exception,PutObjectResult is null!");
        } else {
            PutObjectResultData putObjectResultData = new PutObjectResultData();
            BeanUtil.copyProperties(putObjectResult, putObjectResultData);
            return putObjectResultData;
        }
    }

    public PutObjectResultData uploadString(String content, String objectKey) throws AliyunOSSException {
        return this.uploadByte(content.getBytes(), objectKey);
    }

    public String uploadNetworkStream(String url, String objectKey) throws IOException, AliyunOSSException {
        this.uploadInputStream((new URL(url)).openStream(), objectKey);
        return this.generateUrl(objectKey);
    }

    public String uploadFileInputStream(String localFile, String objectKey) throws IOException, AliyunOSSException {
        this.uploadInputStream(new FileInputStream(localFile), objectKey);
        return this.generateUrl(objectKey);
    }

    public String uploadFileInputStream(InputStream inputStream, String objectKey) throws IOException, AliyunOSSException {
        this.uploadInputStream(inputStream, objectKey);
        return this.generateUrl(objectKey);
    }

    public void shutdown() {
        ossClient.shutdown();
    }

    public String generateUrl(String objectKey) {
        StringBuffer sb = new StringBuffer("/");
        if (StrUtil.isNotBlank(ossClientConfig.getFirstKey())) {
            sb.append(ossClientConfig.getFirstKey());
        }

        sb.append(objectKey);
        return sb.toString();
    }
}
