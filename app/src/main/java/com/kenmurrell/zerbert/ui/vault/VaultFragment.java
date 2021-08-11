package com.kenmurrell.zerbert.ui.vault;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.kenmurrell.zerbert.DataUtils;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentVaultBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VaultFragment extends Fragment
{

    private static final String TAG = "SMSReceiver";

    private VaultViewModel vaultViewModel;
    private FragmentVaultBinding binding;


    private Map<String, Integer> items;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        vaultViewModel = new ViewModelProvider(this).get(VaultViewModel.class);
        binding = FragmentVaultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.vaultUnlockTextinput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.vaultStatus.setText("");
                }
            }
        });

        items = new HashMap<String, Integer>()
        {{
            put(getString(R.string.note2_hash), R.string.note2_encrypted_text);
            put(getString(R.string.note3_hash), R.string.note3_encrypted_text);
            put(getString(R.string.note4_hash), R.string.note4_encrypted_text);
            put(getString(R.string.note5_hash), R.string.note5_encrypted_text);
            put(getString(R.string.note6_hash), R.string.note6_encrypted_text);
            put(getString(R.string.note7_hash), R.string.note7_encrypted_text);
            put(getString(R.string.note8_hash), R.string.note8_encrypted_text);
            put(getString(R.string.note9_hash), R.string.note9_encrypted_text);
            put(getString(R.string.note10_hash), R.string.note10_encrypted_text);
        }};

        InputFilter[] editFilters = binding.vaultUnlockTextedit.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        binding.vaultUnlockTextedit.setFilters(newFilters);

        Button unlockButton= root.findViewById(R.id.vault_unlock_button);
        unlockButton.setOnClickListener(this::onUnlockButton);
        return root;
    }

    private void onUnlockButton(View view)
    {
        Editable editableText = Objects.requireNonNull(binding.vaultUnlockTextedit.getText());
        String text = editableText.toString();
        editableText.clear();
        String sha1 = DataUtils.SHA1(text);
        if (items.containsKey(sha1))
        {
            AcceptedPasswordState(text, sha1);
        }
        else
        {
            InvalidPasswordState();
        }
    }

    private void InvalidPasswordState()
    {
        binding.vaultStatus.setText(R.string.vault_status_invalid);
        binding.vaultStatus.setTextColor(Color.RED);
        binding.vaultNoteTitle.setText("");
        binding.vaultNoteArea.setText("");

    }

    private void AcceptedPasswordState(String name, String sha1)
    {
        binding.vaultStatus.setText(R.string.vault_status_accepted);
        binding.vaultStatus.setTextColor(Color.GREEN);
        binding.vaultNoteTitle.setText(name);

        int noteId = items.get(sha1);
        try {
            String text = DataUtils.decryptText(getResources().getString(noteId), name);
            binding.vaultNoteArea.setText(text);
            binding.vaultNoteArea.setMovementMethod(new ScrollingMovementMethod());
        }
        catch (DataUtils.CryptoException e)
        {
            Snackbar.make(binding.getRoot(), "Decryption Error", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Log.e(TAG, "Decryption Error: " + e.getMessage());
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}