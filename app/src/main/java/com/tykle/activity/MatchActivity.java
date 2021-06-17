package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tykle.Classess.HelperClass;
import com.tykle.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchActivity extends AppCompatActivity implements View.OnClickListener {

    String url;
    private Activity activity;
    private CircleImageView friendImg, userImg;
    private Button keep_swapping, send_message;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        url = getIntent().getStringExtra("frindImg");
        activity = MatchActivity.this;
        InitViews();
        setData();
        //clicks
        btn_back.setOnClickListener(this);
        keep_swapping.setOnClickListener(this);
        send_message.setOnClickListener(this);
    }

    private void setData() {

        Picasso.with(activity).load(url).into(friendImg);
        Picasso.with(activity).load(HelperClass.getProfileImage(activity)).into(userImg);
    }

    private void InitViews() {

        friendImg = findViewById(R.id.frindImg);
        userImg = findViewById(R.id.userImg);
        send_message = findViewById(R.id.send_message);
        keep_swapping = findViewById(R.id.keep_swapping);
        btn_back = findViewById(R.id.btn_back);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;

            case R.id.keep_swapping:
                Intent interest = new Intent(activity, InterestActivity.class);
                startActivity(interest);
                finish();
                break;

            case R.id.send_message:
                Intent match = new Intent(activity, MatchesRequestsActivity.class);
                startActivity(match);
                finish();
                break;

        }
    }
}
