package com.codeseasy.loginui;



import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.codeseasy.loginui.control.controlArgs;
import com.codeseasy.loginui.tcp.CtrlTcpSender;
import com.shy.rockerview.MyRockerView;

import static android.view.KeyEvent.*;

public class FullscreenActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{
    /**
     * intent 传入数据
     */
    public  String myIP ;   //传入IP地址
    public static final String GET_IP = "get_ip";




    private ImageButton set_imbt;//设置按钮

    private PopupWindow mPopWindow; //弹出窗口

    private Button start_btn;


    H264TcpReceiver sender;
    private ImageButton LB,LT,RB,RT,A,B,X,Y;
    MyRockerView mRockerView1;
    MyRockerView mRockerView2;
    MyRockerView mRockerView3;
    private Vibrator vibrator;

    private boolean move_flag = false;

    TextView tv1;

    /**
     * 记录触摸屏幕的位置
     */
    private int sx,sy;

    TextView direction2_Text;
    TextView direction1_Text;

    //用于显示视频
    SurfaceView surface;


    EditText editText;

    // 记录下上次按键触发情况
    int LAST_ROCKER_LEFT1 = 0;
    int LAST_ROCKER_LEFT2 = 0;
    int LAST_ROCKER_RIGHT1 = 0;
    int LAST_ROCKER_RIGHT2 = 0;
    int LAST_ROCKER_MID1 = 0;
    int LAST_ROCKER_MID2 = 0;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        /**
         * 传入IP
         */
        myIP = intent.getStringExtra(GET_IP);
        //对IP 进行处理 去除IP首的 '/'
        myIP = myIP.substring(1,myIP.length());
        Log.d("FullDEBUG",myIP);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_fullscreen);





        set_imbt = (ImageButton) findViewById(R.id.setting_imbt);
        set_imbt.setOnClickListener(this);

        start_btn = findViewById(R.id.start_video);
        start_btn.setOnClickListener(this);

        surface = (SurfaceView)findViewById(R.id.surfaceView);

        byte[] b  =  new byte[200];


        LB = (ImageButton) findViewById(R.id.LB_btn);
        LT = (ImageButton) findViewById(R.id.LT_btn);
        RB = (ImageButton) findViewById(R.id.RB_btn);
        RT = (ImageButton) findViewById(R.id.RT_btn);
        A = (ImageButton) findViewById(R.id.A_btn);
        B = (ImageButton) findViewById(R.id.B_btn);
        X = (ImageButton) findViewById(R.id.X_btn);
        Y = (ImageButton) findViewById(R.id.Y_btn);

        LT.setOnTouchListener(this);
        LB.setOnTouchListener(this);
        RT.setOnTouchListener(this);
        RB.setOnTouchListener(this);
        A.setOnTouchListener(this);
        B.setOnTouchListener(this);
        X.setOnTouchListener(this);
        Y.setOnTouchListener(this);


        LT.setOnClickListener(this);
        LB.setOnClickListener(this);
        RT.setOnClickListener(this);
        RB.setOnClickListener(this);
        A.setOnClickListener(this);
        B.setOnClickListener(this);
        X.setOnClickListener(this);
        Y.setOnClickListener(this);



        mRockerView1 = (MyRockerView) findViewById(R.id.rocker1);  // 1
        mRockerView2 = (MyRockerView) findViewById(R.id.rocker2);  // 2
        mRockerView3 = (MyRockerView) findViewById(R.id.rocker3);  // 3
        mRockerView1.setOnTouchListener(this);
        mRockerView2.setOnTouchListener(this);
        mRockerView3.setOnTouchListener(this);
        //MyRockerView mRockerViewZ = (MyRockerView)findViewById(R.id.rockerZ_view);    // 2
        direction1_Text = (TextView) findViewById(R.id.direction1_Text);    // 1当前方向
        direction2_Text = (TextView) findViewById(R.id.direction2_Text);   // 2  方向

        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        /**
         * 注册左侧8方向 rocker 按键行为
         */
        mRockerView1.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_8, new MyRockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(MyRockerView.Direction direction) {
                sender.sending(0,ACTION_UP,LAST_ROCKER_LEFT1,0);
                sender.sending(0,ACTION_UP,LAST_ROCKER_LEFT2,0);
                switch (direction) {
                    case DIRECTION_CENTER:
                        direction1_Text.setText("当前方向：中心");
                        break;
                    case DIRECTION_UP:
                        // directionChange();
                        direction1_Text.setText("当前方向：上");
                        sender.sending(0,ACTION_DOWN,KEYCODE_W ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_W;
                        // 上 触发事件
                        break;

                    case DIRECTION_RIGHT:
                        //directionChange();
                        direction1_Text.setText("当前方向：右");
                        sender.sending(0,ACTION_DOWN,KEYCODE_D ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_D;
                        break;

                    case DIRECTION_DOWN:
                        //directionChange();
                        direction1_Text.setText("当前方向：下");
                        sender.sending(0,ACTION_DOWN,KEYCODE_S ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_S;
                        break;

                    case DIRECTION_LEFT:
                        //directionChange();
                        direction1_Text.setText("当前方向：左");
                        sender.sending(0,ACTION_DOWN,KEYCODE_A ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_A;
                        break;
                    case DIRECTION_UP_LEFT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_W ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_W;
                        sender.sending(0,ACTION_DOWN,KEYCODE_A ,0);
                        LAST_ROCKER_LEFT2 = KEYCODE_A;
                        break;
                    case DIRECTION_UP_RIGHT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_W ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_W;
                        sender.sending(0,ACTION_DOWN,KEYCODE_D ,0);
                        LAST_ROCKER_LEFT2 = KEYCODE_D;
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_S ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_S;
                        sender.sending(0,ACTION_DOWN,KEYCODE_D ,0);
                        LAST_ROCKER_LEFT2 = KEYCODE_D;
                        break;
                    case DIRECTION_DOWN_LEFT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_S ,0);
                        LAST_ROCKER_LEFT1 = KEYCODE_S;
                        sender.sending(0,ACTION_DOWN,KEYCODE_A ,0);
                        LAST_ROCKER_LEFT2 = KEYCODE_A;
                        break;
                }
            }

            @Override
            public void onFinish() {

                Log.d("KeyTest", "finish");
            }
        });
        /**
         *
         */
        mRockerView2.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_8, new MyRockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(MyRockerView.Direction direction) {
                sender.sending(0,ACTION_UP,LAST_ROCKER_MID1,0);
               // ctrlSender.sending(0,ACTION_UP,LAST_ROCKER_RIGHT2,0);
                switch (direction) {
                    case DIRECTION_CENTER:
                        //directionChange();
                        // 中心方向触发事件，一般归零
                        direction2_Text.setText("当前方向：中心");

                        break;
                    case DIRECTION_UP:
                        // directionChange();
                        direction2_Text.setText("当前方向：上");
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_UP ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_UP;
                        // 上 触发事件
                        break;

                    case DIRECTION_RIGHT:
                        //directionChange();
                        direction2_Text.setText("当前方向：右");
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_RIGHT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_RIGHT;
                        break;

                    case DIRECTION_DOWN:
                        //directionChange();
                        direction2_Text.setText("当前方向：下");
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_DOWN ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_DOWN;
                        break;

                    case DIRECTION_LEFT:
                        //directionChange();
                        direction2_Text.setText("当前方向：左");
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_LEFT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_LEFT;
                        break;
                    case DIRECTION_UP_LEFT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_UP_LEFT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_UP_LEFT;
                        break;
                    case DIRECTION_UP_RIGHT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_UP_RIGHT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_UP_RIGHT;
                        break;
                    case DIRECTION_DOWN_LEFT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_DOWN_LEFT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_DOWN_LEFT;
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_DPAD_DOWN_RIGHT ,0);
                        LAST_ROCKER_MID1 = KEYCODE_DPAD_DOWN_RIGHT;
                        break;
                }
            }

            @Override
            public void onFinish() {
                Log.d("KeyTest", "finish");
            }
        });



        /**
         * 注册左侧8方向 rocker 按键行为
         */
        mRockerView3.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_8, new MyRockerView.OnShakeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void direction(MyRockerView.Direction direction) {

                switch (direction) {
                    case DIRECTION_CENTER:
                        direction1_Text.setText("当前方向：中心");

                        break;
                    case DIRECTION_UP:
                        // directionChange();
                        direction1_Text.setText("当前方向：上");
                        sender.sending(0,ACTION_DOWN,KEYCODE_W ,0);
                        break;

                    case DIRECTION_RIGHT:
                        //directionChange();
                        direction1_Text.setText("当前方向：右");
                        sender.sending(0,ACTION_DOWN,KEYCODE_D ,0);
                       // ctrlSender.sending(controlArgs.RightKey3);
                        break;

                    case DIRECTION_DOWN:
                        //directionChange();
                        direction1_Text.setText("当前方向：下");
                        sender.sending(0,ACTION_DOWN,KEYCODE_S ,0);
                        break;

                    case DIRECTION_LEFT:
                        //directionChange();
                        direction1_Text.setText("当前方向：左");
                        sender.sending(0,ACTION_DOWN,KEYCODE_A ,0);
                        break;
                    case DIRECTION_UP_LEFT:
                        sender.sending(0,ACTION_DOWN,KEYCODE_W ,0);
                        break;
                    case DIRECTION_UP_RIGHT:
                        //ctrlSender.sending(controlArgs.UpRightKey3);
                        break;
                    case DIRECTION_DOWN_RIGHT:
                        //ctrlSender.sending(controlArgs.DownRightKey3);
                        break;
                    case DIRECTION_DOWN_LEFT:
                        //ctrlSender.sending(controlArgs.DownLeftKey3);
                        break;
                }
            }

            @Override
            public void onFinish() {

                Log.d("KeyTest", "finish");
            }
        });
    }




    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.setting_imbt:
                showPopupWindow();
                break;
            case R.id.show:
                mRockerView1.setVisibility(View.VISIBLE);
                mRockerView2.setVisibility(View.VISIBLE);
                mRockerView3.setVisibility(View.VISIBLE);
                RT.setVisibility(View.VISIBLE);
                LT.setVisibility(View.VISIBLE);
                RB.setVisibility(View.VISIBLE);
                LB.setVisibility(View.VISIBLE);

                A.setVisibility(View.VISIBLE);
                B.setVisibility(View.VISIBLE);
                X.setVisibility(View.VISIBLE);
                Y.setVisibility(View.VISIBLE);
                break;
            case R.id.hide:
                mRockerView2.setVisibility(View.INVISIBLE);
                mRockerView1.setVisibility(View.INVISIBLE);
                mRockerView3.setVisibility(View.INVISIBLE);
                RT.setVisibility(View.INVISIBLE);
                LT.setVisibility(View.INVISIBLE);
                RB.setVisibility(View.INVISIBLE);
                LB.setVisibility(View.INVISIBLE);

                A.setVisibility(View.INVISIBLE);
                B.setVisibility(View.INVISIBLE);
                X.setVisibility(View.INVISIBLE);
                Y.setVisibility(View.INVISIBLE);
                break;

            case R.id.move:
                move_flag = !move_flag;
                if(move_flag)
                    tv1.setText("停止移动");
                else
                    tv1.setText("移动组件");
                break;
            case R.id.LB_btn:
                direction2_Text.setText("LB");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_L1,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.LT_btn:
                direction2_Text.setText("LT");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_L2,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.RB_btn:
                direction2_Text.setText("RB");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_R1,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.RT_btn:
                direction2_Text.setText("RT");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_R2,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.A_btn:
                direction2_Text.setText("A");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_A,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.B_btn:
                direction2_Text.setText("B");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_B,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.X_btn:
                direction2_Text.setText("X");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_X,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.Y_btn:
                direction2_Text.setText("Y");
                sender.sending(0,ACTION_UP,KEYCODE_BUTTON_Y,0);
                vibrator.vibrate(new long[]{0,50},-1);
                break;
            case R.id.start_video:
                //开一个线程
                start_btn.setVisibility(View.INVISIBLE);
                sender = new H264TcpReceiver(FullscreenActivity.this,surface,myIP);
                sender.start();
                break;
            case R.id.commit:
                myIP = editText.getText().toString();
            default:
                break;
        }
    }

    /**
     * 定义PopWindow
     */
    private void showPopupWindow(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_menu, null);

        mPopWindow = new PopupWindow(contentView,
                500, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        tv1 = (TextView)contentView.findViewById(R.id.move);
        if(move_flag)
            tv1.setText("停止移动");
        else
            tv1.setText("移动组件");
        TextView tv2 = (TextView)contentView.findViewById(R.id.show);
        TextView tv3 = (TextView)contentView.findViewById(R.id.hide);
        TextView tv4 = (TextView)contentView.findViewById(R.id.commit);
        editText = (EditText) contentView.findViewById(R.id.inputIP);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        mPopWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER,0,0);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!move_flag ){
            // 处理按键按下
            switch(v.getId()) {
                case R.id.LB_btn:
                    direction2_Text.setText("LB");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_L1,0);
                    //vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.LT_btn:
                    direction2_Text.setText("LT");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_L2,0);
                    //vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.RB_btn:
                    direction2_Text.setText("RB");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_R1,0);
                    //vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.RT_btn:
                    direction2_Text.setText("RT");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_R2,0);
                    //vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.A_btn:
                    direction2_Text.setText("A");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_A,0);
                   // vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.B_btn:
                    direction2_Text.setText("B");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_B,0);
                    //vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.X_btn:
                    direction2_Text.setText("X");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_X,0);
                   // vibrator.vibrate(new long[]{0,50},-1);
                    break;
                case R.id.Y_btn:
                    direction2_Text.setText("Y");
                    sender.sending(0,ACTION_DOWN,KEYCODE_BUTTON_Y,0);
                   // vibrator.vibrate(new long[]{0,50},-1);
                    break;
                default:
                    break;
            }
            return false;  // 返回 false 可以继续响应 onClick
        }
        int Id  = v.getId();
        View mv_view = findViewById(Id);
        move(mv_view,event);

        return true;// 不会中断触摸事件的返回
    }

    private void move(View v,MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                sx = (int) event.getRawX();
                sy = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                int x = (int) event.getRawX();
                int y = (int) event.getRawY();
                // 获取手指移动的距离
                int dx = x - sx;
                int dy = y - sy;
                // 得到imageView最开始的各顶点的坐标
                int l =v.getLeft();
                int r =v.getRight();
                int t = v.getTop();
                int b = v.getBottom();
                // 更改imageView在窗体的位置
                v.layout(l + dx, t + dy, r + dx, b + dy);
                // 获取移动后的位置
                sx = (int) event.getRawX();
                sy = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                // 记录最后图片在窗体的位置
                break;

        }
    }


}