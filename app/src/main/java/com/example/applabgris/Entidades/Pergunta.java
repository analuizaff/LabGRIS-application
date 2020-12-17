package com.example.applabgris.Entidades;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Pergunta {
    private ArrayList<Alternativa> alternativas;
    private String tituloPergunta;
    private String resposta;
    public Map<String, Boolean> perguntas = new HashMap<>();

    public Pergunta(){

    }

    public Pergunta(String tituloPergunta, String resposta){
        this.tituloPergunta = tituloPergunta;
        this.resposta = resposta;
    }

    public ArrayList<Alternativa> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(ArrayList<Alternativa> alternativas) {
       this.alternativas = alternativas;
    }

    public String getTituloPergunta() {
        return tituloPergunta;
    }

    public void setTituloPergunta(String tituloPergunta) {
        this.tituloPergunta = tituloPergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tituloPergunta", tituloPergunta);
        result.put("resposta", resposta);

        return result;
    }
}
