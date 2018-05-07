package com.example.android.firebaseupload.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.firebaseupload.Model.ImageUpload;

import com.example.android.firebaseupload.R;
import com.example.android.firebaseupload.activities.SignatureActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class InvoiceFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String LOG_TAG = "InvoiceFragment";

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseRef;
    private ImageView imageView;
    private EditText pharmacyName, practiceNo, customerName, message, startKm, endKm;
    private Uri uri;
    Button sendBtn;
    TextView invoiceCounter;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

    ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);

        imageView = (ImageView) view.findViewById(R.id.invoiceImageView);
        pharmacyName = (EditText) view.findViewById(R.id.ETphamracyName);
        practiceNo = (EditText) view.findViewById(R.id.ETpraticeNumber);
        Button erasePad = (Button) view.findViewById(R.id.clear_button);
         ImageButton btSignature=(ImageButton)view.findViewById(R.id.btSignature);
        btSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SignatureActivity.class);
                startActivity(intent);
            }
        });
        erasePad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClearPad(v);
            }
        });
        customerName = (EditText) view.findViewById(R.id.etPharmacistName);
        sendBtn = (Button) view.findViewById(R.id.send_button);
        invoiceCounter = (TextView) view.findViewById(R.id.invoiceCounter);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpload_Click(v);
            }
        });

        message = (EditText) view.findViewById(R.id.ETmessage);
        startKm = (EditText) view.findViewById(R.id.ETstart);
        endKm = (EditText) view.findViewById(R.id.ETend);
        ImageButton buttonAttach = (ImageButton) view.findViewById(R.id.btAttach);
        buttonAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBrowse_click(v);
            }
        });
        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);

    }


    public static InvoiceFragment newInstance(int position) {
        InvoiceFragment f = new InvoiceFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    //click to search for Signature image on SD card
    public void btnBrowse_click(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SelectImage"), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(final View view) {
        if (uri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Sending Data");
            progressDialog.show();

            StorageReference StorageReference = mStorageReference.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(uri));
            StorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                    ImageUpload imageUpload = new ImageUpload(
                            pharmacyName.getText().toString().trim()
                            , practiceNo.getText().toString()
                            , taskSnapshot.getDownloadUrl().toString(),
                            customerName.getText().toString()
                            , message.getText().toString()
                            , startKm.getText().toString()
                            , endKm.getText().toString()
                            , invoiceCounter.getText().toString());
                    String uploadUrl = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadUrl).setValue(imageUpload);
                    onStart();
                    btnClearPad(view);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploading..." + (int) progress + "%");
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_LONG).show();
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


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("key_code", getActivity().MODE_PRIVATE);
        int code = sharedPreferences.getInt("code", 0);
        if (code <= 0) {
            code = 1;
        } else {
            code++;
        }
        sharedPreferences.edit().putInt("code", code).apply();
        String newKey = "" + code;
        invoiceCounter.setText(newKey);


//    } private boolean validateName() {
//        if (inputName.getText().toString().trim().isEmpty()) {
//            inputLayoutName.setError(getString(R.string.err_msg_name));
//            requestFocus(inputName);
//            return false;
//        } else {
//            inputLayoutName.setErrorEnabled(false);
//        }
//
//        return true;
    }
}
