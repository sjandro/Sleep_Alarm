package com.asalazar.alex.sleepalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sjand on 6/26/2016.
 */
public class StartServiceOnBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentSetAlarm = new Intent(context, ScreenDetectService.class);
        intentSetAlarm.putExtra("startAlarmOnBoot", true);
        context.startService(intentSetAlarm);
    }
}
