package com.example.pravoslaviebg.ui.prayer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.pravoslaviebg.R;

public class PrayerDetailsFragment extends Fragment {

    public PrayerDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prayer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameText = view.findViewById(R.id.textPrayerTitle);
        TextView descText = view.findViewById(R.id.textPrayerContent);

        TextView purposeText = view.findViewById(R.id.textPurpose);
        TextView saintNameText = view.findViewById(R.id.textSaintName);

        ImageView imageView = view.findViewById(R.id.imagePrayerIcon);

        int prayerId = getArguments().getInt("id", -1);
        PrayerViewModel viewModel = new ViewModelProvider(this).get(PrayerViewModel.class);

        viewModel.prayerDetailsLiveData.observe(getViewLifecycleOwner(), details -> {
            nameText.setText(details.getTitle());
            descText.setText(details.getContent());
            purposeText.setText(details.getPurpose());
            saintNameText.setText(details.getSaint());
            Glide.with(requireContext()).load(details.getImageUrl()).into(imageView);
        });

        viewModel.fetchPrayerDetails(prayerId);
    }
}
