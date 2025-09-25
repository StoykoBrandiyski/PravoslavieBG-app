package com.example.pravoslaviebg.ui.prayer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pravoslaviebg.models.prayer.Prayer;
import com.example.pravoslaviebg.models.prayer.PrayerDetails;
import com.example.pravoslaviebg.repositories.PrayerRepository;

import java.util.ArrayList;
import java.util.List;

public class PrayerViewModel extends ViewModel {

    private final PrayerRepository prayerRepository;
    private Prayer selectedPrayer;
    public MutableLiveData<List<Prayer>> prayersLiveData = new MutableLiveData<>();
    public MutableLiveData<PrayerDetails> prayerDetailsLiveData = new MutableLiveData<>();


    public PrayerViewModel() {
        this.prayerRepository = new PrayerRepository();
    }


    public List<Prayer> getPrayers() {
        return new ArrayList<>();
    }

    public void selectPrayer(Prayer prayer) {
        selectedPrayer = prayer;
    }

    public Prayer getSelectedPrayer() {
        return selectedPrayer;
    }

    public void fetchPrayers() {
        this.prayerRepository.getPrayers(prayersLiveData);
    }

    public void fetchPrayerDetails(int id) {
        this.prayerRepository.getPrayerDetails(id, prayerDetailsLiveData);
    }

    public void fetchPrayersFiltered(String filters, String values) {
        this.prayerRepository.getPrayersByFilter(prayersLiveData, filters, values);
    }
}