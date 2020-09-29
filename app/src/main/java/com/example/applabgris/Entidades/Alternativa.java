package com.example.applabgris.Entidades;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Alternativa {
    private boolean resposta;
    private String tituloAlternativa;
    public Map<String, Boolean> alternativas = new HashMap<>();


   public Alternativa(){

   }
    public Alternativa(String tituloAlternativa, boolean resposta){
       this.tituloAlternativa = tituloAlternativa;
       this.resposta = resposta;
    }

    public String getTituloAlternativa() {
        return tituloAlternativa;
    }


    public void setTituloAlternativa(String tituloAlternativa) {
        this.tituloAlternativa = tituloAlternativa;
    }

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tituloAlternativa", tituloAlternativa);
        result.put("resposta", resposta);

        return result;
    }

}

