package com.stackerz.app.Instances;

import android.os.Bundle;

/**
 * Created by ed on 4/11/14.
 */
public class NovaInstances {

    //constants for field references
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String FLAVOR = "flavor";
    public static final String ADDRFXD = "addrfxd";
    public static final String ADDRFLT = "addrflt";
    public static final String NET = "net";
    public static final String SECGRP = "secgrp";



    //fields
    String id;
    String name;
    String status;
    String flavor;
    String addrfxd;
    String addrflt;
    String net;
    String secgrp;


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

    public String getAddrfxd() {
        return addrfxd;
    }

    public void setAddrfxd(String addrfxd) {
        this.addrfxd = addrfxd;
    }

    public String getAddrflt() {
        return addrflt;
    }

    public void setAddrflt(String addrflt) {
        this.addrflt = addrflt;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getSecgrp() {
        return secgrp;
    }

    public void setSecgrp(String secgrp) {
        this.secgrp = secgrp;
    }
}


