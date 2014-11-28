package com.stackerz.app.Security;

/**
 * Created by ed on 4/11/14.
 */
public class Security {

    //constants for field references

    public static final String NAME = "name";
    public static final String DESC = "desc";
    public static final String ID = "id";

    //fields
    String name;
    String desc;
    String id;


//getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


