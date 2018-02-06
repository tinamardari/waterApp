package com.example.cmardari.myapplication.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    private SharedPreferences sharedPreferences;

    public static final String WATER_COUNTER = "water_counter";
    public static final String REMINDER_COUNTER = "reminder_counter";
    private static final int DEFAULT_NUMBER = 0;

    public PreferenceUtils(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private void setWaterGlasses(int glasses){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER_COUNTER, glasses);
        editor.apply();
    }

    public int getWaterGlasses(){
        return sharedPreferences.getInt(WATER_COUNTER, DEFAULT_NUMBER);
    }

    public void incrementGlasses(){
        setWaterGlasses(getWaterGlasses() + 1);
    }

    private void setWaterReminders( int reminders){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REMINDER_COUNTER, reminders);
        editor.apply();
    }

    public int getWaterReminders(){
        return sharedPreferences.getInt(REMINDER_COUNTER, DEFAULT_NUMBER);
    }

    public void incrementReminder(){
        setWaterReminders(getWaterReminders() + 1);
    }


    public void reset() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}
