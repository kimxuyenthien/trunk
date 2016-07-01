package de.gimik.app.allpresanapp.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.JsonObject;
import de.gimik.app.allpresanapp.R;
import de.gimik.app.allpresanapp.Utils.Constant;
import de.gimik.app.allpresanapp.networking.AllpresansAPI;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.io.IOException;

/**
 * Created by Dang on 29.03.2016.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.d(TAG, "GCM Registration Token: " + token);
            if (!token.equals(sharedPreferences.getString(Constant.GcmPreferences.SENT_TOKEN_TO_SERVER, "")))
                sendRegistrationToServer(token);
            else {
                Log.d(TAG, "already registred");
            }

            // Subscribe to topic channels
            //subscribeTopics(token);

            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constant.GcmPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        AllpresansAPI.getService().pushService(2, token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // on success
                        new Action1<JsonObject>() {
                            @Override
                            public void call(JsonObject jsonElement) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);
                                Log.i(TAG, "Save token to allpresan server success: " + token);
                                Log.d(TAG, jsonElement + "");
                                // You should store a boolean that indicates whether the generated token has been
                                // sent to your server. If the boolean is false, send the token to your server,
                                // otherwise your server should have already received the token.
                                sharedPreferences.edit().putString(Constant.GcmPreferences.SENT_TOKEN_TO_SERVER, token).apply();
                            }
                        },

                        // on error
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);
                                Log.i(TAG, "Save token to allpresan server error: " + token);
                                throwable.printStackTrace();
                                sharedPreferences.edit().putString(Constant.GcmPreferences.SENT_TOKEN_TO_SERVER, "").apply();
                            }
                        },

                        // on completed
                        new Action0() {
                            @Override
                            public void call() {
                            }
                        }
                );
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
