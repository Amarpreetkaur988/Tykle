package com.tykle.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.StoringMechanism;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.activity.CoreSplashActivity;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.GPSTracker;
import com.tykle.Classess.HelperClass;
import com.tykle.Classess.LocationActivity;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.R;
import com.tykle.service.ExitService;
import com.tykle.util.chat.ChatHelper;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends CoreSplashActivity {
    final static int REQUEST_LOCATION = 199;
    final static int REQUEST_GPS = 101;
    static final String APP_ID = "74478";
    static final String AUTH_KEY = "PnwR-tk9jhE7maM";
    static final String AUTH_SECRET = "gbgxRbrR7NnGOxj";
    static final String ACCOUNT_KEY = "Wyw2F2nC-AVc7ByRx-xm";
    public static int Splash_time_out = 0;
    LocationActivity locationActivity;
    LocationRequest locationRequest;
    LocationSettingsRequest.Builder builder;
    Boolean sentToSettings = false;
    private SpinKitView progress;
    private ImageView imageView;
    private Activity activity;
    private String token;
    private GoogleApiClient googleApiClient;
    private boolean firstTimeAccess;
    private SharedPrefsHelper sharedPrefsHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = SplashActivity.this;
        sharedPrefsHelper = SharedPrefsHelper.getInstance();

        startService(new Intent(getApplicationContext(), ExitService.class));

        if (checkConfigsWithSnackebarError()) {
            proceedToTheNextActivityWithDelay();
        }


        QBSettings.getInstance().setStoringMehanism(StoringMechanism.UNSECURED); //call before init method for QBSettings
        QBSettings.getInstance().init(getApplicationContext(), APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        final LocationManager manager = (LocationManager) SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SplashActivity.this)) {
//            Toast.makeText(SplashActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
            // getpermissions();
        }
        // Todo Location Already on  ... end

        if (!hasGPSDevice(SplashActivity.this)) {
            Toast.makeText(SplashActivity.this, "Gps not Supported", Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SplashActivity.this)) {
            Log.e("TAG", "Gps already enabled");
            Toast.makeText(SplashActivity.this, "Gps not enabled", Toast.LENGTH_SHORT).show();
            enableLoc();
        } else {
            Log.e("TAG", "Gps already enabled");
            // Toast.makeText(SplashActivity.this,"Gps already enabled",Toast.LENGTH_SHORT).show();
//            CheckPermissions();
        }
    }

    @Override
    protected String getAppName() {
        return null;
    }

    @Override
    protected void proceedToTheNextActivity() {

        if (checkSignIn()) {
            restoreChatSession();
        } else {
            getpermissions();
        }

    }

    @Override
    protected boolean checkSignIn() {
        return SharedPrefsHelper.getInstance().hasQbUser();
    }

    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged() && sharedPrefsHelper.hasQbUser()) {

            getpermissions();
        } else {
            QBUser currentUser = getUserFromSession();
            if (currentUser == null) {
                Intent login = new Intent(activity, LoginActivity.class);
                startActivity(login);
                finish();

            } else {

                loginToChat(currentUser);
            }
        }
    }

    private QBUser getUserFromSession() {

        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        QBSessionManager qbSessionManager = QBSessionManager.getInstance();
        if (qbSessionManager.getSessionParameters() == null) {
            ChatHelper.getInstance().destroy();
            return null;
        }
        Integer userId = qbSessionManager.getSessionParameters().getUserId();
        user.setId(userId);
        return user;
    }

    private void loginToChat(final QBUser user) {

        ProgressDialogFragment.show(getSupportFragmentManager(), R.string.dlg_restoring_chat_session);

        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {

                ProgressDialogFragment.hide(getSupportFragmentManager());


                initControls();
            }

            @Override
            public void onError(QBResponseException e) {
                ProgressDialogFragment.hide(getSupportFragmentManager());
                sharedPrefsHelper.clearAllData();
                HelperClass.logout(activity);
                initControls();

//                showSnackbarError(findViewById(R.id.layout_root), R.string.error_recreate_session, e,
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                loginToChat(user);
//                            }
//                        });
            }
        });
    }


    private void initControls() {

        imageView = findViewById(R.id.image);
        progress = findViewById(R.id.progress);
        locationActivity = new LocationActivity(activity, true);
        locationActivity.getLocation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                GPSTracker gpsTracker = new GPSTracker(activity);
                HelperClass.setLatitude(String.valueOf(gpsTracker.getLatitude()), activity);
                HelperClass.setLongitude(String.valueOf(gpsTracker.getLongitude()), activity);

                if (HelperClass.getToken(activity).equalsIgnoreCase("1")) {

                    Intent main = new Intent(activity, InterestActivity.class);
                    startActivity(main);
                    finish();


                } else {
                    Intent login = new Intent(activity, LoginActivity.class);
                    startActivity(login);
                    finish();
                }


                firstTimeAccess = true;


            }
        }, Splash_time_out);
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


    //New Gps code enabling
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SplashActivity.this, REQUEST_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    void getpermissions() {
        try {
            if (ActivityCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.CAMERA + Manifest.permission.ACCESS_FINE_LOCATION + Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            } else {
                initControls();
//
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean gallery = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean locationfine = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean locationcourse = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && camera && gallery && locationfine && locationcourse) {

                    initControls();
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle("Permissions");
                    builder.setMessage("Storage Permissions are Required");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //send to settings
                            Toast.makeText(SplashActivity.this, "Go to Settings to Grant the Storage Permissions and restart application", Toast.LENGTH_LONG).show();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", SplashActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                            .create()
                            .show();
                } else {
                    Toast.makeText(SplashActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
                break;
        }


    }


}
