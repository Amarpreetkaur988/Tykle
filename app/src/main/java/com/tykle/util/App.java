package com.tykle.util;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;
import com.tykle.ModelClassess.CommonInstances;
import com.tykle.ModelClassess.SampleConfigs;
import com.tykle.util.configs.ConfigUtils;

import java.io.IOException;
import java.util.Locale;

public class App extends CoreApp {

    private static CommonInstances commonInstances;
    private static App instance;
    private static SampleConfigs sampleConfigs;
    private static final String TAG = App.class.getSimpleName();


    public static App getContext() {
        return instance;
    }

    public static CommonInstances getCommonInstances() {
        return commonInstances;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifecycle.init(this);
        initSampleConfigs();

        commonInstances = new CommonInstances();


    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }




}
