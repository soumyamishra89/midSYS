package digitalhealth.de.midsys.cloudmessaging;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import digitalhealth.de.midsys.MainActivity;
import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 23/05/17.
 */

public class MidsysCloudMessageService extends FirebaseMessagingService {

    private final String TAG = MidsysCloudMessageService.class.getName();

    private static final Random r = new Random();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "Message Notification: " + remoteMessage.getNotification());
        if (remoteMessage.getNotification() !=null) {
            ///Log.d(TAG, "Message Data: " + remoteMessage.getData());
            sendNotification(remoteMessage.getNotification());
        }
    }

    private void sendNotification(RemoteMessage.Notification msg){
        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int notification_id = r.nextInt();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getApplicationContext().getString(R.string.midSYSMsg), msg.getTitle());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.midsys_logo)
                .setStyle(new NotificationCompat.InboxStyle().setBigContentTitle(msg.getBody()).setSummaryText(msg.getTitle()))
                .setContentTitle(msg.getTitle())
                .setContentText(msg.getBody())
                //.setSubText(msg.getTitle())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                .setColor(0x036303)
                .setAutoCancel(true)
                .setLights(0x036303, 500, 500);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notification_id, mBuilder.build());
    }
}
