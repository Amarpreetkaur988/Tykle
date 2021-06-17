package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.GetProfileModel;
import com.tykle.R;
import com.tykle.adapter.CustomPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements OnClickListener {

    private Activity activity;
    private String img1 = "", img2 = "", img3 = "", img4 = "";
    private List<String> product_images = new ArrayList<>();
    private ViewPager viewPager;
    private TextView user_name, user_age, user_about;
    private CircleIndicator indicator;
    private String user_id;
    private TextView tv_title, user_status;
    private ImageView iv_firsticon;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initViews();

        user_id = getIntent().getStringExtra("id");
        tv_title.setText("Profile");

        getdata();

        //listners
        iv_firsticon.setOnClickListener(this);


    }

    private void initViews() {

        activity = UserProfileActivity.this;
        viewPager = findViewById(R.id.viewPager);
        user_status = findViewById(R.id.user_status);
        user_name = findViewById(R.id.user_name);
        user_age = findViewById(R.id.user_age);
        user_about = findViewById(R.id.user_about);
        indicator = findViewById(R.id.indicator);
        iv_firsticon = findViewById(R.id.iv_firsticon);
        tv_title = findViewById(R.id.tv_title);
    }

    private void getdata() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);


            HelperClass.showProgressDialog(activity);

            apiService.GetProfile(user_id).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    HelperClass.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {


                            user_name.setText(response.body().getDetails().getUserName());
                            user_status.setText(response.body().getDetails().getRelationshipStatus());
                            user_age.setText(response.body().getDetails().getAge());
                            user_about.setText(response.body().getDetails().getAbout());
                            img1 = response.body().getDetails().getImage1();
                            img2 = response.body().getDetails().getImage2();
                            img3 = response.body().getDetails().getImage3();
                            img4 = response.body().getDetails().getImage4();


                            if (!img1.isEmpty()) {

                                product_images.add(img1);
                            }

                            if (!img2.isEmpty()) {

                                product_images.add(img2);
                            }

                            if (!img3.isEmpty()) {
                                product_images.add(img3);

                            }
                            if (!img4.isEmpty()) {
                                product_images.add(img4);

                            }


                            CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(UserProfileActivity.this, product_images);

                            viewPager.setAdapter(mCustomPagerAdapter);

                            indicator.setViewPager(viewPager);

                            mCustomPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {
                    HelperClass.hideProgressDialog();
                }
            });

        } else {


            Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_firsticon:
                onBackPressed();
                break;
        }

    }
}
