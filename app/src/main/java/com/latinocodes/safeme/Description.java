package com.latinocodes.safeme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.latinocodes.safeme.R;

import java.util.HashMap;

public class Description extends AppCompatActivity {
    final static int IMG_REQUEST_CODE = 1;
    final String TAG = "DescriptionActivity";
    TextView descriptionText;
    Button sendAlert;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        descriptionText =  (TextView) findViewById(R.id.description);
        //send alert button
        sendAlert = (Button) findViewById(R.id.submit);
        //onclicklistener for the send alert button: Send Alert
        sendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });

        //image button
        image = (ImageView) findViewById(R.id.imageupload);
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

    public void openPopup() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Alert Sent")
                .setMessage("An Alert has been sent Out!")
                .setPositiveButton("Find Help", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String uri = "geo"+coordinates.get("latitude")+", "+ coordinates.get("longitude");
//                        String gweburi = "http://maps.google.com/maps?saddr="+coordinates.get("latitude")+","+ coordinates.get("longitude") +"&daddr=" + 40.7449992 + "," + -74.0239707;
                        String gweburi = "http://maps.google.com/maps?&daddr=" + 40.7449992 + "," + -74.0239707;
//                        Uri gmapsIntent = Uri.parse(uri);
                        Uri gwebIntent = Uri.parse(gweburi);
//                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmapsIntent);
//                        mapIntent.setPackage("com.google.android.apps.maps");
//                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
//                            System.out.println("ARRR!!&*)*()(_"+uri);
//                            startActivity(mapIntent);
//                        }else{

                            Intent mapIntent= new Intent(Intent.ACTION_VIEW, gwebIntent);
                            System.out.println("ARRR!!&*)*()(_"+gweburi);
                            startActivity(mapIntent);
//                        }

                    }
                })
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

}
