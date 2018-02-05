package com.example.cmardari.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cmardari on 2/5/2018.
 */

public class WaterReminderIntentService extends IntentService {
    private String TAG = WaterReminderIntentService.class.getSimpleName();

    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service created");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Doing in background");
        if (intent.getAction().equals(MainActivity.REMIND_ACTION)) {
            PreferenceUtils.incrementGlasses(this);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed");
        super.onDestroy();
    }
}
