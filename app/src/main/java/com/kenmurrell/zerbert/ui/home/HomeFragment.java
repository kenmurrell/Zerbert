package com.kenmurrell.zerbert.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SharedPreferences settings;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        ImageButton loveButton = root.findViewById(R.id.loveButton);
        loveButton.setOnClickListener(this::onLoveButton);
        ImageButton thinkButton = root.findViewById(R.id.thinkButton);
        thinkButton.setOnClickListener(this::onThinkButton);
        ImageButton hornyButton = root.findViewById(R.id.hornyButton);
        hornyButton.setOnClickListener(this::onHornyButton);
        ImageButton angryButton = root.findViewById(R.id.angryButton);
        angryButton.setOnClickListener(this::onAngryButton);

        settings = getActivity().getSharedPreferences("userpreferences",0);

        return root;
    }

    private boolean onLoveButton(View view){
        String number = settings.getString("number", "0");
        boolean r = sendMessage("love!love!love!", number);
        Snackbar.make(view, r? "pass" : "fail", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        return true;
    }

    private boolean onThinkButton(View view){
        String number = settings.getString("number", "0");
        boolean r = sendMessage("think!think!think!", number);
        Snackbar.make(view, r? "pass" : "fail", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        return true;
    }

    private boolean onHornyButton(View view){
        String number = settings.getString("number", "0");
        boolean r = sendMessage("sex!sex!sex!", number);
        Snackbar.make(view, r? "pass" : "fail", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        return true;
    }

    private boolean onAngryButton(View view){
        String number = settings.getString("number", "0");
        boolean r = sendMessage("grrr!grrr!grrr!", number);
        Snackbar.make(view, r? "pass" : "fail", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        return true;
    }

    public static Boolean sendMessage(String msg, String number) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(number)) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number, null, msg, null, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}