package com.asramaum.siarum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.asramaum.siarum.emart.ChooseAccountMart;
import com.balysv.materialripple.MaterialRippleLayout;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainMenuActivity extends AppCompatActivity {
    private MaterialRippleLayout btnMenuCard1, btnMenuCard2, btnMenuCard3, btnMenuCard4, btnMenuCard5, btnGuide;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    private ProgressDialog dialog;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    public String appVersionValue;
    public String newAppDownloadLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_settings_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });

        dialog = new ProgressDialog(MainMenuActivity.this);
        dialog.setMessage("Mohon tunggu ... ");
        dialog.setCancelable(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        if(!user.isEmailVerified()){
            showDialogUnVerified();
        } else{
            handleAccountType();
        }

        if(!isConnected(MainMenuActivity.this)){
            dialog.dismiss();
            buildDialog(MainMenuActivity.this).show();
        }

        btnMenuCard1 = (MaterialRippleLayout) findViewById(R.id.btn1);
        btnMenuCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, ChooseAccountAttendace.class);
                i.putExtra("state", getString(R.string.astra));
                startActivity(i);
            }
        });

        btnMenuCard2 = (MaterialRippleLayout) findViewById(R.id.btn2);
        btnMenuCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(MainMenuActivity.this, "http://asrama.um.ac.id/events/");
            }
        });

        btnMenuCard3 = (MaterialRippleLayout) findViewById(R.id.btn3);
        btnMenuCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDevelopmentMessage(MainMenuActivity.this).show();
            }
        });

        btnMenuCard4 = (MaterialRippleLayout) findViewById(R.id.btn4);
        btnMenuCard4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenuActivity.this, ChooseAccountMart.class);
                i.putExtra("state", getString(R.string.astra));
                startActivity(i);
            }
        });

        btnMenuCard5 = (MaterialRippleLayout) findViewById(R.id.btn5);
        btnMenuCard5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(MainMenuActivity.this, "http://asrama.um.ac.id/tentang-prta/");
            }
        });

        btnGuide = (MaterialRippleLayout) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(MainMenuActivity.this, "http://asrama.um.ac.id/siarum/");
            }
        });
    }

    protected void onPostResume() {
        super.onPostResume();

        //FIREBASE REMOTE CONFIG
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());
        long cacheExpiration = 0;

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Once the config is successfully fetched it must be activated before newly fetched values are returned.
                        mFirebaseRemoteConfig.activateFetched();
                        checkAppVersion();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        buildDialog(MainMenuActivity.this).show();
                    }
                });
    }

    private void checkAppVersion() {
        appVersionValue = mFirebaseRemoteConfig.getString("appVersionValue");
        newAppDownloadLink = mFirebaseRemoteConfig.getString("newAppDownloadLink");
        if (!appVersionValue.equals(getString(R.string.app_version_))){
            updateAppForce(MainMenuActivity.this).show();
        }
    }

    public AlertDialog.Builder updateAppForce (Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Versi Terbaru Tersedia");
        builder.setCancelable(false);
        builder.setMessage("Versi aplikasi SIARUM terbaru telah tersedia. " +
                "Untuk melanjutkan, perbarui aplikasi ke versi terkini terlebih dahulu.");
        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openBrowser(MainMenuActivity.this, newAppDownloadLink);
                finish();
            }
        });
        return builder;
    }


    public static void openBrowser(Context context, String url) {

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }



    private void handleAccountType() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //leave it empty
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            GetUserInfo getUserInfo = new GetUserInfo();
            getUserInfo.setAccountType(ds.child(userID).getValue(GetUserInfo.class).getAccountType());

            if(getUserInfo.getAccountType().equals(true)){
                showDialogPRTAAccountInfo();
            } else if (getUserInfo.getAccountType().equals(false)){
                showDialogWargaAccountInfo();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void showDialogPRTAAccountInfo(){
        dialog.dismiss();
        new MaterialStyledDialog.Builder(this)
                .setTitle("Akun PRTA")
                .setDescription("Anda masuk sebagai Pengurus Rumah Tangga Asrama Mahasiswa UM.")
                .setPositiveText("OK")
                .setCancelable(false)
                .setScrollable(true)
                .setHeaderDrawable(R.drawable.auth_id)
                .show();
    }
    private void showDialogWargaAccountInfo(){
        dialog.dismiss();
        new MaterialStyledDialog.Builder(this)
                .setTitle("Akun Warga")
                .setDescription("Anda masuk sebagai Warga Asrama Mahasiswa UM.")
                .setPositiveText("OK")
                .setCancelable(false)
                .setScrollable(true)
                .setHeaderDrawable(R.drawable.auth_id)
                .show();
    }

    private void showDialogUnVerified(){
        dialog.dismiss();
        new MaterialStyledDialog.Builder(this)
                .setTitle("Email Belum Terverifikasi")
                .setDescription("Maaf, Anda masih belum dapat menggunakan aplikasi ini. Verifikasi akun Anda terlebih dahulu. Periksa kotak masuk email Anda untuk melakukan verifikasi.")
                .setPositiveText("OK")
                .setNegativeText("Kirim Ulang")
                .setHeaderDrawable(R.drawable.app_logo)
                .setCancelable(false)
                .setScrollable(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(MainMenuActivity.this, UserLogin.class));
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }})
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        resendVerification();
                    }})
                .show();
    }

    private void resendVerification(){
        new MaterialStyledDialog.Builder(this)
                .setTitle("Kirim Ulang Verifikasi")
                .setDescription("Jika Anda tidak mendapat pesan email verifikasi akun, Anda dapat mengirimnya kembali. Apakah Anda yakin ingin mengirim ulang?")
                .setPositiveText("Kirim")
                .setNegativeText("Batal")
                .setCancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        FirebaseAuth.getInstance().getCurrentUser()
                                .sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(MainMenuActivity.this, "Email berhasil dikirim ulang",Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(new Intent(MainMenuActivity.this, UserLogin.class));
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }})
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showDialogUnVerified();
                    }})
                .show();
    }

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Tidak Ada Sambungan Internet");
        builder.setCancelable(false);
        builder.setMessage("Anda membutuhkan koneksi data atau WiFi untuk mengaksesnya. Periksa kembali sambungan internet Anda.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }


    public AlertDialog.Builder onDevelopmentMessage(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Fitur Belum Tersedia");
        builder.setCancelable(false);
        builder.setMessage("Mohon maaf, fitur ini masih dalam masa pengembangan.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
