package com.example.cmardari.myapplication.utilities;

import android.content.Context;

import com.example.cmardari.myapplication.services.ReminderJobService;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ReminderUtilities {

    private static final String REMINDER_JOB_TAG = "water_reminder_tag";
    private final int REMINDER_INTERVAL = 1;
    private final int REMINDER_INTERVAL_SECONDS = (int) TimeUnit.MINUTES.toSeconds(REMINDER_INTERVAL);
    private final int SYNC_FLEXTIME_SECONDS = REMINDER_INTERVAL_SECONDS; //number of SECONDS of leeway you want to give your execution window
    private boolean isInitialized;

    synchronized public void scheduleReminder(Context context) {
        if (isInitialized) return;

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintsJob = dispatcher.newJobBuilder()
                .setService(ReminderJobService.class)
                .setTag(REMINDER_JOB_TAG)
                .setRecurring(true)
               /* .setConstraints(Constraint.DEVICE_CHARGING)*/
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0, REMINDER_INTERVAL_SECONDS))
                .setReplaceCurrent(true) //if the job is ever remade we're going to replace the old job with the new one
                .build();

        dispatcher.mustSchedule(constraintsJob);


        isInitialized = true;
    }
}
