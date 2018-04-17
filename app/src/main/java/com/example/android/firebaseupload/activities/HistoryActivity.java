package com.example.android.firebaseupload.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;

import com.example.android.firebaseupload.activities.Interface.ItemClickListener;
import com.example.android.firebaseupload.adapters.HistoryAdapter;
import com.example.android.firebaseupload.adapters.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.android.firebaseupload.R.id.pharmacyName;
import static com.example.android.firebaseupload.R.id.practiceNo;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    DataSnapshot dataSnapshot;
    ArrayList<ImageUpload> mImageUpload = new ArrayList<>();
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("History");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mImageUpload = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //progressDialog = new ProgressDialog(this);
        // progressDialog.setMessage("Loading");
        //progressDialog.show();
        databaseReference = FirebaseDatabase.getInstance().getReference(InvoiceActivity.FB_DATABASE_PATH);
        databaseReference.keepSynced(true);
        //progressDialog.dismiss();
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageUpload, HistoryViewHolder>(ImageUpload.class, R.layout.image_item, HistoryViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(HistoryViewHolder viewHolder, final ImageUpload model, final int position) {
                viewHolder.set_pharmacy.setText(model.getPharmacyName());
                viewHolder.set_practice.setText(model.getPracticeNo());
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                        builder.setMessage("Do you want to delete this data?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int selectedItems = position;
                                firebaseRecyclerAdapter.getRef(selectedItems).removeValue();
                                firebaseRecyclerAdapter.notifyItemRemoved(selectedItems);
                                recyclerView.invalidate();
                                onStart();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Confirm");
                        dialog.show();
                        return false;
                    }
                });
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                        intent.putExtra("PHARMACY_KEY", model.getPharmacyName());
                        intent.putExtra("PRACTICE_KEY", model.getPracticeNo());
                        HistoryActivity.this.startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("Pharmacy Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Query Q = databaseReference.orderByChild("pharmacyName").startAt(newText).endAt(newText + "\uf8ff");

                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ImageUpload, HistoryViewHolder>(ImageUpload.class, R.layout.image_item, HistoryViewHolder.class, Q) {
                    @Override
                    protected void populateViewHolder(HistoryViewHolder viewHolder, final ImageUpload model, final int position) {
                        viewHolder.set_pharmacy.setText(model.getPharmacyName());
                        viewHolder.set_practice.setText(model.getPracticeNo());
                        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                                builder.setMessage("Do you want to delete this data?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;
                                        firebaseRecyclerAdapter.getRef(selectedItems).removeValue();
                                        firebaseRecyclerAdapter.notifyItemRemoved(selectedItems);
                                        recyclerView.invalidate();
                                        onStart();
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.setTitle("Confirm");
                                dialog.show();

                                return false;
                            }
                        });
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                                intent.putExtra("PHARMACY_KEY", model.getPharmacyName());
                                intent.putExtra("PRACTICE_KEY", model.getPracticeNo());
                                HistoryActivity.this.startActivity(intent);
                            }
                        });
                    }
                };
                recyclerView.setAdapter(firebaseRecyclerAdapter);
                return false;
            }
        });
        return true;

    }
}







