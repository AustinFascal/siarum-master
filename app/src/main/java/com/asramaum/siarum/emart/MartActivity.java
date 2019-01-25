package com.asramaum.siarum.emart;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.asramaum.siarum.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MartActivity extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productStockNotePA = db.collection("Product_Stock_Note_PA");
    private CollectionReference productStockNotePI = db.collection("Product_Stock_Note_PI");

    private MartNoteAdapter martNoteAdapter;

    Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mart);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.finestWhite));
        }

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Daftar Produk");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(mToolbar.getTitle());

        mTitle.setTextColor(getResources().getColor(R.color.colorBlack));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this,"google_sans_regular.ttf",true);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Bundle bundleAccount = getIntent().getExtras();
        final String accountState = bundleAccount.getString("state");

        Query queryAstra = productStockNotePA.orderBy("stock", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MartNote> optionsAstra = new FirestoreRecyclerOptions.Builder<MartNote>()
                .setQuery(queryAstra, MartNote.class)
                .build();

        Query queryAstri = productStockNotePI.orderBy("stock", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MartNote> optionsAstri = new FirestoreRecyclerOptions.Builder<MartNote>()
                .setQuery(queryAstri, MartNote.class)
                .build();

        if(accountState.equals("ASTRA")){
            martNoteAdapter = new MartNoteAdapter(optionsAstra);
            FloatingActionButton buttonNewNote = findViewById(R.id.button_add_note);
            buttonNewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MartActivity.this, NewMartNoteActivity.class);
                    i.putExtra("state", getString(R.string.astra));
                    startActivity(i);
                }
            });
        } else if (accountState.equals("ASTRI")){
            martNoteAdapter = new MartNoteAdapter(optionsAstri);
            FloatingActionButton buttonNewNote = findViewById(R.id.button_add_note);
            buttonNewNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MartActivity.this, NewMartNoteActivity.class);
                    i.putExtra("state", getString(R.string.astri));
                    startActivity(i);
                }
            });
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(martNoteAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                martNoteAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        martNoteAdapter.setOnItemClickListener(new MartNoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                MartNote note = documentSnapshot.toObject(MartNote.class);
                String id = documentSnapshot.getId();
                String name = note.getTitle();
                int price = note.getPrice();
                int stock = note.getStock();

                if (accountState.equals("ASTRI")) {
                    Intent i = new Intent(MartActivity.this, NewMartNoteActivity.class);
                    i.putExtra("state", "EDIT_ASTRI");
                    i.putExtra("productId", id);
                    i.putExtra("productName", name);
                    i.putExtra("productPrice", price);
                    i.putExtra("productStock", stock);
                    startActivity(i);
                } else if (accountState.equals("ASTRA")) {
                    Intent i = new Intent(MartActivity.this, NewMartNoteActivity.class);
                    i.putExtra("state", "EDIT_ASTRA");
                    i.putExtra("productId", id);
                    i.putExtra("productName", name);
                    i.putExtra("productPrice", price);
                    i.putExtra("productStock", stock);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        martNoteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        martNoteAdapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
