package com.kenmurrell.zerbert;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    Pattern PHONE_NUMBER_PATTERN = Pattern.compile("[0123456789]{10,12}");

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
        switch (key) {
            case "partner_number":
                String phoneStr = sharedPreferences.getString(key, "");
                boolean valid_number = PHONE_NUMBER_PATTERN.matcher(phoneStr).matches()
                        && PhoneNumberUtils.isGlobalPhoneNumber(phoneStr);
                Snackbar.make(view, valid_number ? "Partner number set!" : "Not a valid phone number!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                sharedPreferences.edit().putBoolean("valid_partner_number", valid_number).apply();
                break;
            case "partner_name":
                boolean valid_name = !sharedPreferences.getString(key, "").isEmpty();
                Snackbar.make(view, valid_name ? "Partner name set!" : "Not a valid partner name!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
                sharedPreferences.edit().putBoolean("valid_partner_name", valid_name).apply();
                break;
            case "show_messages":
                long lastReceived = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                sharedPreferences.edit().putLong("last_received_emotion", lastReceived).apply();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
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
            EditTextPreference phonePref = findPreference("partner_number");
            Objects.requireNonNull(phonePref).setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
                @Override
                public void onBindEditText(@NonNull EditText editText)
                {
                    // sets number-only keypad on this setting
                    editText.setInputType(InputType.TYPE_CLASS_PHONE);
                }
            });
        }
    }
}