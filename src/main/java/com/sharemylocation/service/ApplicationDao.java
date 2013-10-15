package com.sharemylocation.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sharemylocation.converters.Converter;

public class ApplicationDao {

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
        DBCursor dbCursor = statusCollection.find();
        List<T> list = new ArrayList<>();
        Iterator<DBObject> iterator = dbCursor.iterator();
        while(iterator.hasNext()){
            list.add(converter.fromMongo(iterator.next()));
        }
        return list;
    }
}
