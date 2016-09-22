package com.asalazar.alex.sleepalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sjand on 3/19/2016.
 */
public class Utilities {
    private static final String TAG = "UTILITIES";


    public static String theMonth(int month){
        String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return monthNames[month];
    }

    public static String theTime(int time){
        String[] timeNames = {"12AM", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM",
                "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM"};
        return timeNames[time];
    }

    public static String theHour(int time){
        String[] timeNames = {"12,AM", "1,AM", "2,AM", "3,AM", "4,AM", "5,AM", "6,AM", "7,AM", "8,AM", "9,AM", "10,AM", "11,AM",
                "12,PM", "1,PM", "2,PM", "3,PM", "4,PM", "5,PM", "6,PM", "7,PM", "8,PM", "9,PM", "10,PM", "11,PM"};
        return timeNames[time];
    }

    public ArrayList<Integer> timeRange(int hourS, int hourE, int minS, int minE){
        ArrayList<Integer> hours = new ArrayList<>();

        if(hourS >= hourE && minE <= minS){
            for(int i = hourS;  i < 24; ++i)
                hours.add(i);

            for(int i = 0; i <= hourE; ++i)
                hours.add(i);
        }
        else{
            for(int i = hourS; i <= hourE; ++i)
                hours.add(i);
        }

        return hours;
    }



    public void writeSavedTimes(String time, String type){
        try{
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Sleep_Alarm");
            directory.mkdirs();

            String savedAlarm = type+"saved_alarm.txt";
            FileWriter fw = new FileWriter("sdcard/Sleep_Alarm/" + savedAlarm);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(time);
            bw.close();
        }
        catch (IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
        }
    }

    public String getSavedAlarm(String type){
        String line = "";
        try{
            String savedAlarm = type+"saved_alarm.txt";
            FileReader fr = new FileReader("sdcard/Sleep_Alarm/"+savedAlarm);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            Log.i(TAG, "writeSavedTimes: " + line);
            //while ((line = br.readLine()) != null)   {
            //    continue;
            //}
            return line;
        }
        catch(IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
            return "XX:XX";

        }
    }

    public void writeIsSnoozed(String snoozed){
        try{
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Sleep_Alarm");
            directory.mkdirs();

            String savedAlarm = "snoozed.txt";
            FileWriter fw = new FileWriter("sdcard/Sleep_Alarm/" + savedAlarm);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(snoozed);
            bw.close();
        }
        catch (IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
        }
    }

    public String getIsSnoozed(){
        String line = "";
        try{
            String savedAlarm = "snoozed.txt";
            FileReader fr = new FileReader("sdcard/Sleep_Alarm/"+savedAlarm);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            //while ((line = br.readLine()) != null)   {
            //    continue;
            //}
            return line;
        }
        catch(IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
            return "FALSE";

        }
    }

    public void writePower(String pow){
        try{
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Sleep_Alarm");
            directory.mkdirs();

            String power = "power.txt";
            FileWriter fw = new FileWriter("sdcard/Sleep_Alarm/" + power);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(pow);
            bw.close();
        }
        catch (IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
        }
    }

    public String getPower(){
        String line = "";
        try{
            String power = "power.txt";
            FileReader fr = new FileReader("sdcard/Sleep_Alarm/"+power);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            //while ((line = br.readLine()) != null)   {
            //    continue;
            //}
            return line;
        }
        catch(IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
            return "FALSE";

        }
    }

    public void writeBrightnessSetting(String brightnessSetting){
        try{
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Sleep_Alarm");
            directory.mkdirs();

            String savedAlarm = "brightness.txt";
            FileWriter fw = new FileWriter("sdcard/Sleep_Alarm/" + savedAlarm);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(brightnessSetting);
            bw.close();
        }
        catch (IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
        }
    }

    public int getBrightnessSetting(){
        String line = "";
        try{
            String savedAlarm = "brightness.txt";
            FileReader fr = new FileReader("sdcard/Sleep_Alarm/"+savedAlarm);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            //while ((line = br.readLine()) != null)   {
            //    continue;
            //}
        }
        catch(IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());

        }
        return Integer.valueOf(line);
    }

    public void toastMsg(CharSequence str, Context context){
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }



    public void dimDisplay(final Context context){



        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                if(getDismiss().equals("TRUE"))
                    return;
                if(getIsSnoozed().equals("TRUE"))
                    return;
                if(getPower().equals("FALSE"))
                    return;
                else{
                    int n = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
                    Log.i(TAG, "System brightness: " + n);
                    if(n > 0)
                        writeBrightnessSetting(Integer.toString(n));
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
                    Intent in = new Intent(context,DummyBrightnessActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                }
            }
        }.start();
    }

    public void restoreDisplayBrightness(Context listener){
        try{
            int currentBrightness = Settings.System.getInt(listener.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);
            int savedBrightness = getBrightnessSetting();
            if(currentBrightness > savedBrightness){
                System.out.println("Current brightness > saved brightness");
            }
            else{
                Settings.System.putInt(listener.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, savedBrightness);
            }
            Intent in = new Intent(listener,DummyBrightnessActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            listener.startActivity(in);
        }
        catch (Exception ex){
            System.out.println("restoreDisplayBrightness: " + ex);
        }
    }

    public void writeDismiss(String s){
        try{
            File directory = new File(Environment.getExternalStorageDirectory()+ File.separator+"Sleep_Alarm");
            directory.mkdirs();

            String file = "dismissBttn.txt";
            FileWriter fw = new FileWriter("sdcard/Sleep_Alarm/" + file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.close();
        }
        catch (IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
        }
    }

    public String getDismiss(){
        String line = "";
        try{
            String file = "dismissBttn.txt";
            FileReader fr = new FileReader("sdcard/Sleep_Alarm/"+file);
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();
            //while ((line = br.readLine()) != null)   {
            //    continue;
            //}
        }
        catch(IOException ex){
            Log.i(TAG, "Can't open file... " + ex.toString());
            return "FALSE";
        }
        return line;
    }

    public  boolean checkIfBetween(int hS, int mS, int hE, int mE){
        int from = hS * 100 + mS;
        int to = hE * 100 + mE;
        Date date = new Date();
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);
        System.out.println(c.get(Calendar.HOUR_OF_DAY));
        System.out.println(c.get(Calendar.MINUTE));
        System.out.println(t);
        boolean isBetween = to > from && t >= from && t <= to || to < from && (t >= from || t <= to);
        return isBetween;
    }
}
