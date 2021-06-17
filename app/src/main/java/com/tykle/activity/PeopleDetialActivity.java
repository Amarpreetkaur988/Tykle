package com.tykle.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tykle.R;

public class PeopleDetialActivity extends AppCompatActivity
{
    ImageView img_like,img_back,people_img;
    private TextView tv_description,tv_age,tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_people_detial );

        img_back=findViewById( R.id.img_back );
        img_like=findViewById(R.id.img_like);
        people_img=findViewById( R.id.people_img );

        tv_description=findViewById( R.id.tv_description );
        tv_age=findViewById( R.id.tv_age );
        tv_name=findViewById( R.id.tv_name );

    }
}
