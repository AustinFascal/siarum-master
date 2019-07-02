package com.asramaum.siarum;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by austi on 1/27/2019.
 */

public class ProfileBottomSheetDialog extends BottomSheetDialogFragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;

    String fullUsername, userFirstName, userLastName, userMail;

    Animation animFadeInFast;
    LinearLayout header_part;

    //TextView currentTemperatureField, detailsField, greetingText, txtQuotes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_account, container, true);

        MaterialRippleLayout myAccountButton = v.findViewById(R.id.myAccountButton);
        MaterialRippleLayout notificationsButton = v.findViewById(R.id.notificationsButton);
        MaterialRippleLayout changeProfileButton = v.findViewById(R.id.changeProfileButton);
        MaterialRippleLayout signOutButton = v.findViewById(R.id.signOutButton);
        MaterialRippleLayout deleteAccountButton = v.findViewById(R.id.deleteAccountButton);

        myAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialog);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
            handleUserFirstName();
        }
    }

    private void handleUserFirstName() {
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
        if (userID != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                GetUserInfo getUserInfo = new GetUserInfo();
                if (ds.child(userID).getValue(GetUserInfo.class) != null) {
                    getUserInfo.setName(ds.child(userID).getValue(GetUserInfo.class).getName());
                    getUserInfo.setEmail(ds.child(userID).getValue(GetUserInfo.class).getEmail());

                    fullUsername = getUserInfo.getName();
                    userMail = getUserInfo.getEmail();
                    //String arr[] = fullUsername.split(" ", 2);
                    //userFirstName = arr[0];
                    //userLastName = arr[1];

                    TextView userNameText = this.getView().getRootView().findViewById(R.id.usernameText);
                    TextView userEmailText = this.getView().getRootView().findViewById(R.id.userEmailText);
                    userNameText.setText(fullUsername);
                    userEmailText.setText(userMail);
                }
            }
        }
        // load the animation
        //animFadeInFast = AnimationUtils.loadAnimation(getApplicationContext(),
                //R.anim.animation_fade_in_fast);
        // set animation listener
        //animFadeInFast.setAnimationListener(this.getView().getContext().bindService());
        // animation for image
        //header_part = (LinearLayout) this.getView().getRootView().findViewById(R.id.header_part);
        // start the animation
       // header_part.setVisibility(View.VISIBLE);
       // header_part.startAnimation(animFadeInFast);

        // Setup current hour for greeting text
        /*Calendar cal = Calendar.getInstance();
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
        }*/

    }

    /*private void handleUserFirstName() {

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
    }*/

    /*private void showData(DataSnapshot dataSnapshot) {

        if (userID != null) {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Rider getUserInfo = new Rider();
                if (ds.child(userID).getValue(Rider.class) != null) {
                    getUserInfo.setName(ds.child(userID).getValue(Rider.class).getName());
                    getUserInfo.setEmail(ds.child(userID).getValue(Rider.class).getEmail());

                    fullUsername = getUserInfo.getName();
                    userMail = getUserInfo.getEmail();
                    //String arr[] = fullUsername.split(" ", 2);
                    //userFirstName = arr[0];
                    //userLastName = arr[1];

                    Toast.makeText(getContext(), fullUsername, Toast.LENGTH_SHORT).show();
                    TextView userNameText =  this.getView().findViewById(R.id.usernameText);
                    //TextView userEmailText = this.getContext().findViewById(R.id.userEmailText);
                    userNameText.setText(fullUsername);
                    //userEmailText.setText(userMail);
                }
            }
        }


    }*/
}
