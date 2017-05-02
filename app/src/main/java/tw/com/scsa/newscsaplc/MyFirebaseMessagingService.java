package tw.com.scsa.newscsaplc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Initial_1 on 2016/11/30.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public  final String KEY = "tw.com.scsa.newscsaplc";
    String topics_id;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        SharedPreferences remdname = getApplication().getSharedPreferences(KEY, Context.MODE_PRIVATE);
        topics_id= remdname.getString("topics_id", "");
        FirebaseMessaging.getInstance().subscribeToTopic(topics_id);
        sendNotification(remoteMessage);
            // ...

            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From111: " + remoteMessage.getFrom());
        Log.d(TAG, "From111: " + remoteMessage.getNotification().getBody().toString());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Log.d(TAG, "From111: " + remoteMessage.getNotification().getBody().toString());
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                remoteMessage.getNotification().getIcon();
                //generateNotification(remoteMessage.getNotification(),"我是誰");

            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.

    }
    private void sendNotification(RemoteMessage remoteMessage) {
        //震動設定
        long[] vibratepattern = { 100, 400, 500, 400 };
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= Uri. parse("android.resource://" + getPackageName() + "/" + R.raw.mysound);

//notification 設定
        int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.myicon1 : R.mipmap.myicon1;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(vibratepattern)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
