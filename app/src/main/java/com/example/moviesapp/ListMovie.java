package com.example.moviesapp;

public class ListMovie {
    String title;
    String rating;
    String overview;
    String movieID;
    String userID;
    String posterPath;
    String backdropPath;
    String releaseDate;
    String movieDbID;

    public ListMovie() {
    }

    public ListMovie(String title, String rating, String overview, String movieID, String userID, String posterPath, String backdropPath, String releaseDate, String movieDbID) {
        this.title = title;
        this.rating = rating;
        this.overview = overview;
        this.movieID = movieID;
        this.userID = userID;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.movieDbID = movieDbID;
    }

    public String getMovieDbID() {
        return movieDbID;
    }

    public void setMovieDbID(String movieDbID) {
        this.movieDbID = movieDbID;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
