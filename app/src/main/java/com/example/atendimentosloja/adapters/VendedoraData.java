package com.example.atendimentosloja.adapters;

import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

public class VendedoraData {
    private String nome;
    private float mediaTempo;
    private List<PieEntry> pieEntries;

    public VendedoraData(String nome, float mediaTempo, List<PieEntry> pieEntries) {
        this.nome = nome;
        this.mediaTempo = mediaTempo;
        this.pieEntries = pieEntries;
    }

    public String getNome() {
        return nome;
    }

    public float getMediaTempo() {
        return mediaTempo;
    }

    public void setMediaTempo(float mediaTempo) {
        this.mediaTempo = mediaTempo;
    }

    public List<PieEntry> getPieEntries() {
        return pieEntries;
    }

}
