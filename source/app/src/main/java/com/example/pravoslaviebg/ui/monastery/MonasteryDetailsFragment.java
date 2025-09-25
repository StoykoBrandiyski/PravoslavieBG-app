package com.example.pravoslaviebg.ui.monastery;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.ImageGalleryAdapter;
import com.example.pravoslaviebg.styles.HorizontalSpaceItemDecoration;
import com.example.pravoslaviebg.ui.prayer.PrayerViewModel;

public class MonasteryDetailsFragment extends Fragment {

    public MonasteryDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monastery_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameText = view.findViewById(R.id.textMonasteryName);
        TextView descriptionText = view.findViewById(R.id.textMonasteryDesc);
        TextView locationText = view.findViewById(R.id.textMonasteryLocation);

        TextView areaText = view.findViewById(R.id.textMonasteryArea);
        TextView saintNameText = view.findViewById(R.id.textMonasterySaintName);
        TextView typeMonasteryText = view.findViewById(R.id.textMonasteryType);

        ImageView imageView = view.findViewById(R.id.imageMainMonastery);

        int monasteryId = getArguments().getInt("id", -1);
        MonasteryViewModel viewModel = new ViewModelProvider(this).get(MonasteryViewModel.class);

        RecyclerView recyclerGallery = view.findViewById(R.id.recyclerMonasteriesGallery);

        recyclerGallery.setLayoutManager(new GridLayoutManager(getContext(), 2)); // for grid style
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gallery_spacing);
        recyclerGallery.addItemDecoration(new HorizontalSpaceItemDecoration(spacingInPixels));

        viewModel.monasteryDetailsLiveData.observe(getViewLifecycleOwner(), details -> {
            nameText.setText(details.getName());
            locationText.setText(details.getLocation());
            descriptionText.setText(details.getDescription());
            areaText.setText(details.getArea());
            saintNameText.setText(details.getSaintName());
            typeMonasteryText.setText(details.getType());
            Glide.with(requireContext()).load(details.getImageUrls().get(0)).into(imageView);

            recyclerGallery.setAdapter(new ImageGalleryAdapter(details.getImageUrls()));
        });

        viewModel.fetchMonasteryDetails(monasteryId);
    }
}
