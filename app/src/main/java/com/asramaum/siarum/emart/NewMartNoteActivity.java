package com.asramaum.siarum.emart;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asramaum.siarum.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import me.anwarshahriar.calligrapher.Calligrapher;

public class NewMartNoteActivity extends AppCompatActivity {

    private EditText editProductName;
    private EditText editProductPrice;
    private EditText editProductStock;

    private MartNoteAdapter martNoteAdapter;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mart_note);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.finestWhite));
        }

        Bundle bundle = getIntent().getExtras();
        //String productId = bundle.getString("productId");
        String productName = bundle.getString("productName");
        int productPrice = bundle.getInt("productPrice");
        int productStock = bundle.getInt("productStock");
        String state = bundle.getString("state");


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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

        editProductName = findViewById(R.id.editProductName);
        editProductPrice = findViewById(R.id.editProductPrice);
        editProductStock = findViewById(R.id.editProductStock);

        if (state.equals("EDIT_ASTRA")){
            mToolbar.setTitle("Perbarui detil produk");
            editProductName.setText(productName);
            editProductPrice.setText(Integer.toString(productPrice));
            editProductStock.setText(String.valueOf(productStock));
        } else if (state.equals("EDIT_ASTRI")){
            mToolbar.setTitle("Perbarui detil produk");
            editProductName.setText(productName);
            editProductPrice.setText(String.valueOf(productPrice));
            editProductStock.setText(String.valueOf(productStock));
        } else{
            mToolbar.setTitle("Produk baru");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {
        final Bundle bundle = getIntent().getExtras();
        final String productId = bundle.getString("productId");
        final String state = bundle.getString("state");

        String name = editProductName.getText().toString();

            int price = Integer.parseInt(editProductPrice.getText().toString().trim());
            int stock = Integer.parseInt(editProductStock.getText().toString().trim());

            if (state.equals("ASTRA")){
                CollectionReference dbCollectionRef = FirebaseFirestore.getInstance()
                        .collection("Product_Stock_Note_PA");
                dbCollectionRef.add(new MartNote(name, price, stock));
                Toast.makeText(this, "Produk baru berhasil dimasukkan", Toast.LENGTH_SHORT).show();
                finish();
            } else if (state.equals("ASTRI")){
                CollectionReference dbCollectionRef = FirebaseFirestore.getInstance()
                        .collection("Product_Stock_Note_PI");
                dbCollectionRef.add(new MartNote(name, price, stock));
                Toast.makeText(this, "Produk baru berhasil dimasukkan", Toast.LENGTH_SHORT).show();
                finish();
            } else if (state.equals("EDIT_ASTRA")){
                CollectionReference dbCollectionRef = FirebaseFirestore.getInstance()
                        .collection("Product_Stock_Note_PA");
                dbCollectionRef.document(productId).update("title",name);
                dbCollectionRef.document(productId).update("price",price);
                dbCollectionRef.document(productId).update("stock",stock);
                Toast.makeText(this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else if (state.equals("EDIT_ASTRI")){
                CollectionReference dbCollectionRef = FirebaseFirestore.getInstance()
                        .collection("Product_Stock_Note_PI");
                dbCollectionRef.document(productId).update("title",name);
                dbCollectionRef.document(productId).update("price",price);
                dbCollectionRef.document(productId).update("stock",stock);
                Toast.makeText(this, "Produk berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            }
        }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
