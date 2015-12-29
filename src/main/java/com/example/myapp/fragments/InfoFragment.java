package com.example.myapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapp.AddFrieldActivity;
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
        TextView un = (TextView) view.findViewById(R.id.userName);
        TextView pw = (TextView) view.findViewById(R.id.password);
        try {
            un.setText(Auth.userInfo.getString("email"));
            pw.setText(Auth.userInfo.getString("password"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {

    }

}
