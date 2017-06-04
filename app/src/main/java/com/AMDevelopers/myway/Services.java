package com.AMDevelopers.myway;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Services extends Service {

    public static LocationManager Coordinates;
    public static Location location;
    double Lat1, Long1, Lat2, Long2;
    public static int id;
    DataBase User;
    Socket socket;
    DataOutputStream send;
    DataInputStream recieve;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        try {
            User = new DataBase(getApplicationContext(), "User", null, 1);
        } catch (SQLException sqlex) {

        }
        String response = User.CheckSession();
        if (!response.equalsIgnoreCase("No Logged In User")){
            id = Integer.parseInt(response.substring(0, response.indexOf(",")));
        }
        Coordinates = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Coordinates.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Lat2 = location.getLatitude();
                Long2 = location.getLongitude();
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
        });
        location = Coordinates.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Lat1 = location.getLatitude();
        Long1 = location.getLongitude();
        Lat2 = location.getLatitude();
        Long2 = location.getLongitude();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (id == 0){
                        this.cancel();
                        stopSelf();
                    }
                    String ID = new Encryption().Encrypt(String.valueOf(id));
                    double EarthRadius = 6371.0;
                    double ConvertedLatitude = Math.toRadians(Lat2 - Lat1);
                    double ConvertedLongitude = Math.toRadians(Long2 - Long1);
                    double RadLat1 = Math.toRadians(Lat1);
                    double RadLat2 = Math.toRadians(Lat2);
                    double Haversine = (Math.sin(ConvertedLatitude / 2) * Math.sin(ConvertedLatitude / 2)) + (Math.cos(RadLat1) * Math.cos(RadLat2) * Math.sin(ConvertedLongitude / 2) * Math.sin(ConvertedLongitude / 2));
                    double Ratio = 2 * Math.atan2(Math.sqrt(Haversine), Math.sqrt(1 - Haversine));
                    double Distance = EarthRadius * Ratio;
                    double AvgSpeed = (Distance * 1000) / (2.0 * 60.0);
                    socket = new Socket("54.187.117.45", 6310);
                    send = new DataOutputStream(socket.getOutputStream());
                    recieve = new DataInputStream(socket.getInputStream());
                    send.writeUTF("NAVIGATE");
                    send.writeUTF(ID);
                    send.writeDouble(Lat1);
                    send.writeDouble(Long1);
                    send.writeDouble(Lat2);
                    send.writeDouble(Long2);
                    send.writeDouble(AvgSpeed);
                    recieve.close();
                    send.close();
                    socket.close();
                    Lat1 = Lat2;
                    Long1 = Long2;
                } catch (final IOException e) {

                }
            }
        }, (1000 * 2 * 60), (1000 * 2 * 60));
        return START_STICKY;
    }
}
