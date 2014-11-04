package com.stackerz.app;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by ed on 4/11/14.
 */
public class EndpointsParser {
    public static List<Endpoints> parseFeed(String content) {

        try {
            JSONArray ar = new JSONArray(content);
            List<Endpoints> endpointsList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {

                JSONObject obj = ar.getJSONObject(i);
                Endpoints endpoint = new Endpoints();

                endpoint.setName(obj.getString("name"));
                endpoint.setType(obj.getString("type"));
                endpoint.setRegion(obj.getString("region"));
                endpoint.setPublicURL(obj.getString("publicURL"));

                endpointsList.add(endpoint);
            }

            return endpointsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}


