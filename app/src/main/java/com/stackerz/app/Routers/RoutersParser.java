package com.stackerz.app.Routers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.stackerz.app.Networks.Networks;

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
public class RoutersParser extends Activity{
    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String SNAT = "snat";
    public static final String IP = "ip";
    public static final String ID = "id";

    public String authToken;
    public String neutronURL;

    public static RoutersParser parser = null;

    public static RoutersParser shared(){
        if (parser  == null){
            parser  = new RoutersParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String routersJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Routers routers = new Routers();
            JSONObject router = new JSONObject(routersJSON);
            JSONArray routerobj = router.getJSONArray("routers");
            for (int i = 0; i < routerobj.length(); i++) {
                JSONObject objsrv = routerobj.getJSONObject(i);
                routers.setName(objsrv.getString("name"));
                routers.setState(objsrv.getString("admin_state_up"));
                routers.setId(objsrv.getString("id"));
                JSONArray gateway = objsrv.getJSONArray("external_gateway_info");
                for (int j = 0; j < 1; j++) {
                    JSONObject objgw = gateway.getJSONObject(j);
                    routers.setSnat(objgw.getString("enable_snat"));
                    JSONArray addr = objgw.getJSONArray("external_fixed_ips");
                    for (int k = 0; k < 1; k++){
                        JSONObject objaddr = addr.getJSONObject(k);
                        routers.setIp(objaddr.getString("ip_address"));
                    }
                }
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, routers.getName());
                map.put(STATE, routers.getState());
                map.put(SNAT, routers.getSnat());
                map.put(IP, routers.getIp());
                map.put(ID, routers.getId());
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


