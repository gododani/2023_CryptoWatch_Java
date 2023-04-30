package com.example.cryptowatch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 20000908;
    private static final String CHANNEL_ID = "CryptoWatch_notification_channel";
    private final int NOTIFICATION_ID = 0;
    private Context mContext;
    private NotificationManager mManager;

    public NotificationHandler(Context context) {
        this.mContext = context;
        this.mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }


    private void createChannel(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {return;}
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"CryptoWatch", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.RED);
        channel.setDescription("Notification from CryptoWatch");
        mManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void send(String message){
        Intent intent  = new Intent(mContext, CryptoActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("CryptoWatch")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_my_crypto)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        this.mManager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel(){
        this.mManager.cancel(NOTIFICATION_ID);
    }
}
