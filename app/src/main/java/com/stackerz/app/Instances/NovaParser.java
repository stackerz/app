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
    public static final String NETID = "network";
    public static final String ADDR = "address";
    public static final String TYPE = "type";
    public static final String HOST = "host";

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
        ArrayList<HashMap<String, String>> tempList = new ArrayList<HashMap<String, String>>();
        try {
            NovaInstances novaInstance = new NovaInstances();
            JSONObject nova = new JSONObject(novaJSON);
            JSONArray servers = nova.getJSONArray("servers");

            for (int i = 0; i < servers.length(); i++) {
                JSONObject objsrv = servers.getJSONObject(i);
                novaInstance.setName(objsrv.getString("name"));
                novaInstance.setId(objsrv.getString("id"));
                novaInstance.setStatus(objsrv.getString("OS-EXT-STS:vm_state"));
                novaInstance.setHost(objsrv.getString("OS-EXT-SRV-ATTR:host"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, novaInstance.getName());
                map.put(ID, novaInstance.getId());
                map.put(STATUS, novaInstance.getStatus());
                map.put(HOST, novaInstance.getHost());
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

    public static String parseFlavor(String instanceDetail){
        ArrayList<HashMap<String, String>> flavorList = NovaParser.shared().getFlavorList();
        String temp = null;
        JSONObject novaDetail = null;
        try {
            novaDetail = new JSONObject(instanceDetail);
            JSONObject server = novaDetail.getJSONObject("server");
            JSONObject flavor = server.getJSONObject("flavor");
            if (flavorList !=null){
            temp = flavor.getString("id");
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

    public static ArrayList<HashMap<String, String>> parseNet(String netDetail){
        ArrayList<HashMap<String, String>> netList = new ArrayList<HashMap<String, String>>();
        String netId = null, addr = null, addrType = null;
        JSONObject net = null;
        try {
            net = new JSONObject(netDetail);
            JSONObject server = net.getJSONObject("server");
            JSONObject addresses = server.getJSONObject("addresses");
            Iterator<String> keys=addresses.keys();
            while(keys.hasNext())
            {
                String key=keys.next();
                String value=addresses.getString(key);
                netId = key;

            JSONArray network = addresses.getJSONArray(key);
            for (int i = 0; i < network.length(); i++) {
                JSONObject objnet = network.getJSONObject(i);
                addr = objnet.getString("addr");
                addrType = objnet.getString("OS-EXT-IPS:type");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NETID, netId);
                map.put(ADDR, addr);
                map.put(TYPE, addrType);
                netList.add(map);
            }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return netList;
    }

}


