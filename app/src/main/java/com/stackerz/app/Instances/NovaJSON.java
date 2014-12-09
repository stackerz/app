package com.stackerz.app.Instances;

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
public class NovaJSON extends Activity {

    String novaJSON;
    String nova;
    String auth;
    String novaJSONdetail;
    String novaJSONip;
    String id;
    RequestQueue queue = null;

    public static NovaJSON parser = null;

    public static NovaJSON shared(){
        if (parser  == null){
            parser  = new NovaJSON();
        }
        return parser ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNovaJSONdetail() {
        return novaJSONdetail;
    }

    public void setNovaJSONdetail(String novaJSONdetail) {
        this.novaJSONdetail = novaJSONdetail;
    }

    public String getNovaJSONip() {
        return novaJSONip;
    }

    public void setNovaJSONip(String novaJSONip) {
        this.novaJSONip = novaJSONip;
    }

    public String receiveData (String novaURL, String authToken){
        setNova(novaURL);
        setAuth(authToken);
        getJSON();
        getNovaJSON();
        return novaJSON;
    }

    public String receiveDetail (String id){
        setId(id);
        getJSONdetail();
        getNovaJSONdetail();
        return novaJSONdetail;
    }

    public String receiveIP (String id){
        setId(id);
        getJSONip();
        getNovaJSONip();
        return novaJSONip;
    }

    public void getJSON() {
        final String authToken = getAuth();
        String novaURL = getNova();
        novaURL = novaURL+"/servers/detail";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, novaURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Nova on Response", response.toString());
                        setNovaJSON(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Nova on Error", "Error: " + error.getMessage());
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

    public void getJSONdetail() {
        final String authToken = getAuth();
        String novaURL = getNova();
        novaURL = novaURL+"/servers/"+id;


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, novaURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Nova on Response", response.toString());
                        setNovaJSONdetail(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Nova on Error", "Error: " + error.getMessage());
                        setNovaJSONdetail(error.toString());
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

    public void getJSONip() {
        final String authToken = getAuth();
        String novaURL = getNova();
        novaURL = novaURL+"/servers/"+id+"/ips";


        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, novaURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Nova on Response", response.toString());
                        setNovaJSONip(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("Nova on Error", "Error: " + error.getMessage());
                        setNovaJSONip(error.toString());
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




