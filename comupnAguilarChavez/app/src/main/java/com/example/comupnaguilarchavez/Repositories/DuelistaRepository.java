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
    void create(Duelista duelista);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Duelista> duelista);
    @Update
    void updateCuenta(Duelista duelista);

    @Query("SELECT MAX(idDuelista) FROM Duelista")
    int getLastId();
    @Query("SELECT * FROM Duelista WHERE idDuelista = :duelistaId")
    Duelista findDuelistById(int duelistaId);

    @Query("SELECT * FROM Duelista WHERE synced = 0")
    List<Duelista> getUnsyncedPokemones();

}
