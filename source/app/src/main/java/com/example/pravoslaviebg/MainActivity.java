package com.example.pravoslaviebg;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pravoslaviebg.adapters.SearchAdapter;
import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.User;
import com.example.pravoslaviebg.models.callbacks.UserDetailsCallback;
import com.example.pravoslaviebg.models.prayer.Prayer;
import com.example.pravoslaviebg.models.search.ResultItem;
import com.example.pravoslaviebg.models.search.SearchResult;
import com.example.pravoslaviebg.network.ApiService;
import com.example.pravoslaviebg.network.RetrofitClient;
import com.example.pravoslaviebg.repositories.UserRepository;
import com.example.pravoslaviebg.services.workers.DailyNotificationWorker;
import com.example.pravoslaviebg.utils.TokenManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.pravoslaviebg.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final UserRepository userRepository;
    private final ApiService apiSerivce;
    private SearchAdapter searchAdapter;
    private NavigationView navigationView;
    private RecyclerView searchResultsRecyclerView;
    private SearchView searchView;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private MutableLiveData<User> detailsLiveData = new MutableLiveData<>();


    public MainActivity() {
        this.apiSerivce = RetrofitClient.getApiService();
        this.userRepository = new UserRepository();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Request Permission in Activity For Notifications (One-Time)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1001); // Request code
            }
        }


        // Force notification when launch app
        WorkRequest testRequest = new OneTimeWorkRequest.Builder(DailyNotificationWorker.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build();

        WorkManager.getInstance(this).enqueue(testRequest);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        searchAdapter = new SearchAdapter(new ArrayList<>(), item -> {
            Bundle bundle = new Bundle();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            searchView.setQuery("", false);
            searchView.clearFocus();
            searchView.setIconified(true);

            searchResultsRecyclerView.setVisibility(View.GONE);

            switch (item.type) {
                case SAINT:
                    bundle.putInt("saintId", item.id);
                    navController.navigate(R.id.action_global_saintDetailsFragment, bundle);
                    break;
                case MONASTERY:
                    bundle.putInt("id", item.id);
                    navController.navigate(R.id.action_global_monasterytDetailsFragment, bundle);
                    break;
                case PRAYER:
                    bundle.putInt("id", item.id);
                    navController.navigate(R.id.action_global_prayertDetailsFragment, bundle);
                    break;
                default:
                    Toast.makeText(this, "Неизвестен тип", Toast.LENGTH_SHORT).show();
                    return;
            }
        });

        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchResultsRecyclerView.setAdapter(searchAdapter);

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.saintListFragment, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headerView = navigationView.getHeaderView(0);
        TextView firstNameTextView = headerView.findViewById(R.id.userFirstName);
        TextView emailTextView = headerView.findViewById(R.id.userEmail);

        this.loadUserData();

        detailsLiveData.observe(this, user -> {
            if (user != null) {
                firstNameTextView.setText(user.getName());
                emailTextView.setText(user.getEmail());
            } else {
                Toast.makeText(
                        MainActivity.this,
                        "User not found",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

    }

    private void loadUserData() {
        userRepository.getUserDetails(TokenManager.getToken(MainActivity.this), new UserDetailsCallback() {
            @Override
            public void onSuccess(User user) {
                detailsLiveData.setValue(user);
            }

            @Override
            public void onAuthError() {
                Toast.makeText(MainActivity.this, "Изисква се повторно влизане", Toast.LENGTH_LONG).show();
                TokenManager.clearToken(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MainActivity.this, "Грешка: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView =  (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 4) {
                    performSearch(query);
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 4) {
                    performSearch(newText);
                    searchResultsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    searchResultsRecyclerView.setVisibility(View.GONE);
                }
                return true;
            }
        });

        return true;
    }

    private void performSearch(String query) {
        this.apiSerivce.search(query).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.isSuccessful()) {
                    displayResults(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Грешка при търсене", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayResults(SearchResult result) {
        List<ResultItem> displayItems = new ArrayList<>();

        for (Saint saint : result.Saints) {
            displayItems.add(new ResultItem(ResultItem.Type.SAINT, saint.Id, saint.getName(), "Светец"));
        }

        for (Monastery monastery : result.Monasteries) {
            displayItems.add(new ResultItem(ResultItem.Type.MONASTERY, monastery.getId(), monastery.getName(), monastery.getLocation()));
        }
        for (Book book : result.Books) {
            displayItems.add(new ResultItem(ResultItem.Type.Book, book.getId(), book.getTitle(), book.getAuthor()));
        }
        for (Prayer prayer : result.Prayers) {
            displayItems.add(new ResultItem(ResultItem.Type.PRAYER, prayer.getId(), prayer.getTitle(), "Molitva"));
        }
        this.searchAdapter.updateResults(displayItems);
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout && !TokenManager.getToken(this).isEmpty()) {
            TokenManager.clearToken(this);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_dashboard) {
            Intent intent = new Intent(this, UserDashboardActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(this, "Launching Quiz...", Toast.LENGTH_SHORT).show();

        if (id == R.id.nav_question_game) {

            Intent intent = new Intent(this, QuestionGameActivity.class);
            startActivity(intent);
            return true;
        }

        // Let the NavController handle other destinations
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}