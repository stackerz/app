package com.stackerz.app.Routers;

/**
 * Created by ed on 4/11/14.
 */
public class Routers {

    //constants for field references

    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String SNAT = "snat";
    public static final String IP = "ip";
    public static final String ID = "id";

    //fields
    String name;
    String state;
    String snat;
    String ip;
    String id;


//getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSnat() {
        return snat;
    }

    public void setSnat(String snat) {
        this.snat = snat;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


