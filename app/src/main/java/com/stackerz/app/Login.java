package com.stackerz.app;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by limedv0 on 17/10/2014.
 */
public class Login extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_connect);
        Bundle extras = getIntent().getExtras();


    }
}
