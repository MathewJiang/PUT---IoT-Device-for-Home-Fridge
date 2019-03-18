package com.example.zhepingjiang.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.zhepingjiang.navigation.MainActivity;
import com.example.zhepingjiang.navigation.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {

//    Handler handler = new Handler(Looper.getMainLooper());
//
//    public NotificationService() {
//        super("NotificationService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(NotificationService.this, "TOAST!!!", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private Timer timer = new Timer();
//
//
//    @Override
//    public IBinder onBind(Intent intent)
//    {
//        return null;
//    }
//
//    @Override
//    public void onCreate()
//    {
//        super.onCreate();
//
////        timer.scheduleAtFixedRate(new TimerTask() {
////            @Override
////            public void run() {
////                // sendRequestToServer();   //Your code here
////                // Notification tryout
//////                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//////                String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//////
//////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//////                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
//////
//////                    // Configure the notification channel.
//////                    notificationChannel.setDescription("Channel description");
//////                    notificationChannel.enableLights(true);
//////                    notificationChannel.setLightColor(Color.RED);
//////                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//////                    notificationChannel.enableVibration(true);
//////                    notificationManager.createNotificationChannel(notificationChannel);
//////                }
//////
//////
//////                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//////
//////                notificationBuilder.setAutoCancel(true)
//////                        .setDefaults(Notification.DEFAULT_ALL)
//////                        .setWhen(System.currentTimeMillis())
//////                        .setSmallIcon(android.R.drawable.stat_notify_error)
//////                        .setTicker("Hearty365")
//////                        //     .setPriority(Notification.PRIORITY_MAX)
//////                        .setContentTitle("Default notification")
//////                        .setContentText("This is a default notification.")
//////                        .setContentInfo("Info");
//////
//////                notificationManager.notify(/*notification id*/1, notificationBuilder.build());
////
////            }
////        }, 0, 1000); // 1 sec
//    }
//
//    @Override
//    public void onDestroy()
//    {
//        super.onDestroy();
//    }

    private MessageThread messageThread = null;

    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    private int messageNotificationID = 100;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;
    NotificationManagerCompat notificationManagerCompat = null;

    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        Toast.makeText(this, "Notification!!!", Toast.LENGTH_LONG).show();

//        messageNotification = new Notification();
//        messageNotification.icon = R.drawable.ic_launcher_background;
//        messageNotification.tickerText = "New Alert";
//        messageNotification.defaults = Notification.DEFAULT_SOUND;
//        messageNotificatioManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//
//        messageIntent = new Intent(this, MainActivity.class);
//        messagePendingIntent = PendingIntent.getActivity(this,0, messageIntent,0);
//


//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("Expiration Warning!")
//                .setContentText("Food is about to expire!")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create an explicit intent for an Activity in your app
        // Intent i = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        messagePendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        messageThread = new MessageThread();
        messageThread.isRunning = true;
        messageThread.start();

        return super.onStartCommand(intent, flags, startID);
    }

    @Override
    public void onDestroy(){
        System.exit(0);
        super.onDestroy();
    }



    public class MessageThread extends Thread {
        public boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(10*1000);

                    String serverMessage = getServerMessage();
                    if (serverMessage != null && !serverMessage.equals("")) {
                        // messageNotification.setLatestEventInfo(MessageService.this,"新消息","奥巴马宣布,本拉登兄弟挂了!"+serverMessage,messagePendingIntent);
                        // messageNotificatioManager.notify(messageNotificationID, messageNotification);
//                        messageNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//                        messageNotificatioManager.notify(messageNotificationID, messageNotification);
//                        messageNotificationID++;
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("My notification")
                                .setContentText("Hello World!")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(messagePendingIntent)
                                .setAutoCancel(true);

                        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                        notificationManagerCompat.notify(messageNotificationID, builder.build());
                        messageNotificationID++;
                    }
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }

        public String getServerMessage() {
            return "temp server Message";
        }

        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = getString(R.string.app_name) + ": Channel Name";
                String description = getString(R.string.app_name) + ": Channel Description";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

    }
}