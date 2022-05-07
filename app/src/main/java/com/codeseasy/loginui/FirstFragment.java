package com.codeseasy.loginui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codeseasy.loginui.cardView.Device;
import com.codeseasy.loginui.cardView.DeviceAdapter;
import com.codeseasy.loginui.udp.ReceiveMsgHandler;
import com.codeseasy.loginui.udp.UdpReceive;
import com.codeseasy.loginui.udp.UdpSend;

import java.io.IOException;
import java.util.ArrayList;

import at.markushi.ui.CircleButton;

public class FirstFragment extends Fragment {

    private Context context;
    private int content; //Fragment的显示内容
    private CircleButton homebt;

    private ArrayList<Device> deviceList = new ArrayList<>();

    public Handler handler;

    private DeviceAdapter adapter;

    // 下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * 发送广播
     */



    public FirstFragment() {
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_fragment, container, false);
        return v;

    }
    void init(){
            deviceList.clear();
        for(int i = 0;i < 3;i++) {
            Device d = new Device("device","ip");
            deviceList.add(d);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //在这里注册button
        //获取context
        context = getActivity();
        // final User user ;
        //homebt = (CircleButton) getActivity().findViewById(R.id.startfind);
        /*homebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, FullscreenActivity.class);
                startActivity(intent);
            }
        });*/
        //定义handler 响应MESSAGE
        handler = new ReceiveMsgHandler(this);

        //init();
        /**
         * RecyclerView
         */
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_device);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(adapter);

        /**
         * 下拉刷新
         */
        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.first_fragment_refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.cyan);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            //刷新时进行的方法
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UdpSend s = new UdpSend(context);
                        s.start();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }

        });
        /**
         * 启动接收线程 不听监听端口 45454
         */

        try {
            UdpReceive recv = new UdpReceive(context, handler);
            recv.start(); // 启动
           // UdpSend sendUdp = new UdpSend(context);
            //sendUdp.start(); // 只进行一次发送工作
        }
        catch (IOException e){
            e.printStackTrace();
        }
        /*Button send_btn = (Button)  getActivity().findViewById(R.id.send);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UdpSend s = new UdpSend(context);
                s.start();
            }
        });*/
    }

    /**
     * 像设备列表中添加设备
     * @param msg
     */
    public void addDeviceIP(String msg){
        String[] device_info = msg.split("\n");
        Device d = new Device(device_info[0],device_info[1]);
        for(int i = 0;i < deviceList.size();i++){
            if(deviceList.get(i).getName().contains(device_info[0]) )
                return;
        }
        deviceList.add(d);
        // 开一个线程更新UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        adapter.notifyDataSetChanged();
                        //隐藏刷新

                    }
                });
            }
        }).start();
    }



}
