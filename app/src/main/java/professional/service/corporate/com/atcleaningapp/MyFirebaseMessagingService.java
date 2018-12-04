package professional.service.corporate.com.atcleaningapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Context context = AppController.getAppContext();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);


            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }
        String channelUrl = null;
        if(remoteMessage.getData().get("sendbird")!=null) {
            try {
                JSONObject sendBird = new JSONObject(remoteMessage.getData().get("sendbird"));
                JSONObject channel = (JSONObject) sendBird.get("channel");
                channelUrl = (String) channel.get("channel_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
            sendNotification(this, remoteMessage.getData().get("message"), channelUrl);
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String type = data.getString("type");
            int user_id = data.getInt("user_id");
            int project_id = data.getInt("project_id");


            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            //Adding notification data to the intent
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("title", title);
            pushNotification.putExtra("image", imageUrl);
            pushNotification.putExtra("type", type);
            pushNotification.putExtra("user_id", user_id);
            pushNotification.putExtra("project_id",project_id);
            //creating MyNotificationManager object
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = null;
            //creating an intent for the notification
            if(type.equals("Alert")) {
                intent = new Intent(getApplicationContext(), NavigationBarUser.class);
                intent.putExtra("project_id",project_id);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_cleaners)
                        // .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);



                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Random random = new Random();
                int m = random.nextInt(9999*31 - 1000) + 1000;
                notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
                startActivity(intent);
            }
            if(type.equals("Assigment")) {
                intent = new Intent(getApplicationContext(), AwardedWorksByEmployersToMe.class);
                intent.putExtra("project_id",project_id);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_cleaners)
                        // .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);



                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Random random = new Random();
                int m = random.nextInt(9999*31 - 1000) + 1000;
                notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
                startActivity(intent);
            }
            if(type.equals("Award")) {
                intent = new Intent(getApplicationContext(), EmployerViewForAssignedProject.class);
                intent.putExtra("project_id",project_id);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_cleaners)
                        // .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);



                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Random random = new Random();
                int m = random.nextInt(9999*31 - 1000) + 1000;
                notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
                startActivity(intent);
            }
            if(type.equals("hirer")) {
                intent = new Intent(getApplicationContext(), NavigationBarUser.class);
                intent.putExtra("project_id",project_id);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_cleaners)
                        // .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);



                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Random random = new Random();
                int m = random.nextInt(9999*31 - 1000) + 1000;
                notificationManager.notify(m /* ID of notification */, notificationBuilder.build());
                startActivity(intent);
            }

            if(imageUrl.equals("null")){
                //displaying small notification

                mNotificationManager.showSmallNotification(title, message, intent);
            }else{
                //if there is an image
                //displaying a big notification
                mNotificationManager.showBigNotification(title, message, imageUrl, intent);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

    }
    String channelUrl = null;
    JSONObject data;

    public static void sendNotification(Context context, String messageBody, String channelUrl) {
        Intent intent = new Intent(context, NavigationBarUser.class);
        intent.putExtra("groupChannelUrl", channelUrl);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_cleaners)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentText(messageBody)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
