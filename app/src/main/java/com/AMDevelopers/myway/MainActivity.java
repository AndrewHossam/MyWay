package com.AMDevelopers.myway;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static MainActivity main;
	static int ID;
	static String UserName, Name;
	public static ArrayList<com.AMDevelopers.myway.MenuItem> Items;
	public static DataBase User;
	public static LocationManager Coordinates;
	public static Location location;
	public static boolean NORMAL = true, TSP = false;
	public static double MyLat, MyLong, DestLat, DestLong;
	public static ArrayList<String> TSPDests;
	public static String Route = "", SRoute = "";
	boolean GPSEnabled, NetworkEnabled;
	int progress = 0;
	ProgressBar bar;
	Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main = this;

		final ActionBar Bar = getActionBar();
        Bar.setDisplayHomeAsUpEnabled(true);
        Bar.setHomeButtonEnabled(false);
        Bar.setTitle("");
        Bar.setIcon(R.drawable.logo);

		Coordinates = (LocationManager) MainActivity.main.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

		if (ActivityCompat.checkSelfPermission(MainActivity.main.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.main.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(main,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
					PackageManager.PERMISSION_GRANTED);
		}

		GPSEnabled = true;
		NetworkEnabled = true;

		try {
			GPSEnabled = Coordinates.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch(Exception ex) {}

		try {
			NetworkEnabled = Coordinates.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch(Exception ex) {}

		if (!GPSEnabled){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setMessage("GPS is Required but Disabled : Enable GPS , Restart App.");

			alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					finish();
				}
			});

			alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();


		}

		if (!NetworkEnabled){
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setMessage("Network is Required but Disabled : Enable Network , Restart App.");

			alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					finish();
				}
			});

			alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		if(GPSEnabled && NetworkEnabled){
			Coordinates.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {

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
			MyLat = location.getLatitude();
			MyLong = location.getLongitude();
		}


		bar = (ProgressBar) findViewById(R.id.Progress);

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				bar.setProgress(progress++);
				if (!GPSEnabled || !NetworkEnabled){
					timer.cancel();
				}
				if(progress == 1){
					Items = new ArrayList<>();
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.logo, ""));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_home_white_24dp, "Home"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_directions_white_24dp, "Directions"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_new_releases_white_24dp, "What 's New"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_refresh_white_24dp, "Refresh"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_info_white_24dp, "About"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_report_problem_white_24dp, "Report Problem"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_settings_white_24dp, "Settings"));
					Items.add(new com.AMDevelopers.myway.MenuItem(R.drawable.ic_power_settings_new_white_24dp, "Log Out"));
				}
				if(progress == 100){
					try {
						User = new DataBase(getApplicationContext(), "User", null, 1);
					} catch (SQLException sqlex) {

					}
				}
				if(progress == 200){
					DestLat = 1000;
					DestLong = 1000;
					Route = "";
					SRoute = "";
					TSPDests = new ArrayList<>();
					MainActivity.TSPDests.add(MainActivity.MyLong + ":" + MainActivity.MyLat);
				}
				if(progress == 500){
					String response = User.CheckSession();
					if (!response.equalsIgnoreCase("No Logged In User")){
						ID = Integer.parseInt(response.substring(0, response.indexOf(",")));
						UserName = response.substring(response.indexOf(",") + 1, response.lastIndexOf(","));
						Name = response.substring(response.lastIndexOf(",") + 1);
						startActivity(new Intent(getApplicationContext(), AppActivity.class));
						finish();
					} else {
						startActivity(new Intent(getApplicationContext(), HomeActivity.class));
						finish();
					}
					timer.cancel();
				}
			}
		}, 10, 10);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
