package com.kenmurrell.zerbert.ui.vault;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VaultViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public VaultViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}