package com.stackerz.app;

/**
 * Created by ed on 4/11/14.
 */
public class Endpoints {

    String publicURL;
    String region;
    String type;
    String name;

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
