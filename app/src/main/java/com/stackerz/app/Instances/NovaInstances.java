package com.stackerz.app.Instances;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by ed on 4/11/14.
 */
public class NovaInstances {

    //constants for field references
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String FLAVOR = "flavor";
    public static final String NETID1 = "netid1";
    public static final String ADDR1 = "addr1";

    //fields
    String id;
    String name;
    String status;
    String flavor;
    String netid1;
    String addr1;


//getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getNetid1() {
        return netid1;
    }

    public void setNetid1(String netid1) {
        this.netid1 = netid1;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }
}


