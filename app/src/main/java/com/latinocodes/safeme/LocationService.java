package com.latinocodes.safeme;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.support.v4.app.ActivityCompat;


public class LocationService extends Service {
    /**This class handles the location background service for the application be creating a location
     * listeners**/
    private static final String TAG = "MyLocationService";
    private LocationManager locationManager =null;
    private static final int Location_Interval =1000;
    private static final float Location_Distance = 10f;

    private class LocationListener implements android.location.LocationListener{
        /**This location listener captures all location changes and sends a broadcast to SafeMeActivity
         * with the updated user coordinates.**/
        Location mLastLocation;
        public LocationListener(String provider){
            Log.e(TAG, "LocationListener"+provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location){
            Log.e(TAG, "onLocationChanged:" + location);
            mLastLocation.set(location);
//            Double longitude=location.getLongitude();
//            Double latitude=location.getLatitude();
//            Toast.makeText(LocationService.this,longitude + "yes" + latitude + " ",Toast.LENGTH_LONG).show();
            sendMyBroadCast(); //send broadcast containing updated coordinates
        }

        @Override
        public void onProviderDisabled(String provider){
            Log.e(TAG, "onProviderDisabled:" + provider);
        }

        @Override
        public void onProviderEnabled(String provider){
            Log.e(TAG, "onProviderEnabled:" + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
            Log.e(TAG, "onStatusChanged:" + provider);
        }
        /**
         * This method is responsible to send broadCast to specific Action
         * */
        private void sendMyBroadCast()
        {
            try
            {
                Intent broadCastIntent = new Intent();
                broadCastIntent.setAction(SafeMeActivity.BROADCAST_ACTION);

                // add location to intent as extra
                broadCastIntent.putExtra("long", mLastLocation.getLongitude());
                broadCastIntent.putExtra("lat", mLastLocation.getLatitude());

                sendBroadcast(broadCastIntent);
                Log.e(TAG, "Broadcast sent");

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    // start of LocationService class definition
    LocationListener [] mLocationListeners = new LocationListener[]{
        new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate(){
        Log.e(TAG, "OnCreate");
        initializeLocationManager();
        try{
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    Location_Interval,
                    Location_Interval,
                    mLocationListeners[0]);
        }
        catch (java.lang.SecurityException e){
            Log.i(TAG, "Failed to request location update, ignore", e);
        } catch(IllegalArgumentException e){
            Log.i(TAG, "network provider does not exist, "+ e.getMessage());
        }
    }

    @Override
    public void onDestroy(){
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if(locationManager != null){
            for (int i=0; i<mLocationListeners.length; i++){
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.removeUpdates(mLocationListeners[i]);
                }catch(Exception e){
                    Log.i(TAG, "Failed to remove location listener, ignore", e);
                }
            }
        }

    }

    private void initializeLocationManager(){
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ Location_Interval + " LOCATION_DISTANCE: " + Location_Distance);
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}



