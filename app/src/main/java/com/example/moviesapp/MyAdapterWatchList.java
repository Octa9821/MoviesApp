package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapterWatchList extends RecyclerView.Adapter<MyAdapterWatchList.MyViewHolder> {

    private Context mContext;
    private List<Movie> mData;

    public MyAdapterWatchList(Context mContext, List<Movie> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        v = inflater.inflate(R.layout.movie_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterWatchList.MyViewHolder holder, int position) {
        holder.id.setText("Rating: " + mData.get(position).getRating() +" ★");
        holder.name.setText(mData.get(position).getTitle());
        holder.description.setText(mData.get(position).getOverview());

        // Implementing Glide to display images
        Glide.with(mContext)
                .load("https://image.tmdb.org/t/p/w500" + mData.get(position).getPosterPath())
                .into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, mData.get(position).getTitle() + " clicked.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MovieDetailsOnListActivity.class);
                intent.putExtra("movie_poster", mData.get(position).getPosterPath());
                intent.putExtra("movie_backdrop", mData.get(position).getBackdropPath());
                intent.putExtra("movie_title", mData.get(position).getTitle());
                intent.putExtra("movie_rating", mData.get(position).getRating());
                intent.putExtra("movie_overview", mData.get(position).getOverview());
                intent.putExtra("movie_release_date", mData.get(position).getReleaseDate());
                intent.putExtra("movie_id", mData.get(position).getMovieID());
                intent.putExtra("movieDbID",mData.get(position).getMovieDbID()); // TODO see if this works
                mContext.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView id, name, description;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.txt_rating);
            name = itemView.findViewById(R.id.txt_title);
            img = itemView.findViewById(R.id.imageView);
            description = itemView.findViewById(R.id.txtDescription);
        }
    }

}
