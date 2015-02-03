package com.stackerz.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ed on 22/10/14.
 */
public class Splash extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent;
        //String password="";
        //SharedPreferences sharedPreferences = getSharedPreferences("Login_Credentials", 0);
        //if (sharedPreferences.getString("Password",password).isEmpty()) {

            intent = new Intent(this, Connect.class);
        //} else {
        //    intent = new Intent(this, Stackerz.class);
        //}
        startActivity(intent);
        finish();
    }
        
}
