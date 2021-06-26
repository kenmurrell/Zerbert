package com.kenmurrell.zerbert.ui.random;

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
import com.kenmurrell.zerbert.databinding.FragmentRandomBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFragment extends Fragment {

    private RandomViewModel randomViewModel;
    private FragmentRandomBinding binding;
    private final Random r;

    private static final List<Integer> pictures = new ArrayList<Integer>(){{
        add(R.drawable.blank);
        add(R.drawable.icons8_love_96);
    }};

    public RandomFragment()
    {
        super();
        r = new Random();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        randomViewModel = new ViewModelProvider(this).get(RandomViewModel.class);
        binding = FragmentRandomBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textRandom;
        randomViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button randomButton = root.findViewById(R.id.pictures_random_button);
        randomButton.setOnClickListener(this::onRandomButton);
        return root;
    }

    private void onRandomButton(View view)
    {
        int i = r.nextInt(pictures.size());
        binding.picturesImage.setImageResource(pictures.get(i));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}