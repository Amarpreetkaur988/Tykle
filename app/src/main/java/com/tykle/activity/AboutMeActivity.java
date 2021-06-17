package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.UpdateInfo;
import com.tykle.R;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;
import com.tykle.util.chat.ChatHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutMeActivity extends BaseActivity implements View.OnClickListener {
    TextView title;
    ImageView next;
    Button btn_next;
    EditText et_about;
    Activity activity;
    String userName, email, number, interst, age, image, qbId, aboutt;
    private LinearLayout parentPanel;
    private NestedScrollView aboutYouself;
    private String interest;
    private List<String> listofImages = new ArrayList<>();
    private String sage = "", sIdentify = null, sLooking_for = null, slanguage = null, sProfession = null, user_id, relationshipStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        title = findViewById(R.id.title);
        title.setText("About Me");
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);
        aboutYouself = findViewById(R.id.aboutYouself);
        et_about = findViewById(R.id.et_about);
        btn_next = findViewById(R.id.btn_next);
        parentPanel = findViewById(R.id.parentPanel);
        activity = AboutMeActivity.this;

        Intent intent = getIntent();
        sage = intent.getStringExtra("Age");
        sIdentify = intent.getStringExtra("Identity");
        slanguage = intent.getStringExtra("Language");
        interest = intent.getStringExtra("interest");
        sProfession = intent.getStringExtra("Profession");
        sLooking_for = intent.getStringExtra("Looking_for");
        user_id = intent.getStringExtra("user_id");
        relationshipStatus = intent.getStringExtra("relationshipStatus");

        //clicks
        btn_next.setOnClickListener(this);
        aboutYouself.setOnClickListener(this);

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next:
                Validate();
                break;

            case R.id.aboutYouself:
                et_about.requestFocus();
                break;

        }

    }

    private void Validate() {

        String about = et_about.getText().toString();

        if (TextUtils.isEmpty(about)) {
            et_about.setError(AppConstants.mandatoryField);
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);
        } else {

            UpdateInfoApi(about);
        }

    }

    private void UpdateInfoApi(String about) {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.UpdateInfo(user_id, sage, sIdentify, sLooking_for, interest,
                    slanguage, sProfession, about, relationshipStatus).enqueue(new Callback<UpdateInfo>() {
                @Override
                public void onResponse(Call<UpdateInfo> call, Response<UpdateInfo> response) {

                    HelperClass.hideProgressDialog();

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            userName = response.body().getDetails().getUserName();
                            email = response.body().getDetails().getEmail();
                            number = response.body().getDetails().getPhone();
                            aboutt = response.body().getDetails().getAbout();
                            interst = response.body().getDetails().getInterested();
                            age = response.body().getDetails().getAge();
                            image = response.body().getDetails().getImage1();
                            qbId = response.body().getDetails().getQuickboxId();


                            int index = email.lastIndexOf("@");

                            String login = email.substring(0, index);

                            String passeword = response.body().getDetails().getPassword();

                            signIn(login, passeword, qbId);

                        } else {

                            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);
                        }
                    }

                }

                @Override
                public void onFailure(Call<UpdateInfo> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);
                }
            });

        } else {


            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }
    }

    private void GoOnline() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.GoOnlineOffline("1", "0",
                    SharedPrefsHelper.getInstance().getQbUser().getId() + "").enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            startActivity(new Intent(activity, InterestActivity.class));

                            finishAffinity();
                        }
                    }

                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                }
            });

        } else {


        }
    }


    public void signIn(String email, String password, String qbId) {

        progressDialog.show();

        final QBUser qbUser = new QBUser(email, password);
        qbUser.setId(Integer.parseInt(qbId));

        ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                progressDialog.dismiss();
                SharedPrefsHelper.getInstance().saveQbUser(qbUser);

                HelperClass.setQuickBloxid(AboutMeActivity.this.qbId, activity);
                HelperClass.setID(user_id, activity);
                HelperClass.setUsername(userName, activity);
                HelperClass.setEmail(email, activity);
                HelperClass.setPhone(number, activity);
                HelperClass.setuserInterst(interst, activity);
                HelperClass.setAbout(aboutt, activity);
                HelperClass.setAge(age, activity);
                HelperClass.setProfileImage(image, activity);
                HelperClass.setToken("1", activity);

                ProgressDialogFragment.hide(getSupportFragmentManager());

                GoOnline();


            }

            @Override

            public void onError(QBResponseException e) {
                progressDialog.dismiss();

                ShowSnackBar.longSnackBar(parentPanel, activity, e.getMessage());

                ProgressDialogFragment.hide(getSupportFragmentManager());

            }
        });
    }

}

