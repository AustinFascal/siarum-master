package com.asramaum.siarum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.anwarshahriar.calligrapher.Calligrapher;

public class AttendanceActivity extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    List<MenuData> mFlowerList;
    MenuData mFlowerData;
    private ProgressDialog dialog;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

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
                startActivity(new Intent(AttendanceActivity.this, SettingsActivity.class));
            }
        });

        mRecyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(AttendanceActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        dialog = new ProgressDialog(AttendanceActivity.this);
        dialog.setMessage("Mohon tunggu ... ");
        dialog.setCancelable(false);
        dialog.show();
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

                        Toasty.success(AttendanceActivity.this, "Data berhasil dimuat", Toast.LENGTH_SHORT, true).show();

                        dialog.dismiss();
                        getSheetLink();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dialog.dismiss();
                        buildDialog(AttendanceActivity.this).show();
                    }
                });
    }

    public AlertDialog.Builder buildDialog(Context c) {
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

    private void getSheetLink() {
        Bundle bundleAccount = getIntent().getExtras();
        final String accountState = bundleAccount.getString("state");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Absensi Kegiatan");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        TextView mSubTitle = (TextView) mToolbar.findViewById(R.id.toolbar_subtitle);
        mTitle.setText(mToolbar.getTitle());

        mTitle.setTextColor(getResources().getColor(R.color.colorBlack));
        mSubTitle.setTextColor(getResources().getColor(R.color.colorBlack));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        String sheetLink1L = mFirebaseRemoteConfig.getString("sheetLink1L"),
                sheetLink2L = mFirebaseRemoteConfig.getString("sheetLink2L"),
                sheetLink3L = mFirebaseRemoteConfig.getString("sheetLink3L"),
                sheetLink4L = mFirebaseRemoteConfig.getString("sheetLink4L"),
                sheetLink5L = mFirebaseRemoteConfig.getString("sheetLink5L"),
                sheetLink6L = mFirebaseRemoteConfig.getString("sheetLink6L"),
                sheetLink7L = mFirebaseRemoteConfig.getString("sheetLink7L"),
                sheetLink8L = mFirebaseRemoteConfig.getString("sheetLink8L"),
                sheetLink9L = mFirebaseRemoteConfig.getString("sheetLink9L"),
                sheetLink10L = mFirebaseRemoteConfig.getString("sheetLink10L"),
                sheetLink11L = mFirebaseRemoteConfig.getString("sheetLink11L");

        mFlowerList = new ArrayList<>();

        if (accountState.equals("ASTRA")){
            mToolbar.setSubtitle("PRTA Asrama Putra");
            mSubTitle.setText(mToolbar.getSubtitle());

            mFlowerData = new MenuData(getString(R.string.menuTitleTxt1L), R.drawable.menu_bg1, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink1L, getString(R.string.sheetLink1LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt2L), R.drawable.menu_bg2, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink2L, getString(R.string.sheetLink2LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt3L), R.drawable.menu_bg3, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink3L, getString(R.string.sheetLink3LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt4L), R.drawable.menu_bg4, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink4L, getString(R.string.sheetLink4LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt5L), R.drawable.menu_bg5, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink5L, getString(R.string.sheetLink5LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt6L), R.drawable.menu_bg6, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink6L, getString(R.string.sheetLink6LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt7L), R.drawable.menu_bg7, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink7L, getString(R.string.sheetLink7LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt8L), R.drawable.menu_bg8, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink8L, getString(R.string.sheetLink8LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt9L), R.drawable.menu_bg9, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink9L, getString(R.string.sheetLink9LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt10L), R.drawable.menu_bg10, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink10L, getString(R.string.sheetLink10LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt11L), R.drawable.menu_bg11, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    sheetLink11L, getString(R.string.sheetLink11LChart));
            mFlowerList.add(mFlowerData);
            MyAdapter myAdapter = new MyAdapter(AttendanceActivity.this, mFlowerList);
            mRecyclerView.setAdapter(myAdapter);

        } else if (accountState.equals("ASTRI")){
            mToolbar.setSubtitle("PRTA Asrama Putri");
            mSubTitle.setText(mToolbar.getSubtitle());

            mFlowerData = new MenuData(getString(R.string.menuTitleTxt1P), R.drawable.menu_bg1, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink1P), getString(R.string.sheetLink1PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt2P), R.drawable.menu_bg2, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink2P), getString(R.string.sheetLink2PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt3P), R.drawable.menu_bg3, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink3P), getString(R.string.sheetLink3PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt4P), R.drawable.menu_bg4, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink4P), getString(R.string.sheetLink4PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt5P), R.drawable.menu_bg5, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink5P), getString(R.string.sheetLink5PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt6P), R.drawable.menu_bg6, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink6P), getString(R.string.sheetLink6PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt7P), R.drawable.menu_bg7, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink7P), getString(R.string.sheetLink7PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt8P), R.drawable.menu_bg8, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink8P), getString(R.string.sheetLink8PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt9P), R.drawable.menu_bg9, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink9P), getString(R.string.sheetLink9PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt10P), R.drawable.menu_bg10, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink10P), getString(R.string.sheetLink10PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt11P), R.drawable.menu_bg11, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                    getString(R.string.sheetLink11P), getString(R.string.sheetLink11PChart));
            mFlowerList.add(mFlowerData);
            MyAdapter myAdapter = new MyAdapter(AttendanceActivity.this, mFlowerList);
            mRecyclerView.setAdapter(myAdapter);
        } else if (accountState.equals("ASTRA_WARGA")){
            mToolbar.setSubtitle("Warga Asrama Putra");
            mSubTitle.setText(mToolbar.getSubtitle());

            mFlowerData = new MenuData(getString(R.string.menuTitleTxt1L), R.drawable.menu_bg1, "",
                    sheetLink1L, getString(R.string.sheetLink1LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt2L), R.drawable.menu_bg2, "",
                    sheetLink2L, getString(R.string.sheetLink2LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt3L), R.drawable.menu_bg3,"",
                    sheetLink3L, getString(R.string.sheetLink3LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt4L), R.drawable.menu_bg4,"",
                    sheetLink4L, getString(R.string.sheetLink4LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt5L), R.drawable.menu_bg5, "",
                    sheetLink5L, getString(R.string.sheetLink5LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt6L), R.drawable.menu_bg6, "",
                    sheetLink6L, getString(R.string.sheetLink6LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt7L), R.drawable.menu_bg7,"",
                    sheetLink7L, getString(R.string.sheetLink7LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt8L), R.drawable.menu_bg8, "",
                    sheetLink8L, getString(R.string.sheetLink8LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt9L), R.drawable.menu_bg9, "",
                    sheetLink9L, getString(R.string.sheetLink9LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt10L), R.drawable.menu_bg10, "",
                    sheetLink10L, getString(R.string.sheetLink10LChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt11L), R.drawable.menu_bg11, "",
                    sheetLink11L, getString(R.string.sheetLink11LChart));
            mFlowerList.add(mFlowerData);
            MyAdapter myAdapter = new MyAdapter(AttendanceActivity.this, mFlowerList);
            mRecyclerView.setAdapter(myAdapter);

        } else if (accountState.equals("ASTRI_WARGA")){
            mToolbar.setSubtitle("Warga Asrama Putri");
            mSubTitle.setText(mToolbar.getSubtitle());

            mFlowerData = new MenuData(getString(R.string.menuTitleTxt1P), R.drawable.menu_bg1, "",
                    getString(R.string.sheetLink1P), getString(R.string.sheetLink1PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt2P), R.drawable.menu_bg2, "",
                    getString(R.string.sheetLink2P), getString(R.string.sheetLink2PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt3P), R.drawable.menu_bg3, "",
                    getString(R.string.sheetLink3P), getString(R.string.sheetLink3PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt4P), R.drawable.menu_bg4,"",
                    getString(R.string.sheetLink4P), getString(R.string.sheetLink4PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt5P), R.drawable.menu_bg5, "",
                    getString(R.string.sheetLink5P), getString(R.string.sheetLink5PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt6P), R.drawable.menu_bg6, "",
                    getString(R.string.sheetLink6P), getString(R.string.sheetLink6PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt7P), R.drawable.menu_bg7,"",
                    getString(R.string.sheetLink7P), getString(R.string.sheetLink7PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt8P), R.drawable.menu_bg8, "",
                    getString(R.string.sheetLink8P), getString(R.string.sheetLink8PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt9P), R.drawable.menu_bg9, "",
                    getString(R.string.sheetLink9P), getString(R.string.sheetLink9PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt10P), R.drawable.menu_bg10,"",
                    getString(R.string.sheetLink10P), getString(R.string.sheetLink10PChart));
            mFlowerList.add(mFlowerData);
            mFlowerData = new MenuData(getString(R.string.menuTitleTxt11P), R.drawable.menu_bg11, "",
                    getString(R.string.sheetLink11P), getString(R.string.sheetLink11PChart));
            mFlowerList.add(mFlowerData);

            MyAdapter myAdapter = new MyAdapter(AttendanceActivity.this, mFlowerList);
            mRecyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
