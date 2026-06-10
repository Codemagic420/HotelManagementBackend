package com.kea.hotel.hotelbackend.neo4j.config;

import jakarta.persistence.EntityManagerFactory;
import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.neo4j.core.DatabaseSelectionProvider;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

// Spring Boot 4.0 with JPA + Neo4j: both auto-configurations use
// @ConditionalOnMissingBean(PlatformTransactionManager.class), so only one transaction manager
// gets auto-created. We declare both explicitly so each data store has the right one,
// and point Neo4j repositories at neo4jTransactionManager via @EnableNeo4jRepositories.
@Configuration
@EnableNeo4jRepositories(
        basePackages = "com.kea.hotel.hotelbackend.neo4j.repository",
        transactionManagerRef = "neo4jTransactionManager",
        neo4jTemplateRef = "neo4jTemplate"
)
public class Neo4jConfig {

    @Bean("transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean("neo4jTransactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(Driver driver,
                                                           DatabaseSelectionProvider selectionProvider) {
        return new Neo4jTransactionManager(driver, selectionProvider);
    }

    @Bean("neo4jTemplate")
    public Neo4jTemplate neo4jTemplate(Neo4jClient neo4jClient,
                                       Neo4jMappingContext neo4jMappingContext,
                                       Neo4jTransactionManager neo4jTransactionManager) {
        return new Neo4jTemplate(neo4jClient, neo4jMappingContext, neo4jTransactionManager);
    }
}
