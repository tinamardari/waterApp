package com.example.cmardari.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.cmardari.myapplication.services.WaterReminderIntentService;
import com.example.cmardari.myapplication.utilities.DisplayUtilities;
import com.example.cmardari.myapplication.utilities.PreferenceUtils;
import com.example.cmardari.myapplication.utilities.ReminderUtilities;

public class MainPresenter implements SharedPreferences.OnSharedPreferenceChangeListener {

    private String LOG_TAG = MainPresenter.class.getSimpleName();
    private MainView view;
    private SharedPreferences preferences;
    private ReminderUtilities reminderUtilities;
    private PreferenceUtils preferenceUtils;
    private IntentFilter intentFilter;

    public static final String REMIND_ACTION = "remind_action";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isCharging = action.equals(Intent.ACTION_POWER_CONNECTED);
            showCharging(isCharging);
        }
    };

    private void showCharging(boolean isCharging) {
        if (isCharging) {
            view.showChargingImageviewON();
        } else {
            view.showChargingImageviewOFF();
        }
    }

    MainPresenter(MainView view) {
        this.view = view;
        reminderUtilities = new ReminderUtilities();


        preferenceUtils = new PreferenceUtils(view.getContext());
    }

    void onReady() {
        updateWaterGlassesCounter();
        updateReminderCounter();

        preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        reminderUtilities.scheduleReminder(view.getContext());

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
    }

    public void registerReceiver() {
        view.getContext().registerReceiver(receiver, intentFilter);
    }

    public void unregisterReceiver() {
        view.getContext().unregisterReceiver(receiver);
    }

    private void updateReminderCounter() {
        int remindersCounter = preferenceUtils.getWaterReminders();
        String formattedWaterReminders = view.getContext().getResources().getQuantityString(R.plurals.charging_remider, remindersCounter, remindersCounter);
        view.updateReminders(formattedWaterReminders);
    }

    private void updateWaterGlassesCounter() {
        view.updateGlasses(String.valueOf(preferenceUtils.getWaterGlasses()));
    }

    void incrementGlasses() {
        //Toast.makeText(view.getContext(), "Glup Glup Glup", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(view.getContext(), WaterReminderIntentService.class);
        intent.setAction(REMIND_ACTION);
        view.getContext().startService(intent);
    }

    void onDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        receiver = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceUtils.WATER_COUNTER)) {
            updateWaterGlassesCounter();
        } else if (key.equals(PreferenceUtils.REMINDER_COUNTER)) {
            updateReminderCounter();
        }
    }

    public void resetSharedPreferences() {
        preferenceUtils.reset();
        updateReminderCounter();
        updateWaterGlassesCounter();
    }

    public void updateChargingImageview() {
        Log.d(LOG_TAG, "updateChargingimageview");
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BatteryManager batteryManager = (BatteryManager) view.getContext().getSystemService(Context.BATTERY_SERVICE);
            Log.d(LOG_TAG, "Plugged " + batteryManager.isCharging());
            showCharging(batteryManager.getIntProperty(BatteryManager.));
        } else */
        {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = view.getContext().registerReceiver(null, ifilter);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            boolean isCharging = plugged == BatteryManager.BATTERY_PLUGGED_AC ||
                    plugged == BatteryManager.BATTERY_PLUGGED_USB ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                            plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS);

            showCharging(isCharging);
        }
    }

    public void onWaterClick() {
        if (view.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            showToast(Gravity.LEFT | Gravity.BOTTOM,
                    (int) DisplayUtilities.getPixels(view.getContext(), 100),
                    (int) DisplayUtilities.getPixels(view.getContext(), 15));
        } else {
            showToast(Gravity.BOTTOM,
                    (int) DisplayUtilities.getPixels(view.getContext(), 0),
                    (int) DisplayUtilities.getPixels(view.getContext(), 110));
        }
        incrementGlasses();
    }

    private void showToast(int gravity, int xOffset, int yOffset) {
        CustomToast toast = new CustomToast(view.getContext());
        toast.makeToast(R.mipmap.ic_thumb, R.string.good_job, gravity, xOffset, yOffset);
    }

    public void animate(View view) {
        ObjectAnimator animationL = ObjectAnimator.ofFloat(view, "rotation", 0f, 30.0f);
        animationL.setDuration(250);

        ObjectAnimator animationR = ObjectAnimator.ofFloat(view, "rotation", 30.0f, -30.0f);
        animationR.setDuration(250);

        ObjectAnimator animationL2 = ObjectAnimator.ofFloat(view, "rotation", -30.0f, 0.0f);
        animationL.setDuration(200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animationL).before(animationR);
        animatorSet.play(animationR).before(animationL2);
        animatorSet.start();
    }

}

