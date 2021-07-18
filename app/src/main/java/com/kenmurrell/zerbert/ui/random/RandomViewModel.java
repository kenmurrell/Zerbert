package com.kenmurrell.zerbert.ui.random;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RandomViewModel extends ViewModel
{

    private MutableLiveData<String> mText;

    public RandomViewModel()
    {
        mText = new MutableLiveData<>();
        mText.setValue("Press the next button!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}