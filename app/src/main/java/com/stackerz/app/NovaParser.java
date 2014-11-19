package com.stackerz.app;

import android.app.Activity;
import android.content.Intent;
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

    public String novaJSON;
    public String authToken;
    public String novaURL;

    public static NovaParser parser = null;

    public static NovaParser shared(){
        if (parser  == null){
            parser  = new NovaParser();
        }
        return parser ;
    }

    public String getNovaJSON() {
        return novaJSON;
    }

    public void setNovaJSON(String novaJSON) {
        this.novaJSON = novaJSON;
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
        Bundle b = getIntent().getBundleExtra(Stackerz.NOVABUNDLE);
        setAuthToken(b.getString("AuthToken"));
        setNovaURL(b.getString("NovaURL"));
        getJSON();
        parseJSON(novaJSON);
    }

    public void getJSON() {
    novaURL = getNovaURL();
    novaURL = novaURL+"/servers";

    JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, novaURL, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.d("Nova", response.toString());
                    setNovaJSON((response).toString());
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Nova", "Error: " + error.getMessage());

                }
            }
    ) {
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("User-Agent", "stackerz");
            params.put("Accept", "application/json");
            params.put("Content-Type", "application/json; charset=utf-8");
            params.put("X-Auth-Token", authToken);
            return params;
        }

    };

    RequestQueue queue = Volley.newRequestQueue(this);
    queue.add(getRequest);

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
                novaInstance.setId(objsrv.getString("type"));
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


