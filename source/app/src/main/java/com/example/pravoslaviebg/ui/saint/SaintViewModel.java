package com.example.pravoslaviebg.ui.saint;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.SaintDetails;
import com.example.pravoslaviebg.repositories.SaintRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

public class SaintViewModel extends ViewModel {

    private final SaintRepository saintRepository;
    private Saint selectedSaint;
    public MutableLiveData<List<Saint>> saintsLiveData = new MutableLiveData<>();
    public MutableLiveData<SaintDetails> saintDetailsLiveData = new MutableLiveData<>();

    public SaintViewModel() {
        this.saintRepository = new SaintRepository();
    }

    public List<Saint> getSaints() {
        return new ArrayList<Saint>();
    }

    public void selectSaint(Saint saint) {
        selectedSaint = saint;
    }

    public Saint getSelectedSaint() {
        return selectedSaint;
    }

    public void fetchSaints() {
        this.saintRepository.getSaints(saintsLiveData);
    }

    public void fetchSaintDetails(int id) {
        this.saintRepository.getSaintDetails(id, saintDetailsLiveData);
    }

    public void fetchSaintsFiltered(String filters, String values) {
        this.saintRepository.getSaintsByFilter(saintsLiveData, filters, values);
    }
}