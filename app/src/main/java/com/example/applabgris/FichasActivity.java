package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applabgris.Entidades.Alternativa;
import com.example.applabgris.Entidades.Categoria;
import com.example.applabgris.Entidades.Ficha;
import com.example.applabgris.Entidades.Pergunta;
import com.example.applabgris.Entidades.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FichasActivity extends AppCompatActivity {
    private TextView nomeFicha;

    ExpandableListView expandableListView;
    ExpandableListAdapter altexpandableListView;

    List<Categoria> langs;
    Map<Categoria, List<Pergunta>> topics;
    Map<Pergunta, List<Alternativa>> altTopics;

    ExpandableListAdapter listAdapter;

    ListView listView;

    private DatabaseReference reference;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    Ficha ficha;
    String name = null;
    String nome = null;
    String keyFicha = null;
    int posicaoPergunta;
    int posicaoCategoria;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Log.d("TAG", "AAAAAAAAAAAAAAAAAAAAAAAAAAH");

        setContentView(R.layout.activity_fichas);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            nome = extra.getString("nomeFicha");
            Log.d("TAG", nome);
        }

        database = FirebaseDatabase.getInstance();
        //popularDadosListaFicha();
        popularDadosFicha(nome);

        expandableListView = (ExpandableListView) findViewById(R.id.ficha);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //fillDataAlt(topics.get(langs.get(i)).get(i1));

                openDialog(topics.get(langs.get(i)).get(i1), i1, i);
                Toast.makeText(FichasActivity.this,langs.get(i).getTituloCategoria() + "  :  " + topics.get(langs.get(i)).get(i1).getTitulo(), Toast.LENGTH_LONG).show();
                posicaoCategoria = i;
                posicaoPergunta = i1;
                return false;
            }
        });
    }

    public void openDialog(Pergunta pergunta, int iPergunta, int iCategoria){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View row = getLayoutInflater().inflate(R.layout.list_alternativas,null);
        ListView l1 = (ListView) row.findViewById(R.id.listAlternativasPergunta);
        String resposta = null;
        ArrayList<Alternativa> alternativas = pergunta.getAlternativas();

        ArrayList<String> alternativassTitulo = new ArrayList<String>();
        for(int i =0; i<alternativas.size(); i++){
            alternativassTitulo.add(alternativas.get(i).getTituloAlternativa());
            if(alternativas.get(i).isResposta() == true){
                resposta = alternativassTitulo.get(i);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, alternativassTitulo);
        l1.setAdapter(arrayAdapter);
        l1.setChoiceMode(l1.CHOICE_MODE_SINGLE);
        for (int i=0; i<alternativassTitulo.size(); i++){
            if(alternativas.get(i).isResposta() == true) {
                l1.setItemChecked(i, true);
            }
         }

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem  = (String) adapterView.getItemAtPosition(i);
                updateRespostas(keyFicha, selectedItem, true, i );
                Toast.makeText(FichasActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        arrayAdapter.notifyDataSetChanged();

        alert.setView(row);
        alert.setTitle(pergunta.getTitulo());
        AlertDialog dialog = alert.create();
        dialog.show();





    }

    public void popularDadosFicha(String nomeFicha){

        /*Aqui eu recupero a ficha do banco*/
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("fichaAppTeste").orderByChild("tituloFicha").equalTo(nomeFicha).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Ficha ficha1;
                Categoria cat;
                for (DataSnapshot fichaSnapshot : snapshot.getChildren()) {
                    /*Joga a fichaSnapshot (recuperada do banco na classe de ficha)*/
                     ficha1 = fichaSnapshot.getValue(Ficha.class);



                   /*  cat = fichaSnapshot.child("categorias").getValue(Categoria.class);
                     cat.setTituloCategoria("BASE");
                     ArrayList<Categoria> cats = new ArrayList<Categoria>();
                     cats.add(cat);
                     ficha1.setCategorias(cats);*/


                   keyFicha = ficha1.getkeyFicha();
                     /*Passo categoria e respostas para a variaveis que vao tratar a tela*/
                   langs = new ArrayList<>();
                   topics = new HashMap<>();


                   for(int i=0; i<ficha1.getCategorias().size(); i++){
                      langs.add(ficha1.getCategorias().get(i));
                      topics.put(langs.get(i), langs.get(i).getPerguntas());
                   }


                   listAdapter = new MyExListAdapter(FichasActivity.this, langs, topics);
                   expandableListView.setAdapter(listAdapter);

                   /*PARTE QUE DEVERIA ESTAR NO ON CREATE*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void inserirFicha(Ficha ficha){
        myRef = database.getReference("fichaAppTeste");
        //myRef.setValue(ficha);
        String key = myRef.child("teste").push().getKey();

       ficha.setkeyFicha(key);

        myRef.child(key).setValue(ficha);
    }

    private void updateRespostas(String keyFicha, String tituloAlternativa, boolean resposta, int iAlternativa){

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("fichaAppTeste");

        Alternativa alternativa = new Alternativa(tituloAlternativa, resposta);

        Map<String, Object> alternativaValues = alternativa.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/fichaAppTeste/" + keyFicha + "/categorias/"+posicaoCategoria+"/perguntas/"+posicaoPergunta+"/alternativas/"+iAlternativa, alternativaValues);

        reference.updateChildren(childUpdates);
    }


  /*  public void fillData() {

        ficha = new Ficha();

        //CRIANDO FICHA, CATEGORIAS E PERGUNTAS
        ArrayList<Categoria> categorias = new ArrayList<>();
        Categoria categoria = new Categoria();
        categoria.setTituloCategoria("Categoria teste");
        categorias.add(categoria);

        ArrayList<Pergunta> perguntas = new ArrayList<>();
        Pergunta pergunta = new Pergunta();
        pergunta.setTitulo("Pergunta teste");
        perguntas.add(pergunta);
        categoria.setPerguntas(perguntas);

        ArrayList<Alternativa> alternativas = new ArrayList<>();
        Alternativa alternativa = new Alternativa();
        alternativas.add(alternativa);
        alternativa.setTituloAlternativa("Alternativa teste");
        alternativa.setResposta(false);
        pergunta.setAlternativas(alternativas);

        ficha.setCategorias(categorias);
        ficha.setTituloFicha("TESTE");

       inserirFicha(ficha);
    }
    public void fillDataAlt(Pergunta pergunta){

        ArrayList<String> teste = new ArrayList<String>();
        teste.add("bbbbbbbb");
        teste.add("cccccccc");

        //this.listView = (ListView) findViewById(R.id.listAlternativas);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, teste) ;
        //this.listView.setAdapter(arrayAdapter);
    }*/

}