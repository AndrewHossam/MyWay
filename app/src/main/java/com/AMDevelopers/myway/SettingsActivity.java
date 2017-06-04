package com.AMDevelopers.myway;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends Fragment {

    Button Normal;
    Button TSP;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View Settings = inflater.inflate(R.layout.activity_settings, container, false);
        Normal = (Button) Settings.findViewById(R.id.Normal);
        Normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.NORMAL = true;
                MainActivity.TSP = false;
                MainActivity.DestLat = 1000;
                MainActivity.DestLong = 1000;
                MainActivity.Route = "";
                MainActivity.SRoute = "";
                MapActivity.DistanceDirections = null;
                MapActivity.SpeedDirections = null;
                Toast.makeText(getActivity().getApplicationContext(), "Normal Mode Activated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        TSP = (Button) Settings.findViewById(R.id.TSP);
        TSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.NORMAL = false;
                MainActivity.TSP = true;
                MainActivity.TSPDests = new ArrayList<>();
                MainActivity.TSPDests.add(MainActivity.MyLong + ":" + MainActivity.MyLat);
                Toast.makeText(getActivity().getApplicationContext(), "TSP Mode Activated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        return Settings;
    }
}
