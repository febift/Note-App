package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ImageView iv_profile;
    EditText et_email, et_password;
    Button btn_login;
    TextView create_account_text_view_btn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iv_profile = findViewById(R.id.iv_profile);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        create_account_text_view_btn = findViewById(R.id.create_account_text_view_btn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan Tunggu...");
        progressDialog.setCancelable(false);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_email.getText().length() > 0 && et_password.getText().length() > 0){
                    login(et_email.getText().toString(), et_password.getText().toString());
                }else {
                    Toast.makeText(LoginActivity.this, "Silakan Isi Semua Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        create_account_text_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
            }
        });
    }

    public void login(String email, String password){
        progressDialog.show();

        //login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    if (task.getResult().getUser() != null){
                        reload();
                    }else{
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }
}