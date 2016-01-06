package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.myapp.tools.Auth;

import java.util.HashMap;

public class EditInfoActivity extends Activity implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_editinfo);
        ImageButton btn = (ImageButton) findViewById(R.id.id_title_left_btn);
        TextView text = (TextView) findViewById(R.id.app_title);
        btn.setImageDrawable(this.getResources().getDrawable(R.drawable.back));
        text.setText(this.getResources().getString(R.string.edit_info_title));
        Button doEditBtn = (Button) findViewById(R.id.doEdit);
        doEditBtn.setOnClickListener(this);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_title_left_btn:
                this.finish();
                break;
            case R.id.doEdit:
                doEdit();
                break;
        }
    }

    private void doEdit() {
        EditText nn = (EditText) findViewById(R.id.nickname);
        final String nnStr = nn.getText().toString();
        if (nnStr.equals("")) {
            Toast.makeText(getApplicationContext(), "昵称不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final Activity at = this;
        Runnable downloadRun = new Runnable() {
            @Override
            public void run() {
                HashMap ret = new Auth().editInfo(nnStr);
                mHandler.obtainMessage(1, ret).sendToTarget();
            }
        };
        new Thread(downloadRun).start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = "info";
            HashMap map = (HashMap) msg.obj;
            str = map.get("msg").toString();
            Toast.makeText(getApplicationContext(), str,
                    Toast.LENGTH_SHORT).show();
            if (!(Boolean) map.get("error")) {
                    finish();
            }
        }
    };
}