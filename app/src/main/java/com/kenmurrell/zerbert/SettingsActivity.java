package com.kenmurrell.zerbert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    Pattern PHONE_NUMBER_PATTERN = Pattern.compile("[0123456789]{10,11}");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        View view = findViewById(android.R.id.content);
        if(key.equals("partner_number"))
        {
            if(!PHONE_NUMBER_PATTERN.matcher(sharedPreferences.getString(key, "")).matches())
            {
                Snackbar.make(view, "Not a valid phone number!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
            else
            {
                Snackbar.make(view, "Partner number set!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        }
        else if(key.equals("partner_name"))
        {
            if (sharedPreferences.getString(key, "").equals(""))
            {
                Snackbar.make(view, "Partner name should be set!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
            else
            {
                Snackbar.make(view, "Partner name set!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    public static class SettingsFragment extends PreferenceFragmentCompat
    {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
        {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}