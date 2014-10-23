package com.stackerz.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by ed on 22/10/14.
 */
public class Connect extends Activity implements View.OnClickListener{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        Button connect = (Button) findViewById(R.id.connectButtonMain);
    }
        @Override
        public void onClick(View v){
            v.getId();
            //v.setBackground(R.drawable.rounded_red);
            v.setBackgroundColor(Color.RED);
            Intent intent = new Intent(Connect.this, Login.class);
            startActivity(intent);
            finish();
    }
}
