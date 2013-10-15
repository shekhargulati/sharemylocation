package com.sharemylocation.config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoConfig {

    @Produces
    @ApplicationScoped
    public DB db() {
        try {
            String host = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
            int port = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
            String dbname = System.getenv("OPENSHIFT_APP_NAME");
            String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
            String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
            MongoClient mongoClient = new MongoClient(new ServerAddress(host, port));
            mongoClient.setWriteConcern(WriteConcern.SAFE);
            DB db = mongoClient.getDB(dbname);
            if (db.authenticate(username, password.toCharArray())) {
                return db;
            }
            throw new RuntimeException("Not able to authenticate with MongoDB");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
