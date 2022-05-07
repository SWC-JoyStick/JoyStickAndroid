package com.codeseasy.loginui.udp;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.Fragment;

import com.codeseasy.loginui.FirstFragment;
import com.codeseasy.loginui.MainActivity;

public class ReceiveMsgHandler extends Handler {
    public static final int RECEIVE_COMMOND = 1;
    private FirstFragment fragment;

    public ReceiveMsgHandler(FirstFragment fragment) {
        this.fragment = fragment;
    }

    // 有消息，自动处理
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case RECEIVE_COMMOND:
                if (fragment != null && msg.obj != null) {
                    fragment.addDeviceIP(msg.obj.toString());
                }
                break;
            default:
                break;
        }
    }
}