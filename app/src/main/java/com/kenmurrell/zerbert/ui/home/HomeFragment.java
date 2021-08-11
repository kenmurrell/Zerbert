package com.kenmurrell.zerbert.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;
import com.kenmurrell.zerbert.EmotionDecoder;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
{

    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton loveButton = root.findViewById(R.id.loveButton);
        loveButton.setOnClickListener(view -> onButton(view, "love"));
        ImageButton thinkButton = root.findViewById(R.id.babyButton);
        thinkButton.setOnClickListener(view -> onButton(view, "baby"));
        ImageButton hornyButton = root.findViewById(R.id.hornyButton);
        hornyButton.setOnClickListener(view -> onButton(view, "horny"));
        ImageButton angryButton = root.findViewById(R.id.angryButton);
        angryButton.setOnClickListener(view -> onButton(view, "angry"));
        ImageButton hungryButton = root.findViewById(R.id.hungryButton);
        hungryButton.setOnClickListener(view -> onButton(view, "hungry"));
        ImageButton sadButton = root.findViewById(R.id.sadButton);
        sadButton.setOnClickListener(view -> onButton(view, "sad"));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        return root;
    }

    private void onButton(View view, String code)
    {
        if(!sharedPreferences.getBoolean("valid_partner_name", false))
        {
            Snackbar.make(view, "Cannot send, partner name not set!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return;
        }
        if(!sharedPreferences.getBoolean("valid_partner_number", false))
        {
            Snackbar.make(view, "Cannot send, partner number is invalid!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
            return;
        }
        try
        {
            String name = sharedPreferences.getString("partner_name", "null");
            String number = sharedPreferences.getString("partner_number", "null");
            SmsManager.getDefault().sendTextMessage(number, null, EmotionDecoder.encode(code), null, null);
            String text = "Sent " + code + " to " + name + "!";
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        catch (UnsupportedOperationException e)
        {
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}