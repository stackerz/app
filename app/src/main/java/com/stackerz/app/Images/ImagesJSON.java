package com.stackerz.app.Images;

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
public class ImagesJSON extends Activity {

    String glanceJSON;
    String glance;
    String auth;
    RequestQueue queue = null;

    public static ImagesJSON parser = null;

    public static ImagesJSON shared(){
        if (parser  == null){
            parser  = new ImagesJSON();
        }
        return parser ;
    }

    public String getGlanceJSON() {
        return glanceJSON;
    }

    public void setGlanceJSON(String glanceJSON) {
        this.glanceJSON = glanceJSON;
    }

    public String getGlance() {
        return glance;
    }

    public void setGlance(String glance) {
        this.glance = glance;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String receiveData (String glanceURL, String authToken){
        setGlance(glanceURL);
        setAuth(authToken);
        getJSON();
        getGlanceJSON();
        return glanceJSON;
    }

    public void getJSON() {
        final String authToken = getAuth();
        String glanceURL = getGlance();
        glanceURL = glanceURL+"/v2.0/images";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, glanceURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Glance on Response", response.toString());
                        setGlanceJSON(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Glance on Error", "Error: " + error.getMessage());
                        setGlanceJSON(error.toString());
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




