package com.example.myapp.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.myapp.*;
import com.example.myapp.custom.CustomList;
import com.example.myapp.tools.Auth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MessageFragment extends ListFragment implements View.OnClickListener {
    private String[] uid = null;
    private String[] label = null;
    private View view = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        final HashMap map = new HashMap() {{
            put("uid", Auth.uid);
            put("token", Auth.token);
        }};
        final Activity at = this.getActivity();
        Runnable downloadRun = new Runnable() {
            @Override
            public void run() {
                HashMap ret = new Auth().getFrields();
                mHandler.obtainMessage(1, ret).sendToTarget();
            }
        };
        new Thread(downloadRun).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        //Toast.makeText(this.getActivity(), item + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this.getActivity(), ChatActivity.class);
        intent.putExtra("uid", uid[position]);
        intent.putExtra("tname", label[position]);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String str = "info";
            HashMap map = (HashMap) msg.obj;
            str = map.get("msg").toString();
            if (getActivity() != null) {
                if (!(Boolean) map.get("error")) {
                    JSONArray data = (JSONArray) map.get("data");
                    label = new String[data.length()];
                    Integer[] icon = new Integer[data.length()];
                    uid = new String[data.length()];
                    String[] unread = new String[data.length()];
                    String[] addtime = new String[data.length()];
                    for (int i = 0; i < data.length(); i++) {
                        try {
                            JSONObject info = (JSONObject) data.get(i);
                            label[i] = info.getString("name");
                            uid[i] = info.getString("id");
                            unread[i] = info.getString("unread");
                            addtime[i] = info.getString("addtime");
                            icon[i] = R.drawable.headpic;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    CustomList adapter = new
                            CustomList(getActivity(), label, icon, uid,unread,addtime);
                    setListAdapter(adapter);
                }
            }
        }
    };
}