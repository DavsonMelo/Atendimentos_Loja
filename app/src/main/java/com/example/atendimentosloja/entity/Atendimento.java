package com.example.atendimentosloja.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tab_atendimentos")
public class Atendimento implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "dateInicio")
    private String dateInicio;

    @ColumnInfo(name = "dateFim")
    private String dateFim;

    @ColumnInfo(name = "nomeAtendimento")
    private String nomeAtendimento;

    @ColumnInfo(name = "conversao")
    private boolean conversao;

    @ColumnInfo(name = "feed")
    private String feed;

    @ColumnInfo(name = "boleta")
    private Integer boleta;

    @ColumnInfo(name = "tempoAtendimento")  // Nova coluna com anotação
    private Integer tempoAtendimento;

    public Atendimento(){  }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateInicio() {
        return dateInicio;
    }

    public void setDateInicio(String dateInicio) {
        this.dateInicio = dateInicio;
    }

    public String getDateFim() {
        return dateFim;
    }

    public void setDateFim(String dateFim) {
        this.dateFim = dateFim;
    }

    public String getNomeAtendimento() {
        return nomeAtendimento;
    }

    public void setNomeAtendimento(String nomeAtendimento) {
        this.nomeAtendimento = nomeAtendimento;
    }

    public boolean isConversao() {
        return conversao;
    }

    public void setConversao(boolean conversao) {
        this.conversao = conversao;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public Integer getBoleta() {
        return boleta;
    }

    public void setBoleta(Integer boleta) {
        this.boleta = boleta;
    }

    public Integer getTempoAtendimento() {
        return tempoAtendimento;
    }

    public void setTempoAtendimento(Integer tempoAtendimento) {
        this.tempoAtendimento = tempoAtendimento;
    }
}
