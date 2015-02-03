package com.stackerz.app.Instances;

/**
 * Created by ed on 4/11/14.
 */
public class NovaInstances {

    //constants for field references
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String FLAVOR = "flavor";
    public static final String NETID = "netid";
    public static final String ADDR = "addr";
    public static final String HOST = "host";

    //fields
    String id;
    String name;
    String status;
    String flavor;
    String host;
    String netid;
    String addr;


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

    public String getNetid() {
        return netid;
    }

    public void setNetid(String netid) {
        this.netid = netid;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}


