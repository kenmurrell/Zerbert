package com.kenmurrell.zerbert.ui.vault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentVaultBinding;

import java.util.HashMap;
import java.util.Map;

public class VaultFragment extends Fragment {

    private VaultViewModel vaultViewModel;
    private FragmentVaultBinding binding;

    private static final Map<String, Integer> items;

    static {
        items = new HashMap<>();
        items.put("test1", R.drawable.pokeball_024px);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vaultViewModel = new ViewModelProvider(this).get(VaultViewModel.class);
        binding = FragmentVaultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button unlockButton= root.findViewById(R.id.vault_unlock_button);
        unlockButton.setOnClickListener(this::onUnlockButton);
        return root;
    }

    private void onUnlockButton(View view)
    {
        String text = binding.vaultUnlockTextfield.getEditText().getText().toString();
        if(!items.containsKey(text))
        {
            binding.vaultNoteImage.setImageResource(R.drawable.icons8_eggplant_96);
        }
        else
        {
            binding.vaultNoteImage.setImageResource(items.get(text));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}