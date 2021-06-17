package com.tykle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.FilterUserModel;
import com.tykle.ModelClassess.GetUsers;
import com.tykle.R;
import com.tykle.adapter.CustomPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements OnClickListener {

    private ViewPager viewPager;

    private TextView user_name, user_age, user_status;

    private EmojiconTextView user_about;

    private GetUsers.Detail detailuser;

    private FilterUserModel.Detail detailfilter;

    private CircleIndicator indicator;

    private List<String> product_images = new ArrayList<>();

    private String frindImg;

    private ImageView rejectBtn, acceptBtn;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        type = getIntent().getStringExtra("type");
        if (type.equalsIgnoreCase("users")) {

            detailuser = (GetUsers.Detail) getIntent().getExtras().getSerializable("details");
        } else {

            detailfilter = (FilterUserModel.Detail) getIntent().getExtras().getSerializable("details");
        }


        initviews();
        setViewPager();
        setdata();

        //onClicklistners
        rejectBtn.setOnClickListener(this);
        acceptBtn.setOnClickListener(this);

    }

    private void setdata() {

        if (type.equalsIgnoreCase("users")) {
            user_about.setText(detailuser.getAbout());
            user_age.setText(detailuser.getAge());
            user_status.setText(detailuser.getRelationshipStatus());
            user_name.setText(detailuser.getUserName());

        } else {

            user_about.setText(detailfilter.getAbout());
            user_status.setText(detailfilter.getRelationshipStatus());
            user_age.setText(detailfilter.getAge());
            user_name.setText(detailfilter.getUserName());

        }

    }

    private void initviews() {

        user_status = findViewById(R.id.user_status);
        user_name = findViewById(R.id.user_name);
        user_age = findViewById(R.id.user_age);
        viewPager = findViewById(R.id.viewPager);
        acceptBtn = findViewById(R.id.acceptBtn);
        rejectBtn = findViewById(R.id.rejectBtn);
        user_about = findViewById(R.id.user_about);
        indicator = findViewById(R.id.indicator);
    }

    private void setViewPager() {

        if (type.equalsIgnoreCase("users")) {

            if (!detailuser.getImage1().isEmpty()) {

                product_images.add(detailuser.getImage1());

            }
            if (!detailuser.getImage2().isEmpty()) {

                product_images.add(detailuser.getImage2());


            }
            if (!detailuser.getImage3().isEmpty()) {

                product_images.add(detailuser.getImage3());

            }
            if (!detailuser.getImage4().isEmpty()) {

                product_images.add(detailuser.getImage4());

            }


        } else {

            if (!detailfilter.getImage1().isEmpty()) {

                product_images.add(detailfilter.getImage1());

            }
            if (!detailfilter.getImage2().isEmpty()) {

                product_images.add(detailfilter.getImage2());


            }
            if (!detailfilter.getImage3().isEmpty()) {

                product_images.add(detailfilter.getImage3());

            }
            if (!detailfilter.getImage4().isEmpty()) {

                product_images.add(detailfilter.getImage4());

            }

        }


        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(ProfileActivity.this, product_images);

        viewPager.setAdapter(mCustomPagerAdapter);

        indicator.setViewPager(viewPager);

        mCustomPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rejectBtn:
                onBackPressed();
                break;

            case R.id.acceptBtn:
                Animation animFadein1;
                animFadein1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                final AnimationSet s1 = new AnimationSet(true);
                s1.setInterpolator(new AccelerateInterpolator());
                s1.addAnimation(animFadein1);
                acceptBtn.startAnimation(s1);
                PerformActionOnUser("1");
                onBackPressed();
                break;

        }
    }

    private void PerformActionOnUser(String action) {

        String friend_id;
        if (type.equalsIgnoreCase("users")) {

            friend_id = detailuser.getId();

        } else {

            friend_id = detailfilter.getId();

        }


        if (HelperClass.isNetworkConnected(ProfileActivity.this)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);
            apiService.UserAction(HelperClass.getID(ProfileActivity.this), friend_id, action).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {


                            String status = response.body().get("status").toString();
                            if (status.equalsIgnoreCase("3")) {

                                if (type.equalsIgnoreCase("users")) {

                                    frindImg = detailuser.getImage1();
                                } else {

                                    frindImg = detailfilter.getImage1();
                                }

                                Intent match = new Intent(ProfileActivity.this, MatchActivity.class);
                                match.putExtra("frindImg", frindImg);
                                startActivity(match);

                            }

                        } else {

                            Toast.makeText(ProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    Toast.makeText(ProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {


            Toast.makeText(ProfileActivity.this, "Network error", Toast.LENGTH_SHORT).show();
        }

    }

}
