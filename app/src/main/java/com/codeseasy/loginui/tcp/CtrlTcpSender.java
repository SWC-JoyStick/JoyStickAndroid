package com.codeseasy.loginui.tcp;


import static android.view.KeyEvent.*;
//  META_CTRL_ON;
//  KEYCODE_BUTTON_A;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// META_CTRL_ON
//

/**
 * 连接PC 从android 传输控制指令到PC
 *
 */
public class CtrlTcpSender {

    private static final int PORT = 1062;

    public Handler mMainHandler;

    // Socket变量
    private Socket socket;

    // 线程池
    // 为了方便,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    byte[] b;
    OutputStream outputStream;

    public CtrlTcpSender(final String IP) {
        // 初始化线程池
        mThreadPool = Executors.newCachedThreadPool();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建Socket对象 & 指定服务端的IP 及 端口号 1062
                    Log.d("MainDebug","try Connecting");
                    socket = new Socket(IP, PORT);
                    Log.d("MainDebug","try Connecting2");
                    outputStream = socket.getOutputStream();
                    // 判断客户端和服务器是否连接成功
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static byte[] IntTo32Bytes(int a){
        byte[] ans=new byte[4];
        for(int i=0;i<4;i++)
            ans[i]=(byte)(a>>(i*8));//截断 int 的低 8 位为一个字节 byte，并存储起来
        return ans;
    }



    public void sending(int type,int action,int keyCode,int metaState){
        // 将相应的控制信号 转换成字节数组中的控制指令
        if(type == 0){
            // 共11 字节 最后以字节放'\n' 用来分隔
            b = new byte[11];
            int cnt = 0;
            b[cnt++] = (byte)type;
            b[cnt++] = (byte) action;
            // 大端
            for(int i = 3;i >=0;i-- ){
                b[cnt++] = (byte) (keyCode>>(i*8));
            }
            for(int i = 3;i >=0;i--) {
                b[cnt++] = (byte) (metaState >> (i * 8));
            }
            b[cnt] = (byte) '\n';

        }else if(type == 6){
            //

        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(b);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
