package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
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

        final Activity at = this;
        Runnable downloadRun = new Runnable() {
            @Override
            public void run() {
                if (new Auth().verify(at)) {
                    verifyHandler.obtainMessage(1).sendToTarget();
                }
            }
        };
        new Thread(downloadRun).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doLogin:
                this.doLogin();
                break;
        }
    }

    private void doLogin() {
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
        final Activity at = this;
        Runnable downloadRun = new Runnable() {
            @Override
            public void run() {
                HashMap ret = new Auth().Login(map,at);
                mHandler.obtainMessage(1, ret).sendToTarget();
            }
        };
        new Thread(downloadRun).start();
    }

    private Handler verifyHandler = new Handler() {
        public void handleMessage(Message msg) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = "info";
            HashMap map = (HashMap) msg.obj;
            str = map.get("msg").toString();
            if (!(boolean) map.get("error")) {
                Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT).show();

                startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), MainActivity.REQUEST_EXIT);
            }
            Toast.makeText(getApplicationContext(), str,
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MainActivity.REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }
}