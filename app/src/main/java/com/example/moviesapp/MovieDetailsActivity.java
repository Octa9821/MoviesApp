package com.example.moviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// TODO Create Activity for To Watch List. Add ability to display data from Firebase into these activities.  DONE
// TODO Create separate MovieDetails Activities, one for Logged In, another for Not Logged In, to prevent App Crash when fetching Logged In User ID  DONE
// TODO Implement Add To Watch List Button Function.  DONE

public class MovieDetailsActivity extends AppCompatActivity {

    Button recommendedButton;
    boolean alreadyAdded;
    //Database variables
    ImageButton toWatchButton;
//    DatabaseReference toWatchRef;
    DatabaseReference    toWatchListRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("towatchList");
    ListMovie lMovie;
    FirebaseAuth mFirebaseAuth;
    //Database variables

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getIncomingIntent();

        toWatchButton = findViewById(R.id.add_to_watch_icon);

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
                    Toast.makeText(MovieDetailsActivity.this, "Error. Couldn't fetch Movie ID.", Toast.LENGTH_LONG).show();
                }
                openRecommendedActivity(id, title);
            }
        });

        //Checking if there is anyone logged in
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String currentUserID = user.getUid();
            lMovie = new ListMovie();
            //toWatchRef = database.getReference("towatch");
            toWatchListRef = database.getReference("towatchList").child(currentUserID);  //for storing the checked movies in root based on user's ID

            toWatchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if(getIntent().hasExtra("movie_id")){
                            lMovie.setMovieID(getIntent().getStringExtra("movie_id"));
                            lMovie.setTitle(getIntent().getStringExtra("movie_title"));
                            lMovie.setOverview(getIntent().getStringExtra("movie_overview"));
                            lMovie.setRating(getIntent().getStringExtra("movie_rating"));
                            lMovie.setPosterPath(getIntent().getStringExtra("movie_poster"));
                            lMovie.setBackdropPath(getIntent().getStringExtra("movie_backdrop"));
                            lMovie.setReleaseDate(getIntent().getStringExtra("movie_release_date"));
                            lMovie.setUserID(currentUserID);
                        } else {
                            Toast.makeText(MovieDetailsActivity.this, "Error. Couldn't fetch movie details.", Toast.LENGTH_LONG).show();
                            return;
                        }

                    //DELETE IF BAD
                    alreadyAdded = false;
                    ref.orderByChild("title").equalTo(lMovie.getTitle()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                alreadyAdded = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    if(alreadyAdded){
                        Toast.makeText(MovieDetailsActivity.this, "You already added " + lMovie.getTitle() + " to your list", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //DELETE IF BAD


                    String toWatchID = toWatchListRef.push().getKey();
                        lMovie.setMovieDbID(toWatchID);
                        toWatchButton.setImageResource(R.drawable.ic_heart_60);
                    Toast.makeText(MovieDetailsActivity.this, lMovie.title + " added to Watch List", Toast.LENGTH_SHORT).show();
                    toWatchListRef.child(toWatchID).setValue(lMovie);

                }
            });
        } else {
            Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show();
            toWatchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MovieDetailsActivity.this, "You have to log in to use this functionality", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
