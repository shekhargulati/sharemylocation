package com.sharemylocation.service;

import javax.inject.Inject;

import org.bson.types.ObjectId;

import com.mongodb.DB;
import com.mongodb.DBCollection;
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
}
