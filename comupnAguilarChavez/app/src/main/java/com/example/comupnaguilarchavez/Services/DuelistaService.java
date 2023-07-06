package com.example.comupnaguilarchavez.Services;

import com.example.comupnaguilarchavez.Entities.Duelista;
import com.example.comupnaguilarchavez.Entities.ImageToSave;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DuelistaService {
    @GET("Duelista")
    Call<List<Duelista>> getAllUser(@Query("limit") int limit, @Query("page") int page);

    @GET("Duelista/{id}")
    Call<Duelista> findUser(@Path("id") int id);

    @POST("Pokemones")
    Call<Duelista> create(@Body Duelista duelista);

    @PUT("Pokemones/{id}")
    Call<Duelista> update(@Path("id") int id, @Body Duelista duelista);

    @DELETE("Pokemones/{id}")
    Call<Void> delete(@Path("id") int id);

    @POST("image")
    Call<Duelista> subirImagen(@Body ImageToSave imagen);
}
