package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.myapp.tools.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends Activity implements View.OnClickListener {
    public static final String IP_ADDR = "122.114.62.8";//服务器地址
    public static final int PORT = 9501;//服务器端口号
    private static String msg = null;//要发送的msg
    private Socket socket = null;
    private InputStream input = null;
    private Thread threadGet = null;
    private Thread threadSend = null;
    private Thread threadMsg = null;
    private String tid = null;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        View viewById = this.findViewById(R.id.id_fragment_title);
        ImageButton btn = (ImageButton) viewById.findViewById(R.id.id_title_left_btn);
        Button sendbtn = (Button) this.findViewById(R.id.sendBtn);
        TextView text = (TextView) viewById.findViewById(R.id.app_title);
        btn.setImageDrawable(this.getResources().getDrawable(R.drawable.back));
        text.setText("某某某");
        btn.setOnClickListener(this);
        sendbtn.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        this.tid = bundle.getString("uid");
        doChat();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_title_left_btn:
                this.finish();
                break;
            case R.id.sendBtn:
                send();
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            threadMsg.interrupt();
            threadGet.interrupt();
            threadSend.interrupt();
            input.close();
            socket.close();
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler getHandler = new Handler() {
        public void handleMessage(Message msg) {

            try {

                LayoutInflater flater = LayoutInflater.from(getApplicationContext());
                LinearLayout layout = (LinearLayout) findViewById(R.id.msgContent);

                JSONArray json = new JSONArray(msg.obj.toString());
                for(int i=0;i<json.length();i++) {
                    JSONObject info = (JSONObject)json.get(i);
                    View view = null;
                    if(info.getString("fid").equals(Auth.uid)){
                        view = flater.inflate(R.layout.fragment_msg1, null);
                    }else{
                        view = flater.inflate(R.layout.fragment_msg, null);
                    }
                    ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                    TextView name = (TextView) view.findViewById(R.id.fname);
                    TextView label = (TextView) view.findViewById(R.id.label);
                    imageView.setImageResource(R.drawable.headpic);
                    label.setText(info.getString("content"));
                    name.setText("["+info.getString("fname")+"]");
                    layout.addView(view);
                }
                ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
                sv.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    final Runnable getRun = new Runnable() {
        @Override
        public void run() {
            doGetMsg();
        }
    };
    final Runnable sendRun = new Runnable() {
        @Override
        public void run() {
            doSendMsg();
        }
    };
    Runnable msgRun = new Runnable() {
        @Override
        public void run() {
            if (socket == null) {
                try {
                    socket = new Socket(IP_ADDR, PORT);
                    input = socket.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            threadSend = new Thread(sendRun);
            threadGet = new Thread(getRun);
            threadSend.start();
            threadGet.start();
        }
    };

    private void doChat() {
        HashMap map = new HashMap() {{
            put("fid", Auth.uid);
            put("tid", tid);
        }};
        msg = JSON.toJSONString(map);//获取聊天记录


        threadMsg = new Thread(msgRun);
        threadMsg.start();

    }

    private void send() {
        final EditText msgStr = (EditText) findViewById(R.id.sendText);
        if (msgStr.getText().toString().equals("")) {
            return;
        }
        HashMap map = new HashMap() {{
            put("fid", Auth.uid);
            put("content", msgStr.getText().toString());
            put("tid", tid);
        }};
        //ArrayList list = new ArrayList();
        //list.add(map);
        msg = JSON.toJSONString(map);
        msgStr.setText(null);
    }


    private void doGetMsg() {
        try {
            //读取服务器端数据
            while (true) {
                byte[] buf = new byte[1024];
                int len = 0;
                String gMsg = "";
                while ((len = input.read(buf)) != -1) {
                    String tem = new String(buf, 0, len, "UTF-8");
                    String ck = tem.substring(tem.length() - 5, tem.length());
                    if (ck.equals("//END")) {
                        tem = tem.substring(0, tem.length() - 5);
                        gMsg += tem;
                        break;
                    } else {
                        gMsg += tem;
                    }
                }
                if (gMsg != null) {
                    getHandler.obtainMessage(1, gMsg).sendToTarget();
                }

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {

        }
    }

    private void doSendMsg() {
        try {
            while (true) {
                //向服务器端发送数据
                if (msg != null && msg != "") {
                    PrintWriter out = new PrintWriter(socket.getOutputStream());
                    // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    out.println(msg);
                    out.flush();
                    msg = null;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {

        }
    }
}