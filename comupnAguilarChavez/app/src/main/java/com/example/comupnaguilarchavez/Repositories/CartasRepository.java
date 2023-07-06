package com.example.comupnaguilarchavez.Repositories;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.comupnaguilarchavez.Entities.Cartas;

import java.util.List;

@Dao
public interface CartasRepository {
    @Query("SELECT * FROM Cartas")
    List<Cartas> getAllUser();
    @Insert
    void create(Cartas pokemon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Cartas> cartass);
    @Update
    void updateCuenta(Cartas cartass);

    @Query("SELECT MAX(idCartas) FROM Cartas")
    int getLastId();
    @Query("SELECT * FROM Cartas WHERE idCartas = :cartasId")
    Cartas findCartaById(int cartasId);

    @Query("SELECT * FROM Cartas WHERE synced = 0")
    List<Cartas> getUnsyncedPokemones();

    @Query("SELECT * FROM Cartas WHERE idDuelista = :idCartas")
    List<Cartas> getCartasByDuelistaId(int idCartas);
}
