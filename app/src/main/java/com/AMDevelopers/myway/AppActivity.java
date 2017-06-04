package com.AMDevelopers.myway;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppActivity extends Activity{
	
	ActionBarDrawerToggle Toggler;
	Fragment fragmentMap = new MapActivity();
	Fragment fragmentAbout = new AboutActivity();
	Fragment fragmentReport = new ReportActivity();
	Fragment fragmentSettings = new SettingsActivity();
	Socket socket;
	DataOutputStream send;
	DataInputStream recieve;
	String responseDistance, responseSpeed;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app);
		
		final ActionBar Bar = getActionBar();
		Bar.setDisplayHomeAsUpEnabled(true);
		Bar.setHomeButtonEnabled(true);
		Bar.setTitle("");
		Bar.setIcon(R.drawable.logo);

		new Thread(){
			@Override
			public void run() {
				startService(new Intent(getApplicationContext(), Services.class));
			}
		}.start();

		final DrawerLayout Menu = (DrawerLayout)findViewById(R.id.drawer_layout);
		Toggler = new ActionBarDrawerToggle(this, Menu, R.drawable.logo, R.string.app_name, R.string.app_name){
			@Override
			public void onDrawerOpened(View drawerView) {
			}
			@Override
			public void onDrawerClosed(View drawerView) {
			}
		};
		Menu.setDrawerListener(Toggler);
		
		getFragmentManager().beginTransaction().replace(R.id.Frame, fragmentMap).commit();
		
		final ListView list = (ListView) findViewById(R.id.List);
		list.setAdapter(new Adapter(MainActivity.Items, this));
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String Option = String.valueOf(list.getItemIdAtPosition(position));
				if(Option.equals("1")){
					Menu.closeDrawers();
					getFragmentManager().beginTransaction().replace(R.id.Frame, fragmentMap).commit();
				}
				if (Option.equals("2")) {
					Menu.closeDrawers();
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								if (MainActivity.NORMAL) {
									if (MainActivity.DestLat == 1000 || MainActivity.DestLong == 1000) {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getApplicationContext(), "Destination Position Isn't Selected ... !", Toast.LENGTH_SHORT).show();
											}
										});
										return;
									}
									String ID = new Encryption().Encrypt(String.valueOf(MainActivity.ID));
									socket = new Socket("54.187.117.45", 6310);
									send = new DataOutputStream(socket.getOutputStream());
									recieve = new DataInputStream(socket.getInputStream());
									send.writeUTF("ROUTE");
									send.writeUTF(ID);
									send.writeDouble(MainActivity.MyLat);
									send.writeDouble(MainActivity.MyLong);
									send.writeDouble(MainActivity.DestLat);
									send.writeDouble(MainActivity.DestLong);
									responseDistance = recieve.readUTF();
									responseSpeed = recieve.readUTF();
									recieve.close();
									send.close();
									socket.close();
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (responseDistance.contains("ERROR")) {
												Toast.makeText(getApplicationContext(), responseDistance, Toast.LENGTH_SHORT).show();
												return;
											}
											if (responseSpeed.contains("ERROR")) {
												Toast.makeText(getApplicationContext(), responseSpeed, Toast.LENGTH_SHORT).show();
												return;
											}
											MainActivity.Route = responseDistance;
											MainActivity.SRoute = responseSpeed;
											MapActivity.doRouting();
											MapActivity.doSRouting();
										}
									});
								}
								if (MainActivity.TSP) {
									if (MainActivity.TSPDests.size() <= 1) {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(getApplicationContext(), "Destinations Positions Aren't Selected ... !", Toast.LENGTH_SHORT).show();
											}
										});
										return;
									}
									String ID = new Encryption().Encrypt(String.valueOf(MainActivity.ID));
									socket = new Socket("54.187.117.45", 6310);
									send = new DataOutputStream(socket.getOutputStream());
									recieve = new DataInputStream(socket.getInputStream());
									send.writeUTF("TSP");
									send.writeUTF(ID);
									send.writeUTF(MainActivity.TSPDests.toString());
									responseDistance = recieve.readUTF();
									recieve.close();
									send.close();
									socket.close();
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											if (responseDistance.contains("ERROR") || responseDistance.equalsIgnoreCase("Service Not Available")) {
												Toast.makeText(getApplicationContext(), responseDistance, Toast.LENGTH_SHORT).show();
												return;
											}
											MainActivity.Route = responseDistance;
											MapActivity.doRouting();
										}
									});
								}
							} catch (final UnknownHostException e) {
								Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE, null, e);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});
							} catch (final IOException e) {
								Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE, null, e);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									}
								});
							}
						}
					}).start();
				}
				if (Option.equals("3")) {
					Menu.closeDrawers();
					// what 's new
				}
				if (Option.equals("4")) {
					Menu.closeDrawers();
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					finish();
				}
				if (Option.equals("5")) {
					Menu.closeDrawers();
					getFragmentManager().beginTransaction().replace(R.id.Frame, fragmentAbout).commit();
				}
				if (Option.equals("6")) {
					Menu.closeDrawers();
					getFragmentManager().beginTransaction().replace(R.id.Frame, fragmentReport).commit();
				}
				if (Option.equals("7")) {
					Menu.closeDrawers();
					getFragmentManager().beginTransaction().replace(R.id.Frame, fragmentSettings).commit();
				}
				if (Option.equals("8")) {
					Menu.closeDrawers();
					Services.id = 0;
					stopService(new Intent(getApplicationContext(), Services.class));
					MainActivity.User.LogOut(MainActivity.UserName);
					startActivity(new Intent(getApplicationContext(), HomeActivity.class));
					finish();
				}
			}

		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(Toggler.onOptionsItemSelected(item)){
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
