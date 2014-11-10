package com.stackerz.app;
import android.app.Activity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by ed on 4/11/14.
 */
public class EndpointsParser extends Activity{
    public static final String PUBLICURL = "publicURL";
    public static final String REGION = "region";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();


    public static EndpointsParser parser = null;

    public static EndpointsParser shared(){
        if (parser  == null){
            parser  = new EndpointsParser();
        }
        return parser ;
    }

    public SimpleAdapter initJSON(String endpoints){
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
                for (int j = 0; j < endpoints.length(); j++) {
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
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), jsonList, R.layout.endpoint_list, new String[]{NAME, TYPE, REGION, PUBLICURL}, new int[]{R.id.name, R.id.type, R.id.region, R.id.url});
        return adapter;
    }
}


