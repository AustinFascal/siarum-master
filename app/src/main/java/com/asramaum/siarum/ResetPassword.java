package com.asramaum.siarum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.klinker.android.sliding.SlidingActivity;

import es.dmoral.toasty.Toasty;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ResetPassword extends SlidingActivity {

    EditText email;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    Button sendLink;

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_reset_password);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.finestWhite));
        }

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        progressDialog = new ProgressDialog(this);
        sendLink = (Button)findViewById(R.id.sendLink_btn);

        disableHeader();

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResetRequest();
            }
        });
    }

    private void progressInfo(){
        progressDialog.setMessage("Mohon tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void sendResetRequest(){
        email = (EditText)findViewById(R.id.emailReset);
        final String userEmail = email.getText().toString();

        if(userEmail.equals("")){
            Toasty.error(ResetPassword.this, "Masukkan alamat email", Toast.LENGTH_SHORT, true).show();
        } else{
            progressInfo();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toasty.success(ResetPassword.this, "Link untuk mengganti kata sandi telah berhasil dikirimkan ke "+userEmail+". Periksa kotak masuk email Anda.", Toast.LENGTH_SHORT, true).show();
                        progressDialog.dismiss();
                        onBackPressed();
                    } else{
                        Toasty.success(ResetPassword.this, "Pengiriman link setel ulang kata sandi tidak berhasil. Periksa sambungan internet dan email yang Anda masukkan.", Toast.LENGTH_SHORT, true).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public AlertDialog.Builder dialogSendSuccess(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Berhasil");
        builder.setCancelable(false);
        builder.setMessage("Link untuk mengganti ulang kata sandi telah berhasil dikirimkan ke "+email+". Periksa kotak masuk email Anda.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });
        return builder;
    }

    public AlertDialog.Builder dialogSendFail(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Gagal");
        builder.setCancelable(false);
        builder.setMessage("Pengiriman link setel ulang kata sandi tidak berhasil. Periksa sambungan internet dan email yang Anda masukkan.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }
}
