package com.example.cmardari.myapplication.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cmardari.myapplication.MainPresenter;
import com.example.cmardari.myapplication.utilities.PreferenceUtils;

public class WaterReminderIntentService extends IntentService {
    private String TAG = WaterReminderIntentService.class.getSimpleName();
    private PreferenceUtils preferenceUtils;

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service created");
        preferenceUtils =  new PreferenceUtils(this);
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Doing in background");
        if (intent != null && intent.getAction().equals(MainPresenter.REMIND_ACTION)) {
            preferenceUtils.incrementGlasses();
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        preferenceUtils =  null;
        super.onDestroy();
    }
}
