package com.codeseasy.loginui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.security.PublicKey;

public class ThirdFragment extends Fragment  {

    private Context context;

    public ThirdFragment() {

    }

    TextView userName2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_me,container,false);

        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userName2 = getActivity().findViewById(R.id.userText2);
        userName2.setText(HomeActivity.nickName);
                //在这里注册button

        //获取context
        context = getActivity();

    }
}

