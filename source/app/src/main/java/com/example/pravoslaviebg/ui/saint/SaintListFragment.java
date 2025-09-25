package com.example.pravoslaviebg.ui.saint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.SaintAdapter;
import com.example.pravoslaviebg.models.Saint;

import java.util.ArrayList;
import java.util.List;

public class SaintListFragment extends Fragment {

    private SaintAdapter adapter;
    private SaintViewModel viewModel;
    private boolean isLoadedAllSaints = false;

    public SaintListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saint_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerSaints);
        Spinner spinnerPeriod = view.findViewById(R.id.spinnerPeriod);
        Spinner spinnerType = view.findViewById(R.id.spinnerType);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(SaintViewModel.class);

        viewModel.saintsLiveData.observe(getViewLifecycleOwner(), saints -> {
            adapter = new SaintAdapter(saints, saint -> {
                Bundle bundle = new Bundle();
                bundle.putInt("saintId", saint.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_global_saintDetailsFragment, bundle);
            });
            recyclerView.setAdapter(adapter);
        });

        viewModel.fetchSaints();
        this.isLoadedAllSaints = true;

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int typeFilterValue = spinnerType.getSelectedItemPosition(); // Adjust mapping if needed
                int periodFilterValue = spinnerPeriod.getSelectedItemPosition();

                if (typeFilterValue == 0 && periodFilterValue == 0) {
                    if (!isLoadedAllSaints) {
                        viewModel.fetchSaints();
                    }
                    return;
                }

                // Build query params dynamically
                List<String> filters = new ArrayList<>();
                List<String> values = new ArrayList<>();

                if (typeFilterValue != 0) {
                    String selectedTypeText = spinnerType.getSelectedItem().toString();
                    filters.add("type");
                    values.add(selectedTypeText);
                }

                if (periodFilterValue != 0) {
                    String selectedTypeText = spinnerPeriod.getSelectedItem().toString();
                    filters.add("period");
                    values.add(selectedTypeText);
                }

                if (!filters.isEmpty()) {
                    viewModel.fetchSaintsFiltered(
                            String.join(",", filters), String.join(",", values)
                    );
                    isLoadedAllSaints = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        spinnerPeriod.setOnItemSelectedListener(filterListener);
        spinnerType.setOnItemSelectedListener(filterListener);
    }
}
