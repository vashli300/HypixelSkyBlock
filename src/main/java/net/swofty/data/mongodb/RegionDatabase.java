package net.swofty.data.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.minestom.server.coordinate.Pos;
import net.swofty.region.RegionType;
import net.swofty.region.SkyBlockRegion;
import org.bson.Document;

import java.util.*;

public class RegionDatabase implements MongoDB {

    public final String id;

    public static MongoClient client;
    public static MongoDatabase database;
    public static MongoCollection<Document> collection;

    public RegionDatabase(String id) {
        this.id = id;
    }

    @Override
    public MongoDB connect(String connectionString) {
        ConnectionString cs = new ConnectionString(connectionString);
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(cs).build();
        client = MongoClients.create(settings);

        database = client.getDatabase("Minestom");
        collection = database.getCollection("regions");
        return this;
    }

    @Override
    public void set(String key, Object value) {
        insertOrUpdate(key, value);
    }

    @Override
    public Object get(String key, Object def) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        if (doc == null) {
            return def;
        }
        return doc.get(key);
    }

    public List<Document> getAll() {
        FindIterable<Document> results = collection.find();
        List<Document> list = new ArrayList<>();
        for (Document doc : results) {
            list.add(doc);
        }
        return list;
    }

    @Override
    public String getString(String key, String def) {
        return get(key, def).toString();
    }

    @Override
    public int getInt(String key, int def) {
        return Integer.parseInt(get(key, def).toString());
    }

    @Override
    public long getLong(String key, long def) {
        return Long.parseLong(getString(key, def + ""));
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        return Boolean.parseBoolean(get(key, def).toString());
    }

    @Override
    public <T> List<T> getList(String key, Class<T> t) {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();

        if (found == null) {
            return new ArrayList<>();
        }

        return found.getList(key, t);
    }

    public Document getDocument() {
        Document query = new Document("_id", id);
        return collection.find(query).first();
    }

    @Override
    public boolean remove(String id) {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();

        if (found == null) {
            return false;
        }

        collection.deleteOne(query);
        return true;
    }

    public void insertOrUpdate(String key, Object value) {
        if (exists()) {
            Document query = new Document("_id", id);
            Document found = collection.find(query).first();

            assert found != null;
            collection.updateOne(found, Updates.set(key, value));
            return;
        }
        Document New = new Document("_id", id);
        New.append(key, value);
        collection.insertOne(New);
    }

    public boolean exists() {
        Document query = new Document("_id", id);
        Document found = collection.find(query).first();
        return found != null;
    }

    public static List<SkyBlockRegion> getAllRegions() {
        List<SkyBlockRegion> regions = new ArrayList<>();
        for (Document doc : collection.find()) {
            String name = doc.getString("_id");
            RegionType type = RegionType.valueOf(doc.getString("type"));
            int x1 = doc.getInteger("x1");
            int y1 = doc.getInteger("y1");
            int z1 = doc.getInteger("z1");
            int x2 = doc.getInteger("x2");
            int y2 = doc.getInteger("y2");
            int z2 = doc.getInteger("z2");
            SkyBlockRegion region = new SkyBlockRegion(
                    name,
                    new Pos(x1, y1, z1),
                    new Pos(x2, y2, z2),
                    type);
            regions.add(region);
        }
        return regions;
    }
}
