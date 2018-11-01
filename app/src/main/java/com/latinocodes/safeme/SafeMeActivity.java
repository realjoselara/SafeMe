package com.latinocodes.safeme;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.latinocodes.safeme.model.Emergency;
import com.latinocodes.safeme.model.Notification;
import com.latinocodes.safeme.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SafeMeActivity extends AppCompatActivity {
    private Button bombButton;
    public HashMap<String, Double> coordinates = new HashMap<>();
    public static final String BROADCAST_ACTION = "com.ojastec.broadcastreceiverdemo";
    private String TAG = "SafeMeActivity";
    String MyPREFERENCES = "UserSession";
    SharedPreferences sharedpreferences;
    FirebaseDatabase database;

    private FusedLocationProviderClient mFusedLocationClient;


    //Channel ID for Firebase Cloud Messaging Notification
    public static final String CHANNEL_ID = "Alert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_me);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Testing - Show Token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
//            googleMap.setMyLocationEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object

                                SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                Gson gson = new Gson();
                                String json = sharedPreferences.getString("Userinfo", "");
                                User userdata = gson.fromJson(json, User.class);
                                userdata.setLocationCordinates(location);

                                //update last location update
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                userdata.setLastUpdated(dateFormat.format(date));

                                //update shared preferences
                                String userjson = gson.toJson(userdata); //convert User obj to json format to be stored
                                SharedPreferences.Editor editor = sharedPreferences.edit(); //create editor object
                                editor.putString("Userinfo", userjson); // store to shared preferences as Userinfo to be retirived later.
                                editor.commit();

                                updatelocation();

                            }
                        }
                    });

//            Toast.makeText(this, "Location Enabled", Toast.LENGTH_LONG).show();
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
        bombButton = findViewById(R.id.bomb);
        bombButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification("Bomb", "Bomb found", true);
                Alert(notification);
                openPopup();
            }
        });

        // amber alert button
        Button amberAlert = findViewById(R.id.amber_alert);
        // handle onclick event for amber button: open up Description activity
        amberAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Description.class);
                intent.putExtra("caller", "Amber Alert");
                startActivity(intent);

            }
        });

        //other emergency alert button
        Button other = findViewById(R.id.other_alert);
        //handle onclick event for other emergency button: open up Description activity
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Description.class);
                intent.putExtra("caller", "Other");
                startActivity(intent);
            }
        });

        // active shooter emergency alert button
        Button activeShooter = findViewById(R.id.active_shooter);
        // handle onclick event for active shooter button: open up dialog box
        activeShooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification("Active Shooter", "Shooter", true);
                Alert(notification);
                openPopup();
            }
        });

        //button for attack emergency alert button
        Button attack = findViewById(R.id.rapist);
        //handle onclick event for attack emergency button : open up a dialog box
        attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = new Notification("Attack", "I'm being attacked", true);
                Alert(notification);
                openPopup();
            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

        //openPopup Method after pushing one of the buttons on SafeMeActivity Screen
        public void openPopup () {
            //retrive user info from sharedpreferences
            Gson gson = new Gson();
            String json = sharedpreferences.getString("Userinfo", "");
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

                    Gson gson = new Gson();
                    String json = sharedpreferences.getString("Userinfo", "");
                    User userdata = gson.fromJson(json, User.class);

                    userdata.setLocationCordinates(new ArrayList<Double>(coordinates.values())); //add cordinates to user object
                    System.out.println("JSON::::"+userdata.getLocationCordinates());
                    //update shared preferences

                    String userjson = gson.toJson(userdata); //convert User obj to json format to be stored

                    SharedPreferences.Editor editor = sharedpreferences.edit(); //create editor object
                    editor.putString("Userinfo", userjson); // store to shared preferences as Userinfo to be retirived later.
                    editor.commit();

                    updatelocation();
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

    public void updatelocation(){
        Gson gson = new Gson();
        String json = sharedpreferences.getString("Userinfo", "");
        User userdata = gson.fromJson(json, User.class);
//        ArrayList<Double> locationcord = userdata.getLocationCordinates();
        DatabaseReference user = database.getReference("Users");
        Log.i(TAG, userdata.print());
        user.child(userdata.getUserID()).child("LastLocation").child("0").setValue(userdata.getLocationCordinates().get(0));
        user.child(userdata.getUserID()).child("LastLocation").child("1").setValue(userdata.getLocationCordinates().get(1));
        user.child(userdata.getUserID()).child("LastUpdated").setValue(userdata.getLastUpdated());
    }


    public void Alert(Notification notification){
        Gson gson = new Gson();
        String json = sharedpreferences.getString("Userinfo", "");
        User userdata = gson.fromJson(json, User.class);
        Emergency emergency = new Emergency("None", userdata.getUserID(), notification);

        DatabaseReference alert = database.getReference("Notifications");
        DatabaseReference newalert = alert.push();
        newalert.child("InitiatorId").setValue(userdata.getUserID());
        newalert.child("Type").setValue(emergency.getNotification().getType());
        newalert.child("Location").child("0").setValue(userdata.getLocationCordinates().get(0));
        newalert.child("Location").child("1").setValue(userdata.getLocationCordinates().get(1));
        newalert.child("Description").setValue(emergency.getNotification().getDescription());
        newalert.child("Date").setValue(emergency.getDate());

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

}


