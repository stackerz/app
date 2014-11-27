package com.stackerz.app.Networks;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.stackerz.app.Flavors.Flavors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by ed on 4/11/14.
 */
public class NetworksParser extends Activity{
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String EXTERNAL = "external";
    public static final String TYPE = "type";
    public static final String SHARED = "shared";
    public static final String ID = "id";

    public String authToken;
    public String neutronURL;

    public static NetworksParser parser = null;

    public static NetworksParser shared(){
        if (parser  == null){
            parser  = new NetworksParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String networksJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Networks networks = new Networks();
            JSONObject nets = new JSONObject(networksJSON);
            JSONArray array = nets.getJSONArray("networks");

            for (int i = 0; i < array.length(); i++) {
                JSONObject objsrv = array.getJSONObject(i);
                networks.setName(objsrv.getString("name"));
                networks.setState(objsrv.getString("admin_state_up"));
                networks.setExternal(objsrv.getString("router:external"));
                networks.setType(objsrv.getString("provider:network_type"));
                networks.setShared(objsrv.getString("shared"));
                networks.setId(objsrv.getString("id"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, networks.getName());
                map.put(STATE, networks.getState());
                map.put(EXTERNAL, networks.getExternal());
                map.put(TYPE, networks.getType());
                map.put(SHARED, networks.getShared());
                map.put(ID, networks.getId());
                jsonList.add(map);
            }
        } catch (JSONException e) {
            Log.d("ErrorInitJSON", e.toString());
            e.printStackTrace();
        }

        Collections.sort(jsonList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                return (lhs.get("name")).compareToIgnoreCase(rhs.get("name"));
            }
        });


        return jsonList;
    }


}


