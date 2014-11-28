package com.stackerz.app.Security;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.stackerz.app.Subnets.Subnets;

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
public class SecurityParser extends Activity{
    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String ID = "id";

    public String authToken;
    public String neutronURL;

    public static SecurityParser parser = null;

    public static SecurityParser shared(){
        if (parser  == null){
            parser  = new SecurityParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String securityJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Security security = new Security();
            JSONObject sec = new JSONObject(securityJSON);
            JSONArray secgroup = sec.getJSONArray("security_groups");
            for (int i = 0; i < secgroup.length(); i++) {
                JSONObject objsrv = secgroup.getJSONObject(i);
                security.setName(objsrv.getString("name"));
                security.setDesc(objsrv.getString("description"));
                security.setId(objsrv.getString("id"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, security.getName());
                map.put(DESC, security.getDesc());
                map.put(ID, security.getId());
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


