package com.codeseasy.loginui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.codeseasy.loginui.http.HttpPostUtil;
import com.codeseasy.loginui.http.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    //先定义
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static User user;

    //输入框
    EditText userName_input ;
    EditText password_input;
    private TextView register;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //然后通过一个函数来申请
    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tools.setSystemBarLight(this);
        Tools.setSystemBarColor(this,R.color.white);

        Button button = (Button) findViewById(R.id.Sign);
        userName_input = (EditText) findViewById(R.id.edit_username);
        password_input = (EditText) findViewById(R.id.edit_password);
        register = (TextView) findViewById(R.id.text_register);
        // 文字可点击
        register.setClickable(true);

        // 点击时，跳转注册界面
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,Register.class);
                MainActivity.this.startActivity(intent);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://123.57.245.248:8080/login";
                //getJsonData();
                String userName = userName_input.getText().toString();
                String password = password_input.getText().toString();
                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("userName",userName)
                        .add("password",password)
                        .build();
                HttpPostUtil.sendOkHttpRequest(url, requestBody, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //连接超时时的提醒
                                Toast.makeText(MainActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        //成功获取响应时
                        //不要toString
                        String responseData = response.body().string();
                        //解析，responseData，获取响应数据
                        user = JSON.parseObject(responseData, User.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示回调函数
                                if(user== null)
                                    Toast.makeText(MainActivity.this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this,HomeActivity.class);
                                    intent.putExtra("nickName",user.getNickName());
                                    MainActivity.this.startActivity(intent);
                                }
                            }
                        });

                    }
                });
            }
        });

        verifyStoragePermissions(this);

        //数据库
        DBHelper dbsqlite = new DBHelper(MainActivity.this);
        final SQLiteDatabase db = dbsqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put("username","wsc");
//        values.put("password","123456");
//        values.put("age",21);
//        db.insert("user",null,values);
        //

    }
}
