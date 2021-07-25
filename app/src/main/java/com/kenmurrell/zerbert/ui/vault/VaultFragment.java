package com.kenmurrell.zerbert.ui.vault;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kenmurrell.zerbert.DataUtils;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentVaultBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VaultFragment extends Fragment {

    private VaultViewModel vaultViewModel;
    private FragmentVaultBinding binding;

    private static final Map<String, Integer> items = new HashMap<String, Integer>(){{
        put("805d05d3b2766e6e86862901e1e47e38457bdfb9", R.string.note2); //sha1 = "moby"
    }};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        }catch (DataUtils.CryptoException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}