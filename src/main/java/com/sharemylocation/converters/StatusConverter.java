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
        BasicDBObject lngLat = new BasicDBObject();
        if (status.getLocation() != null) {
            BasicDBList lngLatList = new BasicDBList();
            lngLatList.add(status.getLocation().getLngLat()[0]);
            lngLatList.add(status.getLocation().getLngLat()[1]);
            lngLat.put("type", status.getLocation().getType().getName());
            lngLat.put("lngLat", lngLatList);
        }

        return BasicDBObjectBuilder.start().add("status", status.getStatus()).add("postedOn", status.getPostedOn())
                .add("hashTags", status.getHashTags()).add("postedBy", status.getPostedBy()).add("location", lngLat)
                .get();
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

        BasicDBObject locationObj = (BasicDBObject) basicDBObject.get("location");
        if (locationObj != null && !locationObj.isEmpty()) {
            String type = locationObj.getString("type");
            BasicDBList lngLatList = (BasicDBList) locationObj.get("lngLat");
            double[] lngLat = new double[2];
            if (lngLatList != null) {
                lngLat[0] = (Double) lngLatList.get(0);
                lngLat[1] = (Double) lngLatList.get(1);
            }
            return new Status(id, status, hashTags, postedBy, new Location(Type.type(type), lngLat), postedOn);
        }

        return new Status(id, status, hashTags, postedBy, postedOn);

    }

    private String[] toStringArray(BasicDBList basicDbList) {
        List<String> list = new ArrayList<>();
        if (basicDbList == null) {
            return list.toArray(new String[0]);
        }
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
