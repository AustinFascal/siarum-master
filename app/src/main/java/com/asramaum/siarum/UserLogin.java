package com.asramaum.siarum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import es.dmoral.toasty.Toasty;
import me.anwarshahriar.calligrapher.Calligrapher;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton buttonSignIn, buttonRegister;
    private EditText editTextEmail;
    private ShowHidePasswordEditText editTextPass;
    private TextView textViewForgotPass;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        progressDialog = new ProgressDialog(this);

        buttonSignIn = (AppCompatButton) findViewById(R.id.buttonSignIn);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPass = (ShowHidePasswordEditText) findViewById(R.id.editTextPass);
        textViewForgotPass = (TextView) findViewById(R.id.textViewForgotPass);

        buttonSignIn.setOnClickListener(this);
        textViewForgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLogin.this, ResetPassword.class));
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLogin.this, UserRegister.class));
            }
        });

        loadUserInformation();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadUserInformation(){
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(UserLogin.this, MainMenuActivity.class));
            finish();
        }
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toasty.error(this, "Mohon masukkan email dengan benar", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toasty.success(this, "Mohon masukkan password dengan benar", Toast.LENGTH_SHORT, true).show();
            return;
        }

        authProgressCaller();
    }

    private void authProgressInfo(){
        progressDialog.setMessage("Mohon tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void authProgressCaller(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        authProgressInfo();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            startActivity(new Intent(UserLogin.this, MainMenuActivity.class));
                            finish();
                        } else {
                            buildDialog(UserLogin.this).show();
                        }
                    }
                });
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Autentikasi Gagal");
        builder.setCancelable(false);
        builder.setMessage("Hal ini mungkin terjadi karena akun Anda berlum terdaftar. Periksa juga sambungan data atau internet Anda");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}
