package me.joshshin.shodatchallenge.Services;

import java.util.List;

import me.joshshin.shodatchallenge.models.Photo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jjshin on 5/22/16.
 */
public interface APIService {
    @GET("photos/")
    Call<List<Photo>> getPhotos(@Query("albumId") int id1, @Query("albumId") int id2);
}

