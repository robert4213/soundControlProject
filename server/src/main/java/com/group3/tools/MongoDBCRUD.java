package com.group3.tools;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;

public class MongoDBCRUD {
    /* Connect to mongoDB */
    private MongoClient mongoClient = new MongoClient("localhost", 27017);
    /* Connect to Database in MongoDB */
    private MongoDatabase mongoDatabase = null;

    private MongoCollection<Document> collection = null;

    public MongoDBCRUD(String database, String collectionName){
        /* Connect to Collection */
        mongoDatabase = mongoClient.getDatabase(database);
        collection = mongoDatabase.getCollection(collectionName);
    }

    public void insertResourceData(int resourceid, int value){
        Document document = new Document("resourceid", resourceid).
                append("value", value);
        collection.insertOne(document);
    }

    public void insertResourceData(int resourceid, String value){
        Document document = new Document("resourceid", resourceid).
                append("value", value);
        collection.insertOne(document);
    }

    public void updateResourceData(int resourceid, int value){
        Document newDocument = new Document();
        newDocument.append("$set", new Document().append("value", value));
        Document query = new Document().append("resourceid", resourceid);
        collection.updateOne(query, newDocument);
    }

    public void updateResourceData(int resourceid, String value){
        Document newDocument = new Document();
        newDocument.append("$set", new Document().append("value", value));
        Document query = new Document().append("resourceid", resourceid);
        collection.updateOne(query, newDocument);
    }

    public void updateClaps(String endpoint, int claps){
        Document newDocument = new Document();
        newDocument.append("$set", new Document().append("claps", claps));
        Document query = new Document().append("endpoint", endpoint);
        System.out.println("MongoDB Update: "+endpoint+"  "+claps);
        collection.updateOne(query, newDocument);
    }

    public MongoCursor<Document> getDataByResourceId(int resourceid){
        BasicDBObject query = new BasicDBObject();
        query.put("resourceid", resourceid);
        FindIterable<Document> findIterable = collection.find(query);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        return mongoCursor;
    }

    public MongoCursor<Document> getEndpointByClaps(int claps){
        BasicDBObject query = new BasicDBObject();
        query.put("claps", claps);
        FindIterable<Document> findIterable = collection.find(query);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        return mongoCursor;
    }

}
