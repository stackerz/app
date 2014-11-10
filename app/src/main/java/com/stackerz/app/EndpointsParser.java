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
            Endpoints endpoint = new Endpoints();
            JSONObject ar = new JSONObject(content);
            List<Endpoints> endpointsList = new ArrayList<>();
            JSONObject access = ar.getJSONObject("access");
            JSONArray serviceCatalog = access.getJSONArray("serviceCatalog");

            for(int i=0;i<serviceCatalog.length();i++) {
                JSONObject objsvc = serviceCatalog.getJSONObject(i);
                JSONArray endpoints = objsvc.getJSONArray("endpoints");
                endpoint.setName(objsvc.getString("name"));
                endpoint.setType(objsvc.getString("type"));
                for (int j = 0; j < objsvc.length(); j++) {
                    JSONObject objept = endpoints.getJSONObject(j);
                    endpoint.setRegion(objept.getString("region"));
                    endpoint.setPublicURL(objept.getString("publicURL"));
                }
                endpointsList.add(endpoint);
            }
            return endpointsList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}


