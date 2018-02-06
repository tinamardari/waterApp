package com.example.cmardari.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.cmardari.myapplication.services.WaterReminderIntentService;
import com.example.cmardari.myapplication.utilities.PreferenceUtils;
import com.example.cmardari.myapplication.utilities.ReminderUtilities;

public class MainPresenter implements SharedPreferences.OnSharedPreferenceChangeListener {
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
        Toast.makeText(view.getContext(), "Glup Glup Glup", Toast.LENGTH_SHORT).show();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            BatteryManager batteryManager = (BatteryManager) view.getContext().getSystemService(Context.BATTERY_SERVICE);
            showCharging(batteryManager.isCharging());
        } else {
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent intent = view.getContext().registerReceiver(null, ifilter);
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;
            showCharging(isCharging);
        }
    }
}
