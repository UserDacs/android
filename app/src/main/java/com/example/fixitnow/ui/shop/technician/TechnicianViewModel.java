package com.example.fixitnow.ui.shop.technician;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TechnicianViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TechnicianViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is technician fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}