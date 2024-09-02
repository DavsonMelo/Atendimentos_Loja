package com.example.atendimentosloja.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tab_vendedoras")
public class Vendedora implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nome")
    private String nome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Vendedora(int id, String nome) {
        this.id = id;
        this.nome = nome;


    }
    @Override
    public String toString() {
        return "Vendedora{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
