package com.example.moviesapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MovieDetailsOnListActivity extends AppCompatActivity {

    Button recommendedButton;

    //Database test
    ImageButton deleteButton;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserID = currentUser.getUid();
    //Database test variables


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_on_list);

        getIncomingIntent();


        deleteButton = findViewById(R.id.delete_icon);

        recommendedButton = findViewById(R.id.recommended_button_on_list);
        recommendedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "";
                String title = "";
                if(getIntent().hasExtra("movie_id")){
                    id = getIntent().getStringExtra("movie_id");
                    title = getIntent().getStringExtra("movie_title");
                } else {
                    Toast.makeText(MovieDetailsOnListActivity.this, "Error. Couldn't fetch Movie ID.", Toast.LENGTH_LONG).show();
                }
                openRecommendedActivity(id, title);
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog();
            }
        });

    }

    private void createAlertDialog() {
        String currentMovieID = getIntent().getStringExtra("movieDbID");
        String movieTitle = getIntent().getStringExtra("movie_title");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete " + movieTitle + " from list?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMovie(currentUserID, currentMovieID);
                        Toast.makeText(MovieDetailsOnListActivity.this, movieTitle + " was deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MovieDetailsOnListActivity.this, movieTitle + " was not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }

    private void deleteMovie(String currentUserID, String currentMovieID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("towatchList/" + currentUserID).child(currentMovieID);
        databaseReference.removeValue();
    }

//    public boolean openDialog(){
//        DeleteDialog deleteDialog = new DeleteDialog();
//        deleteDialog.show(getSupportFragmentManager(), "Delete Dialog");
//        return deleteDialog.decision;
//    }

    public void openRecommendedActivity(String id, String title){
        Intent intent = new Intent(this, RecommendedMoviesActivity.class);
        intent.putExtra("movie_id", id);
        intent.putExtra("movie_title", title);
        startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    private void setData(String movieBackdropURL, String moviePosterURL, String movieTitle, String movieReleaseDate, String movieDescription, String movieRating){
        ImageView backdrop = findViewById(R.id.movie_backdrop_on_list);
        Glide.with(this)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + movieBackdropURL)
                .into(backdrop);

        ImageView poster = findViewById(R.id.movie_poster_on_list);
        Glide.with(this)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w500" + moviePosterURL)
                .into(poster);

        TextView title = findViewById(R.id.movie_title_on_list);
        title.setText(movieTitle);

        TextView releaseDate = findViewById(R.id.movie_release_date_on_list);
        releaseDate.setText(movieReleaseDate);

        TextView description = findViewById(R.id.movie_overview_on_list);
        description.setText(movieDescription);

        TextView rating = findViewById(R.id.movie_rating_on_list);
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
