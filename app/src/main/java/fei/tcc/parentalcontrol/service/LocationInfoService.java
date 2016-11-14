package fei.tcc.parentalcontrol.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import fei.tcc.parentalcontrol.dao.LocationDao;

public class LocationInfoService extends Service implements ConnectionCallbacks, OnConnectionFailedListener {

    private LocationDao locationDao;

    private long UPDATE_INTERVAL = 120000;  /* 2 min */

    private static final String TAG = "LocationActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        locationDao = new LocationDao(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkPermission()) {
                    if (mGoogleApiClient.isConnected()) {
                        Log.d(TAG, "Disconnecting to create a new connection with Google Api");
                        mGoogleApiClient.disconnect();
                    }

                    Log.d(TAG, "Connecting with Google Api Client");
                    mGoogleApiClient.connect();
                } else {
                    Log.d(TAG, "App doesn' have permission to access location");
                    // TODO here should must something to the database that identify it is disabled
                }

                Log.d(TAG, "Calling handler again");
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        }, 1);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Obtaining location onConnected");
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            long currentTime = System.currentTimeMillis();
            locationDao.insert(mLocation.getLatitude(), mLocation.getLongitude(), (currentTime / 1000) * 1000);
            Log.d(TAG, "Location isn't null, printing it");
            Log.d("LAT ", String.valueOf(mLocation.getLatitude()));
            Log.d("LON ", String.valueOf(mLocation.getLongitude()));
        } else {
            Log.d(TAG, "Location not found");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

}