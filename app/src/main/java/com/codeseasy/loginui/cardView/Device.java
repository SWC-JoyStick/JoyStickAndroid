package com.codeseasy.loginui.cardView;

import com.codeseasy.loginui.R;

import java.util.Random;

/**
 * 记录device 属性 name + ip
 */
public class Device {
    private String deviceName;
    private String deviceIp;
    private int imageId;

    private int[] picture = {R.drawable.device_bg1,R.drawable.device_bg2,R.drawable.device_bg3,R.drawable.device_bg4,R.drawable.device_bg5,R.drawable.device_bg6,
            R.drawable.device_bg7,R.drawable.device_bg8,R.drawable.device_bg9,R.drawable.device_bg10,R.drawable.device_bg11};
    public Device(String name, String ip){
        deviceName = name;
        deviceIp = ip;

        // 设置 图片
        Random random = new Random();
        int index = random.nextInt(picture.length);
        imageId = picture[index];
    }
    public void setIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public void setName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIP() {
        return deviceIp;
    }

    public String getName() {
        return deviceName;
    }

    public int getImageId(){
        return imageId;
    }
}

