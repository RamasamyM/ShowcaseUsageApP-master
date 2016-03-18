package com.example.admin.myapplication;

/**
 * Created by Admin on 2/19/2016.
 */
import   android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SipActivity extends Activity {

    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
        imageView= (ImageView)findViewById(R.id.imageview_icon);
        textView= (TextView)findViewById(R.id.textview_info);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
