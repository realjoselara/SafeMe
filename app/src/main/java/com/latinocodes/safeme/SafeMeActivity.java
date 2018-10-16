package com.latinocodes.safeme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.widget.EditText;

import java.util.HashMap;

public class SafeMeActivity extends AppCompatActivity {
    private Button bombButton;
    public HashMap<String, Double> coordinates = new HashMap<>();
    public static final String BROADCAST_ACTION = "com.ojastec.broadcastreceiverdemo";
    private String TAG = "SafeMeActivity";

    EditText editemailaddress, editpassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_me);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            Toast.makeText(this, "Location Enabled", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

//        startService(new Intent(LOCATION_SERVICE));
        Intent locationService = new Intent(this, LocationService.class);
        startService(locationService);

        registerMyReceiver();


        //Bomb Button Event
        bombButton = (Button) findViewById(R.id.bomb);
        bombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });

        // amber alert button
        Button amberAlert = (Button) findViewById(R.id.amber_alert);
        // handle onclick event for amber button: open up Description activity
        amberAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Description.class);
                startActivity(intent);

            }
        });

        //other emergency alert button
        Button other = (Button) findViewById(R.id.other_alert);
        //handle onclick event for other emergency button: open up Description activity
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Description.class);
                startActivity(intent);
            }
        });

        // active shooter emergency alert button
        Button activeShooter = (Button) findViewById(R.id.active_shooter);
        // handle onclick event for active shooter button: open up dialog box
        activeShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });

        //button for attack emergency alert button
        Button attack = (Button) findViewById(R.id.rapist);
        //handle onclick event for attack emergency button : open up a dialog box
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });


    }



        //openPopup Method after pushing one of the buttons on SafeMeActivity Screen
        public void openPopup () {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle("Alert Sent")
                    .setMessage("An Alert has been sent Out!")
                    .setPositiveButton("Find Help", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String uri = "geo" + coordinates.get("latitude") + ", " + coordinates.get("longitude");
                            String gweburi = "http://maps.google.com/maps?saddr=" + coordinates.get("latitude") + "," + coordinates.get("longitude") + "&daddr=" + 40.7449992 + "," + -74.0239707;
//                            String gweburi = "http://maps.google.com/maps?&daddr=" + 40.7449992 + "," + -74.0239707;
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

                        }
                    })
                    .show();
        }


        /**
         * This method is responsible to register an action to BroadCastReceiver
         * */
        private void registerMyReceiver () {

            try {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(BROADCAST_ACTION);
                this.registerReceiver(new MyBroadcastReceiver(), intentFilter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        /**This class extends BroadcastReceiver and defines the onReceieve method**/
        class MyBroadcastReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context ctxt, Intent i) {
                /**This method overrides the BroadcastReciever.onReceieve method. When called
                 * it pulls the users location coordinates passed as Intent extras from the service**/
                try {
                    Log.d(TAG, "onReceive() called");

                    // uncomment this line if you had sent some data

                    coordinates.put("longitude", i.getDoubleExtra("long", 0));
                    coordinates.put("latitude", i.getDoubleExtra("lat", 0));
                    Log.e(TAG, "coordinates received");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Toast.makeText(getApplicationContext(), "Emergency Alert Sent!", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}


