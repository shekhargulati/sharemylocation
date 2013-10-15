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

    public Status(String status, String[] hashTags, String postedBy, Location location) {
        super();
        this.status = status;
        this.hashTags = hashTags;
        this.postedBy = postedBy;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Date getPostedOn() {
        return postedOn;
    }

    public String[] getHashTags() {
        return hashTags;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public Location getLocation() {
        return location;
    }

}
