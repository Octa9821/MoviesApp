package com.example.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NowPlayingMoviesActivity extends AppCompatActivity {

    List<Movie> nowPlayingList;
    RecyclerView recyclerViewNowPlaying;
    FloatingActionButton directionsFAB;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_movies);

        nowPlayingList = new ArrayList<Movie>();
        recyclerViewNowPlaying = findViewById(R.id.recyclerViewNowPlaying);
        directionsFAB = findViewById(R.id.directions_floating_button);

        Retrofit retrofitNowPlaying = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TmdbAPI tmdbAPInowplaying = retrofitNowPlaying.create(TmdbAPI.class);

        Call<GetMovieResponse> callNowPlaying = tmdbAPInowplaying.getMoviesNowPlaying();
        callNowPlaying.enqueue(new Callback<GetMovieResponse>() {
            @Override
            public void onResponse(Call<GetMovieResponse> call, Response<GetMovieResponse> response) {
                if (response.isSuccessful()) {
                    GetMovieResponse responseBody = response.body();
                    if (responseBody != null) {
                        nowPlayingList = responseBody.getMovies();
                        PutDataIntoRecyclerViewUpcoming(nowPlayingList);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMovieResponse> call, Throwable t) {
                Toast.makeText(NowPlayingMoviesActivity.this, "Error onFailure: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        directionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NowPlayingMoviesActivity.this, "Directions Button Clicked", Toast.LENGTH_SHORT).show();
                openCinemaFinderActivity();  //TODO maybe autosearch for cinemas
            }
        });
    }

    private void PutDataIntoRecyclerViewUpcoming(List<Movie> movieList){
        MyAdapter myAdapter = new MyAdapter(this, movieList);
        recyclerViewNowPlaying.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNowPlaying.setAdapter(myAdapter);
    }

    public void openCinemaFinderActivity(){
        Intent intent = new Intent(this, CinemaFinderActivity.class);
        startActivity(intent);
    }
}
