package com.stackerz.app.Subnets;

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
public class SubnetsParser extends Activity{
    public static final String NAME = "name";
    public static final String GW = "gw";
    public static final String CIDR = "cidr";
    public static final String ID = "id";

    public String authToken;
    public String neutronURL;

    public static SubnetsParser parser = null;

    public static OnJSONLoaded mListener;

    public static void setOnJSONLoadedListener(OnJSONLoaded listener) {
        mListener = listener;
    }

    public interface OnJSONLoaded {
        void onJsonLoaded(ArrayList<HashMap<String, String>> list);
    }

    public static SubnetsParser shared(){
        if (parser  == null){
            parser  = new SubnetsParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String subnetsJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Subnets subnets = new Subnets();
            JSONObject subnet = new JSONObject(subnetsJSON);
            JSONArray subnetobj = subnet.getJSONArray("subnets");
            for (int i = 0; i < subnetobj.length(); i++) {
                JSONObject objsrv = subnetobj.getJSONObject(i);
                subnets.setName(objsrv.getString("name"));
                subnets.setGw(objsrv.getString("gateway_ip"));
                subnets.setCidr(objsrv.getString("cidr"));
                subnets.setId(objsrv.getString("id"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, subnets.getName());
                map.put(GW, subnets.getGw());
                map.put(CIDR, subnets.getCidr());
                map.put(ID, subnets.getId());
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

        if (mListener != null) {
            mListener.onJsonLoaded(jsonList);
        }

        return jsonList;

    }


}


