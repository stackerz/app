package com.stackerz.app;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by ed on 4/11/14.
 */
public class EndpointsParser extends Activity{
    public static final String NAME = "name";
    public static final String PUBLICURL = "publicURL";
    public static final String REGION = "region";
    public static final String TYPE = "type";
    public static String novaURL = "nova";
    public static String neutronURL = "neutron";
    public static String glanceURL = "glance";
    public static String keystoneURL = "keystone";
    public static String cinderURL = "cinder";

    public static String getNovaURL() {
        return novaURL;
    }

    public static void setNovaURL(String novaURL) {
        EndpointsParser.novaURL = novaURL;
    }

    public static String getNeutronURL() {
        return neutronURL;
    }

    public static void setNeutronURL(String neutronURL) {
        EndpointsParser.neutronURL = neutronURL;
    }

    public static String getGlanceURL() {
        return glanceURL;
    }

    public static void setGlanceURL(String glanceURL) {
        EndpointsParser.glanceURL = glanceURL;
    }

    public static String getKeystoneURL() {
        return keystoneURL;
    }

    public static void setKeystoneURL(String keystoneURL) {
        EndpointsParser.keystoneURL = keystoneURL;
    }

    public static String getCinderURL() {
        return cinderURL;
    }

    public static void setCinderURL(String cinderURL) {
        EndpointsParser.cinderURL = cinderURL;
    }

    public static EndpointsParser parser = null;

    public static EndpointsParser shared(){
        if (parser  == null){
            parser  = new EndpointsParser();
        }
        return parser ;
    }

    public static ArrayList<HashMap<String, String>> parseJSON(String endpoints){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Endpoints endpoint = new Endpoints();
            JSONObject keystone = new JSONObject(endpoints);
            JSONObject access = keystone.getJSONObject("access");
            JSONArray serviceCatalog = access.getJSONArray("serviceCatalog");

            for (int i = 0; i < serviceCatalog.length(); i++) {
                JSONObject objsvc = serviceCatalog.getJSONObject(i);
                JSONArray endpointsArray = objsvc.getJSONArray("endpoints");
                endpoint.setName(objsvc.getString("name"));
                endpoint.setType(objsvc.getString("type"));
                for (int j = 0; j < 1; j++) {
                    JSONObject objept = endpointsArray.getJSONObject(j);
                    endpoint.setRegion(objept.getString("region"));
                    endpoint.setPublicURL(objept.getString("publicURL"));
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, endpoint.getName());
                map.put(TYPE, endpoint.getType());
                map.put(REGION, endpoint.getRegion());
                map.put(PUBLICURL, endpoint.getPublicURL());
                jsonList.add(map);
            }
        } catch (JSONException e) {
            Log.d("ErrorInitJSON", e.toString());
            e.printStackTrace();
        }


        for (Map<String,String> map : jsonList) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (map.containsValue(novaURL)) {
                    novaURL = map.get(PUBLICURL);
                    setNovaURL(novaURL);
                }
                if (map.containsValue(neutronURL)) {
                    neutronURL = map.get(PUBLICURL);
                    setNeutronURL(neutronURL);
                }
                if (map.containsValue(glanceURL)) {
                    glanceURL = map.get(PUBLICURL);
                    setGlanceURL(glanceURL);
                }
                if (map.containsValue(keystoneURL)) {
                    keystoneURL = map.get(PUBLICURL);
                    setKeystoneURL(keystoneURL);
                }
                if (map.containsValue(cinderURL)) {
                    cinderURL = map.get(PUBLICURL);
                    setCinderURL(cinderURL);
                }
            }
            Bundle b = EndpointsParser.toBundle();
            Intent intent = new Intent();
            intent.putExtra("URLs",b);
        }
        return jsonList;
    }

    //	Package data for transfer between activities
    public static Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("NovaURL", novaURL);
        b.putString("NeutronURL", neutronURL);
        b.putString("KeystoneURL", keystoneURL);
        b.putString("CinderURL", cinderURL);
        return b;
    }
}


