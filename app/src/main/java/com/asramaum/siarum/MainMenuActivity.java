package com.asramaum.siarum;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import com.asramaum.siarum.adapter.MainMenuViewPagerAdapter;
import com.asramaum.siarum.adapter.MyFarmViewPagerAdapter;
import com.asramaum.siarum.emart.ChooseAccountMart;
import com.asramaum.siarum.model.MainMenuViewPagerModel;
import com.asramaum.siarum.model.MyFarmViewPagerModel;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.visuality.f32.temperature.TemperatureUnit;
import com.visuality.f32.weather.data.entity.Weather;
import com.visuality.f32.weather.manager.WeatherManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import me.anwarshahriar.calligrapher.Calligrapher;

public class MainMenuActivity extends AppCompatActivity implements LocationListener {
    private MaterialRippleLayout btnMenuCard1, btnMenuCard2, btnMenuCard3, btnMenuCard4, btnMenuCard5, btnGuide;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    private RecyclerView recyclerView;

    private ProgressDialog dialog;

    FirebaseRemoteConfig mFirebaseRemoteConfig;

    LinearLayout btnBottomSheetAccount;
    Button btnBottomSheetSettings;

    //public String appVersionValue;
    //public String newAppDownloadLink;

    ViewPager viewPagerMainMenu;
    ViewPager viewPagerMyFarm;
    MainMenuViewPagerAdapter adapterViewPagerMainMenu;
    MyFarmViewPagerAdapter adapterViewPagerMyFarm;
    List<MyFarmViewPagerModel> myFarmViewPagerModels;
    List<MainMenuViewPagerModel> mainMenuViewPagerModels;

    Double lat = 37.422, lng = -122.084;
    LocationManager locationManager;
    TextView currentTemperatureField, detailsField, greetingText, txtQuotes;
    ImageView weatherIcon, imageQuote;

    String fullUsername, userFirstName, userLastName, userMail;
    public String appVersionValue, newAppDownloadLink,
            weather_icon_50n, weather_icon_50d, weather_icon_13n, weather_icon_13d,
            weather_icon_11n, weather_icon_11d, weather_icon_10n, weather_icon_10d,
            weather_icon_09n, weather_icon_09d, weather_icon_04n, weather_icon_04d,
            weather_icon_03n, weather_icon_03d, weather_icon_02n, weather_icon_02d,
            weather_icon_01n, weather_icon_01d;

    // TODO Give attribution link for OpenWeather API
    String OPEN_WEATHER_MAP_API = "868bd94de047f6ba27cef66c0704dc86";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorGrey));
        }

        // Initialize components
        txtQuotes = findViewById(R.id.txtQuotes);
        imageQuote = findViewById(R.id.image_quote);
        greetingText = findViewById(R.id.greetingText);
        detailsField = findViewById(R.id.weatherDetails);
        currentTemperatureField = findViewById(R.id.currentTemp);
        weatherIcon = findViewById(R.id.currentTempIcon);


        // Getting user's location
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 500L, 250.0f, this);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            lng = location.getLongitude();
            lat = location.getLatitude();
        }

        // Adding new models for viewpagers and set adapter
        myFarmViewPagerModels = new ArrayList<>();
        mainMenuViewPagerModels = new ArrayList<>();

        /*String sheetLink1L = mFirebaseRemoteConfig.getString("sheetLink1L"),
                sheetLink2L = mFirebaseRemoteConfig.getString("sheetLink2L"),
                sheetLink3L = mFirebaseRemoteConfig.getString("sheetLink3L"),
                sheetLink4L = mFirebaseRemoteConfig.getString("sheetLink4L"),
                sheetLink5L = mFirebaseRemoteConfig.getString("sheetLink5L"),
                sheetLink6L = mFirebaseRemoteConfig.getString("sheetLink6L"),
                sheetLink7L = mFirebaseRemoteConfig.getString("sheetLink7L"),
                sheetLink8L = mFirebaseRemoteConfig.getString("sheetLink8L"),
                sheetLink9L = mFirebaseRemoteConfig.getString("sheetLink9L"),
                sheetLink10L = mFirebaseRemoteConfig.getString("sheetLink10L"),
                sheetLink11L = mFirebaseRemoteConfig.getString("sheetLink11L");*/

        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Open Forum", "Senin (Kondisional)",
                R.drawable.menu_bg3, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Kajian Studi Ilmiah", "Selasa",
                R.drawable.menu_bg1, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "", "-"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Maulid Diba'", "Selasa",
                R.drawable.menu_bg9, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Kajian Islam", "Rabu",
                R.drawable.menu_bg8, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Keterampilan", "Rabu",
                R.drawable.menu_bg2, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "", "Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Baca Surat Al-Kahfi dan Yasin", "Kamis",
                R.drawable.menu_bg7, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Bersih Toilet", "Kamis",
                R.drawable.menu_bg5, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Senam", "Minggu (Kondisional)",
                R.drawable.menu_bg4, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Kerja Bakti", "Minggu (Kondisional)",
                R.drawable.menu_bg6, "https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel("Kegiatan Insidental", "Kondisional",
                R.drawable.menu_bg10,"https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));
        myFarmViewPagerModels.add(new MyFarmViewPagerModel( "Lainnya", "Kondisional",
                R.drawable.menu_bg11,"https://script.google.com/macros/s/AKfycbyMB6LP_Lr1ccXZHkT52-RkV03NW5YdYAYuYNgeLUvg1rzK3mO_/exec",
                "","Asrama Putra"));


        mainMenuViewPagerModels.add(new MainMenuViewPagerModel(
                R.drawable.main_menu_profit_simulation, "E-Mart", "Belanja menjadi lebih mudah"));
        mainMenuViewPagerModels.add(new MainMenuViewPagerModel(
                R.drawable.main_menu_plant_schedule, "Pendaftaran PRTA", "Ingin bergabung menjadi PRTA? Daftar sekarang!"));
        mainMenuViewPagerModels.add(new MainMenuViewPagerModel(
                R.drawable.main_menu_harvest_schedule, "Jadwal Kegiatan Asrama", "Lihat jadwal kegiatan harian Asrama UM"));
        mainMenuViewPagerModels.add(new MainMenuViewPagerModel(
                R.drawable.main_menu_scan_desease, "Berita Terkini", "Ketahui berita terkini seputar Asrama UM"));

        adapterViewPagerMyFarm = new MyFarmViewPagerAdapter(myFarmViewPagerModels, this);
        adapterViewPagerMainMenu = new MainMenuViewPagerAdapter(mainMenuViewPagerModels, this);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setImageResource(R.drawable.ic_settings_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
            }
        });

        // Bottom sheet actions
        btnBottomSheetSettings = findViewById(R.id.btnBottomSheetSettings);
        btnBottomSheetSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsBottomSheetDialog settingsBottomSheetDialog = new SettingsBottomSheetDialog();
                settingsBottomSheetDialog.show(getSupportFragmentManager(), "settingsBottomSheetDialog");
                //bottomSheetSettings.show();
            }
        });
        btnBottomSheetAccount = findViewById(R.id.btnBottomSheetAccount);
        btnBottomSheetAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileBottomSheetDialog profileBottomSheetDialog = new ProfileBottomSheetDialog();
                profileBottomSheetDialog.show(getSupportFragmentManager(), "profileBottomSheetDialog");
                //bottomSheetAccountOption.show();
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
            handleUserFirstName();
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

        /*btnGuide = (MaterialRippleLayout) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBrowser(MainMenuActivity.this, "http://asrama.um.ac.id/siarum/");
            }
        });*/
    }

    private void handleUserFirstName() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotHandleName) {
                showDataName(dataSnapshotHandleName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //leave it empty
            }
        });
    }

    private void showDataName(DataSnapshot dataSnapshotHandleName) {
        if (userID != null) {
            for (DataSnapshot ds : dataSnapshotHandleName.getChildren()) {
                GetUserInfo getUserInfo = new GetUserInfo();
                if (ds.child(userID).getValue(GetUserInfo.class) != null) {
                    getUserInfo.setName(ds.child(userID).getValue(GetUserInfo.class).getName());
                    getUserInfo.setEmail(ds.child(userID).getValue(GetUserInfo.class).getEmail());

                    fullUsername = getUserInfo.getName();
                    userMail = getUserInfo.getEmail();
                    String arr[] = fullUsername.split(" ", 2);
                    userFirstName = arr[0];
                    userLastName = arr[1];
                }
            }
        }
        // load the animation
        /*animFadeInFast = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.animation_fade_in_fast);
        // set animation listener
        animFadeInFast.setAnimationListener(this);
        // animation for image
        header_part = (LinearLayout) findViewById(R.id.header_part);
        // start the animation
        header_part.setVisibility(View.VISIBLE);
        header_part.startAnimation(animFadeInFast);*/

        // Setup current hour for greeting text
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("k");
        int formattedDate = Integer.parseInt(String.valueOf(dateFormat.format(date)));

        if (formattedDate > 0 && formattedDate <= 11) {
            greetingText.setText("Selamat Pagi " + userFirstName + "!");
        } else if (formattedDate > 11 && formattedDate <= 15) {
            greetingText.setText("Selamat Siang " + userFirstName + "!");
        } else if (formattedDate > 15 && formattedDate <= 19) {
            greetingText.setText("Selamat Sore " + userFirstName + "!");
        } else if (formattedDate > 19 && formattedDate <= 24) {
            greetingText.setText("Selamat Malam " + userFirstName + "!");
        } else {
            greetingText.setText("gagal_memuat_data");
        }

    }

/*
    // Location handler
    public void taskLoadUp(String lat, String lng) {
        if (WeatherFunction.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(lat, lng);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }*/
/*
    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onLocationChanged(Location location) {
        // New lat and lng when location changed
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainMenuActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String xml = WeatherFunction.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + args[0] +
                    "&lon=" + args[1] + "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject icon = json.getJSONArray("weather").getJSONObject(0);
                    /*JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));

                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(WeatherFunction.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(),icon.getString("icon"),Toast.LENGTH_SHORT).show();
                    Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(weatherIcon);*/

                    // Set weather icon
                    weather_icon_01d = mFirebaseRemoteConfig.getString("weather_icon_01d");
                    weather_icon_01n = mFirebaseRemoteConfig.getString("weather_icon_01n");
                    weather_icon_02d = mFirebaseRemoteConfig.getString("weather_icon_02d");
                    weather_icon_02n = mFirebaseRemoteConfig.getString("weather_icon_02n");
                    weather_icon_03d = mFirebaseRemoteConfig.getString("weather_icon_03d");
                    weather_icon_03n = mFirebaseRemoteConfig.getString("weather_icon_03n");
                    weather_icon_04d = mFirebaseRemoteConfig.getString("weather_icon_04d");
                    weather_icon_04n = mFirebaseRemoteConfig.getString("weather_icon_04n");
                    weather_icon_09d = mFirebaseRemoteConfig.getString("weather_icon_09d");
                    weather_icon_09n = mFirebaseRemoteConfig.getString("weather_icon_09n");
                    weather_icon_10d = mFirebaseRemoteConfig.getString("weather_icon_10d");
                    weather_icon_10n = mFirebaseRemoteConfig.getString("weather_icon_10n");
                    weather_icon_11d = mFirebaseRemoteConfig.getString("weather_icon_11d");
                    weather_icon_11n = mFirebaseRemoteConfig.getString("weather_icon_11n");
                    weather_icon_13d = mFirebaseRemoteConfig.getString("weather_icon_13d");
                    weather_icon_13n = mFirebaseRemoteConfig.getString("weather_icon_14n");
                    weather_icon_50d = mFirebaseRemoteConfig.getString("weather_icon_50d");
                    weather_icon_50n = mFirebaseRemoteConfig.getString("weather_icon_50n");

                    String iconID = icon.getString("icon");
                    detailsField.setText("Cuaca hari ini " + details.getString("description").toLowerCase(Locale.US));
                    if (iconID.contains("01d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_01d).into(weatherIcon);
                    } else if (iconID.contains("01n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_01n).into(weatherIcon);
                    } else if (iconID.contains("02d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_02d).into(weatherIcon);
                    } else if (iconID.contains("02n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_02n).into(weatherIcon);
                    } else if (iconID.contains("03d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_03d).into(weatherIcon);
                    } else if (iconID.contains("03n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_03n).into(weatherIcon);
                    } else if (iconID.contains("04d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_04d).into(weatherIcon);
                    } else if (iconID.contains("04n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_04n).into(weatherIcon);
                    } else if (iconID.contains("09d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_09d).into(weatherIcon);
                    } else if (iconID.contains("09n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_09n).into(weatherIcon);
                    } else if (iconID.contains("10d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_10d).into(weatherIcon);
                    } else if (iconID.contains("10n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_10n).into(weatherIcon);
                    } else if (iconID.contains("11d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_11d).into(weatherIcon);
                    } else if (iconID.contains("11n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_11n).into(weatherIcon);
                    } else if (iconID.contains("13d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_13d).into(weatherIcon);
                    } else if (iconID.contains("13n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_13n).into(weatherIcon);
                    } else if (iconID.contains("50d")) {
                        Glide.with(getApplicationContext()).load(weather_icon_50d).into(weatherIcon);
                    } else if (iconID.contains("50n")) {
                        Glide.with(getApplicationContext()).load(weather_icon_50n).into(weatherIcon);
                    }

                    dialog.dismiss();

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }
    }
/*
    private void checkAppVersion() {
        appVersionValue = mFirebaseRemoteConfig.getString("appVersionValue");
        newAppDownloadLink = mFirebaseRemoteConfig.getString("newAppDownloadLink");
        if (!appVersionValue.equals(getString(R.string.app_version_))){
            updateAppForce(MainMenuActivity.this).show();
        }
    }*/

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
                getSheetLink();
            } else if (getUserInfo.getAccountType().equals(false)){
                showDialogWargaAccountInfo();
            }
        }
    }

    private void getSheetLink() {



        //Bundle bundleAccount = getIntent().getExtras();
        //final String accountState = bundleAccount.getString("state");

        /*viewPagerMyFarm = findViewById(R.id.viewPagerMyFarm);
        viewPagerMyFarm.setAdapter(adapterViewPagerMyFarm);
        viewPagerMyFarm.setPadding(20, 0, 555, 0);
        viewPagerMyFarm.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // View pager menu for main menu
        viewPagerMainMenu = findViewById(R.id.viewPagerMainMenu);
        viewPagerMainMenu.setAdapter(adapterViewPagerMainMenu);
        viewPagerMainMenu.setPadding(20, 0, 130, 0);
        viewPagerMainMenu.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        /*if (accountState.equals("ASTRA")){
            // View pager menu for my farm


        } else if (accountState.equals("ASTRI")){

        } else if (accountState.equals("ASTRA_WARGA")){

        } else if (accountState.equals("ASTRI_WARGA")){

        }*/
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

    private void showDialogPRTAAccountInfo() {
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
