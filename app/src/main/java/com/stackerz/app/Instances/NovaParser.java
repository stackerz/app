package com.stackerz.app.Instances;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedMap;

/**
 * Created by ed on 4/11/14.
 */
public class NovaParser extends Activity{
    public static final String NAME = "name";
    public static final String ID = "id";
    public static final String STATUS = "status";

    public String authToken;
    public String novaURL;

    public static NovaParser parser = null;

    public static NovaParser shared(){
        if (parser  == null){
            parser  = new NovaParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                novaInstance.setStatus(objsrv.getString("OS-EXT-STS:vm_state"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, novaInstance.getName());
                map.put(ID, novaInstance.getId());
                map.put(STATUS, novaInstance.getStatus());
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


