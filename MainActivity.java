package com.example.julytimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private TextView secondsLeft;
    public TextView secondsDone;
    private TextView percent;
    private boolean ran;
    private String backgroundcolor, textcolor, buttoncolor;
    private double percentage;
    private int PROGRESS_CURRENT = 0;
    private long x;
    private long y;
    private long completeTime;
    private double z;
    @SuppressLint("SimpleDateFormat")
    public final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private final DateTimeFormatter dr = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final DateTimeFormatter dz = DateTimeFormatter.ofPattern("HH");
    private final DisplayMetrics displayMetrics = new DisplayMetrics();



    public long timestamp(String arg) throws ParseException {
        return df.parse(arg).getTime();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ConstraintLayout homeScreenLayout = findViewById(R.id.Layout1);
        secondsDone = new TextView(this);
        secondsLeft = new TextView(this);
        percent = new TextView(this);
        backgroundcolor = "#000000";
        textcolor = "#000000";
        buttoncolor = "#000000";
        ran = false;

        NotificationChannel channel = new NotificationChannel("35", "channel", NotificationManager.IMPORTANCE_LOW);
        //channel.setSound(null, null);
        channel.setDescription("Die coole progress-Bar");
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "35");
        builder.setContentText("Ghana in progress")
                .setContentTitle("Ghana in progress")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle());
        int PROGRESS_MAX = 28814999;
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        notificationManager.notify(1, builder.build());

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final Timer tm1 = new Timer();
        //Hier wird alles geupdated
        // Stuff that updates the UI
        DecimalFormat dform = new DecimalFormat("#.#######");
        View layout = findViewById(R.id.Layout1);
        TimerTask tmTk1 = new TimerTask() {
            @Override
            public void run() {
                //Hier wird alles geupdated
                LocalDateTime now2 = LocalDateTime.now();
                ZonedDateTime now = now2.atZone(ZoneId.of("Asia/Kolkata"));
                try {
                    x = timestamp(now.format(dr)) - timestamp("2022-08-24 07:30:00.000");
                    y = timestamp("2023-07-23 19:40:00.000") - timestamp(now.format(dr));
                    completeTime = timestamp("2023-07-23 19:40:00.000") - timestamp("2022-08-24 07:30:00.000");
                } catch (ParseException oi) {
                }
                y = y / 1000;
                z = (double) x / (double) (completeTime);
                z = z * 100;
                x = x / 1000;
                String xString = "Sekunden, seit du mich gesehen hast: " + x;
                String yString = "Sekunden, bis du mich wiedersiehst: " + y;
                String zString = dform.format(z) + " Prozent schon geschafft!";
                if ((Integer.parseInt(now.format(dz)) > 19) || (Integer.parseInt(now.format(dz)) < 7)) {
                    backgroundcolor = "#2E2E2E";
                    textcolor = "#C5C5C5";
                    buttoncolor = "#464646";
                } else {
                    backgroundcolor = "#B7C8EA";
                    textcolor = "#3A5A9B";
                    buttoncolor = "#648AD6";
                }
                runOnUiThread(() -> {
                    // Stuff that updates the UI
                    secondsDone.setText(xString);
                    secondsLeft.setText(yString);
                    percent.setText(zString);
                    secondsDone.setPaddingRelative(40, 15, 40, 20);
                    secondsLeft.setPaddingRelative(40, 15, 40, 20);
                    percent.setPaddingRelative(40, 15, 40, 20);
                    secondsDone.setTextSize(12);
                    secondsLeft.setTextSize(12);
                    percent.setTextSize(12);
                    secondsDone.setTypeface(Typeface.MONOSPACE);
                    secondsLeft.setTypeface(Typeface.MONOSPACE);
                    percent.setTypeface(Typeface.MONOSPACE);

                    if ((percentage < Math.floor(z)) && (ran)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Noch ein Prozent geschafft! Ich liebe dich <3", Toast.LENGTH_LONG);
                        toast.show();
                    }
                    percentage = Math.floor(z);

                    ran = true;

                    if(percentage >= 100) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Jaaaaaaa <3", Toast.LENGTH_LONG);
                        toast.show();
                        tm1.cancel();
                        secondsDone.setText("Sekunden, seit du mich gesehen hast: " + completeTime);
                        secondsLeft.setText("Sekunden, bist du mich wiedersiehst: 0");
                        percent.setText("100 Prozent schon geschafft!");
                    }

                    int width = displayMetrics.widthPixels;
                    secondsDone.measure(0, 0);
                    secondsLeft.measure(0, 0);
                    percent.measure(0, 0);
                    secondsDone.setX(width / 2 - secondsDone.getMeasuredWidth() / 2);
                    secondsLeft.setX(width / 2 - secondsLeft.getMeasuredWidth() / 2);
                    percent.setX(width / 2 - percent.getMeasuredWidth() / 2);
                    int height = displayMetrics.heightPixels;
                    secondsDone.setY(height / 2 - secondsDone.getMeasuredHeight() / 2 - height / 4);
                    secondsLeft.setY(height / 2 - secondsLeft.getMeasuredHeight() / 2 - height / 4 + 130);
                    percent.setY(height / 2 - percent.getMeasuredHeight() / 2 - height / 4 + 260);
                    layout.setBackgroundColor(Color.parseColor(backgroundcolor));
                    secondsDone.setTextColor(Color.parseColor(textcolor));
                    secondsLeft.setTextColor(Color.parseColor(textcolor));
                    percent.setTextColor(Color.parseColor(textcolor));
                    secondsDone.setBackgroundColor(Color.parseColor(buttoncolor));
                    secondsLeft.setBackgroundColor(Color.parseColor(buttoncolor));
                    percent.setBackgroundColor(Color.parseColor(buttoncolor));
                });
                PROGRESS_CURRENT = (int) x;
                builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
                builder.setContentText("Sekunden in Ghana noch! " + z + " Prozent schon geschafft!");
                builder.setContentTitle(y + "");
                notificationManager.notify(1, builder.build());
            }
        };
        homeScreenLayout.addView(secondsDone);
        homeScreenLayout.addView(secondsLeft);
        homeScreenLayout.addView(percent);
        tm1.schedule(tmTk1, 0, 100);
    }
}
