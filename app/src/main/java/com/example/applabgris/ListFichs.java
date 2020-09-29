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
import android.widget.Toast;

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

    String nome;
    ArrayList<String> nomesFichas = new ArrayList<String>();

    ListView listView = null;

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

}