package com.asalazar.alex.sleepalarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sjand on 1/14/2016.
 */
public class Screen extends BroadcastReceiver {

    private static final String TAG = "screenBroadcast";
    public static boolean screenOff;
    public long startTime = 0;
    public int snoozeStartTime = 0 , fiveMinPassed = 0;
    public Utilities utilities;
    SharedPreferences sharedpref;
    public static final String mypreference = "cancelAlarm";
    SharedPreferences sharedprefNextDay;
    public static final String mypreferenceNextDay = "nextDay";
    private Context context;
    private Alarm alarm = new Alarm();
    private SharedPreferences prefs;
    String hS,mS,hE,mE;

    @Override
    public void onReceive(Context context, Intent intent) {
        utilities = new Utilities();
        sharedpref = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        prefs = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        this.context = context;

        try {
            String[] start = utilities.getSavedAlarm("start").split(":");
            String[] end = utilities.getSavedAlarm("end").split(":");
            hS = start[0]; mS = start[1]; hE = end[0]; mE = end[1];
        } catch (Exception ex) {Log.i(TAG, "onReceive: " + ex);}


        String action = intent.getAction();


        if(Constants.ACTION.DISMISS_ACTION.equals(action)) {
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(33);
            alarm.CancelAlarm(context);
            utilities.toastMsg("Alarm dismissed", context);
            utilities.writeDismiss("TRUE");
            utilities.restoreDisplayBrightness(context);
            Log.i(TAG,"Pressed Dismiss");
        } else if(Constants.ACTION.SNOOZE_ACTION.equals(action)) {
            int hourCur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int hour = hourCur;
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            int minuteCur = minute;
            try {
                int hourS = Integer.valueOf(hS);
                int minuteS = Integer.valueOf(mS);
                int hourE = Integer.valueOf(hE);
                int minuteE = Integer.valueOf(mE);
                ArrayList<Integer> hours = utilities.timeRange(hourS, hourE, minuteS, minuteE);

                minute = minute + prefs.getInt("snoozeTime", 5);

                int endTime = hourE * 100 + minuteE;
                if(minute > 60){
                    minute = minute - 60;
                    hour++;
                }
                int snoozeTime = hour * 100 + minute;

                if(hourS > hourE || (hourS == hourE && minuteS > minuteE)){
                    snoozeTime = endTime;
                }
                System.out.println("snoozeTime > endTime: " + snoozeTime + " > " + endTime);

                boolean isBetween = utilities.checkIfBetween(hourS, minuteS, hourE, minuteE);
                System.out.println(isBetween);
                if(isBetween && !(snoozeTime > endTime)){
                    snooze(hourCur, minute);
                }
                else{
                    utilities.toastMsg("Time is out of range", context);
                }
//                if (hourCur == hourS && hourCur == hourE && minuteCur < minuteS && minuteCur > minuteE) {
//                    //snooze(hourCur, minute);
//                    utilities.toastMsg("Time is out of range", context);
//                    System.out.println("hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE");
//                } else if (hourCur == hourS && hourCur == hourE && minuteE < minuteS) {
//                    snooze(hour, minute);
//                    System.out.println("hourCur == hS && hourCur == hE && mE < mS");
//                } else if (hourCur <= hourS && minuteCur < minuteS && !hours.contains(hourCur)) {
//                    utilities.toastMsg("Time is out of range", context);
//                    System.out.println("hourCur == hS &&  minuteCur < mS");
//                } else if (hourCur == hourE && minuteCur > minuteE) {
//                    utilities.toastMsg("Time is out of range", context);
//                    System.out.println("hourCur == hE && minuteCur > mE");
//                } else if (hourCur == hourS && minuteCur >= minuteS && !(snoozeTime > currentTime)) {
//                    snooze(hour, minute);
//                    System.out.println("hourCur == hourS && minuteCur >= minuteS");
//                } else if (hourCur == hourE && minuteCur <= minuteE && !(snoozeTime > currentTime)) {
//                    snooze(hour, minute);
//                    System.out.println("hourCur == hourE && minuteCur <= minuteE");
//                } else if(snoozeTime > currentTime){
//                    utilities.toastMsg("Time is out of range", context);
//                    System.out.println("snoozeTime > currentTime: " + snoozeTime + " > " + currentTime);
//                } else if (hours.contains(hourCur)) {
//                    snooze(hour, minute);
//                    System.out.println("hours.contains(hourCur)");
//                } else {
//                    System.out.println("snoozeBttn: no condition satisfy");
//                    utilities.toastMsg("Time is out of range", context);
//                }
            }
            catch (Exception ex){
                System.out.println("notification snooze button click: " + ex);
            }
            Log.i(TAG, "Pressed Snooze");
        }
        else {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                screenOff = true;
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenOff = false;
            }

            logScreen();
        }
    }

    public void snooze(int hour, int minute){
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.cancel(33);
        utilities.writeIsSnoozed("TRUE");
        utilities.toastMsg("Alarm snoozed for " + prefs.getInt("snoozeTime", 5) + " minutes", context);
        alarm.setOnetimeTimerWithSeconds(context, hour, minute, 0);
        utilities.restoreDisplayBrightness(context);
    }

    public void logScreen(){
        Date c = Calendar.getInstance().getTime();
        Long mil = Calendar.getInstance().getTimeInMillis();
        //checkIfNextDay(c.toString());
        if (!screenOff) {
            Log.i(TAG, "SCREEN ON "+c+","+mil);

            startTime = System.nanoTime();
            triggerAlarm();

        } else {
            Log.i(TAG, "SCREEN OFF " + c + "," + mil);

            if(sharedpref.getString("isOutOfRange", "").equals("yes")) {
                alarm.CancelAlarm(context);
                Log.i(TAG, "OutOfRage: yes, Alarm cancled...");
            }

            if(utilities.getIsSnoozed().equals("TRUE")){
                utilities.writeIsSnoozed("FALSE");
                alarm.CancelAlarm(context);
            }
            utilities.writeDismiss("FALSE");
        }
    }

    private void checkIfNextDay(String s) {
        sharedprefNextDay = context.getSharedPreferences(mypreferenceNextDay, Context.MODE_PRIVATE);
        String savedDay = sharedprefNextDay.getString("next_day", "");
        SharedPreferences.Editor editor = sharedprefNextDay.edit();

        String parts[] = s.split(" ");

        Log.i(TAG, "checkIfNextDay: " + parts[2] + " savedDay: " + savedDay);

        if(!parts[2].equals(savedDay)){
            Log.i(TAG,"In if statement (checkIfNextDay)");
            utilities.writePower("TRUE");
            editor.putString("next_day", parts[2]).apply();
            alarm.setOnetimeTimerWithSeconds(context, Integer.valueOf(hS), Integer.valueOf(mS), 0);
        }
    }

    public void triggerAlarm(){
        System.out.println("In broadcastor...");
        SharedPreferences.Editor editor = sharedpref.edit();

        try{
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                int hourS = Integer.valueOf(hS);
                int minuteS = Integer.valueOf(mS);
                int hourE = Integer.valueOf(hE);
                int minuteE = Integer.valueOf(mE);
                ArrayList<Integer> hours = utilities.timeRange(hourS, hourE, minuteS, minuteE);

            if(utilities.getPower().equals("TRUE")) {
                if (utilities.getIsSnoozed().equals("FALSE")) {
                    snoozeStartTime = minute;
                    if(hour == hourS && hour == hourE && minuteS > minute && minute > minuteE){
                        Log.i(TAG, "tiggerAlarm: hour == hourS && hour == hourE && (minute < minuteS && minute > minuteE)");
                    } else if(hour == hourS && hour == hourE && minuteE < minuteS){
                        alarm.setOnetimeTimer(context, hour, minute);
                    }
                    else if (hour <= hourS && minute < minuteS) {
                        Log.i(TAG, "tiggerAlarm: hour <= hourS and minute < minuteS");
                        alarm.setOnetimeTimerWithSeconds(context,hourS,minuteS, 0);
                        editor.putString("isOutOfRange", "yes").apply();
                    } else if (hour == hourE && minute > minuteE) {
                        Log.i(TAG, "tiggerAlarm: hour == hourE and minute > minuteE");
                        editor.putString("isOutOfRange", "yes").apply();
                    } else if (hour == hourS && minute >= minuteS) {
                        alarm.setOnetimeTimer(context, hour, minute);
                        editor.putString("isOutOfRange", "").apply();
                        Log.i(TAG, "tiggerAlarm: hour == hourS and minute >= minuteS");
                    } else if (hour == hourE && minute <= minuteE) {
                        alarm.setOnetimeTimer(context, hour, minute);
                        editor.putString("isOutOfRange", "").apply();
                        Log.i(TAG, "tiggerAlarm: hour == hourE and minute <= minuteE");
                    } else if (hours.contains(hour)) {
                        alarm.setOnetimeTimer(context, hour, minute);
                        editor.putString("isOutOfRange", "").apply();
                        Log.i(TAG, "tiggerAlarm: hours.contains(hour)");
                    }

                } else if (utilities.getIsSnoozed().equals("TRUE")){// && utilities.getIsDismissed().equals("FALSE")) {
                    fiveMinPassed = minute - snoozeStartTime;
                    System.out.println("fiveMinPassed: " + fiveMinPassed + " " +minute+ " " + snoozeStartTime);
                    if (fiveMinPassed >= prefs.getInt("snoozeTime", 5) || fiveMinPassed < 0) {
                        utilities.writeIsSnoozed("FALSE");
                        fiveMinPassed = 0;
                    }
                }
            }
            else{
                Log.i(TAG, "triggerAlarm: is dismissed");
            }


            Log.i(TAG, "Current hour: " + hour);
            Log.i(TAG, "Current minute: " + minute);
            Log.i(TAG, "From broadcast hourS: " + hourS);
            Log.i(TAG, "From broadcast minuteS: " + minuteS);
            Log.i(TAG, "From broadcast hourE: " + hourE);
            Log.i(TAG, "From broadcast minuteE: " + minuteE);

        }
        catch (Exception ex){
            Log.i(TAG, "triggerAlarm: "+ex);

            Log.i(TAG, "From broadcast hourS: " + hS);
            Log.i(TAG, "From broadcast minuteS: " + mS);
            Log.i(TAG, "From broadcast hourE: " + hE);
            Log.i(TAG, "From broadcast minuteE: " + mE);
        }
    }
}
