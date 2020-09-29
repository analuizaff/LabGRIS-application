package com.example.applabgris;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.applabgris.Entidades.Pergunta;

import java.util.ArrayList;

public class AlternativasDialog extends AppCompatDialogFragment {

    ListView listView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.list_alternativas, null);

        builder.setView(view)
                .setTitle("título")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        listView = view.findViewById(R.id.listAlternativasPergunta);


        return builder.create();
    }

    public void fillTeste(Context context){

        ArrayList<String> teste = new ArrayList<String>();
        teste.add("bbbbbbbb");
        teste.add("cccccccc");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_multiple_choice, teste);
        this.listView.setAdapter(arrayAdapter);

    }
}
