package com.sharemylocation.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.sharemylocation.converters.Converter;
import com.sharemylocation.converters.StatusConverter;
import com.sharemylocation.domain.Location;
import com.sharemylocation.domain.Status;
import com.sharemylocation.domain.StatusWithDistance;
import com.sharemylocation.domain.Type;

public class ApplicationDao {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private DB db;

    public <T> void save(T document, Converter<T> converter) {
        DBCollection statusCollection = db.getCollection("statuses");
        statusCollection.save(converter.toMongo(document));
    }

    public <T> T find(String id, Converter<T> converter) {
        DBCollection statusCollection = db.getCollection("statuses");
        return converter.fromMongo(statusCollection.findOne(new ObjectId(id)));
    }

    public <T> List<T> findAll(Converter<T> converter) {
        DBCollection statusCollection = db.getCollection("statuses");

        DBCursor dbCursor = statusCollection.find().sort(new BasicDBObject("postedOn", -1)).limit(50);
        return toList(dbCursor, converter);
    }

    private <T> List<T> toList(DBCursor dbCursor, Converter<T> converter) {
        List<T> list = new ArrayList<>();
        Iterator<DBObject> iterator = dbCursor.iterator();
        while (iterator.hasNext()) {
            list.add(converter.fromMongo(iterator.next()));
        }
        return list;
    }

    public <T> List<T> findNear(String[] hashTags, String postedBy, double[] lngLat, Converter<T> converter) {
        DBCollection statusCollection = db.getCollection("statuses");
        BasicDBObject cmd = new BasicDBObject();

        BasicDBObject query = new BasicDBObject();

        BasicDBObject geometryObj = new BasicDBObject("type", "Point");
        geometryObj.append("coordinates", lngLat);
        BasicDBObject geometryQuery = new BasicDBObject("$geometry", geometryObj);
        query.append("$near", geometryQuery);
        cmd.append("location", query);

        if (hashTags != null && hashTags.length >0) {
            cmd.append("hashTags", new BasicDBObject("$in", hashTags));
        }

        if (postedBy != null && postedBy != "") {
            cmd.put("postedBy", postedBy);
        }

        logger.info("Near query : \n" + cmd.toString());

        DBCursor dbCursor = statusCollection.find(cmd);
        return toList(dbCursor, converter);
    }

    public List<StatusWithDistance> findGeoNear(String[] hashTags, double[] lngLat, Converter<Status> converter) {
        BasicDBObject cmd = new BasicDBObject();
        cmd.put("geoNear", "statuses");
        cmd.put("near", lngLat);
        cmd.put("spherical", true);
        cmd.put("num", 10);
        if (hashTags != null && hashTags.length >0) {
            BasicDBObject hashTagQuery = new BasicDBObject();
            hashTagQuery.put("hashTags", new BasicDBObject("$in", hashTags));
            cmd.put("query", hashTagQuery);
        }

        cmd.put("distanceMultiplier", 6371);

        logger.info("GeoNear Query  \n" + cmd.toString());
        CommandResult commandResult = db.command(cmd);

        BasicDBList results = (BasicDBList) commandResult.get("results");
        List<StatusWithDistance> statuses = new ArrayList<>();
        if (results == null) {
            return statuses;
        }

        for (Object obj : results) {
            BasicDBObject obj2 = (BasicDBObject) obj;
            DBObject result = ((BasicDBObject) obj2.get("obj"));
            Status status = converter.fromMongo(result);
            StatusWithDistance statusWithDistance = new StatusWithDistance(status, obj2.getDouble("dis"));
            statuses.add(statusWithDistance);
        }

        return statuses;
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("sharemylocation");
        ApplicationDao dao = new ApplicationDao();
        dao.db = db;

        Converter<Status> converter = new StatusConverter();
        String[] hashTags = new String[0];
        String postedBy = "Shekhar Gulati";
        double[] lngLat = new double[] { 27, 90 };
        dao.save(new Status("hello how are you", hashTags, postedBy, new Location(Type.POINT, lngLat)), converter);

        List<Status> allStatus = dao.findAll(converter);

        System.out.println(allStatus);

        List<Status> nearStatuses = dao.findNear(hashTags, postedBy, lngLat, converter);

        System.out.println(nearStatuses);

        List<StatusWithDistance> geoNearStatuses = dao.findGeoNear(hashTags, lngLat, converter);
        System.out.println(geoNearStatuses);

    }
}
