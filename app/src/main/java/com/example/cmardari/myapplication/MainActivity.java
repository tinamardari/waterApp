package com.example.cmardari.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainView {
    private MainPresenter presenter;

    private ImageButton waterButton;
    private TextView waterGlassesTextview;
    private TextView waterRemindersTextview;
    private ImageView chargingImageview;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        waterButton = findViewById(R.id.buttonWater);
        waterGlassesTextview = findViewById(R.id.countWater);
        waterRemindersTextview = findViewById(R.id.reminderText);
        chargingImageview = findViewById(R.id.charging_icon);

        presenter.onReady();

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.onWaterClick();
                presenter.animate(waterButton);
                presenter.animate(waterGlassesTextview);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "OnResume");
        presenter.updateChargingImageview();
        presenter.registerReceiver();
    }

    @Override
    public void updateGlasses(String glassesCounter) {
        waterGlassesTextview.setText(glassesCounter);
    }

    @Override
    public void updateReminders(String remindersCounter) {
        TextView waterRemindersTextview = this.waterRemindersTextview;
        waterRemindersTextview.setText(remindersCounter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showChargingImageviewON() {
        chargingImageview.setImageResource(R.drawable.ic_charging_blue);
    }

    @Override
    public void showChargingImageviewOFF() {
        chargingImageview.setImageResource(R.drawable.ic_charging_gray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    public void resetData(MenuItem item) {
        presenter.resetSharedPreferences();
    }

    public void openAnimation(MenuItem item) {
        Intent intent = new Intent(this, AnimationActivity.class);
        startActivity(intent);
    }

}
