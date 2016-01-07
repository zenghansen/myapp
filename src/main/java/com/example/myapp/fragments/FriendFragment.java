package com.example.myapp.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.myapp.AddFrieldActivity;
import com.example.myapp.R;
import com.example.myapp.fragments.MessageFragment;

public class FriendFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View viewById = getActivity().findViewById(R.id.id_fragment_title);
        Button actionAdd = (Button) viewById.findViewById(R.id.action_addfrield);
        actionAdd.setOnClickListener(this);
       new Thread(sendRun).start();
    }
    final Runnable sendRun = new Runnable() {
        @Override
        public void run() {
            sl();
        }
    };
    @Override
    public void onClick(View v) {

       // Intent intent = new Intent(this.getActivity(), AddFrieldActivity.class);
       // startActivity(intent);
    }
    private void sl(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScrollView sv = (ScrollView) getActivity().findViewById(R.id.scrollView);
        sv.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
    }

}
