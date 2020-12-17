package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapLabel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FichasActivity extends AppCompatActivity {
    private TextView nomeFicha;
    private BootstrapEditText edtResposta;
    private BootstrapLabel label;
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

    Boolean ehNovaFicha = false;
    Ficha novaFicha;




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
        if(popularDadosFicha(nome) != true){
            popularDadosTemplate(nome);
        };

        expandableListView = (ExpandableListView) findViewById(R.id.ficha);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //fillDataAlt(topics.get(langs.get(i)).get(i1));

                if(topics.get(langs.get(i)).get(i1).getAlternativas() != null){
                    openDialogAlternativa(topics.get(langs.get(i)).get(i1), i1, i);
                }
                else if(topics.get(langs.get(i)).get(i1).getAlternativas() == null){
                    onCreateDialog(topics.get(langs.get(i)).get(i1));
                }
                //Toast.makeText(FichasActivity.this,langs.get(i).getTituloCategoria() + "  :  " + topics.get(langs.get(i)).get(i1).getTituloPergunta(), Toast.LENGTH_LONG).show();
                posicaoCategoria = i;
                posicaoPergunta = i1;
                return false;
            }
        });
    }

    public void openDialogAlternativa(Pergunta pergunta, int iPergunta, int iCategoria){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        View row = getLayoutInflater().inflate(R.layout.list_alternativas,null);
        ListView l1 = (ListView) row.findViewById(R.id.listAlternativasPergunta);
        String resposta = null;
        final ArrayList<Alternativa> alternativas = pergunta.getAlternativas();

        final ArrayList<String> alternativassTitulo = new ArrayList<String>();
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


        alert.setView(row).setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem  = (String) adapterView.getItemAtPosition(i);
                updateAlternativas(keyFicha, selectedItem, true, i );

                for(int j=0; j<alternativassTitulo.size(); j++){
                    if(alternativassTitulo.get(j) != selectedItem) {
                        updateAlternativas(keyFicha, alternativassTitulo.get(j), false, j);
                    }
                }
                //Toast.makeText(FichasActivity.this, selectedItem, Toast.LENGTH_SHORT).show();
            }
        });
        arrayAdapter.notifyDataSetChanged();

        alert.setView(row);
        alert.setTitle(pergunta.getTituloPergunta());
        AlertDialog dialog = alert.create();
        dialog.show();

    }
    public void onCreateDialog(final Pergunta pergunta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.resposta_dissertativa, null);

        edtResposta = (BootstrapEditText) dialogView.findViewById(R.id.edtAnswer);
        edtResposta.setText(pergunta.getResposta());

        label = (BootstrapLabel) dialogView.findViewById(R.id.questionTitle);
        label.setText(pergunta.getTituloPergunta());

        builder.setView(dialogView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updateRespostas(keyFicha, pergunta.getTituloPergunta(), edtResposta.getText().toString());
            }
        });
        builder.setView(dialogView).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setView(dialogView);
        builder.show();
    }


    public boolean popularDadosFicha(String nomeFicha){

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
                    ficha = ficha1;



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
        if(keyFicha == null){
            return false;
        }
        return true;

    }
    public boolean popularDadosTemplate(String nomeFicha){
        this.ehNovaFicha = true;
        /*Aqui eu recupero a ficha do banco*/
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Templates de Fichas").orderByChild("tituloFicha").equalTo(nomeFicha).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Ficha ficha;
                Categoria cat;
                for (DataSnapshot fichaSnapshot : snapshot.getChildren()) {
                    /*Joga a fichaSnapshot (recuperada do banco na classe de ficha)*/
                    novaFicha = fichaSnapshot.getValue(Ficha.class);
                    inserirFicha(novaFicha);



                   /*  cat = fichaSnapshot.child("categorias").getValue(Categoria.class);
                     cat.setTituloCategoria("BASE");
                     ArrayList<Categoria> cats = new ArrayList<Categoria>();
                     cats.add(cat);
                     ficha1.setCategorias(cats);*/


                    keyFicha = novaFicha.getkeyFicha();
                    /*Passo categoria e respostas para a variaveis que vao tratar a tela*/
                    langs = new ArrayList<>();
                    topics = new HashMap<>();


                    for(int i=0; i<novaFicha.getCategorias().size(); i++){
                        langs.add(novaFicha.getCategorias().get(i));
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

        return true;
    }

    private void updateAlternativas(String keyFicha, String tituloAlternativa, boolean resposta, int iAlternativa){

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("fichaAppTeste");

        Alternativa alternativa = new Alternativa(tituloAlternativa, resposta);

        Map<String, Object> alternativaValues = alternativa.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/fichaAppTeste/" + keyFicha + "/categorias/"+posicaoCategoria+"/perguntas/"+posicaoPergunta+"/alternativas/"+iAlternativa, alternativaValues);

        reference.updateChildren(childUpdates);
    }
    private void updateRespostas(String keyFicha, String tituloPergunta, String resposta){

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("fichaAppTeste");

        Pergunta pergunta = new Pergunta(tituloPergunta, resposta);

        Map<String, Object> perguntaValues = pergunta.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/fichaAppTeste/" + keyFicha + "/categorias/"+posicaoCategoria+"/perguntas/"+posicaoPergunta+"/resposta/", resposta);
        Toast.makeText(this, "Resposta salva!", Toast.LENGTH_SHORT).show();

        reference.updateChildren(childUpdates);
    }

    private void inserirFicha(Ficha ficha){
        myRef = database.getReference("fichaAppTeste");
        //myRef.setValue(ficha);
        String key = myRef.child("teste").push().getKey();

        ficha.setkeyFicha(key);

        myRef.child(key).setValue(ficha);
    }


    /* public void fillDataAlt(Pergunta pergunta){

        ArrayList<String> teste = new ArrayList<String>();
        teste.add("bbbbbbbb");
        teste.add("cccccccc");

        //this.listView = (ListView) findViewById(R.id.listAlternativas);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, teste) ;
        //this.listView.setAdapter(arrayAdapter);
    }*/

}