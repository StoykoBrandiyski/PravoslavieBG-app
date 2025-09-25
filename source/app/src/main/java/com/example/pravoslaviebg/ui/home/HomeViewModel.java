package com.example.pravoslaviebg.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.repositories.MonasteryRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MonasteryRepository monasteryRepository;
    public MutableLiveData<List<Monastery>> monasteriesLiveData = new MutableLiveData<>();

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        this.monasteryRepository = new MonasteryRepository();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void fetchRandomMonasteries() {
        this.monasteryRepository.getRandomMonasteries(monasteriesLiveData);
    }
}