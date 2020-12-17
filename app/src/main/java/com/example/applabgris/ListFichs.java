package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.applabgris.Entidades.Alternativa;
import com.example.applabgris.Entidades.Categoria;
import com.example.applabgris.Entidades.Ficha;
import com.example.applabgris.Entidades.Pergunta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFichs extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private BootstrapButton btnNewForm;

    String nome;
    ArrayList<String> nomesFichas = new ArrayList<String>();

    ListView listView = null;

    private TextView listTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fichs);

        database = FirebaseDatabase.getInstance();
        listView = (ListView) findViewById(R.id.fichas);

        popularDadosListaFicha();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ListFichs.this, nomesFichas.get(i), Toast.LENGTH_SHORT).show();
                abrirListaFichas(nomesFichas.get(i));
            }
        });

        btnNewForm = (BootstrapButton) findViewById(R.id.btnNewForm);

        btnNewForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFormTemplates();
            }
        });



       //fillData();
    }

    public void popularDadosListaFicha(){
        Log.d("TAG", "TIIIIIIIIIIIIIIIIIIITULOOOOOOOOOOOOOOOOOoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("fichaAppTeste").orderByChild("tituloFicha").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot data : snapshot.getChildren()){
                        nome = data.child("tituloFicha").getValue().toString();
                        nomesFichas.add(nome);
                        Log.d("TAG", nome);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListFichs.this, android.R.layout.simple_expandable_list_item_1, nomesFichas);
                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void abrirListaFichas(String nome){
        Intent intent = new Intent(ListFichs.this, FichasActivity.class);
        intent.putExtra("nomeFicha", nome);
        startActivity(intent);


        //precisa de finish??
    }
    public void fillData() {

        Ficha ficha = new Ficha();

        //CRIANDO FICHA, CATEGORIAS E PERGUNTAS
        ArrayList<Categoria> categorias = new ArrayList<>();
        Categoria categoria = new Categoria();
        categoria.setTituloCategoria("Categoria teste");
        categorias.add(categoria);

        Pergunta testeAberta = new Pergunta();
        testeAberta.setTituloPergunta("PERGUNTA DISSERTATIVA");
        testeAberta.setResposta("nao aguento mais ficar em casa");
        testeAberta.setAlternativas(null);


        ArrayList<Pergunta> perguntas = new ArrayList<>();
        Pergunta pergunta = new Pergunta();
        pergunta.setTituloPergunta("Pergunta teste");
        perguntas.add(pergunta);
        perguntas.add(testeAberta);
        categoria.setPerguntas(perguntas);


        ArrayList<Alternativa> alternativas = new ArrayList<>();
        Alternativa alternativa = new Alternativa();
        Alternativa alternativa2 = new Alternativa();
        alternativas.add(alternativa);
        alternativas.add(alternativa2);
        alternativa.setTituloAlternativa("Alternativa teste");
        alternativa.setResposta(false);
        pergunta.setAlternativas(alternativas);
        alternativa2.setTituloAlternativa("Alternativa teste 2");
        alternativa2.setResposta(false);
        pergunta.setAlternativas(alternativas);
        ficha.setCategorias(categorias);
        ficha.setTituloFicha("TESTE dissertativa");

        inserirFicha(ficha);
    }


    private void inserirFicha(Ficha ficha){
        myRef = database.getReference("Templates de Fichas");
        //myRef.setValue(ficha);
        String key = myRef.child("teste").push().getKey();

        ficha.setkeyFicha(key);

        myRef.child(key).setValue(ficha);
    }

    public void openFormTemplates(){
        Intent intent = new Intent(this, ListFormTemplate.class);
        startActivity(intent);
    }
}