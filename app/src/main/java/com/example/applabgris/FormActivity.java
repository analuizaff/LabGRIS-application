package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FormActivity extends AppCompatActivity {
    //Template data
    String nome = null;
    String keyFicha = null;
    Ficha ficha1;
    

    private BootstrapEditText edtNewFormTitle;
    private BootstrapEditText edtResposta;
    private BootstrapLabel label;
    int posicaoPergunta;
    int posicaoCategoria;

    //Database
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    //Screen layout
    List<Categoria> langs;
    Map<Categoria, List<Pergunta>> topics;
    Map<Pergunta, List<Alternativa>> altTopics;

    ExpandableListAdapter listAdapter;
    ExpandableListView expandableListView;
    ExpandableListAdapter altexpandableListView;
    ListView listView;
    TextView templateNome;

    //Create new form button
    private BootstrapButton btnCreateForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Bundle extra = getIntent().getExtras();


        if(extra != null){
            nome = extra.getString("nomeTemplate");
        }
        templateNome = (TextView) findViewById(R.id.templateNome);
        templateNome.setText(nome);

        database = FirebaseDatabase.getInstance();
        //popularDadosListaFicha();
        popularDadosTemplate(nome);
        //popularDadosTemplate(nome);

        expandableListView = (ExpandableListView) findViewById(R.id.template);
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

        //Create new form
        btnCreateForm = (BootstrapButton) findViewById(R.id.btnCreateForm);
        btnCreateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialogNewForm();
            }
        });
    }


    public void popularDadosTemplate(String nomeFicha){
    /*Aqui eu recupero a ficha do banco*/
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Templates de Fichas").orderByChild("tituloFicha").equalTo(nomeFicha).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Categoria cat;
                for (DataSnapshot fichaSnapshot : snapshot.getChildren()) {
                /*Joga a fichaSnapshot (recuperada do banco na classe de ficha)*/
                ficha1 = fichaSnapshot.getValue(Ficha.class);

                keyFicha = ficha1.getkeyFicha();
                /*Passo categoria e respostas para a variaveis que vao tratar a tela*/
                langs = new ArrayList<>();
                topics = new HashMap<>();

                for(int i=0; i<ficha1.getCategorias().size(); i++){
                    langs.add(ficha1.getCategorias().get(i));
                    topics.put(langs.get(i), langs.get(i).getPerguntas());
                }


                listAdapter = new MyExListAdapter(FormActivity.this, langs, topics);
                expandableListView.setAdapter(listAdapter);

                /*PARTE QUE DEVERIA ESTAR NO ON CREATE*/

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
        l1.setEnabled(false);
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
        edtResposta.setEnabled(false);

        label = (BootstrapLabel) dialogView.findViewById(R.id.questionTitle);
        label.setText(pergunta.getTituloPergunta());

        builder.setView(dialogView).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // updateRespostas(keyFicha, pergunta.getTituloPergunta(), edtResposta.getText().toString());
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

    public void criarNovaFicha(String titulo){
        //titulos = ListFichs.nomesFichas;
        if(titulo == null || titulo == " " || titulo.isEmpty()){
            Toast.makeText(FormActivity.this, "Por favor, defina um título para a nova ficha.",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            this.ficha1.setTituloFicha(titulo);
            inserirFicha(this.ficha1);
            openNovaFicha(titulo);
        }
    }

    public void onCreateDialogNewForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_nova_ficha, null);
        edtNewFormTitle = (BootstrapEditText) dialogView.findViewById(R.id.edtTitulodoTemplate);
        //edtNewFormTitle.setText("testando");


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        criarNovaFicha(edtNewFormTitle.getText().toString());
                    }
                })
                .setTitle(R.string.novaFicha);
        builder.setView(dialogView);
        builder.show();
        //return builder.create();
    }
    private void inserirFicha(Ficha ficha){
        myRef = database.getReference("fichaAppTeste");
        //myRef.setValue(ficha);
        String key = myRef.child("teste").push().getKey();

        ficha.setkeyFicha(key);

        myRef.child(ficha.getTituloFicha()).setValue(ficha);

        Toast.makeText(FormActivity.this, "Ficha criada!",
                Toast.LENGTH_SHORT).show();
    }

    private void openNovaFicha(String nome){
        Intent intent = new Intent(FormActivity.this, FichasActivity.class);
        intent.putExtra("nomeFicha", nome);
        Log.d("TAG", "-------> NOME: " +nome);
        startActivity(intent);
    }

}