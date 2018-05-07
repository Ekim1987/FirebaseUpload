package com.example.android.firebaseupload.fragments;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;

import com.example.android.firebaseupload.fragments.RequestFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";
    private TextView detailPharmacyName;
    private TextView detailPracticeNo;
    private TextView endKilo, startKilo, Name_Detail, message_Detail, invoice_counter;
    private DatabaseReference mDatabaseReference;
    StorageReference storageReference;
    ImageView mSignature;
    TextView invoiceCounter;
    private RequestFragment.OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        detailPracticeNo = (TextView) view.findViewById(R.id.detailPractice);
        endKilo = (TextView) view.findViewById(R.id.endKM);
        startKilo = (TextView) view.findViewById(R.id.startKM);
        Name_Detail = (TextView) view.findViewById(R.id.NameDetail);
        message_Detail = (TextView) view.findViewById(R.id.messageDetail);
        invoiceCounter = (TextView) view.findViewById(R.id.invoiceCounterDetail);
        mSignature = (ImageView) view.findViewById(R.id.imageViewDetail);
        detailPharmacyName = (TextView) view.findViewById(R.id.detailPharmacy);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d(TAG, "onCreate: Started");


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //initialize


//receive data
        Intent intent =getActivity().getIntent();
        String pharmacyName = intent.getExtras().getString("PHARMACY_KEY");
        String practiceNo = intent.getExtras().getString("PRACTICE_KEY");

//bind data
        detailPharmacyName.setText(pharmacyName);
        detailPracticeNo.setText(practiceNo);
//        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
//                        ImageUpload imageUpload = postSnapShot.getValue(ImageUpload.class);
//                        String customerName = imageUpload.getCustomerName();
//                        String message = imageUpload.getMessage();
//                        String startKm = imageUpload.getStartKm();
//                        String endKm = imageUpload.getEndKm();
//                        Name_Detail.setText(customerName);
//                        message_Detail.setText(message);
//                        startKilo.setText(startKm);
//                        endKilo.setText(endKm);
//                    }
//                }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        Query query = mDatabaseReference.child("image").orderByChild("pharmacyName").equalTo(pharmacyName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        //download data from firebase
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

                        Picasso.with(getActivity()).load(imageUri).into(mSignature);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
    }
    }

}


