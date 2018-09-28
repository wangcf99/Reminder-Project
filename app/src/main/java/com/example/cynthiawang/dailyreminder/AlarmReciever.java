package com.example.cynthiawang.dailyreminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class AlarmReciever extends BroadcastReceiver {
    public static int unique_id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {


        String title = ViewActivity.title;
        System.out.println("AlarmReciever: " + title);
        String notes = ViewActivity.notes;
        long when = ViewActivity.when;
        String action = intent.getAction();

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(notes)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .setTicker("Ticker")
                .setAutoCancel(true);

        notificationManager.notify(unique_id,builder.build());
        unique_id++;
    }
}
