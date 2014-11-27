package com.stackerz.app.Images;

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
public class ImagesParser extends Activity{
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String VISIBILITY = "visibility";
    public static final String FORMAT = "format";
    public static final String ID = "id";


    public static ImagesParser parser = null;

    public static ImagesParser shared(){
        if (parser  == null){
            parser  = new ImagesParser();
        }
        return parser ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }





    public static ArrayList<HashMap<String, String>> parseJSON(String glanceJSON){
        ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
        try {
            Images image = new Images();
            JSONObject glance = new JSONObject(glanceJSON);
            JSONArray images = glance.getJSONArray("images");

            for (int i = 0; i < images.length(); i++) {
                JSONObject objsrv = images.getJSONObject(i);
                image.setName(objsrv.getString("name"));
                image.setStatus(objsrv.getString("status"));
                image.setVisibility(objsrv.getString("visibility"));
                image.setFormat(objsrv.getString("disk_format"));
                image.setId(objsrv.getString("id"));
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(NAME, image.getName());
                map.put(STATUS, image.getStatus());
                map.put(VISIBILITY, image.getVisibility());
                map.put(FORMAT, image.getFormat());
                map.put(ID, image.getId());
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


