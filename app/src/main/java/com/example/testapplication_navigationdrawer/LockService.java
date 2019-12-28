package com.example.testapplication_navigationdrawer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import static com.example.testapplication_navigationdrawer.App.CHANNEL_ID;

public class LockService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        //startForeground
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Service is running in background")
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);


        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }
}
