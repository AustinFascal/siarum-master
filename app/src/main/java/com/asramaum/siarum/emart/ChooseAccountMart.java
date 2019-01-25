package com.asramaum.siarum.emart;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.asramaum.siarum.GetUserInfo;
import com.asramaum.siarum.R;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.klinker.android.sliding.SlidingActivity;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ChooseAccountMart extends SlidingActivity {

    private MaterialRippleLayout btnCard1, btnCard2;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    @Override
    public void init(Bundle savedInstanceState) {
        setContent(R.layout.activity_choose_account_mart);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.finestWhite));
        }

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        disableHeader();

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

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

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            GetUserInfo getUserInfo = new GetUserInfo();
            getUserInfo.setAccountType(ds.child(userID).getValue(GetUserInfo.class).getAccountType());


            if(getUserInfo.getAccountType().equals(true)){
                btnCard1 = (MaterialRippleLayout) findViewById(R.id.btn1);
                btnCard1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseAccountMart.this, MartActivity.class);
                        i.putExtra("state", getString(R.string.astra));
                        startActivity(i);
                    }
                });

                btnCard2 = (MaterialRippleLayout) findViewById(R.id.btn2);
                btnCard2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseAccountMart.this, MartActivity.class);
                        i.putExtra("state", getString(R.string.astri));
                        startActivity(i);
                    }
                });
            } else if (getUserInfo.getAccountType().equals(false)){
                btnCard1 = (MaterialRippleLayout) findViewById(R.id.btn1);
                btnCard1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseAccountMart.this, MartActivity.class);
                        i.putExtra("state", getString(R.string.astraWarga));
                        startActivity(i);
                    }
                });

                btnCard2 = (MaterialRippleLayout) findViewById(R.id.btn2);
                btnCard2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ChooseAccountMart.this, MartActivity.class);
                        i.putExtra("state", getString(R.string.astriWarga));
                        startActivity(i);
                    }
                });
            }
        }
    }

}
