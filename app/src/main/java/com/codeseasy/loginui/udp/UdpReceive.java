package com.codeseasy.loginui.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpReceive extends Thread {
    private final int BROADCAST_PORT = 45454;
    private DatagramSocket datagramSocket;
    private Handler handler;
    private Context mContext;

    private final int REQUEST_FLAG = 1;
    private final int RESPOND_FLAG = 2;

    public UdpReceive(Context context,Handler handler) throws IOException {
        // Keep a socket open to listen to all the UDP trafic that is destined for this port

        // 0.0.0.0 代表本机的所有IP地址 datagramSocket可以接收BROADCAST_PORT 上所有的消息
        datagramSocket = new DatagramSocket(BROADCAST_PORT, InetAddress.getByName("0.0.0.0"));
        datagramSocket.setBroadcast(true);
        // handler
        this.handler = handler;
        // Context
        this.mContext = context;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte buf[] = new byte[1024];
                // 接收数据
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(packet);

                //trim 用于删除头尾空白的字符串
                String content = new String(packet.getData()).trim();

                // 判断接收到的报文是不是本机发送的报文
                if (content.startsWith("DISCOVER_REQUEST") &&
                        !packet.getAddress().toString().equals("/" + IPUtil.getLocalIPAddress(mContext))) {
                    // 发送Handler消息  MESSAGE  deviceName+ \n +ip
                    String []tmp = content.split("\n");
                    String message = tmp[1] + "\n" + packet.getAddress().toString();
                    sendHandlerMessage(message);

                    // 准备要发送的数据
                    String deviceName = Settings.Secure.getString(mContext.getContentResolver(), "bluetooth_name");
                    Log.d("my",deviceName);
                    String myIP = IPUtil.getLocalIPAddress(mContext);
                    String str_send = "DISCOVER_RESPONSE"+'\n' + deviceName + '\n' + myIP;
                    //String str_send = "DISCOVER_RESPONSE"+'\n'+deviceName+'\n'+IP;
                    byte[] feedback = str_send.getBytes();
                    // 发送数据 ：单播
                    DatagramPacket sendPacket = new DatagramPacket(feedback, feedback.length, packet.getAddress(), BROADCAST_PORT);
                    datagramSocket.send(sendPacket);


                } else if (content.startsWith("DISCOVER_RESPONSE") &&
                        !packet.getAddress().toString().equals("/" + IPUtil.getLocalIPAddress(mContext))) {

                    // 发送Handler消息  MESSAGE  deviceName+ \n +ip
                    String []tmp = content.split("\n");
                    String message = tmp[1] + "\n" + packet.getAddress().toString();
                    sendHandlerMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 发送Handler消息
     *
     * @param content
     */
    private void sendHandlerMessage(String content) {

        Message msg = new Message();
        msg.what = ReceiveMsgHandler.RECEIVE_COMMOND;
        msg.obj = content;
        handler.sendMessage(msg);
    }
}