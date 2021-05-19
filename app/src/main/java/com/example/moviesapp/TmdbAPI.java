package com.example.moviesapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TmdbAPI {

    @GET("popular?api_key=b46c711d586cb64dadb67448afdb0919")
    Call<GetMovieResponse> getMoviesPopular();

    @GET("top_rated?api_key=b46c711d586cb64dadb67448afdb0919")
    Call<GetMovieResponse> getMoviesTopRated();

    @GET("upcoming?api_key=b46c711d586cb64dadb67448afdb0919")
    Call<GetMovieResponse> getMoviesUpcoming();

    @GET("similar?api_key=b46c711d586cb64dadb67448afdb0919")
    Call<GetMovieResponse> getMoviesRecommended();

    @GET("movie?api_key=b46c711d586cb64dadb67448afdb0919&language=en-US")
    Call<GetMovieResponse> getMoviesSearch(@Query("query") String query);

    @GET("now_playing?api_key=b46c711d586cb64dadb67448afdb0919")
    Call<GetMovieResponse> getMoviesNowPlaying();

//    @Headers("api-key: " + "PUT_YOUR_API_KEY")
//    @GET("android/jsonandroid")
//    Call<JSONResponse> getJSON();
}
