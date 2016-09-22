package com.asalazar.alex.sleepalarm;

import android.Manifest;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ImageButton editBttnStart;
    ImageButton editBttnEnd;
    public FragmentActivity listener;

    private int HOUR, MINUTE;
    Utilities UTILITIES = new Utilities();
    boolean start = false, end = false;

    SharedPreferences sharedpref;
    public static final String mypreference = "mypref";

    private Alarm alarm = new Alarm();

    private Switch power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener = this;

        if (Build.VERSION.SDK_INT >= 23)
            getPermissionToReadUserContacts();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here
            }
            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        NotificationManager manager = (NotificationManager) listener.getSystemService(listener.NOTIFICATION_SERVICE);
        manager.cancel(33);

        power = (Switch) findViewById(R.id.powerSwitch);
        editBttnStart = (ImageButton) findViewById(R.id.editBttnStart);
        editBttnEnd = (ImageButton) findViewById(R.id.editBttnEnd);
        final Button dismissBttn = (Button) findViewById(R.id.dismiss);
        final Button snoozeBttn = (Button) findViewById(R.id.snooze);
        Button trashBttn = (Button) findViewById(R.id.trash);
        final TextView startTV = (TextView) findViewById(R.id.start);
        final TextView endTV = (TextView) findViewById(R.id.end);
        sharedpref = listener.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

//        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        bar.setElevation(5);

        try {
            SetSaveTime();
        }
        catch (Exception ex){
            System.out.println(ex);
        }

        checkPowerStatus();
        checkButtonState();

        power.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NotificationManager manager = (NotificationManager) listener.getSystemService(listener.NOTIFICATION_SERVICE);
                manager.cancel(33);
                //SharedPreferences.Editor editor = sharedpref.edit();

                if(power.isChecked()){
                    UTILITIES.toastMsg("Alarm on", listener);
                    UTILITIES.writePower("TRUE");
                    if(!endTV.getText().equals("XX:XX")){
                        try{
                            //alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                            int hourCur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                            int minuteCur = Calendar.getInstance().get(Calendar.MINUTE);
                            int hS = Integer.valueOf(sharedpref.getString("hourS", ""));
                            int hE = Integer.valueOf(sharedpref.getString("hourE", ""));
                            int mS = Integer.valueOf(sharedpref.getString("minuteS", ""));
                            int mE = Integer.valueOf(sharedpref.getString("minuteE", ""));
                            ArrayList<Integer> hours = UTILITIES.timeRange(hS, hE, mS, mE);

//                            boolean isBetween = UTILITIES.checkIfBetween(hS, mS, hE, mE);
//                            System.out.println(isBetween);
//                            if(isBetween){
//                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
//                            }
                            if (hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE) {
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE");
                            } else if(hourCur == hS && hourCur == hE && mE < mS){
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hourCur == hS && hourCur == hE && mE < mS");
                            } else if (hourCur <= hS && minuteCur < mS) {
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hourCur <= hS && minuteCur < mS");
                            } else if (hourCur == hE && minuteCur > mE) {
                                System.out.println("hourCur == hE && minuteCur > mE");
                            } else if (hourCur == hS && minuteCur >= mS) {
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hourCur == hS && minuteCur >= mS");
                            } else if (hourCur == hE && minuteCur <= mE) {
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hourCur == hE && minuteCur <= mE");
                            } else if (hours.contains(hourCur)) {
                                alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                                System.out.println("hours.contains(hourCur)");
                            }
                            else{
                                System.out.println("powerOn: no condition satisfy" );
                            }
                        }
                        catch (Exception ex){
                            System.out.println("In Power on: " + ex);
                        }
                    }
                }
                else if(!power.isChecked()){
                    UTILITIES.toastMsg("Alarm off", listener);
                    UTILITIES.writePower("FALSE");
                    alarm.CancelAlarm(listener);
                    UTILITIES.restoreDisplayBrightness(listener);
                }
                checkPowerStatus();
            }
        });

        editBttnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                start = true;
                showTimePicker();
            }
        });

        editBttnEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                end = true;
                showTimePicker();
            }
        });

        dismissBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if (startTV.getText().equals("XX:XX") && endTV.getText().equals("XX:XX")) {
                    UTILITIES.toastMsg("Please set an alarm first.", listener);
                } else {
                    try {
                        int hourCur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int minuteCur = Calendar.getInstance().get(Calendar.MINUTE);
                        int hS = Integer.valueOf(sharedpref.getString("hourS", ""));
                        int hE = Integer.valueOf(sharedpref.getString("hourE", ""));
                        int mS = Integer.valueOf(sharedpref.getString("minuteS", ""));
                        int mE = Integer.valueOf(sharedpref.getString("minuteE", ""));
                        ArrayList<Integer> hours = UTILITIES.timeRange(hS, hE, mS, mE);
                        for(int i : hours)
                            System.out.println(i);

                        boolean isBetween = UTILITIES.checkIfBetween(hS, mS, hE, mE);
                        System.out.println(isBetween);
                        if(isBetween){
                            dismiss();
                        }
                        else{
                            UTILITIES.toastMsg("Time is out of range", listener);
                        }

//                        if (hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE) {
//                            //dismiss();
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE");
//                        } else if(hourCur == hS && hourCur == hE && mE < mS){//hourCur == hS && hourCur == hE && mE < mS
//                            dismiss();
//                            System.out.println("hE == hS && hours.contains(hourCur)");
//                        } else if(hourCur == hS && hourCur == hE && mE < mS){
//                            dismiss();
//                            System.out.println("hourCur == hS && hourCur == hE && mE < mS");
//                        } else if (hourCur <= hS && minuteCur < mS && !hours.contains(hourCur)) {
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur <= hS && minuteCur < mS");
//                        } else if (hourCur == hE && minuteCur > mE) {
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur == hE && minuteCur > mE");
//                        } else if (hourCur == hS && minuteCur >= mS) {
//                            dismiss();
//                            System.out.println("hourCur == hS && minuteCur >= mS");
//                        } else if (hourCur == hE && minuteCur <= mE) {
//                            dismiss();
//                            System.out.println("hourCur == hE && minuteCur <= mE");
//                        } else if (hours.contains(hourCur)) {
//                            dismiss();
//                            System.out.println("hours.contains(hourCur)");
//                        }
//                        else{
//                            System.out.println("dismissBttn: no condition satisfy" );
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                        }
                    }
                    catch (Exception ex){System.out.println("dissmissBttnClick: " + ex);}
                }

            }
        });

        snoozeBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int hourCur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int hour =  Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);
                int minuteCur = Calendar.getInstance().get(Calendar.MINUTE);
                minute = minute + sharedpref.getInt("snoozeTime", 5);


                if (startTV.getText().equals("XX:XX") && endTV.getText().equals("XX:XX")) {
                    UTILITIES.toastMsg("Please set an alarm first.", listener);
                } else {
                    try {
                        int hS = Integer.valueOf(sharedpref.getString("hourS", ""));
                        int hE = Integer.valueOf(sharedpref.getString("hourE", ""));
                        int mS = Integer.valueOf(sharedpref.getString("minuteS", ""));
                        int mE = Integer.valueOf(sharedpref.getString("minuteE", ""));
                        ArrayList<Integer> hours = UTILITIES.timeRange(hS, hE, mS, mE);

                        for(int h : hours){
                            System.out.println(h);
                        }

                        System.out.println(hours.size());

                        int endTime = hE * 100 + mE;
                        if(minute > 60){
                            minute = minute - 60;
                            hour++;
                        }

                        int snoozeTime = hour * 100 + minute;

                        if(hS > hE || (hS == hE && mS > mE)){
                            snoozeTime = endTime;
                        }

                        System.out.println("snoozeTime && endTime: " + snoozeTime + " && " + endTime);

                        boolean isBetween = UTILITIES.checkIfBetween(hS, mS, hE, mE);
                        //boolean isBetweenSnooze = UTILITIES.checkIfBetween()
                        System.out.println(isBetween);

                        if(isBetween && !(snoozeTime > endTime)){
                            snooze(hourCur, minute);
                        }
                        else{
                            UTILITIES.toastMsg("Time is out of range", listener);
                        }

//                        if (hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE) {
//                            //snooze(hourCur, minute);
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE");
//                        } else if(hourCur == hS && hourCur == hE && mE < mS){
//                            snooze(hour, minute);
//                            System.out.println("hourCur == hS && hourCur == hE && mE < mS");
//                        }
//                        else if (hourCur <= hS && minuteCur < mS && !hours.contains(hourCur)) {
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur <= hS && minuteCur < mS");
//                        } else if (hourCur == hE && minuteCur > mE) {
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur == hE && minuteCur > mE");
//                        } else if (hourCur == hE && minute > mE) {
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("hourCur == hE && minute > mE");
//                        }
//                        else if (hourCur == hS && minuteCur >= mS && !(snoozeTime > currentTime)) {
//                            snooze(hour, minute);
//                            System.out.println("hourCur == hS && minuteCur >= mS");
//                        } else if (hourCur == hE && minuteCur <= mE && !(snoozeTime > currentTime)) {
//                            snooze(hour, minute);
//                            System.out.println("hourCur == hE && minuteCur <= mE");
//                        }  else if(snoozeTime > currentTime){
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                            System.out.println("snoozeTime > currentTime: " + snoozeTime + " > " + currentTime);
//                        }
//                        else if (hours.contains(hourCur)) {
//                            snooze(hour, minute);
//                            System.out.println("hours.contains(hourCur)");
//                        }
//                        else{
//                            System.out.println("snoozeBttn: no condition satisfy" );
//                            UTILITIES.toastMsg("Time is out of range", listener);
//                        }
                    } catch (Exception ex) {
                        System.out.println("snoozeBttnClick: " + ex);
                    }
                }
            }
        });

        trashBttn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startTV.setText("XX:XX");
                endTV.setText("XX:XX");
                UTILITIES.writeSavedTimes("XX:XX","start");
                UTILITIES.writeSavedTimes("XX:XX","end");
                UTILITIES.writeIsSnoozed("FALSE");
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("hourS", "").apply();
                editor.putString("minuteS", "").apply();
                editor.putString("hourE", "").apply();
                editor.putString("minuteE", "").apply();
                editor.putBoolean("dismissedClicked", false).apply();
                dismissBttn.setVisibility(View.VISIBLE);
                UTILITIES.toastMsg("Alarm deleted", listener);
                alarm.CancelAlarm(listener);
                NotificationManager manager = (NotificationManager) listener.getSystemService(listener.NOTIFICATION_SERVICE);
                manager.cancel(33);

                editor.putBoolean("power", false).apply();
                UTILITIES.writePower("FALSE");
                checkPowerStatus();
                UTILITIES.restoreDisplayBrightness(listener);
            }
        });

        beginBroadcastReceiver(listener);

    }

    public void dismiss(){
        UTILITIES.writeIsSnoozed("FALSE");
        alarm.CancelAlarm(listener);
        UTILITIES.toastMsg("Alarm dismissed", listener);

        UTILITIES.writeDismiss("TRUE");

        NotificationManager manager = (NotificationManager) listener.getSystemService(listener.NOTIFICATION_SERVICE);
        manager.cancel(33);

        checkButtonState();

        UTILITIES.restoreDisplayBrightness(listener);
    }

    public void snooze(int hour, int minute){
        System.out.println("Snooze pressed, minute: " + minute + " hour: " + hour);
        NotificationManager manager = (NotificationManager) listener.getSystemService(listener.NOTIFICATION_SERVICE);
        manager.cancel(33);
        UTILITIES.writeIsSnoozed("TRUE");
        UTILITIES.writeDismiss("FALSE");
        UTILITIES.toastMsg("Alarm snoozed for "+sharedpref.getInt("snoozeTime", 5)+" minutes", listener);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putBoolean("dismissedClicked", false).apply();
        alarm.CancelAlarm(listener);
        alarm.setOnetimeTimerWithSeconds(listener, hour, minute,0);

        checkButtonState();

        UTILITIES.restoreDisplayBrightness(listener);
    }

    @Override
    public void onBackPressed() {}


    @Override
    public void onResume(){
        try {
            //checkButtonState();
            checkPowerStatus();
        }
        catch (Exception ex){
            System.out.println("In onResume: " + ex);
        }
        super.onResume();
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        try {
//            //checkButtonState();
//            checkPowerStatus();
//        }
//        catch (Exception ex){
//            System.out.println("In onResume: " + ex);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.snoozetime) {
            myDialog myDialog = new myDialog();
            myDialog.show(getSupportFragmentManager(), "My Dialog");
        }

        if(id == R.id.userName){
            myDialogName myDialogName = new myDialogName();
            myDialogName.show(getSupportFragmentManager(),"My name");
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPowerStatus() {
        if(UTILITIES.getPower().equals("TRUE")) {
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putBoolean("power", true).apply();
        }
        if(UTILITIES.getPower().equals("FALSE")){
            SharedPreferences.Editor editor = sharedpref.edit();
            editor.putBoolean("power", false).apply();
        }
        boolean powerOn = sharedpref.getBoolean("power", false);
        System.out.println("checkPowerStatus: "+powerOn);

        power.setChecked(powerOn);

        if(powerOn)
            System.out.println("SWITCH ON!");

        ImageView status = (ImageView) listener.findViewById(R.id.status);
        editBttnStart = (ImageButton) findViewById(R.id.editBttnStart);
        editBttnEnd = (ImageButton) findViewById(R.id.editBttnEnd);
        final Button dismissBttn = (Button) findViewById(R.id.dismiss);
        Button snoozeBttn = (Button) findViewById(R.id.snooze);
        Button trashBttn = (Button) findViewById(R.id.trash);
        TextView startTV = (TextView) findViewById(R.id.start);
        TextView endTV = (TextView) findViewById(R.id.end);

        if(power.isChecked()){
            status.setImageResource(R.drawable.ic_action_ic_done_black_18px);

            if(endTV.getText().equals("XX:XX"))
                editBttnEnd.setVisibility(View.VISIBLE);
            else
                editBttnEnd.setVisibility(View.INVISIBLE);
            if(startTV.getText().equals("XX:XX"))
                editBttnStart.setVisibility(View.VISIBLE);
            else
                editBttnStart.setVisibility(View.INVISIBLE);

            startTV.setTextColor(Color.parseColor("#ffffff"));
            endTV.setTextColor(Color.parseColor("#ffffff"));
            dismissBttn.setEnabled(true);
            snoozeBttn.setEnabled(true);
            trashBttn.setEnabled(true);
            trashBttn.setTextColor(Color.parseColor("#ffffff"));

            UTILITIES.writePower("TRUE");
            checkButtonState();

        }
        else{
            status.setImageResource(R.drawable.ic_action_ic_clear_black_18px);

            editBttnEnd.setVisibility(View.GONE);
            editBttnStart.setVisibility(View.GONE);

            alarm.CancelAlarm(listener);
            startTV.setTextColor(Color.parseColor("#7e7e7e"));
            endTV.setTextColor(Color.parseColor("#7e7e7e"));
            dismissBttn.setEnabled(false);
            snoozeBttn.setEnabled(false);
            trashBttn.setEnabled(false);
            trashBttn.setTextColor(Color.parseColor("#7e7e7e"));

            UTILITIES.writePower("FALSE");
            UTILITIES.writeIsSnoozed("FALSE");
            UTILITIES.writeDismiss("FALSE");
            checkButtonState();
        }
    }

    public void beginBroadcastReceiver(Context context) {
        Intent i = new Intent(context, ScreenDetectService.class);
        i.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startService(i);
    }

    private void checkButtonState(){

        Button dismissBttn = (Button) findViewById(R.id.dismiss);
        Button snoozeBttn = (Button) findViewById(R.id.snooze);

        if(UTILITIES.getPower().equals("FALSE")){
            dismissBttn.setTextColor(Color.parseColor("#7e7e7e"));
            snoozeBttn.setTextColor(Color.parseColor("#7e7e7e"));
            return;
        }

        if(UTILITIES.getDismiss().equals("TRUE")){
            dismissBttn.setTextColor(Color.parseColor("#7F9763"));
        }
        else{
            dismissBttn.setTextColor(Color.parseColor("#ffffff"));
        }
        if(UTILITIES.getIsSnoozed().equals("TRUE")){
            snoozeBttn.setTextColor(Color.parseColor("#7F9763"));
        }
        else {
            snoozeBttn.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    private void showTimePicker() {
        Calendar calender = Calendar.getInstance();

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, final int hour, final int minute) {
                HOUR = hour;
                MINUTE = minute;
                System.out.println("Time selected!!!");
                TextView startTV = (TextView) findViewById(R.id.start);
                final TextView endTV = (TextView) findViewById(R.id.end);

                String[] formattedHour = UTILITIES.theHour(HOUR).split(",");
                if(start){

                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("hourS", Integer.toString(HOUR)).apply();
                    editor.putString("minuteS", Integer.toString(MINUTE)).apply();
                    UTILITIES.writeSavedTimes(hour+":"+minute,"start");

                    if(Integer.toString(MINUTE).length() == 1){
                        startTV.setText(formattedHour[0] + ":0" + MINUTE + " " + formattedHour[1]);

                    }
                    else{
                        startTV.setText(formattedHour[0] + ":" + MINUTE + " " + formattedHour[1]);
                    }

                    if(!endTV.getText().equals("XX:XX")){
                        try{
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        }
                        catch (Exception ex){
                            System.out.println("In EditEnd: " + ex);
                        }
                    }
                    alarm.SetAlarm(listener, Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                    editBttnStart.setVisibility(View.INVISIBLE);
                }
                if(end){
                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("hourE", Integer.toString(HOUR)).apply();
                    editor.putString("minuteE", Integer.toString(MINUTE)).apply();
                    UTILITIES.writeSavedTimes(hour+":"+minute,"end");
                    alarm.RestoreBrightnessAlarm(listener,HOUR,MINUTE,0);

                    try{
                        alarm.CancelAlarm(listener);
                        //alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        int hourCur = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                        int minuteCur = Calendar.getInstance().get(Calendar.MINUTE);
                        int hS = Integer.valueOf(sharedpref.getString("hourS", ""));
                        int hE = Integer.valueOf(sharedpref.getString("hourE", ""));
                        int mS = Integer.valueOf(sharedpref.getString("minuteS", ""));
                        int mE = Integer.valueOf(sharedpref.getString("minuteE", ""));
                        ArrayList<Integer> hours = UTILITIES.timeRange(hS, hE, mS, mE);

//                        boolean isBetween = UTILITIES.checkIfBetween(hS, mS, hE, mE);
//                        System.out.println(isBetween);
//                        if(isBetween){
//                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
//                        }

                        if (hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE) {
                            System.out.println("hourCur == hS && hourCur == hE && minuteCur < mS && minuteCur > mE");
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        } else if(hourCur == hS && hourCur == hE && mE < mS){
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                            System.out.println("hourCur == hS && hourCur == hE && mE < mS");
                        }else if (hourCur <= hS && minuteCur < mS) {
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                            System.out.println("hourCur == hS && hourCur == hE && mE < mS");
                        } else if (hourCur == hE && minuteCur > mE) {
                            System.out.println("hourCur == hE && minuteCur > mE");
                        } else if (hourCur == hS && minuteCur >= mS) {
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        } else if (hourCur == hE && minuteCur <= mE) {
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        } else if (hours.contains(hourCur)) {
                            alarm.setOnetimeTimerWithSeconds(listener,Integer.valueOf(sharedpref.getString("hourS", "")),Integer.valueOf(sharedpref.getString("minuteS", "")),0);
                        }
                        else{
                            System.out.println("snoozeBttn: no condition satisfy" );
                        }
                    }
                    catch (Exception ex){
                        System.out.println("In EditEnd: " + ex);
                    }

                    if(Integer.toString(MINUTE).length() == 1){
                        endTV.setText(formattedHour[0] + ":0" + MINUTE + " " + formattedHour[1]);
                    }
                    else{
                        endTV.setText(formattedHour[0] + ":" + MINUTE + " " + formattedHour[1]);
                    }
                    editBttnEnd.setVisibility(View.INVISIBLE);
                }

                start = false; end = false;
            }
        }, calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), false);//true for 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void SetSaveTime(){
        TextView startTV = (TextView) findViewById(R.id.start);
        TextView endTV = (TextView) findViewById(R.id.end);

        String[] formattedHour = UTILITIES.theHour(Integer.valueOf(sharedpref.getString("hourS", ""))).split(",");
        String[] formattedHourEnd = UTILITIES.theHour(Integer.valueOf(sharedpref.getString("hourE", ""))).split(",");

        if(sharedpref.getString("minuteS", "").length() == 1){
            startTV.setText(formattedHour[0] + ":0" + sharedpref.getString("minuteS", "") + " " + formattedHour[1]);
        }
        else{
            startTV.setText(formattedHour[0] + ":" + sharedpref.getString("minuteS", "")+ " " + formattedHour[1]);
        }

        if(sharedpref.getString("minuteE", "").length() == 1){
            endTV.setText(formattedHourEnd[0] + ":0" + sharedpref.getString("minuteE", "") + " " + formattedHourEnd[1]);
        }
        else{
            endTV.setText(formattedHourEnd[0] + ":" + sharedpref.getString("minuteE", "")+ " " + formattedHourEnd[1]);
        }

    }


    private static final int WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 1;

    // Called when the user is performing an action which requires the app to read the
    // user's contacts
    public void getPermissionToReadUserContacts() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI
            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
//        UTILITIES.writeSnoozeColor("#7e7e7e");
//        UTILITIES.writeDismissColor("#7e7e7e");
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                UTILITIES.writeDismiss("FALSE");
                UTILITIES.writeIsSnoozed("FALSE");
                UTILITIES.writePower("FALSE");
                UTILITIES.writeSavedTimes("", "start");
                UTILITIES.writeSavedTimes("", "end");
                checkButtonState();
                Toast.makeText(this, "Storage write permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage write permission denied", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
