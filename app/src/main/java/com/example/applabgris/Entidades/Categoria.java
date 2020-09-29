package com.example.applabgris.Entidades;

import java.util.ArrayList;

public class Categoria {
    private ArrayList<Pergunta> perguntas;
    private String tituloCategoria;


    public ArrayList<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(ArrayList<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public String getTituloCategoria() {
        return tituloCategoria;
    }

    public void setTituloCategoria(String tituloCategoria) {
        this.tituloCategoria = tituloCategoria;
    }
}
