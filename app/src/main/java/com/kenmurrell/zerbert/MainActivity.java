package com.kenmurrell.zerbert;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.kenmurrell.zerbert.databinding.ActivityMainBinding;
import com.kenmurrell.zerbert.ui.SmsListener;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SmsReceiver receiver;
    private final IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpGif();
                Snackbar
                        .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_random, R.id.nav_vault)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //temporary until i get this setup
        SharedPreferences.Editor editor  = getSharedPreferences("userpreferences", 0).edit();
        editor.putString("number", "6138858659");
        editor.putString("name", "tester");
        editor.commit();

        receiver = new SmsReceiver();
        this.registerReceiver(receiver,  filter);

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                PopUpGif();
            }
        });


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        // Register receiver if activity is in front
        this.registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // Unregister receiver if activity is not in front
        this.unregisterReceiver(receiver);
        super.onPause();
    }

    public void PopUpGif()
    {
        ImageView image = new ImageView(this);
        Glide.with(this).load(R.drawable.animated_heart).into(image);
        image.setImageResource(R.drawable.animated_heart);
        AlertDialog pop = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(true)
                .setTitle("Someone sent you a heart!")
                .setView(image)
                .setInverseBackgroundForced(true)
                .setNeutralButton("Close",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                }).create();
        pop.show();
    }
}