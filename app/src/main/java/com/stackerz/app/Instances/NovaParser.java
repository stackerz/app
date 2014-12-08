package com.stackerz.app.Instances;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.stackerz.app.Flavors.FlavorsParser;
import com.stackerz.app.System.ObscuredSharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;

/**
 * Created by ed on 4/11/14.
 */
public class NovaParser extends Activity{

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String FLAVOR = "flavor";
    public static final String ADDRFXD = "addrfxd";
    public static final String ADDRFLT = "addrflt";
    public static final String NET = "net";
    public static final String SECGRP = "secgrp";

    public String authToken;
    public String novaURL;

    public SharedPreferences shPref;

    public ArrayList<HashMap<String, String>> flavorList;

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

    public void setFlavorList(ArrayList<HashMap<String, String>> flavorList) {
        this.flavorList = flavorList;
    }

    public ArrayList<HashMap<String, String>> getFlavorList(){
        return flavorList;
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
                String instanceDetail = String.valueOf(NovaJSON.shared().getJSONdetail(novaInstance.getId()));
                novaInstance.setFlavor(parseDetail(instanceDetail));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, novaInstance.getName());
                map.put(ID, novaInstance.getId());
                map.put(STATUS, novaInstance.getStatus());
                map.put(FLAVOR, novaInstance.getFlavor());
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

    public static String parseDetail(String instanceDetail){
        ArrayList<HashMap<String, String>> flavorList = NovaParser.shared().getFlavorList();
        String temp = null;
        NovaInstances novaInstance = new NovaInstances();
        JSONObject novaDetail = null;
        try {
            novaDetail = new JSONObject(instanceDetail);
            JSONObject server = novaDetail.getJSONObject("server");
            JSONObject flavor = server.getJSONObject("flavor");
            temp = flavor.getString("id");
            novaInstance.setFlavor(flavor.getString("id"));
            if (flavorList !=null){
            for (Map<String,String> map : flavorList) {
                if (map.containsValue(temp)) {
                    temp = map.get(NAME);
                }
            }
            }
            /*JSONObject addresses = server.getJSONObject("addresses");
            Iterator<String> keys=addresses.keys();
            while(keys.hasNext())
            {
                String key=keys.next();
                String value=addresses.getString(key);
                novaInstance.setNet(value);
            }
            JSONObject security_groups = server.getJSONObject("security_groups");*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return temp;
    }

}


