package com.AMDevelopers.myway;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

    static HomeActivity home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home = this;

        final ActionBar Bar = getActionBar();
        Bar.setDisplayHomeAsUpEnabled(true);
        Bar.setHomeButtonEnabled(false);
        Bar.setTitle("");
        Bar.setIcon(R.drawable.logo);

        final Button login = (Button) findViewById(R.id.LogInButton);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login.setBackgroundColor(0xff79b4d4);
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                login.setBackgroundColor(0xff67a2bf);
            }
        });

        final Button signup = (Button) findViewById(R.id.SignUpButton);
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup.setBackgroundColor(0xfffdfdfd);
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                signup.setBackgroundColor(0xffffffff);
            }
        });
    }
}
