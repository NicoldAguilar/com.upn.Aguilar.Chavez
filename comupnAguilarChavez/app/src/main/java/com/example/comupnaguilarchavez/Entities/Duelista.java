package com.example.comupnaguilarchavez.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Duelista")
public class Duelista {
    @PrimaryKey()
    public int idDuelista;

    //para sincronizar
    @ColumnInfo(name = "synced")
    private boolean syncro; //true en el mockapi - false no esta en mockapi

    public String nombreDuelista;
    public String idCartitas;

    public int getIdDuelista() {
        return idDuelista;
    }

    public void setIdDuelista(int idDuelista) {
        this.idDuelista = idDuelista;
    }

    public boolean isSyncro() {
        return syncro;
    }

    public void setSyncro(boolean syncro) {
        this.syncro = syncro;
    }

    public String getNombreDuelista() {
        return nombreDuelista;
    }

    public void setNombreDuelista(String nombreDuelista) {
        this.nombreDuelista = nombreDuelista;
    }

    public String getIdCartitas() {
        return idCartitas;
    }

    public void setIdCartitas(String idCartitas) {
        this.idCartitas = idCartitas;
    }
}
