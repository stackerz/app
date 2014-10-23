package com.stackerz.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by limedv0 on 17/10/2014.
 */
public class Login extends Activity implements View.OnClickListener{

    public Button connect;
    public String username, password, endpoint;
    public EditText userInput, passInput, serverInput;
    public SharedPreferences shPref ;
    public Editor toEdit;
    public LinearLayout mainLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getInit();
    }

    public void getInit(){
        connect = (Button)findViewById(R.id.connectButtonLogin);
        userInput = (EditText)findViewById(R.id.userName);
        serverInput = (EditText)findViewById(R.id.server);
        passInput = (EditText)findViewById(R.id.password);
        connect.setOnClickListener(this);

    }

    public void sharedPreferences() {
        //shPref = getSharedPreferences("Login Credentials", MODE_PRIVATE);
        shPref = new ObscuredSharedPreferences(this, this.getSharedPreferences("Login Credentials", Context.MODE_PRIVATE) );
        toEdit = shPref.edit();
        toEdit.putString("Username", username);
        toEdit.putString("Password", password);
        toEdit.putString("Endpoint", endpoint);
        toEdit.commit();

        // retrieve => shPref.getString("Username",null);
    }
    @Override
    public void onClick(View v){
        boolean reachable = false;
        v.getId();
        //v.setBackground(R.drawable.rounded_red);
        //v.setBackgroundColor(Color.RED);
        username = userInput.getText().toString();
        password = passInput.getText().toString();
        endpoint = serverInput.getText().toString();
        if (isNetworkAvailable()){
            if (isValidUrl(endpoint)){
                reachable = true;
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "This is not a valid URL address, make sure there are no blank spaces at the end. Please try again.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,120);
                toast.show();
                //serverInput.setText("");
                reachable = false;
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You don't seem to be connected to the network now. Please try again later.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,120);
            toast.show();
            //serverInput.setText("");
            reachable = false;
        }
        if (reachable) {
            sharedPreferences();
            Intent intent = new Intent(Login.this, Stackerz.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url);
        if(m.matches())
            return true;
        else
            return false;
    }


}
