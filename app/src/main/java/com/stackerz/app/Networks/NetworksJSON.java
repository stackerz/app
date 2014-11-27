package com.stackerz.app.Networks;

import android.app.Activity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.stackerz.app.System.VolleySingleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ed on 19/11/14.
 */
public class NetworksJSON extends Activity {

    String neutronJSON;
    String neutron;
    String auth;
    RequestQueue queue = null;

    public static NetworksJSON parser = null;

    public static NetworksJSON shared(){
        if (parser  == null){
            parser  = new NetworksJSON();
        }
        return parser ;
    }

    public String getNeutronJSON() {
        return neutronJSON;
    }

    public void setNeutronJSON(String neutronJSON) {
        this.neutronJSON = neutronJSON;
    }

    public String getNeutron() {
        return neutron;
    }

    public void setNeutron(String neutron) {
        this.neutron = neutron;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String receiveData (String neutronURL, String authToken){
        setNeutron(neutronURL);
        setAuth(authToken);
        getJSON();
        getNeutronJSON();
        return neutronJSON;
    }

    public void getJSON() {
        final String authToken = getAuth();
        String neutronURL = getNeutron();
        neutronURL = neutronURL+"/v2.0/networks";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, neutronURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Neutron on Response", response.toString());
                        setNeutronJSON(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Neutron on Error", "Error: " + error.getMessage());
                        setNeutronJSON(error.toString());
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", authToken);
                params.put("User-Agent", "stackerz");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };


        queue = VolleySingleton.getInstance(this).getRequestQueue();
        //VolleySingleton.getInstance(this).addToRequestQueue(getRequest);
        queue.add(getRequest);
    }
}




