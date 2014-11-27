package com.stackerz.app.Images;

/**
 * Created by ed on 4/11/14.
 */
public class Images {

    //constants for field references

    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String VISIBILITY = "visibility";
    public static final String FORMAT = "format";
    public static final String ID = "id";

    //fields
    String name;
    String status;
    String visibility;
    String format;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}


