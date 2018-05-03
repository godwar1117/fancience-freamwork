package com.fancience.oss.protocol;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;

/**
 * @Package com.fancience.oss.protocol
 * @Description:
 * @author: shoucai.wang
 * @date: 26/01/2018 14:19
 */
public class PutObjectRequestData {
    private String appName;
    private String ossPath;
    private List<MultipartFile> multipartFileList;
    private List<String> fileNameList;
    private String filePath;

    private PutObjectRequestData(String appName, String ossPath, List<MultipartFile> multipartFileList) {
        this.appName = appName;
        this.ossPath = ossPath;
        this.multipartFileList = multipartFileList;
    }

    private PutObjectRequestData(String appName, String ossPath, List<String> fileNameList, String filePath) {
        this.appName = appName;
        this.ossPath = ossPath;
        this.fileNameList = fileNameList;
        this.filePath = filePath;
    }

    public static PutObjectRequestData.Builder newBuiler() {
        return new PutObjectRequestData.Builder();
    }

    public String getAppName() {
        return this.appName;
    }

    public String getOssPath() {
        return this.ossPath;
    }

    public List<String> getFileNameList() {
        return this.fileNameList;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public List<MultipartFile> getMultipartFileList() {
        return this.multipartFileList;
    }

    public static class Builder {
        private String appName = null;
        private String ossPath = null;
        List<MultipartFile> multipartFileList = null;
        private List<String> fileNameList = null;
        private String filePath = null;

        public Builder() {
        }

        public PutObjectRequestData.Builder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public PutObjectRequestData.Builder setOssPath(String ossPath) {
            this.ossPath = ossPath;
            return this;
        }

        public PutObjectRequestData.Builder setMultipartFileList(List<MultipartFile> multipartFileList) {
            this.multipartFileList = multipartFileList;
            return this;
        }

        public PutObjectRequestData.Builder setFileNameList(List<String> fileNameList) {
            this.fileNameList = fileNameList;
            return this;
        }

        public PutObjectRequestData.Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public PutObjectRequestData buildMultipartFileList() {
            if (StrUtil.isBlank(this.appName)) {
                throw new IllegalArgumentException(String.valueOf("appName must be set"));
            } else if (StrUtil.isBlank(this.ossPath)) {
                throw new IllegalArgumentException(String.valueOf("ossPath must be set"));
            } else if (CollectionUtil.isEmpty(this.multipartFileList)) {
                throw new IllegalArgumentException(String.valueOf("multipartFileList must be set"));
            } else {
                Iterator var1 = this.multipartFileList.iterator();

                MultipartFile file;
                do {
                    if (!var1.hasNext()) {
                        return new PutObjectRequestData(this.appName, this.ossPath, this.multipartFileList);
                    }

                    file = (MultipartFile) var1.next();
                } while (!file.isEmpty());

                throw new IllegalArgumentException(String.valueOf("multipartFileList include empty"));
            }
        }

        public PutObjectRequestData buildFileNameList() {
            if (StrUtil.isBlank(this.appName)) {
                throw new IllegalArgumentException(String.valueOf("appName must be set"));
            } else if (StrUtil.isBlank(this.ossPath)) {
                throw new IllegalArgumentException(String.valueOf("ossPath must be set"));
            } else if (CollectionUtil.isEmpty(this.fileNameList)) {
                throw new IllegalArgumentException(String.valueOf("fileList must be set"));
            } else if (StrUtil.isBlank(this.filePath)) {
                throw new IllegalArgumentException(String.valueOf("fileList must be set"));
            } else {
                return new PutObjectRequestData(this.appName, this.ossPath, this.fileNameList, this.filePath);
            }
        }
    }
}
