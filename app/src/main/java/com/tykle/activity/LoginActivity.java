package com.tykle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.Classess.LocationActivity;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.CheckUserInfo;
import com.tykle.ModelClassess.SocialApi;
import com.tykle.R;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;
import com.tykle.util.chat.ChatHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements OnClickListener {

    int error = 0;
    ImageView iv_facebook_login;
    String facebook_id = "";
    String first_name = "", last_name = "", name = "", email_fb = "", gender = "", birthday = "", picture = "";
    CallbackManager callbackManager;
    LocationActivity locationActivity;
    QBUser qbUser;
    private Button btn_login;
    private TextView tv_create, tv_forgot_password;
    private Activity activity;
    private EditText et_email, et_password;
    private String token;
    private String socialUsersername;
    private String pictureurl;
    private int year, month, day;
    private String facebook_age = "";
    private String Latitude = null;
    private String Logitude = null;
    private URL profilePicture;
    private AccessToken accessToken;
    private NestedScrollView parentPanel;
    private boolean firstTimeAccess;
    private String deviceToken;
    private String qbid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//compulsory to paste here
        setContentView(R.layout.activity_login);
        activity = LoginActivity.this;
        Latitude = HelperClass.getLatitude(activity);
        Logitude = HelperClass.getLongitude(activity);
        initControls();

        firstTimeAccess = true;

        deviceToken = FirebaseInstanceId.getInstance().getToken();


        //clicks
        btn_login.setOnClickListener(this);

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.tykle",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }


    private void initControls() {
        callbackManager = CallbackManager.Factory.create();// compulsory
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        btn_login = findViewById(R.id.btn_login);
        parentPanel = findViewById(R.id.parentPanel);
        tv_create = findViewById(R.id.tv_create);

        tv_create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));

            }
        });


        //Forget Password
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
//                startActivity(intent);
            }
        });


        //facebook button
        iv_facebook_login = findViewById(R.id.iv_facebook_login);
        iv_facebook_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LoginFragment", "facebook Integration button clicked");

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("LoginActivity", "onSuccess method called");
                        getFacebookData(loginResult);
                        accessToken = loginResult.getAccessToken();
                        Log.e("Loginactivity", "acccessToken=>" + accessToken);
                    }

                    @Override

                    public void onCancel() {
                        Log.e("LoginActivity", "onCancel() method called");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.e("LoginActivity", "onError method call" + error);
                    }
                });
                LoginManager.getInstance().logInWithReadPermissions(activity,
                        Arrays.asList("public_profile", "email"));
            }
        });
    }

    private void getFacebookData(LoginResult loginResult) {
        //accessToken = loginResult.getAccessToken();
        // Log.e( "LoginActivity", "LoginResult accesstoken=>" + accessToken );
        Log.e("LoginActivity", "getFacebookData() called >>");
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    facebook_id = object.getString("id");

                    //check permisson first name
                    if (object.has("first_name")) {
                        first_name = object.getString("first_name");
                    }

                    //check permisson last name
                    if (object.has("last_name")) {
                        last_name = object.getString("last_name");
                        Log.e("LoginActivity", "last_name" + last_name);
                    }
                    //check permisson email
                    if (object.has("email")) {
                        email_fb = object.getString("email");
                    }

                    //check permisson gender
                    if (object.has("gender")) {
                        gender = object.getString("gender");
                    }
                    //check permisson birthday
                    if (object.has("birthday")) {
                        birthday = object.getString("birthday");
                        day = Integer.parseInt(birthday.substring(0, 2));
                        month = Integer.parseInt(birthday.substring(3, 5));
                        year = Integer.parseInt(birthday.substring(6));
                        int intage = getAge(year, month, day);
                        facebook_age = String.valueOf(intage);
                        Log.e("LoginActivity", "birthday->" + birthday);
                        Log.e("LoginActivit¬y", "facebook_age->" + facebook_age);
                        Log.e("LoginActivity", "intage->" + intage);
                    }

                    socialUsersername = first_name + " " + last_name;
                    Log.e("LoginActivity", "last_name" + socialUsersername);

                    JSONObject jsonObject = new JSONObject(object.getString("picture"));
                    Log.e("Loginactivity", "json oject get picture" + jsonObject);
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    Log.e("Loginactivity", "json oject get picture" + dataObject);
                    profilePicture = new URL("https://graph.facebook.com/" + facebook_id + "/picture?width=500&height=500");
                    Log.e("LoginActivity", "json object=>" + object.toString());


                    if (email_fb.equalsIgnoreCase("")) {
                        ShowSnackBar.longSnackBar(parentPanel, activity, "Email not found in your facebook account");
                        facebooksignout();

                    } else {
                        hitSocailApi();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                Log.e("LoginActivity", "agebefore" + facebook_age);
            }
        });

        Bundle bundle = new Bundle();
        Log.e("LoginActivity", "bundle set");
        bundle.putString("fields", "id, first_name, last_name,email,picture,gender,birthday,location");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebooksignout() {
        LoginManager.getInstance().logOut();
    }

    private void hitSocailApi() {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.SocialApi(socialUsersername, email_fb, "Android", facebook_id, "Facebook",
                    HelperClass.getLatitude(activity), HelperClass.getLongitude(activity), deviceToken, profilePicture.toString()).enqueue(new Callback<SocialApi>() {
                @Override
                public void onResponse(Call<SocialApi> call, Response<SocialApi> response) {

                    HelperClass.hideProgressDialog();

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            String qbid = response.body().getDetails().getQuickboxId() + "";

                            int index = email_fb.lastIndexOf("@");

                            String login = email_fb.substring(0, index);

                            signIn(login, "Shubham1");

                        } else if (response.body().getSuccess().equalsIgnoreCase("2")) {

                            ShowSnackBar.longSnackBar(parentPanel, activity, response.body().getMessage());

                        } else {

                            ShowSnackBar.longSnackBar(parentPanel, activity, response.body().getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(Call<SocialApi> call, Throwable t) {
                    HelperClass.hideProgressDialog();

                    ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.Servererror);

                }
            });

        } else {

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.checknetwork);
        }


    }

    public int getAge(int year, int month, int day) {
        final Calendar birthDay = Calendar.getInstance();
        birthDay.set(year, month, day);
        final Calendar current = Calendar.getInstance();
        if (current.getTimeInMillis() < birthDay.getTimeInMillis())
            throw new IllegalArgumentException("age < 0");
        int age = current.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (birthDay.get(Calendar.MONTH) > current.get(Calendar.MONTH) ||
                (birthDay.get(Calendar.MONTH) == current.get(Calendar.MONTH) &&
                        birthDay.get(Calendar.DATE) > current.get(Calendar.DATE)))
            age--;
        return age;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationMethod();
    }

    public void getLocationMethod() {
        locationActivity = new LocationActivity(activity, true);
        locationActivity.getLocation();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                Validate();
        }

    }

    private void Validate() {

        String semail = et_email.getText().toString();

        String sPassword = et_password.getText().toString();

        if (TextUtils.isEmpty(semail)) {

            et_email.setError(AppConstants.mandatoryField);
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);

        } else if (!isValidEmail(semail)) {

            et_email.setError(AppConstants.validemail);

            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.validemail);

        } else if (TextUtils.isEmpty(sPassword)) {

            et_password.setError(AppConstants.mandatoryField);
            ShowSnackBar.longSnackBar(parentPanel, activity, AppConstants.mandatoryField);

        } else {

            int index = semail.lastIndexOf("@");

            String login = semail.substring(0, index);

            signIn(login, sPassword);
        }

    }

    private boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void CheckUserInfoApi(String qbid) {

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            HelperClass.showProgressDialog(activity);

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.checkUserInfo(qbid, deviceToken).enqueue(new Callback<CheckUserInfo>() {

                @Override
                public void onResponse(Call<CheckUserInfo> call, Response<CheckUserInfo> response) {

                    HelperClass.hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            String userName = response.body().getDetails().getUserName();
                            String email = response.body().getDetails().getEmail();
                            String number = response.body().getDetails().getPhone();
                            String interst = response.body().getDetails().getInterested();
                            String age = response.body().getDetails().getAge();
                            String image = response.body().getDetails().getImage1();
                            String qbid = response.body().getDetails().getQuickboxId();
                            String about = response.body().getDetails().getAbout();


                            HelperClass.setQuickBloxid(qbid, activity);
                            HelperClass.setID(response.body().getDetails().getId(), activity);
                            HelperClass.setAbout(about, activity);
                            HelperClass.setUsername(userName, activity);
                            HelperClass.setEmail(email, activity);
                            HelperClass.setPhone(number, activity);
                            HelperClass.setuserInterst(interst, activity);
                            HelperClass.setAge(age, activity);
                            HelperClass.setProfileImage(image, activity);

                            SharedPrefsHelper.getInstance().saveQbUser(qbUser);

                            HelperClass.setToken("1", activity);

                            GoOnline();


                        } else {
                            Intent profile = new Intent(activity, ProfileQuestionActivity.class);
                            profile.putExtra("user_id", response.body().getDetails().getId());
                            startActivity(profile);

                            LoginActivity.this.finish();
                            Toast.makeText(activity, "Please provide your details", Toast.LENGTH_SHORT).show();
                        }
                    }


                }

                @Override
                public void onFailure(Call<CheckUserInfo> call, Throwable t) {

                    HelperClass.hideProgressDialog();

                }
            });

        } else {


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

                            startActivity(new Intent(LoginActivity.this, InterestActivity.class));
                            LoginActivity.this.finish();
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


    public void signIn(String email, String password) {

        progressDialog.show();


        qbUser = new QBUser(email, password);
        ChatHelper.getInstance().login(qbUser, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                progressDialog.dismiss();

                qbid = qbUser.getId().toString();

                CheckUserInfoApi(qbid);

                ProgressDialogFragment.hide(getSupportFragmentManager());


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
