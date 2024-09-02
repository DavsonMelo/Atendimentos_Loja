package com.example.atendimentosloja.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.atendimentosloja.entity.Vendedora;

import java.util.List;

@Dao
public interface VendedoraDao {

    @Query("SELECT * FROM tab_vendedoras") // Seleciona tudo da tabela tab_vendedoras
    LiveData<List<Vendedora>> getAll(); // Retorna todos os dados do bd. No caso, Nomes e ids

    @Query("SELECT * FROM tab_vendedoras WHERE nome like :txt_nome limit 1")
    Vendedora findByName(String txt_nome); // Retorna lista de venddedores pelo nome

    @Insert
    void insert(Vendedora vendedora);

    @Update
    void update(Vendedora vendedora);

    @Delete
    void delete(Vendedora vendedora);
}
