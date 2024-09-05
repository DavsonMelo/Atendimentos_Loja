package com.example.atendimentosloja.adapters;

public class VendedoraData {
    private String nome;
    private float mediaTempo; // ou double, dependendo da precisão desejada

    public VendedoraData(String nome, float mediaTempo) {
        this.nome = nome;
        this.mediaTempo = mediaTempo;
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

}
