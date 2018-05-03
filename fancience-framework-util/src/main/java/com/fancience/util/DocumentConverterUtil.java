package com.fancience.util;

import com.fancience.constant.ConfigConstant;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;

import java.io.File;

/**
 * 这是一个工具类，主要是为了使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx)
 * 转化为pdf文件
 * Office2010的没测试
 * @author Jason
 * @Description:
 * @date 18/4/16
 */
public class DocumentConverterUtil {

    private static final String PDF = "pdf";
    private static final String HTML = "html";

    /**
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为html文件
     * @param inputFilePath 源文件路径
     * @return
     */
    public static File openOfficeToHtml(String inputFilePath) {
        return officeCovert(inputFilePath, HTML);
    }

    /**
     * 使Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx) 转化为pdf文件
     * @param inputFilePath 源文件路径,如："D:/论坛.docx"
     * @return
     */
    public static File openOfficeToPdf(String inputFilePath) {
        return officeCovert(inputFilePath, PDF);
    }

    /**
     * 根据操作系统的名称，获取OpenOffice.org 4的安装目录<br>
     * 如我的OpenOffice.org 4安装在：C:/Program Files (x86)/OpenOffice 4
     * @return OpenOffice.org 4的安装目录
     */
    private static String getOfficeHome() {
        //这是返回的是OpenOffice的安装目录,建议将这个路径加入到配置文件中,然后直接通过配置文件获取
        return ConfigConstant.getOpenOfficePath();
    }

    /**
     * 连接OpenOffice.org 并且启动OpenOffice.org
     * @return
     */
    private static OfficeManager getOfficeManager() {
        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration();

        // 设置OpenOffice.org 4的安装目录
        config.setOfficeHome(getOfficeHome());

        // 启动OpenOffice的服务
        OfficeManager officeManager = config.buildOfficeManager();
        officeManager.start();

        return officeManager;
    }

    /**
     * 转换文件
     * @param inputFile
     * @param outputFilePath_end
     * @param inputFilePath
     * @param converter
     */
    private static File converterFile(File inputFile,String outputFilePath_end,String inputFilePath,
                                     OfficeDocumentConverter converter) {

        File outputFile = new File(outputFilePath_end);

        //判断目标路径是否存在,如不存在则创建该路径
        if (!outputFile.getParentFile().exists()){
            outputFile.getParentFile().mkdirs();
        }
        //转换
        converter.convert(inputFile, outputFile);

        // System.out.println("文件:"+inputFilePath+"\n转换为\n目标文件:"+outputFile+"\n成功!");

        return outputFile;
    }

    /**
     * 支持Office2003-2007全部格式的文档(.doc|.docx|.xls|.xlsx|.ppt|.pptx)
     * @param inputFilePath 源文件路径
     * @param postfix 转换文件后缀名
     * @return
     */
    private static File officeCovert(String inputFilePath, String postfix) {
        OfficeManager officeManager = null;
        try {
            if (inputFilePath==null||inputFilePath.trim().length()<=0) {
                System.out.println("输入文件地址为空，转换终止!");
                return null;
            }
            File inputFile = new File(inputFilePath);
            //转换后的文件路径
            String outputFilePath_end=getOutputFilePath(inputFilePath, postfix);
            if (!inputFile.exists()) {
                System.out.println("输入文件不存在，转换终止!");
                return null;
            }
            //获取OpenOffice的安装路径
            officeManager = getOfficeManager();
            //连接OpenOffice
            OfficeDocumentConverter converter=new OfficeDocumentConverter(officeManager);
            //转换并返回转换后的文件对象
            return converterFile(inputFile,outputFilePath_end,inputFilePath,converter);

        } catch (Exception e) {
            System.out.println("转化出错!");
            e.printStackTrace();
        } finally {
            if (officeManager != null) {
                //停止openOffice
                officeManager.stop();
            }
        }
        return null;
    }

    /**
     * 获取输出文件
     * @param inputFilePath
     * @param postfix
     * @return
     */
    public static String getOutputFilePath(String inputFilePath, String postfix) {
        String outputFilePath=inputFilePath.replaceAll("."+getPostfix(inputFilePath),"." + postfix);
        return outputFilePath;
    }

    /**
     * 获取inputFilePath的后缀名
     * @param inputFilePath
     * @return
     */
    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }

    //测试
    public static void main(String[] args) {
        openOfficeToHtml("//Users//fancy//Downloads//四年级 动物的结构与功能-耳膜的工作原理.pptx");
    }
}
