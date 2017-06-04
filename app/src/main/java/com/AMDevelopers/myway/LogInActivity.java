package com.AMDevelopers.myway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity{

	EditText userName;
    EditText password;
	Button login;
    Socket socket;
    DataOutputStream send;
	DataInputStream recieve;
	String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final ActionBar Bar = getActionBar();
        Bar.setDisplayHomeAsUpEnabled(true);
        Bar.setHomeButtonEnabled(false);
        Bar.setTitle("");
        Bar.setIcon(R.drawable.logo);

        userName = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.Password);

        login = (Button) findViewById(R.id.LogIn);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
				login.setBackgroundColor(0xff79b4d4);
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							if (userName.getText().toString().equals("")) {
			                    runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "UserName is missing ... !", Toast.LENGTH_SHORT).show();
										login.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (password.getText().toString().equals("")) {
			                    runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Password is missing ... !", Toast.LENGTH_SHORT).show();
										login.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (userName.getText().toString().contains("'") || userName.getText().toString().contains(",")
			                        || userName.getText().toString().contains("\"") || userName.getText().toString().contains("\\")
			                        || userName.getText().toString().contains("/") || userName.getText().toString().contains(";")) {
			                	runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "UserName mustn't contain : ' , \" \\ / ; ... !", Toast.LENGTH_SHORT).show();
										login.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (password.getText().toString().contains("'") || password.getText().toString().contains(",")
			                        || password.getText().toString().contains("\"") || password.getText().toString().contains("\\")
			                        || password.getText().toString().contains("/") || password.getText().toString().contains(";")) {
			                	runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Password mustn't contain : ' , \" \\ / ; ... !", Toast.LENGTH_SHORT).show();
										login.setBackgroundColor(0xff67a2bf);
									}
								});
			                    return;
			                }
			                String Password = new Encryption().Encrypt(password.getText().toString());
							socket = new Socket("54.187.117.45", 6310);
							send = new DataOutputStream(socket.getOutputStream());
							recieve = new DataInputStream(socket.getInputStream());
							send.writeUTF("LOG IN");
							send.writeUTF(userName.getText().toString());
							send.writeUTF(Password);
							response = recieve.readUTF();
							recieve.close();
							send.close();
							socket.close();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									login.setBackgroundColor(0xff67a2bf);
									if (response.contains(userName.getText().toString())){
										MainActivity.ID = Integer.parseInt(response.substring(0, response.indexOf(",")));
										MainActivity.UserName = response.substring(response.indexOf(",") + 1, response.lastIndexOf(","));
										MainActivity.Name = response.substring(response.lastIndexOf(",") + 1);
										try{
											MainActivity.User.SignUp(MainActivity.ID , MainActivity.UserName, password.getText().toString(), MainActivity.Name);
										}catch (Exception ex){
										}
										MainActivity.User.LogIn(MainActivity.UserName);
										startActivity(new Intent(getApplicationContext(), AppActivity.class));
										HomeActivity.home.finish();
										MainActivity.main.finish();
										finish();
									} else {
										Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
									}
				                }
							});
						} catch (final UnknownHostException e) {
							Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE, null, e);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									login.setBackgroundColor(0xff67a2bf);
				                }
							});
						} catch (final IOException e) {
							Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE, null, e);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									login.setBackgroundColor(0xff67a2bf);
				                }
							});
						}
					}
				}).start();
            }
        });
    }
}
