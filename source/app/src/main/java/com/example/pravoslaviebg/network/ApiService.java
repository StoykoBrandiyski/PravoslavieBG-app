package com.example.pravoslaviebg.network;

import com.example.pravoslaviebg.models.book.Book;
import com.example.pravoslaviebg.models.game.question.QuestionModel;
import com.example.pravoslaviebg.models.monastery.Monastery;
import com.example.pravoslaviebg.models.Saint;
import com.example.pravoslaviebg.models.SaintDetails;
import com.example.pravoslaviebg.models.User;
import com.example.pravoslaviebg.models.monastery.MonasteryDetails;
import com.example.pravoslaviebg.models.prayer.Prayer;
import com.example.pravoslaviebg.models.prayer.PrayerDetails;
import com.example.pravoslaviebg.models.quote.QuoteDaily;
import com.example.pravoslaviebg.models.saint.SaintDailyMind;
import com.example.pravoslaviebg.models.search.SearchResult;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("Rest/Users/Login")
    Call<String> login(@Body Map<String, String> body);

    @POST("Rest/Users/Register")
    Call<Boolean> register(@Body Map<String, String> body);

    @GET("Rest/Saints")
    Call<List<Saint>> getSaints();

    @GET("Rest/Saints/{id}")
    Call<SaintDetails> getSaintDetails(@Path("id") int id);

    @GET("Rest/Saints")
    Call<List<Saint>> getSaintsByFilters(
            @Query("filters") String filters,
            @Query("values") String values
    );

    @GET("Rest/Users/Me")
    Call<User> getUserDetails();

    @POST("Rest/Users/Edit")
    Call<Boolean> updateUser(@Header("Authorization") String token, @Body User user);

    @GET("Rest/Saints/DailyMind")
    Call<List<SaintDailyMind>> saintDailyMind();

    @GET("Rest/Quotes/Daily")
    Call<QuoteDaily> quoteDaily();

    @GET("Rest/Search")
    Call<SearchResult> search(@Query("querry") String query);

    @GET("Rest/Monasteries/Random")
    Call<List<Monastery>> getRandomMonasteries();

    @GET("Rest/Prayers")
    Call<List<Prayer>> getPrayers();

    @GET("Rest/Prayers/{id}")
    Call<PrayerDetails> getPrayerDetails(@Path("id") int id);

    @GET("Rest/Prayers")
    Call<List<Prayer>> getPrayersByFilters(
            @Query("filters") String filters,
            @Query("values") String values
    );

    @GET("Rest/Monasteries")
    Call<List<Monastery>> getMonasteries();

    @GET("Rest/Monasteries/{id}")
    Call<MonasteryDetails> getMonasteryDetails(@Path("id") int id);

    @GET("Rest/Monasteries")
    Call<List<Monastery>> getMonasteriesByFilters(
            @Query("filters") String filters,
            @Query("values") String values
    );

    @GET("Rest/Library")
    Call<List<Book>> getBooks();

    @GET("/Rest/Questions")
    Call<List<QuestionModel>> getQuestions();
}
