package com.example.pravoslaviebg.ui.prayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.PrayerAdapter;
import com.example.pravoslaviebg.adapters.SaintAdapter;
import com.example.pravoslaviebg.databinding.FragmentSlideshowBinding;
import com.example.pravoslaviebg.ui.saint.SaintViewModel;

import java.util.ArrayList;
import java.util.List;

public class PrayerListFragment extends Fragment {


    private PrayerAdapter adapter;
    private PrayerViewModel viewModel;
    private boolean isLoadedAllSaints = false;

    public PrayerListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prayer_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerPrayers);
        Spinner spinnerPurpose = view.findViewById(R.id.spinnerPurpose);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(PrayerViewModel.class);

        viewModel.prayersLiveData.observe(getViewLifecycleOwner(), prayers -> {
            adapter = new PrayerAdapter(prayers, prayer -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", prayer.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_global_prayertDetailsFragment, bundle);
            });
            recyclerView.setAdapter(adapter);
        });

        viewModel.fetchPrayers();
        this.isLoadedAllSaints = true;

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int periodFilterValue = spinnerPurpose.getSelectedItemPosition();

                if (periodFilterValue == 0) {
                    if (!isLoadedAllSaints) {
                        viewModel.fetchPrayers();
                    }
                    return;
                }

                // Build query params dynamically
                List<String> filters = new ArrayList<>();
                List<String> values = new ArrayList<>();

                String selectedTypeText = spinnerPurpose.getSelectedItem().toString();
                filters.add("purpose");
                values.add(selectedTypeText);

                String test = String.join(",", filters);
                String test1 = String.join(",", values);

                if (!filters.isEmpty() && !values.isEmpty()) {
                    viewModel.fetchPrayersFiltered(
                            String.join(",", filters), String.join(",", values)
                    );
                    isLoadedAllSaints = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerPurpose.setOnItemSelectedListener(filterListener);
    }
}