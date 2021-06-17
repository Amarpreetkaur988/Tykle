package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.UserRegister;
import com.tykle.R;
import com.tykle.util.App;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity implements OnClickListener {


    Activity activity;
    private ImageView iv_next;
    private EditText otp1, otp2, otp3, otp4;
    private TextView resend_code;
    private String otp, email, password;
    private LinearLayout parentPanel;

    private List<MultipartBody.Part> imageBody;

    private MultipartBody.Part[] image;

    private boolean firstTimeAccess;
    private String token, name;
    private TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (otp1.getText().hashCode() == s.hashCode()) {

                if (!otp1.getText().toString().trim().equalsIgnoreCase("")) {
                    otp2.requestFocus();

                }
            } else if (otp2.getText().hashCode() == s.hashCode()) {

                if (!otp2.getText().toString().trim().equalsIgnoreCase("")) {
                    otp3.requestFocus();

                }

            } else if (otp3.getText().hashCode() == s.hashCode()) {

                if (!otp3.getText().toString().trim().equalsIgnoreCase("")) {
                    otp4.requestFocus();

                }

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        activity = this;
        Intent intent = getIntent();
        otp = intent.getStringExtra("otp");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        name = intent.getStringExtra("name");


        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        resend_code = findViewById(R.id.resend_code);
        iv_next = findViewById(R.id.iv_next);
        parentPanel = findViewById(R.id.parentPanel);

        otp1.addTextChangedListener(generalTextWatcher);
        otp2.addTextChangedListener(generalTextWatcher);
        otp3.addTextChangedListener(generalTextWatcher);
        otp4.addTextChangedListener(generalTextWatcher);

        //clicks
        iv_next.setOnClickListener(this);
        resend_code.setOnClickListener(this);

        firstTimeAccess = true;

        token = FirebaseInstanceId.getInstance().getToken();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_next:
                Validate();
                break;

            case R.id.resend_code:
                ResendCodeApi();
                break;
        }

    }

    private void ResendCodeApi() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.EmailVerification(email).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    HelperClass.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            otp = response.body().get("OTP").toString();

                            Toast.makeText(activity, "Please check your email", Toast.LENGTH_SHORT).show();


                        } else {

                            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);
                }
            });

        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }


    }

    private void Validate() {

        String sotp1 = otp1.getText().toString();
        String sotp2 = otp2.getText().toString();
        String sotp3 = otp3.getText().toString();
        String sotp4 = otp4.getText().toString();

        if (TextUtils.isEmpty(sotp1)) {
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validotp);

        } else if (TextUtils.isEmpty(sotp2)) {
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validotp);

        } else if (TextUtils.isEmpty(sotp3)) {
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validotp);


        } else if (TextUtils.isEmpty(sotp4)) {
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validotp);
        } else {

            String fullOtp = sotp1 + sotp2 + sotp3 + sotp4;
            if (otp.equalsIgnoreCase(fullOtp)) {

                RegisterUserApi();


            } else {

                ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validotp);
            }

        }
    }

    private void RegisterUserApi() {

        setImageBodyList();

        image = new MultipartBody.Part[imageBody.size()];
        for (int i = 0; i < imageBody.size(); i++) {
            image[i] = imageBody.get(i);
        }


        String userName = App.getCommonInstances().getUserName();
        String phone = App.getCommonInstances().getMobile();


        RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody deviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), "Android");
        RequestBody latitudeBody = RequestBody.create(MediaType.parse("text/plain"), HelperClass.getLatitude(activity));
        RequestBody longitudeBody = RequestBody.create(MediaType.parse("text/plain"), HelperClass.getLongitude(activity));
        RequestBody RegIdBody = RequestBody.create(MediaType.parse("text/plain"), token);

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.UserRegister(userNameBody, emailBody, passwordBody, phoneBody, deviceTypeBody,
                    latitudeBody, longitudeBody, RegIdBody, image).enqueue(new Callback<UserRegister>() {
                @Override
                public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {

                    HelperClass.hideProgressDialog();

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            String user_id = response.body().getDetails().getId();

                            Intent intent = new Intent(activity, ProfileQuestionActivity.class);

                            intent.putExtra("user_id", user_id);

                            startActivity(intent);
                            Toast.makeText(activity, "Account Successfully created", Toast.LENGTH_SHORT).show();
                            OtpActivity.this.finish();


                        } else {

                            Toast.makeText(activity, "Network issue Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRegister> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);

                }
            });


        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }


    }

    private void setImageBodyList() {

        imageBody = new ArrayList<>();

        List<String> imagepathList;

        imagepathList = App.getCommonInstances().getImagesPathList();

        for (Iterator<String> iterator = imagepathList.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            if (value.equalsIgnoreCase("")) {
                iterator.remove();
            }
        }
        for (int i = 0; i < imagepathList.size(); i++) {

            File file = new File(imagepathList.get(i));
            final RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image[]", file.getName(), reqFile);
            imageBody.add(body);
        }

    }


}

