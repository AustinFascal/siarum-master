package com.asramaum.siarum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import es.dmoral.toasty.Toasty;
import me.anwarshahriar.calligrapher.Calligrapher;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton buttonRegister, buttonSignIn;
    private EditText editTextEmail, editTextName, editTextPhone, editTextNIM, editTextOrigin;
    private ShowHidePasswordEditText editTextPass;
    private CheckBox checkBoxAccountType;
    private RadioButton radioButton;

    private RadioGroup radioGroupGender;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    public String prta_pass_value, genderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        buttonSignIn = (AppCompatButton) findViewById(R.id.buttonSignIn);
        buttonRegister = (AppCompatButton) findViewById(R.id.buttonRegister);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextNIM = (EditText) findViewById(R.id.editTextNIM);
        editTextOrigin = (EditText) findViewById(R.id.editTextOrigin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextPass = (ShowHidePasswordEditText) findViewById(R.id.editTextPass);
        checkBoxAccountType = (CheckBox) findViewById(R.id.checkBoxAccountType);

        /*radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);*/


        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        progressDialog = new ProgressDialog(UserRegister.this);
        progressDialog.setMessage("Mohon tunggu ... ");
        progressDialog.setCancelable(false);
        progressDialog.show();
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

                        Toasty.success(UserRegister.this, "Data fetched successfully!", Toast.LENGTH_SHORT, true).show();

                        getLatestPass();
                        mFirebaseRemoteConfig.activateFetched();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        failedFetchPass(UserRegister.this).show();
                    }
                });
    }

    public void getLatestPass(){
        prta_pass_value = mFirebaseRemoteConfig.getString("prta_pass_value");
    }

    public AlertDialog.Builder failedFetchPass(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Gagal Memuat Data");
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void authProgressInfo(){
        progressDialog.setMessage("Mohon tunggu ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void registerUser(){
        String name = editTextName.getText().toString().trim();
        String nim = editTextNIM.getText().toString().trim();
        String origin = editTextOrigin.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        Boolean accountType = checkBoxAccountType.isChecked();

        /*Boolean typeMale = radioButtonMale.isChecked();
        Boolean typeFemale = radioButtonFemale.isChecked();*/

        if(name.isEmpty()){
            editTextName.setError("Masukkan nama lengkap Anda");
            editTextName.requestFocus();
        } else if(nim.isEmpty() || nim.length()<12){
            editTextNIM.setError("Masukkan NIM Anda dengan benar");
            editTextNIM.requestFocus();
        } else if(origin.isEmpty()){
            editTextOrigin.setError("Masukkan daerah asal Anda dengan benar");
            editTextOrigin.requestFocus();
        } else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Masukkan alamat email Anda dengan benar");
            editTextEmail.requestFocus();
        } else if(phone.isEmpty() || phone.length()>=15 || phone.length()<=7 ){
            editTextPhone.setError("Masukkaa nomor telepon Anda dengan benar");
            editTextPhone.requestFocus();
        } else if(password.isEmpty()){
            editTextPass.setError("Masukkan kata sandi Anda");
            editTextPass.requestFocus();
        } else if(password.length()<8) {
            editTextPass.setError("Kata sandi harus mencakup minimal 8 karakter");
            editTextPass.requestFocus();
        } else{
            if(accountType.toString().trim().equals("true")){
                userPRTA();
            } else{
                userWarga();
            }

            radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);

            // get selected radio button from radioGroup
            int selectedId = radioGroupGender.getCheckedRadioButtonId();
            radioButton = (RadioButton) findViewById(selectedId);

            genderType = radioButton.getText().toString();
        }

    }

    private void userPRTA(){
        //final String passDecrypt = new String(Base64.decode(getString(R.string.prta_access_code), Base64.DEFAULT));

        new MaterialDialog.Builder(this)
                .title("Kata Sandi")
                .content("Masukkan kata sandi khusus untuk Pengurus Rumah Tangga Asrama")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Kata Sandi", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (dialog.getInputEditText().getText().toString().equals(prta_pass_value)){
                            authProgressInfo();
                            userAuth();
                        } else{
                            Toasty.success(UserRegister.this, "Maaf, kata sandi yang Anda masukkan salah", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }).show();
    }

    private void userWarga(){
        authProgressInfo();
        userAuth();
    }

    private void userAuth(){
        final String name = editTextName.getText().toString().trim();
        final String nim = editTextNIM.getText().toString().trim();
        final String origin = editTextOrigin.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPass.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final Boolean accountType = checkBoxAccountType.isChecked();
        final String genderTypePass = genderType;

        buttonRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(UserRegister.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){

                            User user = new User(
                                    name, nim, origin, email, phone, accountType, genderTypePass
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                        registerSuccess(UserRegister.this).show();
                                    } else{
                                        buttonRegister.setEnabled(true);
                                        registerFailed(UserRegister.this).show();
                                    }
                                }
                            });
                        } else{
                            registerFailed(UserRegister.this).show();
                        }
                    }
                });
    }

    public AlertDialog.Builder registerFailed(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Registrasi Gagal");
        builder.setCancelable(false);
        builder.setMessage("Hal ini mungkin terjadi karena Anda telah terdaftar. Periksa juga sambungan data atau internet Anda");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buttonRegister.setEnabled(true);
            }
        });
        return builder;
    }

    public AlertDialog.Builder registerSuccess(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Verifikasi Email");
        builder.setCancelable(false);
        builder.setMessage("Email verifikasi telah berhasil terkirim ke "+ FirebaseAuth.getInstance().getCurrentUser().getEmail()+". Periksa kotak masuk email Anda untuk melakukan verifikasi. Kemudian lakukan login pada aplikasi untuk masuk.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
                finish();
            }
        });
        return builder;
    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }

        if(view == buttonSignIn){
            onBackPressed();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

}
