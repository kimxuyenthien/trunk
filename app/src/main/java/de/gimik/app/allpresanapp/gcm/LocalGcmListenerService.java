package de.gimik.app.allpresanapp.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import de.gimik.app.allpresanapp.MainActivity;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.SplashActivity;
import de.gimik.app.allpresanapp.Utils.Constant;

/**
 * Created by Dang on 29.03.2016.
 */
public class LocalGcmListenerService extends GcmListenerService {
    private static final String TAG = GcmListenerService.class.getSimpleName();

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        super.onSendError(msgId, error);
    }

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param data GCM message received.
     */
    private void sendNotification(Bundle data) {
        String message = data.getString("message");
        String extraData = data.getString("data");
        boolean isAcademyNews = false;
        try {
            JSONObject jsonData = new JSONObject(extraData);
            isAcademyNews = jsonData.getBoolean("academy_news");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "extra data from push: " + extraData);
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String title = getString(R.string.app_name);
        Intent notificationIntent = new Intent(this, SplashActivity.class);

//        Bundle extras = new Bundle();
//        extras.putString(Constant.ExtraKey.MESSAGE, message);
        notificationIntent.putExtra(Constant.ExtraKey.MESSAGE, message);
        notificationIntent.putExtra(Constant.ExtraKey.IS_ACADEMY_NEWS, isAcademyNews);
        notificationIntent.putExtra(Constant.ExtraKey.IS_OPEN_FROM_NOTIFICATION, true);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), uniqueInt,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setContentTitle(title)
                .setWhen(when)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
        Notification notification = builder.build();
        notificationManager.notify(uniqueInt, notification);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPreferences.edit().putInt(Constant.ExtraKey.MESSAGE_COUNT, 1).apply();

        // Notify UI that the app has new push notification, refresh the left menu.
        Intent notificationComplete = new Intent(Constant.ExtraKey.MESSAGE_COUNT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notificationComplete);
    }
}
