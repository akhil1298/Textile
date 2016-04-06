package fourever.textile.mainclasses;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
public class GcmMessageHandler extends IntentService {

    String mes, role; // getrole;
    private Handler handler;
    public static int count = 1;
    static NotificationCompat.InboxStyle not = null;
    static SharedPreferences prefs;
    static int c = 1;
    private String flag;

    public static final String ACTION_ON_REGISTERED = "eazito.eazitovendor.mainclasses.ON_REGISTERED";
    public static final String ACTION_ON_MESSAGE = "eazito.eazitovendor.mainclasses.ON_MESSAGE";
    String post_id, post_user_id;
    private String name;
    private String to_user_id;

    public GcmMessageHandler() {
        super("GSMMessageHandler");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        prefs = getSharedPreferences("Pref_Count", MODE_PRIVATE);
        c = prefs.getInt("count", c);

        if (c == 0) {
            c = 1;
        }

        prefs = getSharedPreferences("Pref_Count", MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putInt("count", c);
        edit.commit();

        Bundle extras = intent.getExtras();

        // not = new NotificationCompat.InboxStyle();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("msg");
        role = extras.getString("role");
        if (role.toString().equals("like_comment_noti")) {
            post_id = extras.getString("post_id");
            post_user_id = extras.getString("user_id");
            show_likecomment_noti();
        }

        if (role.toString().equals("sendRequest")) {
            show_follow_request_noti();
        }

        if (role.toString().equals("chat")) {
            name = extras.getString("name");
            to_user_id = extras.getString("to_user_id");
            Intent intnt = new Intent(ACTION_ON_MESSAGE);
            sendBroadcast(intnt);
            show_chat_noti();
        }

       /* mes = extras.getString("alert");
        role = extras.getString("role");
        Intent intnt = new Intent(ACTION_ON_MESSAGE);
        sendBroadcast(intnt);
        showMessageNotification();*/

        GcmReceiver.completeWakefulIntent(intent);
    }


    private void show_likecomment_noti() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                show_like_comment_noti(getApplicationContext(), mes, role, post_id, post_user_id);
            }
        });
    }

    private void show_follow_request_noti() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                show_follow_request_noti(getApplicationContext(), mes, role);
            }
        });
    }

    private void show_chat_noti() {
        handler.post(new Runnable() {

            @Override
            public void run() {
                show_chat_notis(getApplicationContext(), to_user_id ,name ,mes, role);
            }
        });
    }

    private static void show_follow_request_noti(Context context, String message, String role) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("role", role);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notification", "noti");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (not == null || not.equals("null")) {
            not = new NotificationCompat.InboxStyle();
        }
        if (c == 1) {
            if (not != null) {
                not = new NotificationCompat.InboxStyle();
            }
        }

        not.setBigContentTitle("TextTile");
        not.addLine(message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context).setContentText(c + " new messages").setStyle(not)
                .setContentTitle(context.getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setContentIntent(pIntent).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        c++;

        prefs = context.getSharedPreferences("Pref_Count", MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putInt("count", c);
        edit.commit();

        NotificationManager manager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        manager.notify(R.string.app_name, builder.build());
        {
        }
    }

    private static void show_like_comment_noti(Context context, String message, String role, String postid, String postuser_id ) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("role", role);
        intent.putExtra("post_id", postid);
        intent.putExtra("post_user_id", postuser_id);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notification", "noti");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (not == null || not.equals("null")) {
            not = new NotificationCompat.InboxStyle();
        }
        if (c == 1) {
            if (not != null) {
                not = new NotificationCompat.InboxStyle();
            }
        }

        not.setBigContentTitle("TextTile");
        not.addLine(message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context).setContentText(c + " new messages").setStyle(not)
                .setContentTitle(context.getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setContentIntent(pIntent).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        c++;

        prefs = context.getSharedPreferences("Pref_Count", MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putInt("count", c);
        edit.commit();

        NotificationManager manager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        manager.notify(R.string.app_name, builder.build());
        {
        }
    }

    private static void show_chat_notis(Context context,String to_uid, String name, String message, String role) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("role", role);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("role", role);
        intent.putExtra("to_uid", to_uid);
        intent.putExtra("name", name);
        intent.putExtra("message", message);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (not == null || not.equals("null")) {
            not = new NotificationCompat.InboxStyle();
        }
        if (c == 1) {
            if (not != null) {
                not = new NotificationCompat.InboxStyle();
            }
        }

        not.setBigContentTitle("TextTile");
        not.addLine(name +" "+message);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context).setContentText(c + " new messages").setStyle(not)
                .setContentTitle(context.getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true)
                .setContentIntent(pIntent).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);
        c++;

        prefs = context.getSharedPreferences("Pref_Count", MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putInt("count", c);
        edit.commit();

        NotificationManager manager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        manager.notify(R.string.app_name, builder.build());
        { }
    }
}
