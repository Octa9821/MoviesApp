package com.example.moviesapp;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("title")
    private String title;
    @SerializedName("vote_average")
    private String rating;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("backdrop_path")
    private String backdrop;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }
}
