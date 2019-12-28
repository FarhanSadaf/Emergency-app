package com.example.testapplication_navigationdrawer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.testapplication_navigationdrawer.EmergencyActivity;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn = true;
    int count = 0;
    long time = 0, timeDiff = 0;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("tag", count+"");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            count++;
            wasScreenOn = false;
            timeDiff = System.currentTimeMillis() - time;
            Log.e("tag",timeDiff + "");
            time = System.currentTimeMillis();
            if(timeDiff >= 1200)
                count = 0;

            if(count > 4){
                count = 0;
                Intent intent1 = new Intent(context.getApplicationContext(), EmergencyActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                Log.e("tag", "success");
            }

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            count++;
            wasScreenOn = true;
            timeDiff = System.currentTimeMillis() - time;
            Log.e("tag",timeDiff + "");
            time = System.currentTimeMillis();
            if(timeDiff >= 1200)
                count = 0;

            if(count > 4){
                count = 0;
                Intent intent1 = new Intent(context.getApplicationContext(), EmergencyActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                Log.e("tag", "success");
            }

        }
    }
}
