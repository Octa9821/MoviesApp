package com.example.moviesapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecommendedMoviesActivity extends AppCompatActivity {

    List<Movie> recommendedList;
    RecyclerView recyclerViewRecommended;
    TextView recommendedTxt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_movies);

        String id = "";
        String title = "";
        //Toast.makeText(this, "ID is: " + id, Toast.LENGTH_LONG).show();
        recommendedList = new ArrayList<Movie>();
        recyclerViewRecommended = findViewById(R.id.recyclerViewRecommended);
        recommendedTxt = findViewById(R.id.recommended_txt);

        if(getIntent().hasExtra("movie_id")){
            id = getIntent().getStringExtra("movie_id");
            title = getIntent().getStringExtra("movie_title");
            recommendedTxt.setText("Recommended movies based on: " + title);

            Retrofit retrofitRecommended = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/movie/"+id+"/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            TmdbAPI tmdbAPIrecommended = retrofitRecommended.create(TmdbAPI.class);

            Call<GetMovieResponse> callRecommended = tmdbAPIrecommended.getMoviesRecommended();
            callRecommended.enqueue(new Callback<GetMovieResponse>() {
                @Override
                public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                    if(response.isSuccessful()){
                        GetMovieResponse responseBody = response.body();
                        if(responseBody != null){
                            recommendedList = responseBody.getMovies();
                            PutDataIntoRecyclerViewRecommended(recommendedList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                    Toast.makeText(RecommendedMoviesActivity.this, "Error onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(RecommendedMoviesActivity.this, "Error. Couldn't fetch Movie ID.", Toast.LENGTH_SHORT).show();
        }
    }

    private void PutDataIntoRecyclerViewRecommended(List<Movie> movieList){
        MyAdapter myAdapter = new MyAdapter(this, movieList);
        recyclerViewRecommended.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRecommended.setAdapter(myAdapter);
    }
}
