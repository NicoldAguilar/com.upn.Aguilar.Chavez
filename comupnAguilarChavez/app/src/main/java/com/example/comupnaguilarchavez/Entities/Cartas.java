package com.example.comupnaguilarchavez.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cartas")
public class Cartas {

    @PrimaryKey()
    public int idCartas;

    //para sincronizar
    @ColumnInfo(name = "synced")
    private boolean synced; //true en el mockapi - false no esta en mockapi

    public String nombre;
    public int puntosAtaque;
    public int puntosDefensa;
    public String foto;
    public String latitud;
    public String longitud;

    public int getIdCartas() {
        return idCartas;
    }

    public void setIdCartas(int idCartas) {
        this.idCartas = idCartas;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public void setPuntosAtaque(int puntosAtaque) {
        this.puntosAtaque = puntosAtaque;
    }

    public int getPuntosDefensa() {
        return puntosDefensa;
    }

    public void setPuntosDefensa(int puntosDefensa) {
        this.puntosDefensa = puntosDefensa;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

}
