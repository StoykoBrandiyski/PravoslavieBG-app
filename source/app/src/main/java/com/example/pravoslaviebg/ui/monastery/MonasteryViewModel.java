package com.example.pravoslaviebg.ui.monastery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pravoslaviebg.models.monastery.Monastery;

import com.example.pravoslaviebg.models.monastery.MonasteryDetails;
import com.example.pravoslaviebg.repositories.MonasteryRepository;


import java.util.ArrayList;
import java.util.List;

public class MonasteryViewModel extends ViewModel {

    private final MonasteryRepository monasteryRepository;
    private Monastery selectedMonastery;
    public MutableLiveData<List<Monastery>> monasteriesLiveData = new MutableLiveData<>();
    public MutableLiveData<MonasteryDetails> monasteryDetailsLiveData = new MutableLiveData<>();


    public MonasteryViewModel() {
        this.monasteryRepository = new MonasteryRepository();
    }


    public List<Monastery> getMonasteries() {
        return new ArrayList<>();
    }

    public void selectMonastery(Monastery monastery) {
        selectedMonastery = monastery;
    }

    public Monastery getSelectedMonastery() {
        return selectedMonastery;
    }

    public void fetchMonasteries() {
        this.monasteryRepository.getMonasteries(monasteriesLiveData);
    }

    public void fetchMonasteryDetails(int id) {
        this.monasteryRepository.getMonasteryDetails(id, monasteryDetailsLiveData);
    }

    public void fetchMonasteriesFiltered(String filters, String values) {
        this.monasteryRepository.getMonasteriesByFilters(monasteriesLiveData, filters, values);
    }
}