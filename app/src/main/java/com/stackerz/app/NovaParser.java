package com.stackerz.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ed on 4/11/14.
 */
public class NovaParser extends Activity{
    public static final String NAME = "name";
    public static final String ID = "id";

    public String authToken;
    public String novaURL;

    public static NovaParser parser = null;

    public static NovaParser shared(){
        if (parser  == null){
            parser  = new NovaParser();
        }
        return parser ;
    }



    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getNovaURL() {
        return novaURL;
    }

    public void setNovaURL(String novaURL) {
        this.novaURL = novaURL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle b = getIntent().getBundleExtra(Stackerz.NOVABUNDLE);
        //setAuthToken(b.getString("AuthToken"));
        //setNovaURL(b.getString("NovaURL"));
        //getJSON();
        //parseJSON(novaJSON);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String novaJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            NovaInstances novaInstance = new NovaInstances();
            JSONObject nova = new JSONObject(novaJSON);
            JSONArray servers = nova.getJSONArray("servers");

            for (int i = 0; i < servers.length(); i++) {
                JSONObject objsrv = servers.getJSONObject(i);
                novaInstance.setName(objsrv.getString("name"));
                novaInstance.setId(objsrv.getString("id"));
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, novaInstance.getName());
                map.put(ID, novaInstance.getId());
                jsonList.add(map);

        } catch (JSONException e) {
            Log.d("ErrorInitJSON", e.toString());
            e.printStackTrace();
        }


        return jsonList;
    }


}


