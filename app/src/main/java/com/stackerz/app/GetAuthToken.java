package com.stackerz.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ed on 17/10/2014.
 */
public class GetAuthToken extends Activity{

    public String authToken;
    public SharedPreferences shPref = new ObscuredSharedPreferences(this,getApplicationContext().getSharedPreferences("Login_Credentials",0));
    public static GetAuthToken getAuthToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public static GetAuthToken shared(){
        if (getAuthToken == null){
            getAuthToken = new GetAuthToken();
        }

        return getAuthToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public void setGetAuthToken() {
        String username = "";
        String password = "";
        String tenant = "";
        String endpoint= "";
        SSLCerts.sslHandling();

        String user = shPref.getString("Username", username);
        String pass = shPref.getString("Password", password);
        String url = shPref.getString("Endpoint", endpoint);
        String tnt = shPref.getString("Tenant", tenant);
        String json = "{\"auth\": {\"tenantName\": \"" + tnt + "\", \"passwordCredentials\": {\"username\": \"" + user + "\", \"password\": \"" + pass + "\"}}}";

        JSONObject login = null;
        try {
            login = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, url, login,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject access = null;
                        try {
                            access = response.getJSONObject("access");
                            JSONObject token = access.getJSONObject("token");
                            String id = token.getString("id");
                            setAuthToken(id);
                            JSONData.shared().setAuthtoken(authToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("App", response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("App", "Error: " + error.getMessage());
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "stackerz");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(getRequest);

    }

}







