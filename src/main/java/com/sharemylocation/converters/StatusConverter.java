package com.sharemylocation.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.sharemylocation.domain.Location;
import com.sharemylocation.domain.Status;
import com.sharemylocation.domain.Type;

class StatusConverter implements Converter<Status> {

    @Override
    public DBObject toMongo(Status status) {
        return BasicDBObjectBuilder.start().add("status", status.getStatus()).add("postedOn", status.getPostedOn())
                .add("hashTags", status.getHashTags()).add("postedBy", status.getPostedBy())
                .add("location.type", status.getLocation().getType())
                .add("location.lngLat", status.getLocation().getLngLat()).get();
    }

    @Override
    public Status fromMongo(DBObject dbObject) {
        BasicDBObject basicDBObject = (BasicDBObject) dbObject;
        String status = basicDBObject.getString("status");
        Date postedOn = basicDBObject.getDate("postedOn");
        ObjectId objectId = basicDBObject.getObjectId("_id");
        String id = objectId.toString();

        BasicDBList basicDbList = (BasicDBList) basicDBObject.get("hashTags");
        String[] hashTags = toStringArray(basicDbList);
        String postedBy = basicDBObject.getString("postedBy");
        String type = basicDBObject.getString("location.type");
        BasicDBList lngLatList = (BasicDBList) basicDBObject.get("location.lngLat");
        double[] lngLat = { (Double) lngLatList.get(0), (Double) lngLatList.get(1) };
        return new Status(id , status, hashTags, postedBy, new Location(Type.type(type), lngLat), postedOn);
    }

    private String[] toStringArray(BasicDBList basicDbList) {
        List<String> list = new ArrayList<>();
        for (Object obj : basicDbList) {
            list.add((String) obj);
        }
        return list.toArray(new String[0]);
    }

    @Override
    public String getName() {
        return "status-converter";
    }

}
