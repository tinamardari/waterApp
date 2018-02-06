package com.example.cmardari.myapplication;

import android.content.Context;

public interface MainView {
    void updateGlasses(String counter);

    void updateReminders(String counter);

    Context getContext();

    void showChargingImageviewON();

    void showChargingImageviewOFF();
}
