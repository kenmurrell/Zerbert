package com.kenmurrell.zerbert.ui.random;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.kenmurrell.zerbert.R;
import com.kenmurrell.zerbert.databinding.FragmentRandomBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomFragment extends Fragment {

    private RandomViewModel randomViewModel;
    private FragmentRandomBinding binding;
    private final Random r;
    private final List<Integer> history;
    private final int maxHistorySize;

    private static final String TAG = "RandomFragment";
    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy");

    private static final List<Pair<Integer, LocalDate>> pictures = new ArrayList<Pair<Integer, LocalDate>>(){{
        add(Pair.create(R.drawable._0201002_161148, LocalDate.of(2020, 10, 2)));
        add(Pair.create(R.drawable._0201011_113255, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201011_153452, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201011_153456, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201011_185503, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201011_190047, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201011_212309, LocalDate.of(2020, 10, 11)));
        add(Pair.create(R.drawable._0201024_132918, LocalDate.of(2020, 10, 24)));
        add(Pair.create(R.drawable._0201024_132922, LocalDate.of(2020, 10, 24)));
        add(Pair.create(R.drawable._0201114_150449, LocalDate.of(2020, 11, 14)));
        add(Pair.create(R.drawable._0201114_150451, LocalDate.of(2020, 11, 14)));
        add(Pair.create(R.drawable._0201211_160214, LocalDate.of(2020, 12, 11)));
        add(Pair.create(R.drawable._0201211_160224, LocalDate.of(2020, 12, 11)));
        add(Pair.create(R.drawable._0201211_160231, LocalDate.of(2020, 12, 11)));
        add(Pair.create(R.drawable._0201217_220948, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221032, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221035, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221037, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221044, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221102, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201217_221109, LocalDate.of(2020, 12, 17)));
        add(Pair.create(R.drawable._0201223_200648, LocalDate.of(2020, 12, 23)));
        add(Pair.create(R.drawable._0201223_200700, LocalDate.of(2020, 12, 23)));
        add(Pair.create(R.drawable._0201227_171730, LocalDate.of(2020, 12, 27)));
        add(Pair.create(R.drawable._0201227_171740, LocalDate.of(2020, 12, 27)));
        add(Pair.create(R.drawable._0201211_160214, LocalDate.of(2020, 12, 11)));
        add(Pair.create(R.drawable._0210104_171638, LocalDate.of(2021, 1, 4)));
        add(Pair.create(R.drawable._0210104_171644, LocalDate.of(2021, 1, 4)));
        add(Pair.create(R.drawable._0210104_171647, LocalDate.of(2021, 1, 4)));
        add(Pair.create(R.drawable._0210104_171700, LocalDate.of(2021, 1, 4)));
        add(Pair.create(R.drawable._0210214_180700, LocalDate.of(2021, 2, 14)));
        add(Pair.create(R.drawable._0210326_205259, LocalDate.of(2021, 3, 26)));
        add(Pair.create(R.drawable._0210326_205344, LocalDate.of(2021, 3, 26)));
        add(Pair.create(R.drawable._0210326_205358, LocalDate.of(2021, 3, 26)));
        add(Pair.create(R.drawable._0210519_0000, LocalDate.of(2021, 5, 19)));
        add(Pair.create(R.drawable._0210523_0000, LocalDate.of(2021, 5, 23)));
        add(Pair.create(R.drawable._0210530_0000, LocalDate.of(2021, 5, 30)));
        add(Pair.create(R.drawable._0210626_140434, LocalDate.of(2021, 6, 26)));
        add(Pair.create(R.drawable._0210626_141246, LocalDate.of(2021, 6, 26)));
        add(Pair.create(R.drawable._0210626_141301, LocalDate.of(2021, 6, 26)));
        add(Pair.create(R.drawable._0210626_141306, LocalDate.of(2021, 6, 26)));
        add(Pair.create(R.drawable._0210626_141308, LocalDate.of(2021, 6, 26)));
        add(Pair.create(R.drawable._0210717_121101, LocalDate.of(2021, 7, 17)));
        add(Pair.create(R.drawable._0210814_001, LocalDate.of(2021, 8, 14)));
        add(Pair.create(R.drawable._0210814_002, LocalDate.of(2021, 8, 14)));
        add(Pair.create(R.drawable._0210814_003, LocalDate.of(2021, 8, 14)));
    }};

    public RandomFragment()
    {
        super();
        r = new Random();
        maxHistorySize = Math.floorDiv(pictures.size(), 2);
        history = new LinkedList<>();
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
        Log.i(TAG, "Fetching random picture from resources.");
        int i;
        do {
            i = r.nextInt(pictures.size());
        } while (isRecent(i) || !resourceExists(i));

        if(history.size() == maxHistorySize)
        {
            history.remove(0);
        }
        history.add(i);
        int resId = pictures.get(i).first;
        String date = pictures.get(i).second.format(fmt);
        ImageView image = binding.picturesImage;
        Log.i(TAG, String.format("Resource {%d} selected.", resId));
        Glide.with(this).load(resId).into(image);
        binding.textRandom.setText(date);
    }

    private boolean isRecent(int i)
    {
        return !history.isEmpty() && history.contains(i);
    }

    private boolean resourceExists(int i)
    {
        return getResources().getResourceName(pictures.get(i).first) != null;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}