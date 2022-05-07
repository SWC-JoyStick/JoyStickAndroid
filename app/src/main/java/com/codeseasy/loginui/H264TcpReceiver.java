package com.codeseasy.loginui;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;

import com.codeseasy.loginui.media.DecodeTcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class H264TcpReceiver extends Thread{

    //定义按键类型


    private int full =0;
    private int empty = 1;


    public Handler mMainHandler;

    // Socket变量
    private Socket socket;

    // 线程池
    // 为了方便,此处直接采用线程池进行线程管理,而没有一个个开线程
    private ExecutorService mThreadPool;

    /**
     * 发送消息到服务器 变量
     */
    OutputStream outputStream;
    byte[] b;
    /**
     * 接收服务器消息 变量
     */
    OutputStream saveFile;
    private String h264Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test1.h264";


    /**
     * 用于画面显示的变量
     */
    private SurfaceView mSurfaceView;
    private Context mContext;



    public H264TcpReceiver(Context context, SurfaceView sfw, final String IP) {
        // 初始化线程池
        mSurfaceView = sfw;
        mContext = context;
        mThreadPool = Executors.newCachedThreadPool();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    // 创建Socket对象 & 指定服务端的IP 及 端口号
                    Log.d("MainDebug","try Connecting");
                    socket = new Socket(IP, 6666);
                    outputStream = socket.getOutputStream();
                    // 判断客户端和服务器是否连接成功
                    Log.d("MainDebug","try Connecting2");
                    //System.out.println(socket.isConnected());
                    //Socket输入流
                    InputStream in = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    //构造解析器
                    DecodeTcp decoder = new DecodeTcp(mContext,mSurfaceView,in);
                    decoder.start();
                    //创建文件

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

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
