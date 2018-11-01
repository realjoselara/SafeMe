package com.latinocodes.safeme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.latinocodes.safeme.model.Emergency;
import com.latinocodes.safeme.model.Notification;
import com.latinocodes.safeme.model.User;

import java.io.ByteArrayOutputStream;


public class Description extends AppCompatActivity {
    final static int IMG_REQUEST_CODE = 1;
    final String TAG = "DescriptionActivity";
    TextView descriptionText;
    Button sendAlert;
    ImageView image;
    SharedPreferences sharedPreferences;
    String MyPREFERENCES = "UserSession";
    FirebaseDatabase database;
    FirebaseStorage storage;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        intent = getIntent(); //get information passed from calling activity.
        descriptionText = findViewById(R.id.description);
        //send alert button
        sendAlert = findViewById(R.id.submit);
        //onclicklistener for the send alert button: Send Alert
        sendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String desc = descriptionText.getText().toString();
                Alert(new Notification(intent.getStringExtra("caller"), desc, true));
                openPopup();
            }
        });

        //image button
        image = findViewById(R.id.imageupload);
        // onclicklistener for the image button: Upload image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_REQUEST_CODE);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case IMG_REQUEST_CODE:
                    if (resultCode == Activity.RESULT_OK) {
                        //data gives you the image uri. Try to convert that to bitmap
                        image.setImageURI(data.getData());
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e(TAG, "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception in onActivityResult : " + e.getMessage());
        }
    }

    public void openPopup () {
        //retrive user info from sharedpreferences
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Userinfo", "");
        final User user = gson.fromJson(json, User.class);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Alert Sent")
                .setMessage(user.getFirstName()+" An Alert has been sent Out!")
                .setPositiveButton("Find Help", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uri = "geo:" + user.getLocationCordinates().get(0) + ", " + user.getLocationCordinates().get(0)+"?q=Police Station";
                        Log.i(TAG, "locationurl:" + uri);
                        String gweburi = "http://maps.google.com/maps-34.7868114,-34.7868114?q=Police Station";
//                            Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345")
//                          String gweburi = "http://maps.google.com/maps?saddr=" + coordinates.get("latitude") + "," + coordinates.get("longitude") + "?q=Police Station";
//                          String gweburi = "http://maps.google.com/maps?&daddr=" + 40.7449992 + "," + -74.0239707;
                        Uri gmapsIntent = Uri.parse(uri);
                        Uri gwebIntent = Uri.parse(gweburi);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmapsIntent);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            System.out.println("ARRR!!&*)*()(_" + uri);
                            startActivity(mapIntent);
                        } else {

                            mapIntent = new Intent(Intent.ACTION_VIEW, gwebIntent);
                            System.out.println("ARRR!!&*)*()(_" + gweburi);
                            startActivity(mapIntent);
                        }

                    }
                })
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), SafeMeActivity.class);
                            startActivity(intent);
                    }
                })
                .show();
    }

    public void Alert(Notification notification){
        String url ="";
        //get user info from Shared Preferences
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Userinfo", "");
        User userdata = gson.fromJson(json, User.class);
        Emergency emergency = new Emergency("None", userdata.getUserID(), notification);
        String imagepath = userdata.getUserID()+"/incident"+emergency.getDate()+".jpg";
        StorageReference imgref = storage.getReference("Incidents").child(imagepath); //storage reference to store image

        //if image, get image as bitmap and compress to byte array

        if (image.getDrawable() != null && image.getDrawable().getConstantState() != getResources().getDrawable( R.drawable.imageupload).getConstantState()){
            image.setDrawingCacheEnabled(true);
            image.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imgref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e(TAG, "Unable to upload image");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...

                }
            });

            url = imgref.getPath();
        }

        //push notification details
        DatabaseReference alert = database.getReference("Notifications");
        DatabaseReference newalert = alert.push();
        newalert.child("InitiatorId").setValue(userdata.getUserID());
        newalert.child("Type").setValue(emergency.getNotification().getType());
        newalert.child("Location").child("0").setValue(userdata.getLocationCordinates().get(0));
        newalert.child("Location").child("1").setValue(userdata.getLocationCordinates().get(1));
        newalert.child("Description").setValue(emergency.getNotification().getDescription());
        newalert.child("Date").setValue(emergency.getDate());
        newalert.child("Imageurl").setValue(url);

    }
}
