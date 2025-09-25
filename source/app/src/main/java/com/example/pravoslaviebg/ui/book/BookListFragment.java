package com.example.pravoslaviebg.ui.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pravoslaviebg.R;
import com.example.pravoslaviebg.adapters.BookAdapter;

public class BookListFragment extends Fragment {

    private BookAdapter adapter;

    public BookListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerBooks);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BookViewModel viewModel = new ViewModelProvider(this).get(BookViewModel.class);

        viewModel.booksLiveData.observe(getViewLifecycleOwner(), prayers -> {
            adapter = new BookAdapter(prayers);
            recyclerView.setAdapter(adapter);
        });

        viewModel.fetchBooks();
    }
}