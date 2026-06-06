package com.kea.hotel.hotelbackend.neo4j.repository;

import com.kea.hotel.hotelbackend.neo4j.node.Neo4jUserAccount;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Neo4jUserAccountRepository extends Neo4jRepository<Neo4jUserAccount, Long> {
}
