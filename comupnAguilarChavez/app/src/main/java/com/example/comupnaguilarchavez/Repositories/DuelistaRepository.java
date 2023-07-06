package com.example.comupnaguilarchavez.Repositories;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.comupnaguilarchavez.Entities.Duelista;

import java.util.List;

@Dao
public interface DuelistaRepository {
    @Query("SELECT * FROM Duelista")
    List<Duelista> getAllUser();
    @Insert
    void create(Duelista pokemon);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Duelista> pokemones);
    @Update
    void updateCuenta(Duelista pokemon);

    @Query("SELECT MAX(idDuelista) FROM Pokemones")
    int getLastId();
    @Query("SELECT * FROM Pokemones WHERE id = :pokemonId")
    Pokemon findPokemonById(int pokemonId);

    @Query("SELECT * FROM Pokemones WHERE synced = 0")
    List<Pokemon> getUnsyncedPokemones();

}
