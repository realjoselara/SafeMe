package com.latinocodes.safeme;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.Manifest;

import com.latinocodes.safeme.model.Dialog;

public class SafeMeActivity extends AppCompatActivity {
    private Button button;

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
            Toast.makeText(this,"Location Enabled", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    1);
        }

        //Bomb Button Event
        button = (Button) findViewById(R.id.bomb);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopup();
            }
        });

      //  startService(new Intent(LOCATION_SERVICE));
        // startService(new Intent(this,LocationService .class));

    }
    //openPopup Method after pushing one of the buttons on SafeMeActivity Screen
    public void openPopup(){
        Dialog expDialog = new Dialog();
        expDialog.show(getSupportFragmentManager(), "Alert");
    }
}
