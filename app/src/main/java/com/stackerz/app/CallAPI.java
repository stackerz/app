package com.stackerz.app;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by limedv0 on 24/10/2014.
 */
public class CallAPI extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
        //String urlString=strings[0]; // URL to call

        //String resultToDisplay = "";

        //InputStream in = null;

        // HTTP Get
        //try {

        //    URL url = new URL(urlString);

        //    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //    in = new BufferedInputStream(urlConnection.getInputStream());

        //} catch (Exception e ) {

        //    System.out.println(e.getMessage());

        //   return e.getMessage();

        // }

        //return resultToDisplay;
        return null;

    }
}

