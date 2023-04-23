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
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateAccountActivity extends AppCompatActivity {

    EditText et_name, et_email, et_password, et_confirm_password;
    TextView login_text_view_btn, app_name;
    Button btn_create_account;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        app_name= findViewById(R.id.app_name);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        login_text_view_btn = findViewById(R.id.login_text_view_btn);
        btn_create_account = findViewById(R.id.btn_create_account);


        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan Tunggu...");
        progressDialog.setCancelable(false);

        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_name.getText().length() > 0 && et_email.getText().length() > 0 && et_password.getText().length() > 0 && et_confirm_password.getText().length() > 0 ){
                    if (et_password.getText().toString().equals(et_confirm_password.getText().toString())){
                        Register(et_name.getText().toString(), et_email.getText().toString(), et_password.getText().toString());
                    }else{
                        Toast.makeText(CreateAccountActivity.this, "Password Tidak Sesuai", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CreateAccountActivity.this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_text_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void Register(String name, String email, String password){
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser != null){
                        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                reload();
                            }
                        });

                    }else {
                        Toast.makeText(CreateAccountActivity.this, "Register Gagal", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void reload(){
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