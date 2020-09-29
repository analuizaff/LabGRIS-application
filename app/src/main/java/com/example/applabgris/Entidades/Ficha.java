package com.example.applabgris.Entidades;

import java.util.ArrayList;

public class Ficha {
    private ArrayList<Categoria> categoria = new ArrayList<>();
    private String tituloFicha;
    private String keyFicha;
    //equipe
    //data de criaao
    //localizacao


    public ArrayList<Categoria> getCategorias() {
        return categoria;
    }

    public void setCategorias(ArrayList<Categoria> categorias) {
        this.categoria = categorias;
    }

    public String getTituloFicha() {
        return tituloFicha;
    }

    public void setTituloFicha(String tituloFicha) {
        this.tituloFicha = tituloFicha;
    }

    public String getkeyFicha() {
        return keyFicha;
    }

    public void setkeyFicha(String keyFicha) {
        this.keyFicha = keyFicha;
    }
}
