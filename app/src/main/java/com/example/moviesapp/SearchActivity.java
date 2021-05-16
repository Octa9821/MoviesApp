package com.example.moviesapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    String searchText = "";
    List<Movie> searchList;
    RecyclerView recyclerViewSearch;
    TextView txtTitleSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchList = new ArrayList<Movie>();
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        txtTitleSearch = findViewById(R.id.txt_title_search);

        if(getIntent().hasExtra("search_text")){
            searchText = getIntent().getStringExtra("search_text");
            txtTitleSearch.setText("Your Movie Search Based on: '" + searchText + "'");

            Retrofit retrofitSearch = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/search/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


            TmdbAPI tmdbAPIsearch = retrofitSearch.create(TmdbAPI.class);

            Call<GetMovieResponse> callSearch = tmdbAPIsearch.getMoviesSearch(searchText);
            callSearch.enqueue(new Callback<GetMovieResponse>() {
                @Override
                public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                    if(response.isSuccessful()){
                        GetMovieResponse responseBody = response.body();
                        if(responseBody != null){
                            searchList = responseBody.getMovies();
                            PutDataIntoRecyclerViewSearch(searchList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                    Toast.makeText(SearchActivity.this, "Error onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(SearchActivity.this, "Error. Couldn't search text.", Toast.LENGTH_SHORT).show();
        }
    }

    private void PutDataIntoRecyclerViewSearch(List<Movie> movieList){
        MyAdapter myAdapter = new MyAdapter(this, movieList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSearch.setAdapter(myAdapter);
    }
}

