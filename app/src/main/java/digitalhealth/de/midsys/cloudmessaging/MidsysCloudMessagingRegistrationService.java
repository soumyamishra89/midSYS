package digitalhealth.de.midsys.cloudmessaging;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import digitalhealth.de.midsys.R;

/**
 * Created by Soumya Mishra on 23/05/17.
 * <p>
 * This service helps in creating a registration id for cloud messaging
 */

public class MidsysCloudMessagingRegistrationService extends FirebaseInstanceIdService {

    private final String TAG = MidsysCloudMessagingRegistrationService.class.getName();

    @Override
    public void onTokenRefresh(){
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        saveRegistrationInSharedPreference(refreshedToken);
    }

    /**
     * Persist token to firebase database.
     *
     * @param token The new token.
     */
    private void saveRegistrationInSharedPreference(String token){
        Log.i(TAG, "Firebase messaging token: " + token);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.midSYSToken), token);
        editor.apply();
        saveNotificationToken(token);
    }

    // schedules a job to persist the notification token to server if FB user id is available
    private void saveNotificationToken(String notificationToken){

        new UpdateNotificationTokenToServerTask().execute(notificationToken);

    }

    /**
     * This async task saves the notification token to database on server.
     */
    private class UpdateNotificationTokenToServerTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params){

            String notificationToken = params[0];

            String urlString = getTokenRegistrationUrl();
            JSONObject registrationTokenJson = new JSONObject();
            try {
                // another userid Yatindra
                registrationTokenJson.put("userid", "Sudarson");
                registrationTokenJson.put("regid", notificationToken);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;

            URL url = null;
            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");

                urlConnection.connect();
                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
                outputStream.writeBytes(registrationTokenJson.toString());
                outputStream.flush();
                outputStream.close();
                int responseCode = urlConnection.getResponseCode();
                //if(responseCode == 200){
                Log.i(TAG, "Response Code:  " + responseCode);

                Log.i(TAG, url.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * @return url to which notification registration token is sent to store
         */

        private String getTokenRegistrationUrl(){

            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https");
            uriBuilder.authority("us-central1-practical-brace-164921.cloudfunctions.net");

            uriBuilder.appendPath("registerNotificationToken");

            return uriBuilder.toString();
        }
    }
}
