package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.example.applabgris.Entidades.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private BootstrapEditText editName;
    private BootstrapEditText emailAdress;
    private BootstrapEditText editPassword;
    private BootstrapEditText editConfirmpassword;

    private BootstrapButton btnUpdate;
    private BootstrapButton btnCancel;

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editName = (BootstrapEditText) findViewById(R.id.editName);
        emailAdress = (BootstrapEditText) findViewById(R.id.emailAdress);
        editPassword = (BootstrapEditText) findViewById(R.id.editPassword);
        editConfirmpassword = (BootstrapEditText) findViewById(R.id.editConfirmPassword);

        btnUpdate = (BootstrapButton) findViewById(R.id.btnUpdate);
        btnCancel = (BootstrapButton) findViewById(R.id.btnCancel);

        popularDadosUsuario();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();

                String emailCurrentUser = firebaseAuth.getCurrentUser().getEmail();
                reference = FirebaseDatabase.getInstance().getReference();

                reference.child("users").orderByChild("email").equalTo(emailCurrentUser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot userSnapshot : snapshot.getChildren()){
                            User user = userSnapshot.getValue(User.class);
                            updateUser(editName.getText().toString(), user.getEmail(), user.getKeyUser());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void popularDadosUsuario(){
        firebaseAuth = FirebaseAuth.getInstance();

        String emailCurrentUser = firebaseAuth.getCurrentUser().getEmail();

        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("users").orderByChild("email").equalTo(emailCurrentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnapshot : snapshot.getChildren()){

                    User user = userSnapshot.getValue(User.class);
                    editName.setText(user.getName());
                    emailAdress.setText(user.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUser(String name, String email, String keyUser){

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users");

        User user = new User(email, keyUser, name);

        Map<String, Object> userValues = user.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/users/" + keyUser, userValues);

        reference.updateChildren(childUpdates);
    }


}