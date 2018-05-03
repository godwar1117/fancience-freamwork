package com.fancience.oss.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import com.fancience.oss.client.AliyunOSS;
import com.fancience.oss.exception.AliyunOSSException;
import com.fancience.oss.protocol.PutObjectRequestData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Package com.fancience.oss.handler
 * @Description:
 * @author: shoucai.wang
 * @date: 24/01/2018 11:44
 */
public class UploadFileHandler {
    private AliyunOSS aliyunOSS = null;
    private PutObjectRequestData putObjectRequestData = null;

    public UploadFileHandler(PutObjectRequestData putObjectRequestData) throws AliyunOSSException {
        this.putObjectRequestData = putObjectRequestData;
        this.aliyunOSS = AliyunOSS.getInstance(putObjectRequestData.getAppName());
    }

    public List<String> uploadMultipartFiles() throws AliyunOSSException, IOException {
        if (ObjectUtil.isNull(this.putObjectRequestData)) {
            throw new AliyunOSSException("PutObjectRequestData is null");
        } else {
            List<MultipartFile> multipartFileList = this.putObjectRequestData.getMultipartFileList();
            List<String> ossFilePathList = new ArrayList();
            Iterator var3 = multipartFileList.iterator();

            while(var3.hasNext()) {
                MultipartFile file = (MultipartFile)var3.next();
                String fileName = file.getOriginalFilename();
                String objectKey = this.putObjectRequestData.getOssPath() + fileName;
                String ossFilePath = this.aliyunOSS.uploadFileInputStream(file.getInputStream(), objectKey);
                ossFilePathList.add(ossFilePath);
            }

            return ossFilePathList;
        }
    }

    public List<String> uploadLocalFiles() throws AliyunOSSException, IOException {
        if (ObjectUtil.isNull(this.putObjectRequestData)) {
            throw new AliyunOSSException("PutObjectRequestData is null");
        } else {
            List<String> fileList = this.putObjectRequestData.getFileNameList();
            if (CollectionUtil.isEmpty(fileList)) {
                throw new AliyunOSSException("PutObjectRequestData.getFileList is null");
            } else {
                List<String> ossFilePathList = new ArrayList();
                Iterator var3 = fileList.iterator();

                while(var3.hasNext()) {
                    String fileName = (String)var3.next();
                    String objectKey = this.putObjectRequestData.getOssPath() + fileName;
                    String ossFilePath = this.aliyunOSS.uploadFileInputStream(this.putObjectRequestData.getFilePath() + fileName, objectKey);
                    StaticLog.info("upload file to oss success!ossFilePath=" + ossFilePath, new Object[0]);
                    ossFilePathList.add(ossFilePath);
                }

                return ossFilePathList;
            }
        }
    }

    public PutObjectRequestData getPutObjectRequestData() {
        return this.putObjectRequestData;
    }

    public void setPutObjectRequestData(PutObjectRequestData putObjectRequestData) {
        this.putObjectRequestData = putObjectRequestData;
    }
}
