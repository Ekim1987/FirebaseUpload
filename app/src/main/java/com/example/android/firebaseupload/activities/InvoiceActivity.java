package com.example.android.firebaseupload.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.firebaseupload.Model.ImageUpload;
import com.example.android.firebaseupload.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InvoiceActivity extends AppCompatActivity {

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText pharmacyName, practiceNo, customerName, message, startKm, endKm;
    private Uri uri;
    TextView invoiceCounter;

    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";

    public static final int REQUEST_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        imageView = (ImageView) findViewById(R.id.imageView);
        pharmacyName = (EditText) findViewById(R.id.pharmacyName);
        practiceNo = (EditText) findViewById(R.id.practiceNo);
        customerName = (EditText) findViewById(R.id.customerName);
        message = (EditText) findViewById(R.id.message);
        startKm = (EditText) findViewById(R.id.startKm);
        endKm = (EditText) findViewById(R.id.endKm);
        invoiceCounter = (TextView) findViewById(R.id.invoiceCounter);


        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);


    }

    public void btnBrowse_click(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SelectImage"), REQUEST_CODE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Pharmacy", pharmacyName.getText().toString());
        outState.putString("practice", practiceNo.getText().toString());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pharmacyName.setText(savedInstanceState.getString("Pharmacy"));
        practiceNo.setText(savedInstanceState.getString("practice"));



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(final View view) {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Sending Data");
            progressDialog.show();

            StorageReference StorageReference = mStorageReference.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));
            StorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(InvoiceActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                    ImageUpload imageUpload = new ImageUpload(pharmacyName.getText().toString(), practiceNo.getText().toString(), taskSnapshot.getDownloadUrl().toString(),
                            customerName.getText().toString(), message.getText().toString(), startKm.getText().toString(),endKm.getText().toString(),invoiceCounter.getText().toString());
                    String uploadUrl = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadUrl).setValue(imageUpload);
                    onStart();
                    btnClearPad(view);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(InvoiceActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading..." + (int) progress + "%");

                }
            });
        } else {
            Toast.makeText(InvoiceActivity.this, "Please Select Image", Toast.LENGTH_LONG).show();
        }
    }

    public void btnClearPad(View view) {
        pharmacyName.setText("");
        practiceNo.setText("");
        customerName.setText("");
        message.setText("");
        startKm.setText("");
        endKm.setText("");
        imageView.setImageDrawable(null);
    }

    public void btnShowListImage_Click(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
//auto increment Invoice Counter each time user opens activity

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("key_code", MODE_PRIVATE);
        int code = sharedPreferences.getInt("code", 0);

        if (code <= 0) {
            code = 1;
        } else {
            code++;
        }
        sharedPreferences.edit().putInt("code", code).commit();
        String newKey = "" + code;
        invoiceCounter.setText(newKey);
    }
}