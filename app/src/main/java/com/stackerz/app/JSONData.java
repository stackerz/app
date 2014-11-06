package com.stackerz.app;

/**
 * Created by limedv0 on 6/11/2014.
 */
public class JSONData {
    public static JSONData jsondata = null;

    public static JSONData shared(){
        if (jsondata == null){
            jsondata = new JSONData();
        }
        return jsondata;
    }

    public static String authtoken;
    public static String endpoint;

    public static String getAuthtoken() {
        return authtoken;
    }

    public static void setAuthtoken(String authtoken) {
        JSONData.authtoken = authtoken;
    }

    public static String getEndpoint(){
        return endpoint;
    }

    public static void setEndpoint(String endpoint){
        JSONData.endpoint = endpoint;
    }
}
