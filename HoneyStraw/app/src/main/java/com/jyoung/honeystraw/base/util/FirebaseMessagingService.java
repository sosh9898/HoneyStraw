package com.jyoung.honeystraw.base.util;

/**
 * Created by jyoung on 2017. 8. 18..
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.jyoung.honeystraw.R;
import com.jyoung.honeystraw.ui.tabs.TabActivity;

import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";
    SharedPreferencesService preferencesService = new SharedPreferencesService();

    int badgeCount=0;

    private String msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferencesService.load(getApplicationContext());
        if(preferencesService.getPrefIntegerData("badgeCount") != 0) {
            badgeCount = preferencesService.getPrefIntegerData("badgeCount");
            Log.d("badgesdf", badgeCount+"");
        }
        else
            preferencesService.setPrefData("badgeCount", badgeCount);

        badgeCount++;

        Log.d("badgesdf", badgeCount+"");
        preferencesService.setPrefData("badgeCount", badgeCount);
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        msg = remoteMessage.getData().get("body");


        Intent intent = new Intent(this, TabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.main_title)
                .setContentTitle("꿀빨대")
                .setContentText(msg)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000});

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mBuilder.build());

        mBuilder.setContentIntent(contentIntent);

        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", badgeCount);
        badgeIntent.putExtra("badge_count_package_name", getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(getApplicationContext()));
        sendBroadcast(badgeIntent);
    }

    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

}
