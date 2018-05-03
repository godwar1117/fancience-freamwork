package com.fancience;

import com.fancience.oss.config.OSSAppNameEnum;
import com.fancience.oss.exception.AliyunOSSException;
import com.fancience.oss.handler.UploadFileHandler;
import com.fancience.oss.protocol.PutObjectRequestData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    @org.junit.Test
    public void testApp() {
        try {
            // File转换成MutipartFile
            File file = new File("/Users/chentnt/Downloads/300406.jpg");
            FileInputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(), null, inputStream);
            //注意这里面填啥，MultipartFile里面对应的参数就有啥，比如我只填了name，则

            List<MultipartFile> multipartFiles = new ArrayList<>();
            multipartFiles.add(multipartFile);
            PutObjectRequestData putObjectRequestData = PutObjectRequestData.newBuiler()
                    .setAppName(OSSAppNameEnum.FANCIENCE_DEV.getAppName()).setOssPath("/test/")
                    .setMultipartFileList(multipartFiles).buildMultipartFileList();

            UploadFileHandler uploadFileHandler = null;
            try {
                uploadFileHandler = new UploadFileHandler(putObjectRequestData);
                List<String> urls = uploadFileHandler.uploadMultipartFiles();
                urls.forEach(po -> System.out.println(po));
            } catch (AliyunOSSException e) {
                e.printStackTrace();
            }

            //return multipartFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //return null;
        }
    }
}
