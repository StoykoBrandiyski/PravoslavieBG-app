package com.example.pravoslaviebg.ui.monastery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.MonasteryAdapter;

import java.util.ArrayList;
import java.util.List;

public class MonasteryListFragment extends Fragment {

    private MonasteryAdapter adapter;

    private MonasteryViewModel viewModel;

    private boolean isLoadedAllMonasteries = false;

    public MonasteryListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monastery_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerMonasteries);
        Spinner spinnerLocation = view.findViewById(R.id.spinnerLocation);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(MonasteryViewModel.class);

        viewModel.monasteriesLiveData.observe(getViewLifecycleOwner(), monasteries -> {
            adapter = new MonasteryAdapter(monasteries, prayer -> {
                Bundle bundle = new Bundle();
                bundle.putInt("id", prayer.getId());
                NavHostFragment.findNavController(this).navigate(R.id.action_global_monasterytDetailsFragment, bundle);
            });
            recyclerView.setAdapter(adapter);
        });

        viewModel.fetchMonasteries();
        this.isLoadedAllMonasteries = true;

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int periodFilterValue = spinnerLocation.getSelectedItemPosition();

                if (periodFilterValue == 0) {
                    if (!isLoadedAllMonasteries) {
                        viewModel.fetchMonasteries();
                    }
                    return;
                }

                // Build query params dynamically
                List<String> filters = new ArrayList<>();
                List<String> values = new ArrayList<>();

                String selectedTypeText = spinnerLocation.getSelectedItem().toString();
                filters.add("purpose");
                values.add(selectedTypeText);

                if (!filters.isEmpty()) {
                    viewModel.fetchMonasteriesFiltered(
                            String.join(",", filters), String.join(",", values)
                    );
                    isLoadedAllMonasteries = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerLocation.setOnItemSelectedListener(filterListener);
    }
}