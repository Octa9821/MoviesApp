package com.example.moviesapp;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private String movieID;

    private String movieDbID;

    public String getMovieDbID() {
        return movieDbID;
    }

    public void setMovieDbID(String movieDbID) {
        this.movieDbID = movieDbID;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }
}
