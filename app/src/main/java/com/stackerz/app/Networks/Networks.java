package com.stackerz.app.Networks;

/**
 * Created by ed on 4/11/14.
 */
public class Networks {

    //constants for field references

    public static final String NAME = "name";
    public static final String STATE = "state";
    public static final String EXTERNAL = "external";
    public static final String TYPE = "type";
    public static final String SHARED = "shared";
    public static final String ID = "id";

    //fields
    String name;
    String state;
    String external;
    String type;
    String shared;
    String id;


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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExternal() {
        return external;
    }

    public void setExternal(String external) {
        this.external = external;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }
}


