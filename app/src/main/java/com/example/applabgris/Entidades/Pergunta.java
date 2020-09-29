package com.example.applabgris.Entidades;

import java.util.ArrayList;

public class Pergunta {
    private ArrayList<Alternativa> alternativas;
    private String enunciado;

    public ArrayList<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(ArrayList<Alternativa> alternativas) {
       this.alternativas = alternativas;
    }

    public String getTitulo() {
        return enunciado;
    }

    public void setTitulo(String titulo) {
        this.enunciado = titulo;
    }
}
