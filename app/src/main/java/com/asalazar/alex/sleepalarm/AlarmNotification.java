package com.asalazar.alex.sleepalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by sjand on 4/15/2016.
 */
public class AlarmNotification extends BroadcastReceiver {

    NotificationManager notificationManager;
    boolean isNotificActive = false;
    int notiID = 33;
    Utilities utilities;

    SharedPreferences sharedpref;
    public static final String mypreference = "mypref";

    @Override
    public void onReceive(Context context, Intent intent) {
        utilities = new Utilities();
        if(intent.getExtras().getBoolean("start_time", false)){
            System.out.println("time to start");
            if(utilities.getPower().equals("FALSE")){
                showNotification(context);
                utilities.dimDisplay(context);
            }
            utilities.writePower("TRUE");
        }
        else if(intent.getExtras().getBoolean("end_time", false)){
            utilities.restoreDisplayBrightness(context);
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.cancel(33);
        }
        else {
            sharedpref = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            System.out.println("In Alarm Receiver");
            showNotification(context);
            utilities.dimDisplay(context);
        }
    }


    public void showNotification(Context context){

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Intent DISMISS_ACTION = new Intent();
        DISMISS_ACTION.setAction(Constants.ACTION.DISMISS_ACTION);
        PendingIntent pendingIntentDismiss = PendingIntent.getBroadcast(context, 12345, DISMISS_ACTION, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent SNOOZE_ACTION = new Intent();
        SNOOZE_ACTION.setAction(Constants.ACTION.SNOOZE_ACTION);
        PendingIntent pendingIntentSnooze = PendingIntent.getBroadcast(context, 12345, SNOOZE_ACTION, PendingIntent.FLAG_UPDATE_CURRENT);

        String name = "";
        try {
            name = sharedpref.getString("savedName", "");
            name = name.substring(0,1).toUpperCase() + name.substring(1, name.length()).toLowerCase();
        }
        catch (Exception ex){
            System.out.println("showNotification: " + ex);
        }
        String message;

        if(!name.equals(""))
            message = name + " time is up, your display will go dim";
        else
            message = "Time is up, your display will go dim";

        NotificationCompat.Builder notificBuilder = new
                NotificationCompat.Builder(context)
                .setContentTitle(message)
                .setContentText("Please snooze or dismiss this alarm")
                .setTicker("Please snooze or dismiss this alarm")
                .setSmallIcon(R.drawable.ic_alarm)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendIntent)
                .addAction(R.drawable.ic_snooze_black_24dp, "snooze", pendingIntentSnooze)
                .addAction(R.drawable.ic_alarm_off_black_24dp, "dismiss", pendingIntentDismiss);


        if (Build.VERSION.SDK_INT >= 21) notificBuilder.setVibrate(new long[0]);


        Notification note = notificBuilder.build();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notiID, note);

        utilities.writeDismiss("FALSE");
        utilities.writeIsSnoozed("FALSE");

    }
}
