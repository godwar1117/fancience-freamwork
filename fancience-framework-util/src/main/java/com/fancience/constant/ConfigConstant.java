package com.fancience.constant;

import cn.hutool.setting.Setting;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;

/**
 * Created by IntelliJ IDEA
 *
 * @author Jason
 * @Description:
 * @date 18/4/17
 */
public class ConfigConstant {

    private static final String OPOEN_OFFICE_GROUP = "openoffice";

    private static Setting setting;

    /**
     * 系统名称
     */
    private static String OSName;

    static {
        OSName = SystemUtil.getOsInfo().getName();
        setting = new Setting("config.setting", true);
    }


    private static Boolean isWindows(){
        if (OSName.contains("Windows")){
            return true;
        }
        return false;
    }

    private static Boolean isLinux(){
        if (OSName.contains("Linux")){
            return true;
        }
        return false;
    }

    private static Boolean isMaxOS(){
        if (OSName.contains("Mac")){
            return true;
        }
        return false;
    }

    /**
     * 根据不同系统获取不同openoffice安装路径
     * @return
     */
    public static String getOpenOfficePath(){
        Setting openoffice = setting.getSetting(OPOEN_OFFICE_GROUP);
        if (isLinux()){
            return (String) openoffice.get("linux.openoffice.path");
        } else if(isMaxOS()){
            return (String) openoffice.get("mac.openoffice.path");
        } else if (isWindows()){
            return (String) openoffice.get("windows.openoffice.path");
        }
        return "";
    }
    
    public static void main(String[] args){
        System.out.println(getOpenOfficePath());
    }

}
