package com.stackerz.app.Instances;

import android.os.Bundle;

/**
 * Created by ed on 4/11/14.
 */
public class NovaInstances {

    //constants for field references
    public static final String ID = "id";
    public static final String NAME = "name";

    //fields
    String id;
    String name;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public Bundle toBundle(){
        Bundle b = new Bundle();
        b.putString(NAME, this.name);
        b.putString(ID, this.id);
        return b;
    }

}


