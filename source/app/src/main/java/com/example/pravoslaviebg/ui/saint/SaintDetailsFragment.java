package com.example.pravoslaviebg.ui.saint;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pravoslaviebg.adapters.BookAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.ImageGalleryAdapter;
import com.example.pravoslaviebg.adapters.MonasteryAdapter;
import com.example.pravoslaviebg.adapters.PrayerAdapter;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.styles.HorizontalSpaceItemDecoration;

public class SaintDetailsFragment extends Fragment {

    private TextView nameText;
    private TextView descText;
    private ImageView imageView;

    public SaintDetailsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saint_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameText = view.findViewById(R.id.textSaintName);
        TextView descText = view.findViewById(R.id.textSaintDesc);

        TextView celebrationDateText = view.findViewById(R.id.textCelebrationDate);
        TextView helpForText = view.findViewById(R.id.textHelpFor);
        TextView lifePeriodText = view.findViewById(R.id.textLifePeriod);
        TextView typeText = view.findViewById(R.id.textType);

        ImageView imageView = view.findViewById(R.id.imageSaintIcon);

        int saintId = getArguments().getInt("saintId", -1);
        SaintViewModel viewModel = new ViewModelProvider(this).get(SaintViewModel.class);

        RecyclerView recyclerGallery = view.findViewById(R.id.recyclerGallery);
        RecyclerView recyclerPrayers = view.findViewById(R.id.recyclerPrayers);
        RecyclerView recyclerBooks = view.findViewById(R.id.recyclerBooks);
        RecyclerView recyclerMonasteries = view.findViewById(R.id.recyclerMonasteries);

        recyclerGallery.setLayoutManager(new GridLayoutManager(getContext(), 2)); // for grid style
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.gallery_spacing);
        recyclerGallery.addItemDecoration(new HorizontalSpaceItemDecoration(spacingInPixels));

        recyclerPrayers.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBooks.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMonasteries.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.saintDetailsLiveData.observe(getViewLifecycleOwner(), details -> {
            nameText.setText(details.getName());
            descText.setText(details.getDescription());
            celebrationDateText.setText(details.getCelebrationDate());
            helpForText.setText(details.getHelpFor());
            lifePeriodText.setText(details.getLifePeriod());
            typeText.setText(details.getType());
            Glide.with(requireContext()).load(details.getImageUrls().get(0)).into(imageView);

            recyclerGallery.setAdapter(new ImageGalleryAdapter(details.getImageUrls()));
            recyclerPrayers.setAdapter(new PrayerAdapter(details.getPrayers(), null));
            recyclerBooks.setAdapter(new BookAdapter(details.getBooks()));
            recyclerMonasteries.setAdapter(new MonasteryAdapter(details.getMonasteries(), null));
        });

        viewModel.fetchSaintDetails(saintId);
    }

}
