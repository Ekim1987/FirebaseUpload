package com.example.android.firebaseupload.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private TextView detailPharmacyName;
    private TextView detailPracticeNo;
    private TextView endKilo, startKilo, Name_Detail, message_Detail, invoice_counter;
    private DatabaseReference mDatabaseUsers;
    private StorageReference storageReference;
    private ImageView mSignature;
    private TextView invoiceCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //initialize
        detailPharmacyName = (TextView) findViewById(R.id.detailPharmacy);
        detailPracticeNo = (TextView) findViewById(R.id.detailPractice);
        endKilo = (TextView) findViewById(R.id.endKM);
        startKilo = (TextView) findViewById(R.id.startKM);
        Name_Detail = (TextView) findViewById(R.id.NameDetail);
        message_Detail = (TextView) findViewById(R.id.messageDetail);
        invoiceCounter = (TextView) findViewById(R.id.invoiceCounterDetail);
        mSignature = (ImageView) findViewById(R.id.imageViewDetail);

//receive data
        Intent intent = this.getIntent();
        String pharmacyName = intent.getExtras().getString("PHARMACY_KEY");
        String practiceNo = intent.getExtras().getString("PRACTICE_KEY");

//bind data
        detailPharmacyName.setText(pharmacyName);
        detailPracticeNo.setText(practiceNo);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("image").orderByChild("pharmacyName").equalTo(pharmacyName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"

                        ImageUpload imageUpload = issue.getValue(ImageUpload.class);
                        String customerName = imageUpload.getCustomerName();
                        String message = imageUpload.getMessage();
                        String startKm = imageUpload.getStartKm();
                        String endKm = imageUpload.getEndKm();
                        String imageUri = imageUpload.getUri();
                        Name_Detail.setText(customerName);
                        message_Detail.setText(message);
                        startKilo.setText(startKm);
                        endKilo.setText(endKm);


                        Picasso.with(DetailActivity.this).load(imageUri).fit().centerCrop().into(mSignature);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}





