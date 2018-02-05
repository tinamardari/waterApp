package com.example.cmardari.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String REMIND_ACTION = "remind_action";
    private ImageButton waterButton;
    private TextView waterGlassesTextview;
    private TextView waterRemindersTextview;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waterButton = findViewById(R.id.buttonWater);
        waterGlassesTextview = findViewById(R.id.countWater);
        waterRemindersTextview = findViewById(R.id.reminderText);

        updateWaterGlassesCounter();
        updateReminderCounter();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementGlasses();
            }
        });

        Button button = findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationUtils.remindNotification(MainActivity.this);
            }
        });
    }

    private void incrementGlasses() {
        Toast.makeText(this, "Glup Glup Glup", Toast.LENGTH_SHORT).show();
        //TODO IntentService
        Intent intent = new Intent(this, WaterReminderIntentService.class);
        intent.setAction(REMIND_ACTION);
        startService(intent);
    }

    private void updateReminderCounter() {
        int remindersCounter = PreferenceUtils.getWaterReminders(this);
        String formattedWaterReminders = getResources().getQuantityString(R.plurals.charging_remider, remindersCounter, remindersCounter);
        waterRemindersTextview.setText(formattedWaterReminders);
    }

    private void updateWaterGlassesCounter() {
        waterGlassesTextview.setText(String.valueOf(PreferenceUtils.getWaterGlasses(this)));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferenceUtils.WATER_COUNTER)) {
            updateWaterGlassesCounter();
        } else if (key.equals(PreferenceUtils.REMINDER_COUNTER)) {
            updateReminderCounter();
        }
    }

    @Override
    protected void onDestroy() {
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }
}
