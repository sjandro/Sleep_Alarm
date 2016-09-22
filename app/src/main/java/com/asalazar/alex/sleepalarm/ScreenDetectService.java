package com.asalazar.alex.sleepalarm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;


public class ScreenDetectService extends Service {

    private static final String TAG = "ScreenDetectService";

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter("com.alex.activitylogs.BroadcastReceiver");
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        BroadcastReceiver mReceiver = new Screen();
        registerReceiver(mReceiver, filter);

        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            boolean startAlarm = intent.getBooleanExtra("startAlarmOnBoot", false);
            Utilities utilities = new Utilities();
            Alarm alarm = new Alarm();
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            String[] savedTimeParts = utilities.getSavedAlarm("start").split(":");
            String[] savedTimePartsEnd = utilities.getSavedAlarm("end").split(":");
            int hourS = Integer.valueOf(savedTimeParts[0]);
            int minuteS = Integer.valueOf(savedTimeParts[1]);
            int hourE = Integer.valueOf(savedTimePartsEnd[0]);
            int minuteE = Integer.valueOf(savedTimePartsEnd[1]);

            if (startAlarm) {
                alarm.SetAlarm(getApplicationContext(), hourS, minuteS, 0);
                if (utilities.getPower().equals("TRUE")) {
                    try {
                        ArrayList<Integer> hours = utilities.timeRange(hourS, hourE, minuteS, minuteE);

                        if (hour == hourS && hour == hourE && minute < minuteS && minute > minuteE) {
                            Log.i(TAG, "onStartCommand: hour == hourS && hour == hourE && (minute < minuteS && minute > minuteE)");
                        } else if(hour == hourS && hour == hourE && minuteE < minuteS){
                            alarm.setOnetimeTimer(getApplicationContext(), hour, minute);
                        } else if (hour <= hourS && minute < minuteS) {
                            Log.i(TAG, "onStartCommand: hour <= hourS and minute < minuteS");
                            alarm.setOnetimeTimerWithSeconds(getApplicationContext(), hourS, minuteS, 0);
                        } else if (hour == hourE && minute > minuteE) {
                            Log.i(TAG, "onStartCommand: hour == hourE and minute > minuteE");
                        } else if (hour == hourS && minute >= minuteS) {
                            alarm.setOnetimeTimer(getApplicationContext(), hour, minute);
                            Log.i(TAG, "onStartCommand: hour == hourS and minute >= minuteS");

                        } else if (hour == hourE && minute <= minuteE) {
                            alarm.setOnetimeTimer(getApplicationContext(), hour, minute);
                            Log.i(TAG, "onStartCommand: hour == hourE and minute <= minuteE");
                        } else if (hours.contains(hour)) {
                            alarm.setOnetimeTimer(getApplicationContext(), hour, minute);
                            Log.i(TAG, "onStartCommand: hours.contains(hour)");
                        }
                    } catch (Exception ex) {
                        Log.i(TAG, "onStartCommand ex: " + ex);
                    }
                }
            } else {
                Log.i(TAG, "onStartCommand startAlarm: " + startAlarm);
            }
        }
        catch (Exception ex){}

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "In onDestroy");
    }

}
