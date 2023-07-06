package com.example.comupnaguilarchavez.Repositories;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.comupnaguilarchavez.Entities.Cartas;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

@Dao
public interface CartasRepository {
    @Query("SELECT * FROM Cartas")
    List<Cartas> getAllCartas();
    @Insert
    void create(Cartas carta);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cartas> cartass);
    @Update
    void updateCartas(Cartas cartass);

    @Query("SELECT MAX(idCartas) FROM Cartas")
    int getLastId();
    @Query("SELECT * FROM Cartas WHERE idCartas = :cartasId")
    Cartas findCartaById(int cartasId);

    @Query("SELECT * FROM Cartas WHERE synced = 0")
    List<Cartas> getUnsyncedCartas();
}
