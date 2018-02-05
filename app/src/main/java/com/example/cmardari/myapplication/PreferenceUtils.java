package com.example.cmardari.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceUtils {

    public static final String WATER_COUNTER = "water_counter";
    public static final String REMINDER_COUNTER = "reminder_counter";
    private static final int DEFAULT_NUMBER = 0;

    private static void setWaterGlasses(Context context, int glasses){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WATER_COUNTER, glasses);
        editor.apply();
    }

    public static int getWaterGlasses(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(WATER_COUNTER, DEFAULT_NUMBER);
    }

    public static void incrementGlasses(Context context){
        setWaterGlasses(context,getWaterGlasses(context) + 1);
    }

    private static void setWaterReminders(Context context, int reminders){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REMINDER_COUNTER, reminders);
        editor.apply();
    }

    public static int getWaterReminders(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(REMINDER_COUNTER, DEFAULT_NUMBER);
    }

    public static void incrementReminder(Context context){
        setWaterReminders(context,getWaterReminders(context) + 1);
    }


}
