package com.kenmurrell.zerbert;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity
{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SmsReceiver receiver;
    private EmotionDecoder ed;
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

        ed = new EmotionDecoder();
        ed.register(new Emotion("love", R.drawable.animated_heart).setVerbAndReturn("is in"));
        ed.register(new Emotion("baby", R.drawable.tarzan_baby).setVerbAndReturn("wants a"));
        ed.register(new Emotion("horny", R.drawable.olivia_wilde_horny).setVerbAndReturn("is kinda"));
        ed.register(new Emotion("angry", R.drawable.angry_bird).setVerbAndReturn("is really"));
        ed.register(new Emotion("hungry", R.drawable.hungry_pooh).setVerbAndReturn("is super"));
        ed.register(new Emotion("sad", R.drawable.sad_cat).setVerbAndReturn("is"));

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
        String name = sp.getString("partner_name", "null");
        ed.decode(text).ifPresent(e -> {
            ImageView image = new ImageView(this);
            Glide.with(this).load(e.getGif()).into(image);
            image.setImageResource(e.getGif());
            AlertDialog pop = new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(true)
                    .setTitle(String.format("%s %s %s!", name, e.getVerb(), e.getId()))
                    .setView(image)
                    .setInverseBackgroundForced(true)
                    .setNeutralButton("Close", (dialog, which) -> dialog.dismiss()).create();
            pop.show();
        });
    }

    public void PopUpGif()
    {
        // Will replace this with mapping later
        ImageView image = new ImageView(this);
        Glide.with(this).load(R.drawable.animated_heart).into(image);
        image.setImageResource(R.drawable.animated_heart);
        AlertDialog pop = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(true)
                .setTitle("Someone sent you a heart!")
                .setView(image)
                .setInverseBackgroundForced(true)
                .setNeutralButton("Close", (dialog, which) -> dialog.dismiss()).create();
        pop.show();
    }
}