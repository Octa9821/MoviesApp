package com.example.moviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;



public class MovieDetailsActivity extends AppCompatActivity {

    Button recommendedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getIncomingIntent();

        recommendedButton = findViewById(R.id.recommended_button);
        recommendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "";
                String title = "";
                if(getIntent().hasExtra("movie_id")){
                    id = getIntent().getStringExtra("movie_id");
                    title = getIntent().getStringExtra("movie_title");
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Error. Couldn't fetch Movie ID.", Toast.LENGTH_SHORT).show();
                }
                openRecommendedActivity(id, title);
            }
        });
    }

    public void openRecommendedActivity(String id, String title){
        Intent intent = new Intent(this, RecommendedMoviesActivity.class);
        intent.putExtra("movie_id", id);
        intent.putExtra("movie_title", title);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setData(String movieBackdropURL, String moviePosterURL, String movieTitle, String movieReleaseDate, String movieDescription, String movieRating){
        ImageView backdrop = findViewById(R.id.movie_backdrop);
        Glide.with(this)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + movieBackdropURL)
                .into(backdrop);

        ImageView poster = findViewById(R.id.movie_poster);
        Glide.with(this)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + moviePosterURL)
                .into(poster);

        TextView title = findViewById(R.id.movie_title);
        title.setText(movieTitle);

        TextView releaseDate = findViewById(R.id.movie_release_date);
        releaseDate.setText(movieReleaseDate);

        TextView description = findViewById(R.id.movie_overview);
        description.setText(movieDescription);

        TextView rating = findViewById(R.id.movie_rating);
        rating.setText(movieRating + "✰");
    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("movie_backdrop") && getIntent().hasExtra("movie_poster") && getIntent().hasExtra("movie_title") && getIntent().hasExtra("movie_release_date") && getIntent().hasExtra("movie_overview")){
            String movieBackdropURL = getIntent().getStringExtra("movie_backdrop");
            String moviePosterURL = getIntent().getStringExtra("movie_poster");
            String movieTitle = getIntent().getStringExtra("movie_title");
            String movieReleaseDate = getIntent().getStringExtra("movie_release_date");
            String movieDescription = getIntent().getStringExtra("movie_overview");
            String movieRating = getIntent().getStringExtra("movie_rating");

            setData(movieBackdropURL, moviePosterURL, movieTitle, movieReleaseDate, movieDescription, movieRating);
        } else {
            Toast.makeText(this, "Error, couldn't fetch data.", Toast.LENGTH_SHORT).show();
        }
    }

}
