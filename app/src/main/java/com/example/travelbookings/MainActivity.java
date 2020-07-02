package com.example.travelbookings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelbookings.common.FirebaseUtil;
import com.example.travelbookings.model.TravelDeal;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText txtTitle, txtDescription, txtPrice;

    TravelDeal deals;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUtil.openReferences("deals");
        firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        databaseReference = FirebaseUtil.mDatabaseReference;

        txtTitle = findViewById(R.id.editTextTitle);
        txtDescription = findViewById(R.id.editTextDescription);
        txtPrice = findViewById(R.id.editTextPrice);

        TravelDeal deal = (TravelDeal) getIntent().getSerializableExtra("deal");

        if (deal == null) {
            deal = new TravelDeal();
        }

        this.deals = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu:
                saveDeals();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
         txtTitle.setText("");
         txtPrice.setText("");
         txtDescription.setText("");

         txtTitle.requestFocus();
    }

    private void saveDeals() {
        deals.setTitle(txtTitle.getText().toString());
        deals.setDescription(txtDescription.getText().toString());
        deals.setPrice(txtPrice.getText().toString());

        if (deals.getId() == null) {
            databaseReference.push().setValue(deals);
        } else {
            databaseReference.child(deals.getId()).setValue(deals);
        }
    }

    private void deleteDeal() {
        if (deals == null) {
            Toast.makeText(this, "Please save the deal before delete", Toast.LENGTH_SHORT).show();
        } else {
            databaseReference.child(deals.getId()).removeValue();
        }
    }

    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

}
