package com.AMDevelopers.myway;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity extends Activity{

	EditText name;
    EditText userName;
    EditText password;
    RadioGroup gender;
	Button signup;
    Socket socket;
    DataOutputStream send;
    DataInputStream recieve;
    String response;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		
		final ActionBar Bar = getActionBar();
        Bar.setDisplayHomeAsUpEnabled(false);
        Bar.setHomeButtonEnabled(true);
        Bar.setTitle("");
        Bar.setIcon(R.drawable.logo);

        name = (EditText) findViewById(R.id.Name);
        userName = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.password);
        gender = (RadioGroup) findViewById(R.id.Gender);

        signup = (Button) findViewById(R.id.SignUp);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
				signup.setBackgroundColor(0xff79b4d4);
                new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							if (name.getText().toString().equals("")) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Name is missing ... !", Toast.LENGTH_SHORT).show();
										signup.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			            	if (userName.getText().toString().equals("")) {
			            		runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "UserName is missing ... !", Toast.LENGTH_SHORT).show();
										signup.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (password.getText().toString().equals("")) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Password is missing ... !", Toast.LENGTH_SHORT).show();
										signup.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (gender.getCheckedRadioButtonId() == -1) {
			                    runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Gender is missing ... !", Toast.LENGTH_SHORT).show();
										signup.setBackgroundColor(0xff67a2bf);
					                }
								});
			                    return;
			                }
			                if (name.getText().toString().contains("'") || name.getText().toString().contains(",")
			                        || name.getText().toString().contains("\"") || name.getText().toString().contains("\\")
			                        || name.getText().toString().contains("/") || name.getText().toString().contains(";")) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(getApplicationContext(), "Name mustn't contain : ' , \" \\ / ; ... !", Toast.LENGTH_SHORT).show();
										signup.setBackgroundColor(0xff67a2bf);
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
										signup.setBackgroundColor(0xff67a2bf);
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
										signup.setBackgroundColor(0xff67a2bf);
									}
								});
			                    return;
			                }
			                final String Password = new Encryption().Encrypt(password.getText().toString());
			                final String Gender;
			                if (gender.getCheckedRadioButtonId() == R.id.Male){
			                	Gender = "Male";
			                } else {
			                	Gender = "Female";
			                }
						    socket = new Socket("54.187.117.45", 6310);
							send = new DataOutputStream(socket.getOutputStream());
							recieve = new DataInputStream(socket.getInputStream());
							send.writeUTF("SIGN UP");
							send.writeUTF(name.getText().toString());
							send.writeUTF(userName.getText().toString());
							send.writeUTF(Password);
							send.writeUTF(Gender);
							response = recieve.readUTF();
							recieve.close();
							send.close();
							socket.close();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									signup.setBackgroundColor(0xff67a2bf);
									if (response.contains("Signed Up Successfully ... !")){
										MainActivity.ID = Integer.parseInt(response.substring(response.indexOf(":") + 1));
										MainActivity.UserName = userName.getText().toString();
										MainActivity.Name = name.getText().toString();
										MainActivity.User.SignUp(MainActivity.ID , userName.getText().toString(), password.getText().toString(), name.getText().toString());
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
							Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, null, e);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									signup.setBackgroundColor(0xff67a2bf);
								}
							});
						} catch (final IOException e) {
							Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, null, e);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
									signup.setBackgroundColor(0xff67a2bf);
								}
							});
						}
					}
				}).start();
            }

        });
    }
}
