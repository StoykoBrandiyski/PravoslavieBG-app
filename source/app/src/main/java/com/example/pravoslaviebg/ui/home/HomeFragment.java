package com.example.pravoslaviebg.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.pravoslaviebg.adapters.MonasteryAdapter;
import com.example.pravoslaviebg.adapters.SaintAdapter;
import com.example.pravoslaviebg.databinding.FragmentHomeBinding;
import com.example.pravoslaviebg.ui.saint.SaintViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerRandomMonastery);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        MonasteryAdapter adapter = new MonasteryAdapter(new ArrayList<>(), null);
        recyclerView.setAdapter(adapter);

        homeViewModel.monasteriesLiveData.observe(getViewLifecycleOwner(), adapter::updateData);

        homeViewModel.fetchRandomMonasteries();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}