package com.example.android.firebaseupload.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.firebaseupload.R;
import com.example.android.firebaseupload.activities.Interface.ItemClickListener;

import org.w3c.dom.Text;

/**
 * Created by user on 08/03/2018.
 */

public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView set_pharmacy;
    public TextView set_practice;
    public TextView set_pharmacistName;
    public TextView set_endKm;
    public TextView set_startKm;
    public  ImageView set_ImageView;
    public TextView set_message;


    ItemClickListener itemClickListener;


    public HistoryViewHolder(View itemView) {
        super(itemView);

        set_pharmacy = (TextView) itemView.findViewById(R.id.recyclerViewPharmacyName);
        set_practice = (TextView) itemView.findViewById(R.id.recyclerViewPracticeNo);
        set_startKm=(TextView)itemView.findViewById(R.id.startKM);
        set_endKm=(TextView)itemView.findViewById(R.id.endKM);
        set_ImageView=(ImageView)itemView.findViewById(R.id.imageViewDetail);
        set_pharmacistName=(TextView)itemView.findViewById(R.id.NameDetail);
        set_message=(TextView)itemView.findViewById(R.id.messageDetail);
        set_ImageView=(ImageView)itemView.findViewById(R.id.imageViewDetail);
        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());

    }


    }




