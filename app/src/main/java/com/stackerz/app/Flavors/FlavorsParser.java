package com.stackerz.app.Flavors;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

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
public class FlavorsParser extends Activity{
    public static final String NAME = "name";
    public static final String RAM = "ram";
    public static final String VCPUS = "vcpus";
    public static final String DISK = "disk";

    public String authToken;
    public String novaURL;

    public static FlavorsParser parser = null;

    public static FlavorsParser shared(){
        if (parser  == null){
            parser  = new FlavorsParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String flavorsJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Flavors flavors = new Flavors();
            JSONObject flavor = new JSONObject(flavorsJSON);
            JSONArray servers = flavor.getJSONArray("flavors");

            for (int i = 0; i < servers.length(); i++) {
                JSONObject objsrv = servers.getJSONObject(i);
                flavors.setName(objsrv.getString("name"));
                flavors.setVcpus(objsrv.getString("vcpus"));
                flavors.setDisk(objsrv.getString("disk"));
                flavors.setRam(objsrv.getString("ram"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, flavors.getName());
                map.put(VCPUS, flavors.getVcpus());
                map.put(DISK, flavors.getDisk());
                map.put(RAM, flavors.getRam());
                jsonList.add(map);
            }
        } catch (JSONException e) {
            Log.d("ErrorInitJSON", e.toString());
            e.printStackTrace();
        }

        Collections.sort(jsonList, new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
                return (lhs.get("vcpus")).compareToIgnoreCase(rhs.get("vcpus"));
            }
        });


        return jsonList;
    }


}


