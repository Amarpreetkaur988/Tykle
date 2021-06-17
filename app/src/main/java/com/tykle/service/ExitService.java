package com.tykle.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.tykle.Classess.ApiClient;
import com.tykle.Classess.HelperClass;
import com.tykle.InterfaceClass.ApiService;
import com.tykle.activity.InterestActivity;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by omninossolutions on 03/11/18.
 */

public class ExitService extends Service {


    public ExitService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");

    //    GoOffline();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.e("ClearFromRecentService", "END");

   //     GoOffline();

        //Code here
        stopSelf();
    }

    private void GoOffline() {

        if (HelperClass.isNetworkConnected(getBaseContext())) {

            ApiClient.getApiClient();

            ApiService apiService = ApiClient.retrofit.create(ApiService.class);

            apiService.GoOnlineOffline("0", "0",
                    SharedPrefsHelper.getInstance().getQbUser().getId() + "").enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {

                    if (response.isSuccessful()) {
                        if (response.body().get("success").toString().equalsIgnoreCase("1")) {

                            Log.e("done", "done");

                            Intent main = new Intent(getBaseContext(), InterestActivity.class);
                            startActivity(main);

                        } else {

                            Log.e("error", "error");
                        }
                    } else {

                        Log.e("error", "error");
                    }

                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                    Log.e("error", "error");
                }
            });

        } else {


        }
    }
}