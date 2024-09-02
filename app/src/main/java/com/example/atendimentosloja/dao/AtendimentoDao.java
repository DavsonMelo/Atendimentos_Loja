package com.example.atendimentosloja.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.atendimentosloja.entity.Atendimento;

import java.util.List;

@Dao
public interface AtendimentoDao {

    @Query("SELECT * FROM tab_atendimentos")
    LiveData<List<Atendimento>> getAll();

    @Query("SELECT nomeAtendimento FROM tab_atendimentos WHERE  dateFim IS NULL")
    List<String> getFuncionariasPendentes();

    @Query("SELECT * FROM tab_atendimentos WHERE nomeAtendimento = :txt_nomeAtendimento AND dateFim IS NULL LIMIT 1")
    Atendimento findByName(String txt_nomeAtendimento);

    @Insert
    void insert(Atendimento atendimento);

    @Update
    void update(Atendimento atendimento);

    @Delete
    void delete(Atendimento atendimento);
}
