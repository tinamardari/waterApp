package com.example.cmardari.myapplication.services;

import com.example.cmardari.myapplication.utilities.NotificationUtils;
import com.example.cmardari.myapplication.utilities.PreferenceUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ReminderJobService extends JobService {

    private PreferenceUtils preferenceUtils;

    @Override
    public void onCreate() {
        preferenceUtils = new PreferenceUtils(this);
        super.onCreate();
    }

    @Override
    public boolean onStartJob(final JobParameters job) {
        NotificationUtils.remindNotification(ReminderJobService.this);
        preferenceUtils.incrementReminder();
        return false; // its still running
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true; //if conditions are met again it should restart
    }

    @Override
    public void onDestroy() {
        preferenceUtils = null;
        super.onDestroy();
    }
}
