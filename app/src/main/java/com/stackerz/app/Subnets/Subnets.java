package com.stackerz.app.Subnets;

/**
 * Created by ed on 4/11/14.
 */
public class Subnets {

    //constants for field references

    public static final String NAME = "name";
    public static final String GW = "gw";
    public static final String CIDR = "cidr";
    public static final String ID = "id";

    //fields
    String name;
    String gw;
    String cidr;
    String id;


//getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


