package com.example.myapp.fragments;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.myapp.AddFrieldActivity;
import com.example.myapp.LoginActivity;
import com.example.myapp.R;
import com.example.myapp.custom.CustomList;

public class MessageFragment extends ListFragment implements View.OnClickListener {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] label ={
                "item1",
                "item2",
                "item3",
                "item4",
                "item5",
                "item6",
                "item7",
                "item8",
                "item9",
                "item10"
        };
        Integer[] icon = new Integer[]{
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.headpic,
                R.drawable.ic_action_search,
        };
        CustomList adapter = new
                CustomList(getActivity(), label, icon);
        setListAdapter(adapter);
        View viewById = getActivity().findViewById(R.id.id_fragment_title);
        Button actionAdd = (Button) viewById.findViewById(R.id.action_addfrield);

        actionAdd.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this.getActivity(), item + " selected", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this.getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}