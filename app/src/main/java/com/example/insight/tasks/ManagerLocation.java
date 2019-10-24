package com.example.insight.tasks;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.insight.Constants;
import com.example.insight.dto.UserDTO;
import com.example.insight.base.DataBase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static android.content.Context.LOCATION_SERVICE;

public class ManagerLocation {

    private double latitude;
    private double longitude;
    static private Marker marker;
    private Context mainContext;

    public ManagerLocation(Context context, GoogleMap googleMap) {
        mainContext = context;
        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("You")
                .visible(false));

        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                marker.setPosition(new LatLng(latitude, longitude));
                marker.setVisible(true);
                new updateUserTask(latitude, longitude).execute();
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private class updateUserTask extends AsyncTask<Void, Void, Void> {

        private double latitude;
        private double longitude;
        private RestTemplate rest;

        updateUserTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;

            rest = new RestTemplate();
            rest.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DataBase db = new DataBase(mainContext);
            UserDTO thisUser = db.getUser(10);
            thisUser.setLatitude(latitude);
            thisUser.setLongitude(longitude);
            db.updateUser(thisUser);

            thisUser.setTable_id(0);

            try {
                rest.put(Constants.URL.UPDATE_USER, thisUser);
            } catch (Exception e) {
                this.cancel(true);
            }
            return null;
        }

        @Override
        protected void onCancelled() {

        }
    }
}
