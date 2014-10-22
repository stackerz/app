package com.stackerz.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by limedv0 on 17/10/2014.
 */
public class Login extends Activity {

    Button connect;
    String username,password;
    EditText userinput, passinput;
    SharedPreferences shPref;
    SharedPreferences.Editor toEdit;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_connect);
        getInit();
    }

    public void getInit(){
        connect = (Button)findViewById(R.id.connectButtonLogin);
        userinput = (EditText)findViewById(R.id.userName);

    }
}
