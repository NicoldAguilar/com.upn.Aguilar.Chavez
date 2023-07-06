package com.example.comupnaguilarchavez.BD;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.comupnaguilarchavez.Entities.Cartas;
import com.example.comupnaguilarchavez.Entities.Duelista;
import com.example.comupnaguilarchavez.Repositories.CartasRepository;
import com.example.comupnaguilarchavez.Repositories.DuelistaRepository;

@Database(entities = {Duelista.class, Cartas.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DuelistaRepository duelistaRepository();
    public abstract CartasRepository cartitasRepository();

    public static AppDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "vj20231")
                .allowMainThreadQueries()
                .build();
    }
}
