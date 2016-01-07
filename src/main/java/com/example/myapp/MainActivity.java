/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.myapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import com.example.myapp.fragments.FriendFragment;
import com.example.myapp.fragments.InfoFragment;
import com.example.myapp.fragments.MessageFragment;
import com.example.myapp.tools.db.UserDBHelper;

public class MainActivity extends Activity implements View.OnClickListener {
    public static String EXTRA_MESSAGE = null;

    private Button mTabWeixin;
    private Button mTabFriend;
    private Button mTabMe;

    private MessageFragment mWeixin;
    private FriendFragment mFriend;
    private InfoFragment mInfo;

    public static Integer REQUEST_EXIT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        UserDBHelper db = new UserDBHelper(this);
        String uid = db.getUid();
        if(uid ==null){
            this.finish();
        }else {
            setContentView(R.layout.main1);

            // 初始化控件和声明事件
            mTabWeixin = (Button) findViewById(R.id.tab_bottom_weixin);
            mTabFriend = (Button) findViewById(R.id.tab_bottom_friend);
            mTabMe = (Button) findViewById(R.id.tab_bottom_me);
            mTabWeixin.setOnClickListener(this);
            mTabFriend.setOnClickListener(this);
            mTabMe.setOnClickListener(this);

            // 设置默认的Fragment
            setDefaultFragment();
        }
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mWeixin = new MessageFragment();
        transaction.replace(R.id.id_content, mWeixin);
        transaction.commit();
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        View viewById = this.findViewById(R.id.id_fragment_title);
        Button add = (Button) viewById.findViewById(R.id.action_addfrield);
        Button editInfo = (Button) viewById.findViewById(R.id.editInfo);

        add.setVisibility(View.GONE);
        editInfo.setVisibility(View.GONE);
        switch (v.getId())
        {
            case R.id.tab_bottom_weixin:
                if (mWeixin == null)
                {
                    mWeixin = new MessageFragment();
                }
                // 使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, mWeixin);
                break;
            /*case R.id.tab_bottom_friend:
                if (mFriend == null)
                {
                    mFriend = new FriendFragment();
                }
                add.setVisibility(View.VISIBLE);
                transaction.replace(R.id.id_content, mFriend);
                break;*/
            case R.id.tab_bottom_me:
                if (mInfo == null)
                {
                    mInfo = new InfoFragment();
                }
                editInfo.setVisibility(View.VISIBLE);
                transaction.replace(R.id.id_content, mInfo);
                break;
        }
         //transaction.addToBackStack();
        // 事务提交
        transaction.commit();
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
}