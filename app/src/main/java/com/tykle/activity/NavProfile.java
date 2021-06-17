package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.GetProfileModel;
import com.tykle.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavProfile extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView nav_profile_image;
    private ImageView next;
    private ImageView iv_edit;
    private Activity activity;
    private TextView et_location, et_phone, et_age, et_email, et_username, et_interst, tv_status, tv_about;
    private TextView title;
    private List<String> listofimages = new ArrayList<>();
    private RecyclerView images_recycler;
    private CircleImageView image1, image2, image3;
    private String img1 = "", img2 = "", img3 = "", img4 = "";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_profile);
        activity = NavProfile.this;

        initviews();
        setUptoolbar();


        //listners
        next.setOnClickListener(this);


    }

    private void setUptoolbar() {

        title.setText("Profile");


        iv_edit.setVisibility(View.VISIBLE);
        iv_edit.setOnClickListener(this);
    }

    private void initviews() {

        next = findViewById(R.id.next);
        title = findViewById(R.id.title);
        iv_edit = findViewById(R.id.iv_edit);
        tv_about = findViewById(R.id.tv_about);
        tv_status = findViewById(R.id.tv_status);
        et_interst = findViewById(R.id.et_interst);
        et_username = findViewById(R.id.et_username);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        et_age = findViewById(R.id.et_age);
        nav_profile_image = findViewById(R.id.nav_profile_image);
        et_location = findViewById(R.id.et_location);

    }

    private void setUserdata() {

        et_username.setText(HelperClass.getUserName(activity));
        et_phone.setText(HelperClass.getPhone(activity));
        et_email.setText(HelperClass.getEmail(activity));
        String age = HelperClass.getAge(activity);
        tv_about.setText(HelperClass.getAbout(activity));
        et_age.setText(age);


        //check image is null or not


        et_interst.setText(HelperClass.getUserInterst(activity));

        String Useraddress = (HelperClass.getUserCity(activity) + "," + HelperClass.getUserState(activity));
        et_location.setText(Useraddress);


        GetImages();

    }

    private void GetImages() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.GetProfile(HelperClass.getID(activity)).enqueue(new Callback<GetProfileModel>() {
                @Override
                public void onResponse(Call<GetProfileModel> call, Response<GetProfileModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            tv_status.setText(response.body().getDetails().getRelationshipStatus());
                            img1 = response.body().getDetails().getImage1();
                            img2 = response.body().getDetails().getImage2();
                            img3 = response.body().getDetails().getImage3();
                            img4 = response.body().getDetails().getImage4();

                            Glide.with(activity).load(img1).into(nav_profile_image);

                            if (!img2.isEmpty()) {
                                Glide.with(activity).load(img2).into(image1);
                            } else {


                            }

                            if (!img3.isEmpty()) {

                                Glide.with(activity).load(img3).into(image2);
                            } else {


                            }

                            if (!img4.isEmpty()) {

                                Glide.with(activity).load(img4).into(image3);
                            } else {


                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileModel> call, Throwable t) {

                }
            });

        } else {


            Toast.makeText(activity, "Network error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next:
                onBackPressed();
                break;

            case R.id.iv_edit:
                Intent intent = new Intent(NavProfile.this, EditProfileActivity.class);
                intent.putExtra("img1", img1);
                intent.putExtra("img2", img2);
                intent.putExtra("img3", img3);
                intent.putExtra("img4", img4);
                startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUserdata();


    }
}
