package com.stackerz.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by ed on 22/10/14.
 */
public class Splash extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;
        SharedPreferences sharedPreferences = getSharedPreferences("Login Credentials", 0);
        if (sharedPreferences.contains("Username")) {
            intent = new Intent(this, Stackerz.class);
        } else {
            intent = new Intent(this, Connect.class);
        }
        startActivity(intent);
        finish();
    }
        
}
