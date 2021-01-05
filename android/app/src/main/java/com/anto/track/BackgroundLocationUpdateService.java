package com.anto.track;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class BackgroundLocationUpdateService implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
        Log.d("TAG","Hi");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
