package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.myapp.tools.Auth;
import com.example.myapp.tools.HttpRequest;
import com.example.myapp.tools.db.UserDBHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Stack;

public class LoginActivity extends Activity implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        findViewById(R.id.doLogin).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doLogin:
                this.doLogin();
                break;
        }
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                setResult(RESULT_OK, null);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void doLogin() {
        final String url = Auth.serverHost + "Auth.php";
        final EditText email = (EditText) findViewById(R.id.userName);
        final EditText password = (EditText) findViewById(R.id.password);
        final String emailStr = email.getText().toString();
        final String passwordStr = password.getText().toString();
        if (emailStr.equals("") || passwordStr.equals("")) {
            Toast.makeText(getApplicationContext(), "账号或密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final HashMap map = new HashMap() {{
            put("email", emailStr);
            put("password", passwordStr);
        }};
        Runnable downloadRun = new Runnable() {
            @Override
            public void run() {
                HttpRequest foo = new HttpRequest(url, map);
                HashMap<String, Object> ret = foo.sendHttp();
                if (ret.get("error").equals(false)) {
                    mHandler.obtainMessage(1, ret).sendToTarget();
                } else {
                    mHandler.obtainMessage(0, ret).sendToTarget();
                }

            }
        };
        new Thread(downloadRun).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = "info";
            HashMap map = (HashMap) msg.obj;
            switch (msg.what) {
                case 1:
                    JSONObject ret = (JSONObject) map.get("data");
                    try {
                        int err = ret.getInt("error");
                        if (err == 1) {
                            str = ret.getString("msg");
                        } else {
                            str = "登录成功";
                            /*保存token*/
                            String token = setToken(ret.getJSONObject("data"));
                            Toast.makeText(getApplicationContext(), str+token,
                                    Toast.LENGTH_SHORT).show();

                           // finish();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    str = "通讯失败";
                    break;
            }

            Toast.makeText(getApplicationContext(), str,
                    Toast.LENGTH_SHORT).show();
        }
    };
    private String setToken(JSONObject data){
        UserDBHelper db = new UserDBHelper(this);
        try {
            db.setToken(data.getString("uid"),data.getString("token"));
            return db.getToken(data.getString("uid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}