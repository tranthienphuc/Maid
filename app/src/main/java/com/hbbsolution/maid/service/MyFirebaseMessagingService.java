package com.hbbsolution.maid.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hbbsolution.maid.R;
import com.hbbsolution.maid.history.activity.HistoryActivity;
import com.hbbsolution.maid.home.job_near_by_new_version.view.JobNearByNewActivity;
import com.hbbsolution.maid.utils.SessionShortcutBadger;
import com.hbbsolution.maid.workmanager.listworkmanager.view.WorkManagerActivity;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by buivu on 28/10/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils notificationUtils;
    private int countNotification;
    private SessionShortcutBadger sessionShortcutBadger;
    private String title,body;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        // [START_EXCLUDE]
//        // There are two types of messages data messages and notification messages. Data messages are handled
//        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
//        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
//        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
//        // When the user taps on the notification they are returned to the app. Messages containing both notification
//        // and data payloads are treated as notification messages. The Firebase console always sends notification
//        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
//        // [END_EXCLUDE]
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            handleNotification(remoteMessage);
        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        sendNotification(remoteMessage.getNotification().getBody());
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.

//        Log.e(TAG, "From: " + remoteMessage.getFrom());
//
//        if (remoteMessage == null)
//            return;
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification());
//            //sendNotification(remoteMessage.getNotification().getBody());
//        }
    }


    /*
    status  = 6: gửi yêu cầu trực tiếp
     */
    private void handleNotification(RemoteMessage remoteMessage) {
        sessionShortcutBadger = new SessionShortcutBadger(getApplicationContext());
        String status = "";
        status = remoteMessage.getData().get("status");
        pushNotification(remoteMessage);
    }

    private void pushNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        title = data.get("title");
        body = data.get("body");
        PendingIntent pendingIntent = null;
        switch (data.get("status")) {
            case "6": {
                Intent intent = new Intent(this, WorkManagerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_6);
                break;
            }
            case "5": {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("tabMore", 0);
                intent.putExtra("flag", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_5);
                break;
            }
            case "9": {
                Intent intent = new Intent(this, HistoryActivity.class);
                intent.putExtra("tabMore", 0);
                intent.putExtra("flag", 2);
                intent.putExtra("bill", data.get("bill"));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_9);
                break;
            }
            case "1": {
                Intent intent = new Intent(this, WorkManagerActivity.class);
                EventBus.getDefault().postSticky("1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_1);
                break;
            }
            case "8": {
                Intent intent = new Intent(this, WorkManagerActivity.class);
                intent.putExtra("tabMore", 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_8);
                break;
            }
            case "99": {
                Intent intent = new Intent(this, JobNearByNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_99);
                break;
            }
            case "89": {
                Intent intent = new Intent(this, WorkManagerActivity.class);
                intent.putExtra("tabMore", 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                title = getResources().getString(R.string.notification);
                body = getResources().getString(R.string.status_89);
                break;
            }
            default: {
                break;
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setLights(0xff00ff00, 300, 100)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        builder.setSmallIcon(getNotificationIcon(builder));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        // play notification sound
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
        if(!sessionShortcutBadger.isCounted()) {
            sessionShortcutBadger.createCountSession(0);
        }
        countNotification = sessionShortcutBadger.getCount();
        countNotification++;
        ShortcutBadger.applyCount(getApplicationContext(), countNotification);
        sessionShortcutBadger.setCount(countNotification);
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x008000;
            notificationBuilder.setColor(color);
            return com.hbbsolution.maid.R.mipmap.ic_launcher;

        } else {
            return com.hbbsolution.maid.R.mipmap.ic_launcher;
        }
    }

    private boolean isAtActivity(String activity) {
        boolean isAtActivity = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        String current = taskInfo.get(0).topActivity.getShortClassName();
        boolean result = current.contains(activity);
        if (result) {
            isAtActivity = true;
        }
        return isAtActivity;
    }

}
