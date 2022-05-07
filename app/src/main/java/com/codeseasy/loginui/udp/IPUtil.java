package com.codeseasy.loginui.udp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class IPUtil {

    /**
     * 转成字符串
     */
    public static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF) + "." + ((ipAdress >> 8) & 0xFF) + "." + ((ipAdress >> 16) & 0xFF) + "."
                + (ipAdress >> 24 & 0xFF);
    }

    //获取本地IP函数
    public static String getLocalIPAddress(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            // 修改这里，强行打开WIFI会闪退
            //wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return formatIpAddress(ipAddress);
    }


    // 检测



}