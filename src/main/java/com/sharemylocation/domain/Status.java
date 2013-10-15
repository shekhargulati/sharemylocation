package com.sharemylocation.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class Status {

    private String id;

    @NotNull
    private String status;

    private final Date postedOn = new Date();

    private String[] hashTags;

    private String postedBy;

    private Location location;
    
    public Status() {
    }

    public Status(String status, String[] hashTags, String postedBy, Location location) {
        super();
        this.status = status;
        this.hashTags = hashTags;
        this.postedBy = postedBy;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public void setHashTags(String[] hashTags) {
        this.hashTags = hashTags;
    }
    
    
    public String[] getHashTags() {
        return hashTags;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
    public String getPostedBy() {
        return postedBy;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public Location getLocation() {
        return location;
    }

}
