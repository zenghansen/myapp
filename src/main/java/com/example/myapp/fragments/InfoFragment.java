package com.example.myapp.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapp.AddFrieldActivity;
import com.example.myapp.EditInfoActivity;
import com.example.myapp.LoginActivity;
import com.example.myapp.R;
import com.example.myapp.tools.Auth;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoFragment extends Fragment implements View.OnClickListener {
    private View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View viewById = getActivity().findViewById(R.id.id_fragment_title);
        Button edit = (Button) viewById.findViewById(R.id.editInfo);
        edit.setOnClickListener(this);
        Button exit = (Button) getActivity().findViewById(R.id.action_exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editInfo:
                Intent intent = new Intent(this.getActivity(), EditInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.action_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确认退出吗?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        new Auth().exit(getActivity());
                        getActivity().finish();
                        Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent1);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    private void updateInfo() {
        TextView un = (TextView) view.findViewById(R.id.userName);
        TextView nn = (TextView) view.findViewById(R.id.nickname);
        try {
            un.setText(Auth.userInfo.getString("email"));
            nn.setText(Auth.userInfo.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateInfo();
    }

}
