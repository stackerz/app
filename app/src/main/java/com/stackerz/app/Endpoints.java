package com.stackerz.app;

import android.os.Bundle;

/**
 * Created by ed on 4/11/14.
 */
public class Endpoints {

    //constants for field references
    public static final String PUBLICURL = "publicURL";
    public static final String REGION = "region";
    public static final String TYPE = "type";
    public static final String NAME = "name";

    //fields
    String publicURL;
    String region;
    String type;
    String name;


    //getters and setters
    public String getPublicURL() {
        return publicURL;
    }

    public String getRegion() {
        return region;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setPublicURL(String publicURL) {
        this.publicURL = publicURL;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

}


