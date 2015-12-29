package com.example.myapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddFrieldActivity extends Activity implements View.OnClickListener {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addfrield);
        View viewById = this.findViewById(R.id.id_fragment_title);
        ImageButton btn = (ImageButton) viewById.findViewById(R.id.id_title_left_btn);
        TextView text = (TextView) viewById.findViewById(R.id.app_title);
        btn.setImageDrawable(this.getResources().getDrawable(R.drawable.back));
        text.setText(this.getResources().getString(R.string.add_frield_title));
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.finish();
    }
}