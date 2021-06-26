package com.kenmurrell.zerbert.ui.vault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentVaultBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VaultFragment extends Fragment {

    private VaultViewModel vaultViewModel;
    private FragmentVaultBinding binding;

    private static final Map<String, Integer> items;

    static {
        items = new HashMap<>();
        items.put("default", R.drawable.blank);
        items.put("test1", R.drawable.pokeball_024px);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vaultViewModel = new ViewModelProvider(this).get(VaultViewModel.class);
        binding = FragmentVaultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textVault;
        vaultViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button unlockButton= root.findViewById(R.id.vault_unlock_button);
        unlockButton.setOnClickListener(this::onUnlockButton);
        return root;
    }

    private void onUnlockButton(View view)
    {
        String text = Objects.requireNonNull(binding.vaultUnlockTextfield.getEditText()).getText().toString();

        if (items.containsKey(text))
        {
            binding.vaultNoteImage.setImageResource(items.get(text));
            // show "incorrect password" message
        }
        else
        {
            binding.vaultNoteImage.setImageResource(items.get("default"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}