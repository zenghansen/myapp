package com.example.myapp.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapp.R;
public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    public final String[] uid;
    public final String[] unread;
    public final String[] addtime;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] web, Integer[] imageId,String[] uid,String[] unread,String[] addtime) {
        super(context, R.layout.fragment_message_list, web);
        this.context = context;
        this.web = web;
        this.uid = uid;
        this.unread = unread;
        this.addtime = addtime;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.fragment_message_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.label);
        TextView ur = (TextView) rowView.findViewById(R.id.ur);
        TextView at = (TextView) rowView.findViewById(R.id.time);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        txtTitle.setText(web[position]);
        if(!unread[position].equals("0")) {
            ur.setText(unread[position]);
        }else{
            ur.setVisibility(View.GONE);
        }
        at.setText(addtime[position]);
        txtTitle.setId(Integer.parseInt(uid[position]));

        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}