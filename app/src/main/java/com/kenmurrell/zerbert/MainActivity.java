package com.kenmurrell.zerbert;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.kenmurrell.zerbert.databinding.ActivityMainBinding;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SmsReceiver receiver;
    private List<Emotion> emotions;
    private final IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_random, R.id.nav_vault)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        receiver = new SmsReceiver();
        this.registerReceiver(receiver,  filter);

        emotions = new LinkedList<Emotion>(){{
            add(new Emotion.Builder().setId("love").setCxt("%s is really in %s with you!")
                    .addGif(R.drawable.emotion_love_70s)
                    .addGif(R.drawable.emotion_love_bunny)
                    .addGif(R.drawable.emotion_love_disney)
                    .addGif(R.drawable.emotion_love_grey)
                    .addGif(R.drawable.emotion_love_hands)
                    .addGif(R.drawable.emotion_love_hug).build());
            add(new Emotion.Builder().setId("baby").setCxt("%s wants a %s from you...")
                    .addGif(R.drawable.emotion_baby_handsup)
                    .addGif(R.drawable.emotion_baby_mirror)
                    .addGif(R.drawable.emotion_baby_run)
                    .addGif(R.drawable.emotion_baby_sleeping)
                    .addGif(R.drawable.emotion_baby_tarzan).build());
            add(new Emotion.Builder().setId("horny").setCxt("%s is kinda %s for you...")
                    .addGif(R.drawable.emotion_horny_jessica_alba)
                    .addGif(R.drawable.emotion_horny_kisses)
                    .addGif(R.drawable.emotion_horny_neck)
                    .addGif(R.drawable.emotion_horny_olivia_wilde)
                    .addGif(R.drawable.emotion_horny_scratches)
                    .addGif(R.drawable.emotion_horny_vampire_diaries).build());
            add(new Emotion.Builder().setId("angry").setCxt("%s is %s!")
                    .addGif(R.drawable.emotion_angry_bird)
                    .addGif(R.drawable.emotion_angry_dwight)
                    .addGif(R.drawable.emotion_angry_insideout)
                    .addGif(R.drawable.emotion_angry_owl)
                    .addGif(R.drawable.emotion_angry_toddler)
                    .addGif(R.drawable.emotion_angry_tom_jerry).build());
            add(new Emotion.Builder().setId("hungry").setCxt("%s is super %s!")
                    .addGif(R.drawable.emotion_hungry_homer_simpson)
                    .addGif(R.drawable.emotion_hungry_mouse)
                    .addGif(R.drawable.emotion_hungry_pooh)
                    .addGif(R.drawable.emotion_hungry_thinking).build());
            add(new Emotion.Builder().setId("sad").setCxt("%s is a little %s...")
                    .addGif(R.drawable.emotion_sad_cat)
                    .addGif(R.drawable.emotion_sad_drwho)
                    .addGif(R.drawable.emotion_sad_emporer)
                    .addGif(R.drawable.emotion_sad_fetal)
                    .addGif(R.drawable.emotion_sad_michael_scott).build());
        }};

        SmsReceiver.bindListener(this::onReceiveText);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preferences.contains("last_received_emotion"))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("last_received_emotion", 0).apply();
        }
        CheckLastMessages();
    }

    public void CheckLastMessages()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long lastReceivedEmotion = preferences.getLong("last_received_emotion", 0);
        int offlineNum = Integer.parseInt(preferences.getString("messaging_offline", "0"));
        boolean showMessages = preferences.getBoolean("show_messages",false);
        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (c != null && showMessages)
        {
            if (c.moveToFirst())
            {
                for (int j = 0; j < offlineNum; j++)
                {
                    long smsDateNano = c.getLong(c.getColumnIndexOrThrow(Telephony.Sms.DATE));

                    ZoneOffset offset = OffsetDateTime.now().getOffset();
                    LocalDateTime fromText = LocalDateTime.ofEpochSecond(smsDateNano/1000, 0, offset);
                    LocalDateTime fromSett = LocalDateTime.ofEpochSecond(lastReceivedEmotion, 0, ZoneOffset.UTC);;

                    String textNumber = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    textNumber = PhoneNumberUtils.formatNumber(textNumber, "CA");
                    String targetNumber = preferences.getString("partner_number", "0");
                    targetNumber = PhoneNumberUtils.formatNumber(targetNumber, "CA");

                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    int type = c.getInt(c.getColumnIndexOrThrow(Telephony.Sms.TYPE));
                    if(fromText.isAfter(fromSett)
                            && type == Telephony.Sms.MESSAGE_TYPE_INBOX
                            && PhoneNumberUtils.compare(textNumber, targetNumber))
                    {
                        onReceiveText(body);
                    }
                    c.moveToNext();
                }
            }
            c.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume()
    {
        // Register receiver if activity is in front
        this.registerReceiver(receiver, filter);
        CheckLastMessages();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        // Unregister receiver if activity is not in front
        this.unregisterReceiver(receiver);
        super.onPause();
    }

    private void onReceiveText(String text)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sp.getString("partner_name", "Your partner");
        EmotionDecoder.decode(text)
                .flatMap(c -> emotions.stream().filter(e -> e.getId().equals(c)).findFirst())
                .ifPresent(emotion -> {
                    ImageView image = new ImageView(this);
                    Glide.with(this).load(emotion.getGif()).into(image);
                    image.setImageResource(emotion.getGif());
                    AlertDialog pop = new AlertDialog.Builder(MainActivity.this)
                            .setCancelable(true)
                            .setTitle(String.format(emotion.getContext(), name, emotion.getId()))
                            .setView(image)
                            .setInverseBackgroundForced(true)
                            .setNeutralButton("Close", (dialog, which) -> dialog.dismiss()).create();
                    pop.show();

                    SharedPreferences.Editor editor = sp.edit();
                    long lastReceived = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                    editor.putLong("last_received_emotion", lastReceived).apply();
                });
    }
}