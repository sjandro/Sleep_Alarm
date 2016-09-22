package com.asalazar.alex.sleepalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by sjand on 4/18/2016.
 */
public class Alarm {

    private static final String TAG = "Alarm";
    Utilities utilities;

    public Alarm(){
    }

    public void SetAlarm(Context context, int hour, int minute, int second)
    {
        utilities = new Utilities();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,second);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmNotification.class);
        intent.putExtra("start_time", Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 215, intent, 0);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pi);
        System.out.println("Alarm Class(SetAlarm): 24 hour alarm set.");
    }

    public void RestoreBrightnessAlarm(Context context, int hour, int minute, int second)
    {
        int nextMinute = minute + 1;
        utilities = new Utilities();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, nextMinute);
        calendar.set(Calendar.SECOND,second);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmNotification.class);
        intent.putExtra("end_time", Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 315, intent, 0);
        am.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pi);
        System.out.println("Alarm Class(RestoreBrightnessAlarm): Restore brightness alarm set.");
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmNotification.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 512, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        sender.cancel();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmNotification.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 512, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 19)
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        //utilities.dimDisplay(context);
    }

    public void setOnetimeTimerWithSeconds(Context context, int hour, int minute, int second){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmNotification.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 512, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= 19)
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        //utilities.dimDisplay(context);
    }

    public void setOnetimeSnooze(Context context, int hour, int minute){
        minute = minute + 5;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Log.i(TAG, "SnoozedFunction: " + minute);
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmNotification.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 512, intent, 0);
        if (Build.VERSION.SDK_INT >= 19)
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
    }
}
