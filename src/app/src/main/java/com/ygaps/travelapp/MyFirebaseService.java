package com.ygaps.travelapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.FirebaseApp;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.View.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    private  String tourId;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // handle a notification payload.
        // super.onMessageReceived(remoteMessage);
        Log.e("FROM:",remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Map data=remoteMessage.getData();
            if (data.isEmpty()) { // message type is notification.
                Log.e("data", "isNull");
                sendNotification("Invite to join a Tour", remoteMessage.getNotification().getBody());
            } else { // message type is data.
                tourId = data.get("id").toString();
                StringBuilder temp = new StringBuilder();
                temp.append("Someone").append(" invites you to Tour: ").append(data.get("name"));
                String body = temp.toString();
                sendNotification("Invite to join a Tour", body);
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            RemoteMessage.Notification notification = remoteMessage.getNotification();
        }
    }

    @Override
    public void onNewToken(String fcmToken) {
        super.onNewToken(fcmToken);
       // getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", fcmToken).apply();
        Log.d(TAG, "Refreshed token: " + fcmToken);
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fcm_token", "empty");
    }

    private Intent createIntent(String actionName,String messaggeBody)
    {
        Intent intent=new Intent(this,ResponseAction.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("action",actionName);
        intent.putExtra("tourId",tourId);
        intent.putExtra("message",messaggeBody);
        return intent;
    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = createIntent("OK",messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sendBroadcast(intent);

        Intent intent2 = new Intent(this,ResponseAction.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent2.putExtra("action","refuse");
        sendBroadcast(intent2);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.project_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_missed,
                                "Cancel",
                                PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_CANCEL_CURRENT)))
                        .addAction(new NotificationCompat.Action(
                                android.R.drawable.sym_call_outgoing,
                                "OK",
                                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
