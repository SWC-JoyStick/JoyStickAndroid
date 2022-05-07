package com.codeseasy.loginui.udp;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpSend extends Thread{
    private final int BROADCAST_PORT = 45454;
    Context mContext;
    public UdpSend(Context context){
        mContext = context;
    }

    @Override
    public void run() {
        try {
            //发送一次 广播包
            //初始化工作
            DatagramSocket datagramSocket = new DatagramSocket();
            byte buf[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            //获取设备名 准备要发送的数据
            String deviceName = Settings.Secure.getString(mContext.getContentResolver(), "bluetooth_name");
            Log.d("myDebug",deviceName);

            // 数据格式：  "DISCOVER_REQUEST"+'\n' + deviceName + IP
            String myIP = IPUtil.getLocalIPAddress(mContext);
            String str_send = "DISCOVER_REQUEST"+'\n' + deviceName + '\n' + myIP;
            byte[] feedback = str_send.getBytes();
            // 发送数据 ： 广播
            InetAddress broadIP = InetAddress.getByName("255.255.255.255");
            DatagramPacket sendPack = new DatagramPacket(feedback, feedback.length, broadIP, BROADCAST_PORT);

            datagramSocket.send(sendPack);
        }
        catch (SocketException e){
            e.printStackTrace();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
