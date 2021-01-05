package com.anto.track;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.anto.track.api.Api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final String CHANNEL_DEFAULT_IMPORTANCE = "CHANNEL";
    private TrackService service;

    public static MainActivity currentInstance;

    boolean tracking = false;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, this);

        } else {
            Toast.makeText(currentInstance, "Gib location permission.", Toast.LENGTH_LONG).show();
        }
        super.onCreate(savedInstanceState);
        currentInstance = this;
        service = TrackService.getInstance(Settings.Secure.getString(
                getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID), getBaseContext().getSharedPreferences(
                MainActivity.class.getName(), Context.MODE_PRIVATE));

        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!service.isRegistered()) {
                    Snackbar.make(view, "You must register first!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                service.openBrowser();
            }
        });

        //register
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.register();
                updateLayout();
            }
        });

        findViewById(R.id.track_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tracking = !tracking;

                Button b = (Button) findViewById(R.id.track_button);
                b.setText(tracking ? "Stop tracking" : "Start tracking");
            }
        });

        updateLayout();
    }

    private void updateLayout() {
        int registerVisibility = service.isRegistered() ? View.INVISIBLE : View.VISIBLE;
        int trackVisibility = service.isRegistered() ? View.VISIBLE : View.INVISIBLE;
        findViewById(R.id.register_layout).setVisibility(registerVisibility);
        findViewById(R.id.track_layout).setVisibility(trackVisibility);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateLayout();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!tracking) return;

        service.submitPosition(location.getLongitude(), location.getLatitude());
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