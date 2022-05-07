package com.codeseasy.loginui;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.codeseasy.loginui.http.HttpPostUtil;
import com.codeseasy.loginui.http.User;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class Register extends AppCompatActivity {
    private static User user;

    //输入框
    EditText userName_input ;
    EditText nickname_input;
    EditText password_input;
    EditText password2_input;
    String username;
    String password;
    String password2;
    String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName_input = findViewById(R.id.register_username);
        password_input = findViewById(R.id.register_password1);
        password2_input = findViewById(R.id.register_password2);
        nickname_input = findViewById(R.id.register_nickname);


        Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userName_input.getText().toString();
                password = password_input.getText().toString();
                password2 = password2_input.getText().toString();
                nickname = nickname_input.getText().toString();
                if(username == ""){
                    Toast.makeText(Register.this,"账号不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals( password2)){
                    Toast.makeText(Register.this,"前后密码不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("userName",username)
                        .add("password",password)
                        .add("nickName",nickname)
                        .build();

                String url = "http://123.57.245.248:8080/register";
                HttpPostUtil.sendOkHttpRequest(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //连接超时时的提醒
                                Toast.makeText(Register.this,"Fail",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String responseData = response.body().string();
                        //解析，responseData，获取响应数据
                        boolean success = JSON.parseObject(responseData,boolean.class);
                        if(success){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //显示回调函数
                                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();

                                }
                            });
                            try {
                                sleep(500);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            onDestroy();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //显示回调函数
                                    Toast.makeText(Register.this,"用户名已存在！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }
        });
    }
}