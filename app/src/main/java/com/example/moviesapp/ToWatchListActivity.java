package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToWatchListActivity extends AppCompatActivity {

    RecyclerView recyclerViewToWatch;
    DatabaseReference database;
    MyAdapterWatchList myAdapterWatchList;
    ArrayList<Movie> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_watch_list);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserID = currentUser.getUid();
        recyclerViewToWatch = findViewById(R.id.recyclerViewWatchList);
        database = FirebaseDatabase.getInstance().getReference("towatchList/" + currentUserID);
        recyclerViewToWatch.setHasFixedSize(true);
        recyclerViewToWatch.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        myAdapterWatchList = new MyAdapterWatchList(this,movieList);
        recyclerViewToWatch.setAdapter(myAdapterWatchList);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movieList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    movieList.add(movie);
                }

                myAdapterWatchList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        setContentView(R.layout.activity_to_watch_list);
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        String currentUserID = currentUser.getUid();
//        recyclerViewToWatch = findViewById(R.id.recyclerViewWatchList);
//        database = FirebaseDatabase.getInstance().getReference("towatchList/" + currentUserID);
//        recyclerViewToWatch.setHasFixedSize(true);
//        recyclerViewToWatch.setLayoutManager(new LinearLayoutManager(this));
//
//        movieList = new ArrayList<>();
//        myAdapterWatchList = new MyAdapterWatchList(this,movieList);
//        recyclerViewToWatch.setAdapter(myAdapterWatchList);
//
//        database.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Movie movie = dataSnapshot.getValue(Movie.class);
//                    movieList.add(movie);
//                }
//
//                myAdapterWatchList.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
}