package com.tomo.tomolive;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class FirebaseMessage extends FirebaseMessagingService {
    private static final String TAG = "mmmmmm";
    private Intent intent;


    public FirebaseMessage() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            //  intent = new Intent(this, MainActivity.class);

            //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "onMessageReceived: " + data);

            String type = String.valueOf(remoteMessage.getData().get("notificationType"));

          /*  if (!type.equals("") && !type.equals("null")) {

                switch (type) {
                    case "chat":

                        String hostid = String.valueOf(remoteMessage.getData().get("hostid"));
                        String name = String.valueOf(remoteMessage.getData().get("name"));
                        String image = String.valueOf(remoteMessage.getData().get("image"));
                        Log.d("notificationData", "Bundle Contains: key=" + hostid);


                        notificationIntent = new NotificationIntent();
                        notificationIntent.setType(NotificationIntent.CHAT);
                        notificationIntent.setHostid(hostid);
                        notificationIntent.setImage(image);
                        notificationIntent.setName(name);
                        isnotification = true;
                        break;

                    case "live":
                        String image1 = String.valueOf(remoteMessage.getData().get("image"));
                        String hostid1 = String.valueOf(remoteMessage.getData().get("host_id"));
                        String name1 = String.valueOf(remoteMessage.getData().get("name"));
                        String cid = String.valueOf(remoteMessage.getData().get("country_id"));
                        String type1 = String.valueOf(remoteMessage.getData().get("type"));
                        String coin = String.valueOf(remoteMessage.getData().get("coin"));
                        String token = String.valueOf(remoteMessage.getData().get("token"));
                        String channel = String.valueOf(remoteMessage.getData().get("channel"));
                        String view = String.valueOf(remoteMessage.getData().get("view"));

                        Log.d("notificationData", "onCreate: hostid "+hostid1);
                        Thumb thumb=new Thumb();
                        thumb.setImage(image1);
                        thumb.setHostId(hostid1);
                        thumb.setName(name1);
                        thumb.setCountryId(cid);
                        thumb.setType(type1);
                        thumb.setCoin(Integer.parseInt(coin));
                        thumb.setToken(token);
                        thumb.setChannel(channel);
                        thumb.setView(Integer.parseInt(view));

                        notificationIntent=new NotificationIntent();
                        notificationIntent.setType(NotificationIntent.LIVE);
                        notificationIntent.setThumb(thumb);
                        isnotification = true;
                        break;
                    default:
                        isnotification = false;
                        break;

                }


            } */
/*
            if (data != null) {
                String cid = String.valueOf(remoteMessage.getData().get("countryid"));
                String thumb = String.valueOf(remoteMessage.getData().get("thumb"));
                String name = String.valueOf(remoteMessage.getData().get("name"));

                Log.d("notificationDatafffff", "Bundle Contains: key=" + cid);
                Log.d("notificationDataffff", "Bundle Contains: key=" + thumb);
                Log.d("notificationDataffff", "Bundle Contains: key=" + name);

               */
/* if (cid != null && !cid.equals("") && !cid.equals("null")) {
                    ThumbRoot.Datum datum = new ThumbRoot.Datum();
                    datum.setImage(thumb);
                    datum.setName(name);


                    intent = new Intent(this, VideoActivity.class).putExtra("cid", cid)
                            .putExtra("model", new Gson().toJson(datum));

                } else {

                }*//*

            }
*/
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


            String channelId = "01";
            if (remoteMessage.getNotification().getImageUrl() != null) {
                String imageUri = remoteMessage.getNotification().getImageUrl().toString();

                Bitmap bitmap = getBitmapfromUrl(imageUri);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(icon)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap))
                        .setColor(getResources().getColor(R.color.purplepink))
                        .setLights(Color.RED, 1000, 300)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.drawable.ic_msg_icon);


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    String name = "Channel_001";
                    String description = "Channel Description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;

                    NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(0, notificationBuilder.build());


            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(icon)
                        .setColor(getResources().getColor(R.color.purplepink))
                        .setLights(Color.RED, 1000, 300)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.drawable.ic_msg_icon);


                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    String name = "Channel_001";
                    String description = "Channel Description";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;

                    NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                    channel.setDescription(description);
                    notificationManager.createNotificationChannel(channel);
                }
                notificationManager.notify(0, notificationBuilder.build());
            }


        }

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            Log.d(TAG, "getBitmapfromUrl: " + imageUrl);
            return bitmap;

        } catch (Exception e) {
            Log.d(TAG, "getBitmapfromUrl: " + e);
            e.printStackTrace();
            return null;

        }
    }
}
