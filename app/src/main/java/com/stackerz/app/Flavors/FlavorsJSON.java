package com.stackerz.app.Flavors;

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
public class FlavorsJSON extends Activity {

    String novaJSON;
    String nova;
    String auth;
    RequestQueue queue = null;

    public static FlavorsJSON parser = null;

    public static FlavorsJSON shared(){
        if (parser  == null){
            parser  = new FlavorsJSON();
        }
        return parser ;
    }

    public String getNovaJSON() {
        return novaJSON;
    }

    public void setNovaJSON(String novaJSON) {
        this.novaJSON = novaJSON;
    }

    public String getNova() {
        return nova;
    }

    public void setNova(String nova) {
        this.nova = nova;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setCreds (String novaURL, String authToken) {
        setNova(novaURL);
        setAuth(authToken);
    }

    public String receiveData (String novaURL, String authToken){
        setNova(novaURL);
        setAuth(authToken);
        getJSON();
        getNovaJSON();
        return novaJSON;
    }

    public void getJSON() {
        final String authToken = getAuth();
        String novaURL = getNova();
        novaURL = novaURL+"/flavors/detail";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, novaURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Flavors on Response", response.toString());
                        setNovaJSON(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Flavors on Error", "Error: " + error.getMessage());
                        setNovaJSON(error.toString());
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




