package com.example.applabgris;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applabgris.Entidades.Alternativa;
import com.example.applabgris.Entidades.Categoria;
import com.example.applabgris.Entidades.Pergunta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyExListAdapter extends BaseExpandableListAdapter
{
    Context context;
    List<Categoria> langs;
    Map<Categoria, List<Pergunta>> topics;
    ListView listView;

    public MyExListAdapter(Context context, List<Categoria> langs, Map<Categoria, List<Pergunta>> topics) {
        this.context = context;
        this.langs = langs;
        this.topics = topics;
        this.listView = listView;

    }

    @Override
    public int getGroupCount() {
        return langs.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return topics.get(langs.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return langs.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return topics.get(langs.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {

        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        Categoria lang = (Categoria) getGroup(i);

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_categorias,null);
        }

        TextView txtCategoria = (TextView) view.findViewById(R.id.txtCategorias);
        txtCategoria.setText(lang.getTituloCategoria());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        Pergunta topic = (Pergunta) getChild(i,i1);
       // ArrayList<Alternativa> alternativas = topic.getAlternativas();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_perguntas, null);
        }
        TextView txtPergunta = (TextView) view.findViewById(R.id.txtPerguntas);
        txtPergunta.setText(topic.getTituloPergunta());

       // TextView txtAlt =  (TextView) view.findViewById(R.id.txtAlternativa);
       // txtAlt.setText(alternativas.get(0).getTituloAlternativa());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        //Toast.makeText(this.context, "Clicou na pergunta", Toast.LENGTH_SHORT).show();
        return true;
    }
}
