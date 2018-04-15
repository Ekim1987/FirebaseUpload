package com.example.android.firebaseupload.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;
import com.example.android.firebaseupload.adapters.HistoryAdapter;
import com.example.android.firebaseupload.adapters.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private TextView detailPharmacyName;
    private TextView detailPracticeNo;
    private TextView endKilo, startKilo, Name_Detail, message_Detail,invoice_counter;
    DatabaseReference mDatabaseUsers;
    StorageReference storageReference;
    ImageView mSignature;
    TextView invoiceCounter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, "onCreate: Started");


        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("image");

        //initialize
        detailPharmacyName = (TextView) findViewById(R.id.detailPharmacy);
        detailPracticeNo = (TextView) findViewById(R.id.detailPractice);
        endKilo = (TextView) findViewById(R.id.endKM);
        startKilo = (TextView) findViewById(R.id.startKM);
        Name_Detail = (TextView) findViewById(R.id.NameDetail);
        message_Detail = (TextView) findViewById(R.id.messageDetail);
        invoiceCounter = (TextView) findViewById(R.id.invoiceCounterDetail);


//receive data
        Intent intent = this.getIntent();
        String pharmacyName = intent.getExtras().getString("PHARMACY_KEY");
        String practiceNo = intent.getExtras().getString("PRACTICE_KEY");

//bind data
        detailPharmacyName.setText(pharmacyName);
        detailPracticeNo.setText(practiceNo);



        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    ImageUpload imageUpload = postSnapShot.getValue(ImageUpload.class);

                    String customerName = imageUpload.getCustomerName();
                    String message = imageUpload.getMessage();
                    String startKm = imageUpload.getStartKm();
                    String endKm = imageUpload.getEndKm();


                    Name_Detail.setText(customerName);
                    message_Detail.setText(message);
                    startKilo.setText(startKm);
                    endKilo.setText(endKm);

                }
                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

        }


