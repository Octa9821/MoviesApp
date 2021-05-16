package com.example.moviesapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Movie> movies;
    @SerializedName("total_pages")
    private int pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
