package com.kea.hotel.hotelbackend.migration;

import org.springframework.stereotype.Component;

@Component
public class DataMigrator {
    public void migrateToMongo() {
        // TODO: fetch entities from JPA, transform, insert into MongoDB
    }

    public void migrateToNeo4j() {
        // TODO: fetch entities, map nodes + edges
    }
}
