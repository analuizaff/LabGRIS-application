package com.example.applabgris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private BootstrapEditText edtEmail;
    private BootstrapEditText edtPassword;

    private TextView txtRecoveryPassword;

    private BootstrapButton btnLogin;
    private BootstrapButton btnCadastro;


    //Password recovery instances
    private BootstrapEditText edtEmailRecover;

    private BootstrapButton btnCancel;
    private BootstrapButton btnSendEmail;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        edtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        edtPassword = (BootstrapEditText) findViewById(R.id.edtPassword);

        txtRecoveryPassword = (TextView) findViewById(R.id.RecoverPassword);

        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);
        btnCadastro = (BootstrapButton) findViewById(R.id.btnCadastro);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EfetuarLogin(edtEmail.getText().toString(), edtPassword.getText().toString());

            }
        });

        txtRecoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirDialog();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            UsuarioLogado();
        }
    }


    public void UsuarioLogado(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Login efetuado", Toast.LENGTH_SHORT).show();

    }

    public void EfetuarLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            UsuarioLogado();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            // ...
                        }

                        // ...
                    }
                });
    }

    private void abrirCadastro(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);       
        finish();
    }

    private void abrirDialog(){
        dialog = new Dialog(LoginActivity.this);

        dialog.setContentView(R.layout.alert_recovery_password);

        btnCancel = (BootstrapButton) dialog.findViewById(R.id.btnCancel);
        btnSendEmail = (BootstrapButton) dialog.findViewById(R.id.btnSendEmail);

        edtEmailRecover = (BootstrapEditText) dialog.findViewById(R.id.edtEmailRecover);

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                mAuth.sendPasswordResetEmail(edtEmailRecover.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "Email sent.");
                                    Toast.makeText(LoginActivity.this, "Verifique a sua caixa de e-mail", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}