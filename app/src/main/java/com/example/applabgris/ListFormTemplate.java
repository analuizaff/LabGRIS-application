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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFormTemplate extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    ListView listView = null;

    String nome;
    ArrayList<String> nomesFichas = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_form_template);


        database = FirebaseDatabase.getInstance();
        listView = (ListView) findViewById(R.id.templates);

        popularDadosTemplate();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ListFichs.this, nomesFichas.get(i), Toast.LENGTH_SHORT).show();
                abrirListaFichas(nomesFichas.get(i));
            }
        });
    }

    public void popularDadosTemplate(){
        Log.d("TAG", "TIIIIIIIIIIIIIIIIIIITULOOOOOOOOOOOOOOOOOoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child("Templates de Fichas").orderByChild("tituloFicha").addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    for(DataSnapshot data : snapshot.getChildren()){
                        nome = data.child("tituloFicha").getValue().toString();
                        nomesFichas.add(nome);
                        Log.d("TAG", nome);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListFormTemplate.this, android.R.layout.simple_expandable_list_item_1, nomesFichas);
                    listView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void abrirListaFichas(String nome){
        Intent intent = new Intent(ListFormTemplate.this, FichasActivity.class);
        intent.putExtra("nomeFicha", nome);
        startActivity(intent);
    }
}