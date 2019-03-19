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
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhepingjiang.navigation.MainActivity;
import com.example.zhepingjiang.navigation.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.SimpleFormatter;

public class NotificationService extends Service {
    private static final String TAG = NotificationService.class.getSimpleName();
    private MessageThread messageThread = null;

    private Intent messageIntent = null;
    private PendingIntent messagePendingIntent = null;

    private int messageNotificationID = 100;
    private Notification messageNotification = null;
    private NotificationManager messageNotificatioManager = null;
    NotificationManagerCompat notificationManagerCompat = null;

    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    public String f_errorResult = null;
    public String f_dbResult = null;
    private boolean isExpired = false;
    private final long EXPIREDTIME_MS = 2*24*3600*1000;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

//        Toast.makeText(this, "Notification!!!", Toast.LENGTH_LONG).show();

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
                    Thread.sleep(10 * 1000);

                    // 1. Check if there is error message from server
                    String serverMessage = getServerMessage();
                    RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
                    String url = "http://ece496puts.ddns.net:59496/get_alert";

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
                        f_errorResult = response;
                        Log.i(TAG, "onResponse: " + f_errorResult);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (f_errorResult != null) {
                                    f_errorResult = f_errorResult.substring(f_errorResult.indexOf("<h1>") + 4, f_errorResult.indexOf("</h1>"));
                                }

                                if (f_errorResult != null && !f_errorResult.equals("")
                                        && !f_errorResult.equals("OK\n")) {
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setContentTitle("New Alert")
                                            .setContentText(f_errorResult)
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            // Set the intent that will fire when the user taps the notification
                                            .setContentIntent(messagePendingIntent)
                                            .setAutoCancel(true);

                                    notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManagerCompat.notify(messageNotificationID, builder.build());
                                    messageNotificationID++;
                                    f_errorResult = null;
                                }
                            }
                        });

                        t.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, error -> {Toast.makeText(getApplicationContext(),
                            "No response from the server\n Please check the network", Toast.LENGTH_LONG).show();});
                    queue.add(stringRequest);


                    // 2. Check if food gets expired
                    String url2 = "http://ece496puts.ddns.net:59496/raw_sql_br/use putsDB;select uid, std_name, expiry_date, status from grocery_storage;";
                    StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, response -> {
                        f_dbResult = response;
                        Log.i(TAG, "onResponses: " + f_dbResult);

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (f_dbResult != null) {
                                    try {
                                        // f_dbResult.indexOf("<h1>");
                                        // f_dbResult.indexOf("</h1>");
                                        f_dbResult = f_dbResult.substring(f_dbResult.indexOf("<h1>") + 4, f_dbResult.indexOf("</h1>"));
                                    } catch (Exception e) {
                                        Log.i(TAG, "EXCEPTION!!: " + e.toString());
                                    }
                                }

                                String[][] parseResult = strParseHelp(f_dbResult);
                                Date curDate = Calendar.getInstance().getTime();
                                long msDiff = 0;

                                for (String[] result : parseResult) {
                                    String expireDateString = result[2];
                                    try {
                                        Date foodExpirationDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(expireDateString);
                                        msDiff = foodExpirationDate.getTime() - curDate.getTime();
                                        if (msDiff <= EXPIREDTIME_MS) {
                                            isExpired = true;

                                            String status = result[3];
                                            if (status.equals("good") && msDiff <= 0) {
                                                String uid = result[0];
                                                RequestQueue queue3 = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
                                                String url3 = "http://ece496puts.ddns.net:59496/raw_sql_br/use putsDB;" +
                                                        "update grocery_storage set status = 'expired' where uid = '" + uid + "'";
                                                StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3, response -> {

                                                }, error -> {

                                                });
                                                queue3.add(stringRequest3);
                                            }
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, e.toString());
                                    }
                                }

                                if (isExpired) {
                                    NotificationCompat.Builder expirationBuilder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setContentTitle("New Alert")
                                            .setContentText("Food is about to expire")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                            // Set the intent that will fire when the user taps the notification
                                            .setContentIntent(messagePendingIntent)
                                            .setAutoCancel(true);

                                    notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
                                    notificationManagerCompat.notify(messageNotificationID, expirationBuilder.build());
                                    messageNotificationID++;
                                    isExpired = false;
                                    f_dbResult = null;
                                }
                            }
                        });

                        t.start();
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }, error -> {Toast.makeText(getApplicationContext(),
                            "No response from the server\n Please check the network", Toast.LENGTH_LONG).show();});
                    queue.add(stringRequest2);


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

        private String[][] strParseHelp(String input) {
            String[] input_str_line = input.split("<br>");


            int input_str_line_size = input_str_line.length;
            String[][] output = new String[input_str_line_size][];

            int i = 0;
            for (String input_str : input_str_line) {
                input_str = input_str.replaceFirst("\\|", "");
                input_str = input_str.replaceAll(" ", "");
                output[i] = input_str.split("\\|");
                i++;
            }

            return output;
        }

    }
}