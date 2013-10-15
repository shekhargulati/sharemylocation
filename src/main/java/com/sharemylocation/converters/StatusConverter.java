package com.sharemylocation.converters;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sharemylocation.domain.Status;

class StatusConverter implements Converter<Status> {

    @Override
    public DBObject toMongo(Status status) {
        return BasicDBObjectBuilder.start().
                add("status", status.getStatus()).
                add("postedOn", status.getPostedBy()).
                add("hashTags", status.getHashTags()).
                add("postedBy", status.getPostedBy()).
                add("location", status.getLocation()).get();
    }

    @Override
    public Status fromMongo(DBObject u) {
        return null;
    }

    @Override
    public String getName() {
        return "status-converter";
    }

}
