package com.example.sitting_room.pebbletophone;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;


public class GPS{
    private static Context mContext;
    static LocationManager lm;
    public static LocationListener locl;
    Coordinates coordinates = new Coordinates();

    public GPS(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    private void getLocation() {
        Log.i("TAG", "GPS getLocation started");
        // Acquire a reference to the system Location Manager
        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locl = new LocationListener() {

            public void onLocationChanged(Location location) {
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i("TAG","GPS permissions granted for FINE_LOCATION");
            //    ActivityCompat#requestPermissions
            //    here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locl);
        //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locl);//Comment out after testing!!!!!!!!!!!
    }//onCreate

    private void makeUseOfNewLocation(Location loc) {
        Log.i("TAG", "makeUseOfNewLocation started");
        Log.i("TAG", "Latitude is: " + loc.getLatitude());
        Log.i("TAG", "Longitude is: " + loc.getLongitude());
        Log.i("TAG", "Accuracy is:" + loc.getAccuracy());
        coordinates.setCoordinates(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
    }



}//class


