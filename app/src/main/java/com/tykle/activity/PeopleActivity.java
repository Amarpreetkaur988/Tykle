package com.tykle.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.model.ListFriend;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.QBUsers;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.GPSTracker;
import com.tykle.Classess.HelperClass;
import com.tykle.Classess.LocationActivity;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.ModelClassess.FilterUserModel;
import com.tykle.ModelClassess.GetUsers;
import com.tykle.R;
import com.tykle.adapter.CardStackViewAdaptor;
import com.tykle.util.AppConstants;
import com.tykle.util.ShowSnackBar;
import com.tykle.util.chat.ChatHelper;
import com.tykle.util.qb.QbDialogHolder;
import com.tykle.util.qb.callback.QBPushSubscribeListenerImpl;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;
import com.yuyakaido.android.cardstackview.internal.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleActivity extends BaseActivity implements View.OnClickListener {
    private static final String DEBUG_TAG = "Data";
    private ImageView acceptBtn, iv_filter, rejectBtn;
    private RippleBackground rippleBackground;
    private float x1, y1, x2, y2;
    private TextView tv_findnearby;
    private ListFriend dataListFriend = null;
    private ArrayList<String> listFriendID = null;
    private Context mContext;
    private Activity activity;
    private ImageView btn_menu, profile_image;
    private TextView title;
    private DrawerLayout drawer;
    private LocationActivity locationActivity;
    private double lat, longi;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private String interst, frindImg;
    private String filterage, lookingfor, filterdis1, filterdis2, latittude, longitude;
    private TextView nav_profile, nav_matches, nav_logout, nav_message, payment, nav_contactus, nav_help, nav_friends;
    private CardStackViewAdaptor cardStackViewAdaptor;
    private List<GetUsers.Detail> users_data = new ArrayList<>();
    private FrameLayout frame_ripple;
    private String status = "like";
    private List<FilterUserModel.Detail> filtered_list = new ArrayList<>();


    private CardStackView card_stackview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());//compulsory to paste here
        setContentView(R.layout.activity_people);

        findId();
        getLocationMethod();
        setAddress();


        if (HelperClass.getTutorial(activity).equalsIgnoreCase("1")) {

        } else {
            forLikeButton();
        }


        mContext = getApplicationContext();

        //rippleBackgroud
        rippleBackground.startRippleAnimation();


        card_stackview.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {

            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {

                if (direction == SwipeDirection.Top) {
                    int position = card_stackview.getTopIndex() - 1;
                    if (status.equalsIgnoreCase("like")) {

                        PerformActionOnUser("1", position);

                    } else if (status.equalsIgnoreCase("tykle")) {

                        PerformActionOnUser("2", position);
                    }


                } else if (direction == SwipeDirection.Bottom) {
                    int position = card_stackview.getTopIndex() - 1;
                    PerformActionOnUser("0", position);
                }

                if (users_data.size() != 0) {

                    if (card_stackview.getTopIndex() - 1 == users_data.size() - 1) {

                        Getusers();
                    }
                } else {

                    if (card_stackview.getTopIndex() - 1 == filtered_list.size() - 1) {

                        Getusers();
                    }

                }


            }

            @Override
            public void onCardReversed() {

            }

            @Override
            public void onCardMovedToOrigin() {


            }

            @Override
            public void onCardClicked(int index) {

                if (users_data.size() != 0) {

                    Intent profile = new Intent(activity, ProfileActivity.class);
                    profile.putExtra("details", users_data.get(index));
                    profile.putExtra("type", "users");
                    startActivity(profile);

                } else {

                    Intent profile = new Intent(activity, ProfileActivity.class);
                    profile.putExtra("details", filtered_list.get(index));
                    profile.putExtra("type", "filters");
                    startActivity(profile);
                }


            }
        });


        nav_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HelperClass.logout(activity);
                userLogout();
                Intent login = new Intent(activity, LoginActivity.class);
                startActivity(login);
                finishAffinity();


            }
        });
    }


    private void facebooksignout() {
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    protected void userLogout() {
        ChatHelper.getInstance().destroy();
        logout();
        SharedPrefsHelper.getInstance().removeQbUser();

        QbDialogHolder.getInstance().clear();


    }

    private void logout() {
        if (QBPushManager.getInstance().isSubscribedToPushes()) {
            QBPushManager.getInstance().addListener(new QBPushSubscribeListenerImpl() {
                @Override
                public void onSubscriptionDeleted(boolean success) {
                    logoutREST();
                    QBPushManager.getInstance().removeListener(this);
                }
            });
            SubscribeService.unSubscribeFromPushes(activity);
        } else {
            logoutREST();
        }
    }

    private void logoutREST() {
        QBUsers.signOut().performAsync(null);
    }

    private void setAddress() {

        GPSTracker gpsTracker = new GPSTracker(activity);

        lat = gpsTracker.getLatitude();

        longi = gpsTracker.getLongitude();

        latittude = String.valueOf(lat);

        longitude = String.valueOf(longi);

        gettingAddress(latittude, longitude);
    }

    private void PerformActionOnUser(String action, final int position) {

        String friend_id = "";

        if (users_data.size() != 0) {

            friend_id = users_data.get(position).getId();
            frindImg = users_data.get(position).getImage1();

        } else {

            friend_id = filtered_list.get(position).getId();
            frindImg = filtered_list.get(position).getImage1();
        }


        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);
            apiService.UserAction(HelperClass.getID(activity), friend_id, action).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            status = "like";


                            String status = response.body().get("status").toString();
                            if (status.equalsIgnoreCase("3")) {


                                Intent match = new Intent(activity, MatchActivity.class);
                                match.putExtra("frindImg", frindImg);
                                startActivity(match);

                            } else {
                                Getusers();
                            }


                        } else {

                            Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                }
            });


        } else {

            ShowSnackBar.longSnackBar(drawer, activity, AppConstants.checknetwork);

        }

    }

    private void Getusers() {

        interst = HelperClass.getInterst(activity);

        if (HelperClass.isNetworkConnected(activity)) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.GetUsers(HelperClass.getID(activity), interst).enqueue(new Callback<GetUsers>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<GetUsers> call, Response<GetUsers> response) {

                    if (response.isSuccessful()) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {

                            filtered_list.clear();
                            users_data = response.body().getDetails();
                            frame_ripple.setVisibility(View.GONE);
                            card_stackview.setVisibility(View.VISIBLE);
                            cardStackViewAdaptor = new CardStackViewAdaptor(users_data, activity, new CardStackViewAdaptor.BtnClickListener() {
                                @Override
                                public void onBtnClick(int position) {


                                    float motionOriginX = 277.61444f;
                                    float motionOriginY = 901.29584f;
                                    float motionCurrentX = 343.191f;
                                    float motionCurrentY = 375.8803f;
                                    if (card_stackview.getTopIndex() - 1 == users_data.size() - 1) {

                                    } else {

                                        status = "tykle";
                                        SwipeDirection swipeDirection = SwipeDirection.Top;
                                        Point point = Util.getTargetPoint(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
                                        card_stackview.swipe(point, swipeDirection);
                                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);

                                    }


                                }
                            });
                            card_stackview.setAdapter(cardStackViewAdaptor);

                        } else {

                            tv_findnearby.setText("No users found");
                            card_stackview.setVisibility(View.GONE);
                            frame_ripple.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetUsers> call, Throwable t) {

                    tv_findnearby.setText("No users found");
                    card_stackview.setVisibility(View.GONE);
                    frame_ripple.setVisibility(View.VISIBLE);

                }
            });

        } else {


            tv_findnearby.setText("Connection error");
            card_stackview.setVisibility(View.GONE);
            frame_ripple.setVisibility(View.VISIBLE);

            ShowSnackBar.longSnackBar(drawer, activity, AppConstants.checknetwork);

        }


    }


    private void findId() {

        activity = PeopleActivity.this;

        listFriendID = new ArrayList<>();
        dataListFriend = new ListFriend();
        profile_image = findViewById(R.id.profile_image);
        frame_ripple = findViewById(R.id.frame_ripple);
        iv_filter = findViewById(R.id.iv_filter);
        iv_filter.setVisibility(View.VISIBLE);
        tv_findnearby = findViewById(R.id.tv_findnearby);
        drawer = findViewById(R.id.drawer_layout);
        btn_menu = findViewById(R.id.btn_menu);
        nav_matches = findViewById(R.id.nav_matches);
        nav_profile = findViewById(R.id.nav_profile);
        rippleBackground = findViewById(R.id.content);
        payment = findViewById(R.id.payment);
        nav_help = findViewById(R.id.nav_help);
        nav_contactus = findViewById(R.id.nav_contactus);
        nav_logout = findViewById(R.id.nav_logout);
        acceptBtn = findViewById(R.id.acceptBtn);
        rejectBtn = findViewById(R.id.rejectBtn);
        card_stackview = findViewById(R.id.card_stackview);
        nav_friends = findViewById(R.id.nav_friends);
        acceptBtn.setOnClickListener(this);
        rejectBtn.setOnClickListener(this);
        nav_matches.setOnClickListener(this);
        nav_friends.setOnClickListener(this);
        nav_contactus.setOnClickListener(this);
        nav_help.setOnClickListener(this);
        nav_logout.setOnClickListener(this);
        nav_profile.setOnClickListener(this);
        payment.setOnClickListener(this);
        btn_menu.setOnClickListener(this);

        iv_filter.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Getusers();

        setLatLog();
        if (!HelperClass.getProfileImage(activity).isEmpty())
            Picasso.with(activity).load(HelperClass.getProfileImage(activity))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(profile_image);


    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent Interst = new Intent(activity, InterestActivity.class);
            startActivity(Interst);
        }


    }


    public void gettingAddress(String latitude1, String longitude1) {
        Double latitude = Double.parseDouble(latitude1);
        Double longitude = Double.parseDouble(longitude1);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            HelperClass.setCurrentAddress(address, activity);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            HelperClass.setUserCity(city, activity);
            HelperClass.setUserState(state, activity);
            Log.e("DealsFragment", "address>>>" + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setLatLog() {
        locationActivity = new LocationActivity(activity, true);
        locationActivity.getLocation();
    }

    public void getLocationMethod() {
        locationActivity = new LocationActivity(activity, true);
        locationActivity.getLocation();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.nav_profile:
                Intent intent = new Intent(PeopleActivity.this, NavProfile.class);
                startActivity(intent);
                break;

            case R.id.btn_menu:
                drawer.openDrawer(Gravity.START);
                break;


            case R.id.nav_help:
                Intent help = new Intent(PeopleActivity.this, HelpActivity.class);
                startActivity(help);
                break;

            case R.id.nav_contactus:
                Intent contact = new Intent(PeopleActivity.this, ContactUS.class);
                startActivity(contact);
                break;

            case R.id.payment:
                Intent payment = new Intent(PeopleActivity.this, PaymentActivity.class);
                startActivity(payment);
                break;

            case R.id.acceptBtn:
                float motionOriginX = 277.61444f;
                float motionOriginY = 901.29584f;
                float motionCurrentX = 343.191f;
                float motionCurrentY = 375.8803f;


                if (users_data.size() != 0) {

                    if (card_stackview.getTopIndex() - 1 == users_data.size() - 1) {

                    } else {
                        SwipeDirection swipeDirection = SwipeDirection.Top;
                        Point point = Util.getTargetPoint(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
                        card_stackview.swipe(point, swipeDirection);
                        Animation animFadein;
                        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        final AnimationSet s = new AnimationSet(true);
                        s.setInterpolator(new AccelerateInterpolator());
                        s.addAnimation(animFadein);
                        acceptBtn.startAnimation(s);
                    }
                } else {

                    if (card_stackview.getTopIndex() - 1 == filtered_list.size() - 1) {

                    } else {
                        SwipeDirection swipeDirection = SwipeDirection.Top;
                        Point point = Util.getTargetPoint(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
                        card_stackview.swipe(point, swipeDirection);
                        Animation animFadein;
                        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        final AnimationSet s = new AnimationSet(true);
                        s.setInterpolator(new AccelerateInterpolator());
                        s.addAnimation(animFadein);
                        acceptBtn.startAnimation(s);
                    }
                }


                break;

            case R.id.rejectBtn:

                if (users_data.size() != 0) {

                    if (card_stackview.getTopIndex() - 1 == users_data.size() - 1) {

                    } else {
                        SwipeDirection swipeDirection1 = SwipeDirection.Bottom;
                        // Point point1 = Util.getTargetPoint(motionOriginX1, motionOriginY1, 399.44522f, 483.62216f);
                        Point point1 = new Point();
                        card_stackview.swipe(point1, swipeDirection1);
                        Animation animFadein1;
                        animFadein1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        final AnimationSet s1 = new AnimationSet(true);
                        s1.setInterpolator(new AccelerateInterpolator());
                        s1.addAnimation(animFadein1);
                        rejectBtn.startAnimation(s1);
                    }

                } else {

                    if (card_stackview.getTopIndex() - 1 == filtered_list.size() - 1) {

                    } else {
                        SwipeDirection swipeDirection1 = SwipeDirection.Bottom;
                        // Point point1 = Util.getTargetPoint(motionOriginX1, motionOriginY1, 399.44522f, 483.62216f);
                        Point point1 = new Point();
                        card_stackview.swipe(point1, swipeDirection1);
                        Animation animFadein1;
                        animFadein1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
                        final AnimationSet s1 = new AnimationSet(true);
                        s1.setInterpolator(new AccelerateInterpolator());
                        s1.addAnimation(animFadein1);
                        rejectBtn.startAnimation(s1);
                    }
                }


                break;

            case R.id.iv_filter:
                filterdialog();
                break;

            case R.id.nav_matches:
                Intent requests = new Intent(activity, MatchesRequestsActivity.class);
                startActivity(requests);
                break;

            case R.id.nav_friends:
                Intent friends = new Intent(activity, DialogsActivity.class);
                startActivity(friends);
                break;

            case R.id.card_stackview:

                break;

        }

    }


    void filterdialog() {
        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.filter_layout);

        final RadioButton above18,
                twntytothirty,
                thrtytofourty, fourtytofifty,
                olderandwiser,
                nearbyto50km, fifttykmto100km,
                hundredkmto150km, onefifitykmto200km, twohundredkmto250km, male, female, both;

        final Button apply;

        above18 = dialog.findViewById(R.id.filterbyabove18);

        twntytothirty = dialog.findViewById(R.id.filterbytwntytothirty);

        thrtytofourty = dialog.findViewById(R.id.filterbythirtytofourty);

        fourtytofifty = dialog.findViewById(R.id.filterbyfourtytofifty);

        olderandwiser = dialog.findViewById(R.id.filterbyolderandwiser);


        nearbyto50km = dialog.findViewById(R.id.filterbynearbytofifty);

        fifttykmto100km = dialog.findViewById(R.id.filterbyfiftykmtohundredkm);

        hundredkmto150km = dialog.findViewById(R.id.filterbyhundredtoonefiftykm);

        onefifitykmto200km = dialog.findViewById(R.id.filterbyonefiftykmtotwohundredkm);

        twohundredkmto250km = dialog.findViewById(R.id.filterbytwohundredkmtotwofiftykm);


        male = dialog.findViewById(R.id.filterbymale);

        female = dialog.findViewById(R.id.filterbyfemale);

        both = dialog.findViewById(R.id.filterbyboth);

        apply = dialog.findViewById(R.id.btn_apply_filters);


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GPSTracker gpsTracker = new GPSTracker(activity);

                lat = gpsTracker.getLatitude();

                longi = gpsTracker.getLongitude();

                latittude = String.valueOf(lat);

                longitude = String.valueOf(longi);

                if (above18.isChecked()) {

                    filterage = "Above 18";

                } else if (twntytothirty.isChecked()) {

                    filterage = "20-30";


                } else if (thrtytofourty.isChecked()) {

                    filterage = "30-40";

                } else if (fourtytofifty.isChecked()) {

                    filterage = "40-50";

                } else if (olderandwiser.isChecked()) {

                    filterage = "older and wiser";

                } else {

                    filterage = "";
                }


                if (nearbyto50km.isChecked()) {

                    filterdis1 = "50";
                    filterdis2 = "0";

                } else if (fifttykmto100km.isChecked()) {

                    filterdis1 = "100";
                    filterdis2 = "50";

                } else if (hundredkmto150km.isChecked()) {

                    filterdis1 = "150";
                    filterdis2 = "100";

                } else if (onefifitykmto200km.isChecked()) {

                    filterdis1 = "200";
                    filterdis2 = "150";

                } else if (twohundredkmto250km.isChecked()) {

                    filterdis1 = "250";
                    filterdis2 = "200";
                } else {

                    filterdis1 = "500";
                    filterdis2 = "0";
                }

                if (male.isChecked()) {

                    lookingfor = "Male";

                } else if (female.isChecked()) {

                    lookingfor = "Female";

                } else if (both.isChecked()) {

                    lookingfor = "";
                } else {
                    lookingfor = "";
                }


                ApiClient.getApiClient();

                ApiService apiService = ApiClient.retrofit.create(ApiService.class);

                apiService.getFilteredUserList(HelperClass.getID(activity), filterage,
                        lookingfor, latittude,
                        longitude, filterdis1, filterdis2).enqueue(new Callback<FilterUserModel>() {
                    @Override
                    public void onResponse(Call<FilterUserModel> call, Response<FilterUserModel> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {

                            if (response.body().getSuccess().equalsIgnoreCase("1")) {

                                frame_ripple.setVisibility(View.GONE);
                                card_stackview.setVisibility(View.VISIBLE);
                                filtered_list = response.body().getDetails();

                                card_stackview.removeAllViews();
                                users_data.clear();
                                cardStackViewAdaptor = new CardStackViewAdaptor(filtered_list, activity, "filter", new CardStackViewAdaptor.BtnClickListener() {
                                    @Override
                                    public void onBtnClick(int position) {

                                        float motionOriginX = 277.61444f;
                                        float motionOriginY = 901.29584f;
                                        float motionCurrentX = 343.191f;
                                        float motionCurrentY = 375.8803f;
                                        if (users_data.size() != 0) {

                                            if (card_stackview.getTopIndex() - 1 == users_data.size() - 1) {

                                            } else {

                                                status = "tykle";
                                                SwipeDirection swipeDirection = SwipeDirection.Top;
                                                Point point = Util.getTargetPoint(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
                                                card_stackview.swipe(point, swipeDirection);
                                                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);

                                            }

                                        } else {

                                            if (card_stackview.getTopIndex() - 1 == filtered_list.size() - 1) {

                                            } else {

                                                status = "tykle";
                                                SwipeDirection swipeDirection = SwipeDirection.Top;
                                                Point point = Util.getTargetPoint(motionOriginX, motionOriginY, motionCurrentX, motionCurrentY);
                                                card_stackview.swipe(point, swipeDirection);
                                                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);

                                            }

                                        }


                                    }
                                });
                                card_stackview.setAdapter(cardStackViewAdaptor);


                            } else {

                                tv_findnearby.setText("No users found");
                                card_stackview.setVisibility(View.GONE);
                                frame_ripple.setVisibility(View.VISIBLE);

                            }
                        } else {

                            tv_findnearby.setText("No users found");
                            card_stackview.setVisibility(View.GONE);
                            frame_ripple.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onFailure(Call<FilterUserModel> call, Throwable t) {

                        dialog.dismiss();

                        tv_findnearby.setText("No users found");
                        card_stackview.setVisibility(View.GONE);
                        frame_ripple.setVisibility(View.VISIBLE);

                    }
                });

            }
        });

        dialog.setTitle("Custom Dialog");

        Window dialogWindow = dialog.getWindow();

        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);

        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        dialog.show();

    }

    private void forLikeButton() {
        TapTargetView.showFor(this,                 // `tachis` is an Activity
                TapTarget.forView(findViewById(R.id.acceptBtn), "This is a Like Button", "Press it, When you like someone")
                        // All options below are optional
                        .outerCircleColor(android.R.color.white)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.colorPink)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPink)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getResources().getDrawable(R.drawable.like))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional

                        forRejectButton();
                    }
                });
    }

    private void forRejectButton() {
        TapTargetView.showFor(this,                 // `tachis` is an Activity
                TapTarget.forView(findViewById(R.id.rejectBtn), "This is a Reject button", "Press it, When you dislike someone.")
                        // All options below are optional
                        .outerCircleColor(android.R.color.white)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.colorPink)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPink)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getResources().getDrawable(R.drawable.like))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        // This call is optional
                        forfilterButton();

                    }
                });
    }

    private void forfilterButton() {
        TapTargetView.showFor(this,                 // `tachis` is an Activity
                TapTarget.forView(findViewById(R.id.iv_filter), "This is a Filter button", "You can filter the users.")
                        // All options below are optional
                        .outerCircleColor(android.R.color.white)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.colorPink)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.white)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.colorPink)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                        .icon(getResources().getDrawable(R.drawable.like))                     // Specify a custom drawable to draw as the target
                        .targetRadius(60),                  // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional

                        HelperClass.setTutoiral("1", activity);
                    }
                });
    }

}



