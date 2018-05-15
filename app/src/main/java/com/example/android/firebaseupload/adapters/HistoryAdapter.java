package com.example.android.firebaseupload.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.android.firebaseupload.Interface.ItemClickListener;
import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;
import com.example.android.firebaseupload.activities.DetailActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 08/03/2018.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryListViewHolder> implements Filterable {
    private Context context;
    private ArrayList<ImageUpload> imageUploads;
    private ArrayList<ImageUpload> filterList;
    private onItemClickListener mListener;
    CustomFilter filter;
    private onItemLongClickListner mLongClickListener;

    public HistoryAdapter(Context context, ArrayList<ImageUpload> imageUploads) {
        this.context = context;
        this.imageUploads = imageUploads;
        this.filterList = imageUploads;

    }

    @Override
    public HistoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new HistoryListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(HistoryListViewHolder holder, int position) {
        ImageUpload imageUpload = imageUploads.get(position);
        holder.pharmacyName.setText(imageUpload.getPharmacyName());
        holder.practiceNumber.setText(imageUpload.getPracticeNo());
    }

    @Override
    public int getItemCount() {
        return imageUploads.size();
    }


    public class HistoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView pharmacyName;
        public TextView practiceNumber;

        public HistoryListViewHolder(View itemView) {
            super(itemView);
            pharmacyName = (TextView) itemView.findViewById(R.id.recyclerViewPharmacyName);
            practiceNumber = (TextView) itemView.findViewById(R.id.recyclerViewPracticeNo);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mLongClickListener.onLongClick(position);
                }

            }
            return false;

        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);


    }

    public interface onItemLongClickListner {
        void onLongClick(int position);
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;

    }

    public void setOnItemLongClickListener(onItemLongClickListner onItemLongClickListener) {
        mLongClickListener = onItemLongClickListener;
    }
//filters searchView data in recycleView.
    @Override
    public Filter getFilter() {
        if (filter==null){
            filter=new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<ImageUpload> filters = new ArrayList<ImageUpload>();

                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getPharmacyName().toUpperCase().contains(constraint)) {
                        ImageUpload imageUpload = new ImageUpload(filterList.get(i).getPharmacyName(), filterList.get(i).getPracticeNo());
                        filters.add(imageUpload);
                    }
                }
                filterResults.count = filters.size();
                filterResults.values = filters;
            } else{
                filterResults.count = filterList.size();
            filterResults.values = filterList;
        }
            return filterResults;
    }
        @Override
            protected void publishResults (CharSequence constraint, FilterResults results){
                imageUploads=(ArrayList<ImageUpload>)results.values;
                notifyDataSetChanged();
            }

            }
        }



