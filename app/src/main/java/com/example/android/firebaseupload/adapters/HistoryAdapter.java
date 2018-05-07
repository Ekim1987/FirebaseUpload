package com.example.android.firebaseupload.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;


import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;


import com.example.android.firebaseupload.activities.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 08/03/2018.
 */
//public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder>  {
//
//
//
//    Context context;
//    ArrayList<ImageUpload> imageUploads;
//    HistoryViewHolder holder;
//    HistoryActivity historyActivity;
//    View itemView;
//    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
//    private ArrayList<DataSnapshot> mSnapshots;
//
//    public HistoryAdapter(Context context, ArrayList<ImageUpload> imageUploads) {
//        this.context = context;
//        this.imageUploads = imageUploads;
//    }
//
//    @Override
//    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
//        holder = new HistoryViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
//        final String PharmacyName = imageUploads.get(position).getPharmacyName();
//        final String PracticeNo = imageUploads.get(position).getPracticeNo();
//        holder.set_pharmacy.setText(PharmacyName);
//        holder.set_practice.setText(PracticeNo);
//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                openDetailActivity(PharmacyName,PracticeNo);
//
//            }
//
//        });
//
//            }
//
//
//
//
//    // holder.set_pharmacy.setText(imageUploads.get(position).getPharmacyName());
//    //holder.set_practice.setText(imageUploads.get(position).getPracticeNo());
//    public void openDetailActivity(String pharmacyName, String practiceNo) {
//
//        intent.putExtra("PHARMACY_KEY", pharmacyName);
//        intent.putExtra("PRACTICE_KEY", practiceNo);
//        context.startActivity(intent);
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return imageUploads.size();
//
//
//
//    }
//}
//
//
//
