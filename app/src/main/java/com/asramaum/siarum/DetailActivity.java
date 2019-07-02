package com.asramaum.siarum;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.balysv.materialripple.MaterialRippleLayout;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.klinker.android.sliding.SlidingActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import me.anwarshahriar.calligrapher.Calligrapher;

public class DetailActivity extends SlidingActivity {

    private CoordinatorLayout coordinatorLayout;
    private MaterialRippleLayout scanButtonWrapper;
    private TextView textDescription;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    private ProgressDialog dialog;

    String scannedData;
    String formattedDate;
    String scannerUsername;
    String activityName;

    Button scanBtn, viewDataBtn;
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_detail);
        final Activity activity =this;
        scanBtn = (Button)findViewById(R.id.scan_btn);
        viewDataBtn = (Button)findViewById(R.id.view_data_btn);
        scanButtonWrapper = (MaterialRippleLayout)findViewById(R.id.scanButtonWrapper);
        textDescription = (TextView)findViewById(R.id.textDescription);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
        }

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        final Bundle bundle = getIntent().getExtras();
        final String menuTitleTxt = bundle.getString("1");
        final String sheetLink = bundle.getString("sheetLink");

        TextView txtTitle = (TextView) findViewById(R.id.activity_title);
        ImageView imgTitle = (ImageView) findViewById(R.id.activity_image);

        txtTitle.setText(menuTitleTxt);
        imgTitle.setImageResource(bundle.getInt("2"));

        dialog = new ProgressDialog(DetailActivity.this);

        if (txtTitle.getText().equals("Kajian Studi Ilmiah")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg1);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Keterampilan")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg2);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Open Forum")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg3);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Senam")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg4);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Bersih Toilet")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg5);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Kerja Bakti")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg6);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Pembacaan Surat Al-Kahfi dan Yasin")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg7);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Kajian Islam")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg8);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Maulid Diba'")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg9);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Kegiatan Insidental")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg10);
                }
            }, 500);
        } else if (txtTitle.getText().equals("Lainnya")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setImage(R.drawable.menu_bg11);
                }
            }, 500);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConnected(DetailActivity.this))
                    buildDialog(DetailActivity.this).show();
                else {
                    IntentIntegrator integrator = new IntentIntegrator(activity);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.setPrompt("Posisikan QR Code pada kotak pemindai.");
                    integrator.setBeepEnabled(true);
                    integrator.setCameraId(0);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }
            }
        });
        viewDataBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialogViewType();

//                new FinestWebView.Builder(DetailActivity.this).titleDefault(menuTitleTxt)
//                        .webViewUserAgentString("Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0")
//                        .show(new String(Base64.decode(sheetLink, Base64.DEFAULT)));
                // decrypt link
            }
        });

        handleAccountType();
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

    private void showDialogViewType(){
        final Bundle bundle = getIntent().getExtras();
        final String sheetLink = bundle.getString("sheetLink");
        final String sheetLinkChart = bundle.getString("sheetLinkChart");

        new MaterialStyledDialog.Builder(this)
                .setTitle("Jenis Tampilan")
                .setDescription("Pilih tampilan data yang Anda inginkan")
                .setPositiveText("Tabel")
                .setNegativeText("Grafik")
                .setHeaderDrawable(R.drawable.chartthumb)
                .setCancelable(true)
                .setScrollable(true)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(DetailActivity.this, WebViewActivity.class);
                        i.putExtra("sheetLink", sheetLink);
                        i.putExtra("dataType", "table");
                        startActivity(i);
                    }})
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent(DetailActivity.this, WebViewActivity.class);
                        i.putExtra("sheetLinkChart", sheetLinkChart);
                        i.putExtra("dataType", "chart");
                        startActivity(i);
                    }})
                .show();
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            GetUserInfo getUserInfo = new GetUserInfo();
            getUserInfo.setAccountType(ds.child(userID).getValue(GetUserInfo.class).getAccountType());
            getUserInfo.setName(ds.child(userID).getValue(GetUserInfo.class).getName());

            if(getUserInfo.getAccountType().equals(true)){
                scanButtonWrapper.setVisibility(View.VISIBLE);
                textDescription.setText(R.string.textDescriptionPRTA);
                scannerUsername = getUserInfo.getName();
            } else if (getUserInfo.getAccountType().equals(false)){
                scanButtonWrapper.setVisibility(View.INVISIBLE);
                textDescription.setText(R.string.textDescriptionWarga);
            }
        }
    }

    @Override
    protected void onResume() {
        if(!isConnected(DetailActivity.this))
            buildDialog(DetailActivity.this).show();
        super.onResume();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null) {
            scannedData = result.getContents();
            if (scannedData != null) {
                // Here we need to handle scanned data...
                new SendRequest().execute();
                dialog.setMessage("Mengirim ... ");
                dialog.setCancelable(false);
                dialog.show();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}
        protected String doInBackground(String... arg0) {

            try{
                //Enter script URL Here
                Bundle bundle = getIntent().getExtras();
                String databaseLink = bundle.getString("3");
                String actName = bundle.getString("1");

                //URL url = new URL(new String(Base64.decode(databaseLink, Base64.DEFAULT)));
                URL url = new URL(databaseLink);
                // decrypt link

                activityName = actName;
                Calendar c = Calendar.getInstance();

                System.out.println("Current time =&gt; "+c.getTime());

                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                formattedDate = df.format(c.getTime());

                JSONObject postDataParams = new JSONObject();
                //Passing scanned code as parameter
                postDataParams.put("activityName",activityName);
                postDataParams.put("scannedData",scannedData);
                postDataParams.put("scannerUsername",scannerUsername);
                postDataParams.put("formattedDate", formattedDate);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();

                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";
                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if(!isConnected(DetailActivity.this)){
                dialog.dismiss();

                Snackbar snackbarSendNotice = Snackbar
                        .make(coordinatorLayout, "Pengiriman tidak berhasil. Periksa koneksi internet Anda.", Snackbar.LENGTH_INDEFINITE);
                snackbarSendNotice.show();

                View snackbarView = snackbarSendNotice.getView();
                snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TextView textView = (TextView)snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
            } else{
                dialog.dismiss();

                Snackbar snackbarSendResult = Snackbar
                        .make(coordinatorLayout, scannedData, Snackbar.LENGTH_LONG);
                snackbarSendResult.show();

                View snackbarViewSendResult = snackbarSendResult.getView();
                snackbarViewSendResult.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TextView textViewSendResult = (TextView)snackbarViewSendResult.findViewById(android.support.design.R.id.snackbar_text);
                textViewSendResult.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}