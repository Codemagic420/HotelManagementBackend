# Hotel Management System - Final Project Report

## COVER PAGE

Hotel Management System
Mandatory Assignment 1 & 2 + Advanced Implementation

Group Members:
Asger Valdemar Bergøe
Magnus Sørensen
Sophus Ingi Sophusson
Joel Darko-Martinez

KEA - Copenhagen School of Design and Technology
Date of Delivery: May 26, 2026

GitHub Repositories:
https://github.com/Codemagic420/HotelManagementBackend
https://github.com/Driconaari/HotelSQLGroupWork
https://github.com/Driconaari/Hotel-LLM-Assistant

---

## TABLE OF CONTENTS

1. Introduction
2. System Architecture
3. Relational Database (MySQL)
4. Backend Application (Spring Boot)
5. Document Database (MongoDB)
6. Graph Database (Neo4j)
7. Data Migration
8. AI Integration
9. Database Comparison
10. Challenges and Learning
11. Conclusion
12. References
13. Appendices

---

# 1. INTRODUCTION

We developed a hotel management system handling guest reservations, room bookings, housekeeping, pricing, and automated billing. The system was built through two mandatory assignments and extended with advanced features including containerized deployment, AI-powered data enrichment, and real-time staff assistance in Danish language.

The system manages core business processes: guest registration with AI enrichment, room reservations with automated status tracking, room inventory management, housekeeping task assignment, automated invoice generation with minibar and service items, and real-time AI-assisted staff operations in Danish.

---

# 2. SYSTEM ARCHITECTURE

## Polyglot Persistence Approach

We chose three databases for different purposes. MySQL serves as the primary transactional database ensuring data consistency for critical operations like billing. MongoDB caches pre-joined documents for fast dashboard reads without joins. Neo4j analyzes relationships between guests, rooms, and reservations for loyalty and analytics queries.

The backend uses Java Spring Boot for transactional operations with role-based access control. A separate Python service handles AI functionality, keeping machine learning concerns independent. Docker Compose containerizes all services for consistent local development and cloud deployment.

## Deployment Strategy

We deployed locally using Docker Compose starting all services with a single command. Spring Boot runs on port 8080, MySQL on port 3307, MongoDB on port 27017, Neo4j on ports 7474 and 7687. Database initialization scripts run automatically during startup, creating schema, test data, stored procedures, user roles, and audit triggers.

For production, we designed cloud-ready architecture with load balancing across multiple Spring Boot instances, managed databases handling backups and failover, and automatic scaling based on demand.

---

# 3. RELATIONAL DATABASE (MYSQL)

## Schema Design

We designed a relational schema with 14 tables: guests, rooms, room_types, reservations, bills, bill_items, season_rates, cleaners, and room_cleaning_tasks. The schema achieves Third Normal Form eliminating redundancy.

We use BIGINT for all ID columns ensuring scalability and MongoDB compatibility. Monetary values use DECIMAL for exact precision preventing floating-point errors in billing. Status fields use ENUM restricting values to predefined sets. Timestamps use TIMESTAMP for audit trails.

Primary keys use AUTO_INCREMENT for automatic unique values. Foreign keys with cascade rules maintain referential integrity. ON DELETE CASCADE applies to owned relationships. ON DELETE RESTRICT prevents deleting strongly-referenced data.

## Indexes and Optimization

We indexed frequently-searched columns: email for guest lookup, reservation reference numbers for booking searches, and composite indexes on check-in and check-out dates for availability queries.

## Stored Objects and Business Logic

A stored function retrieves room pricing based on room type and check-in date, automatically looking up seasonal rates. A stored procedure calculates final bills atomically by computing room charges, adding minibar items, adding service charges, and inserting the bill with all items in a single transaction. If any step fails, the operation rolls back.

An after-checkout trigger marks rooms as vacant and dirty, creates housekeeping tasks, and logs to the audit trail. An audit trigger records every change with old and new values, creating a complete history for compliance and dispute resolution.

Views provide role-based data access. A housekeeping view shows only information cleaners need without exposing guest names or pricing.

## Security and User Roles

We created four user roles with different privileges executed during startup: root (administrative setup), appuser (SELECT, INSERT, UPDATE, DELETE - Spring Boot application user), staff (limited access through views), cleaner (minimal access to housekeeping tasks only).

We prevented SQL injection using prepared statements at the application level. Parameterized queries separate structure from data values. We never hardcoded passwords, storing them in environment variables. Passwords are hashed using BCrypt.

## CRUD Application

The backend implements layered architecture: REST controllers handle HTTP requests, services implement business logic, repositories abstract database access through Spring Data JPA, and entities represent database tables.

Controllers expose REST endpoints following HTTP semantics: GET retrieves data, POST creates resources, PUT updates resources, DELETE removes resources. We implemented pagination for large result sets using LIMIT and OFFSET clauses at the database level.

---

# 4. BACKEND APPLICATION (SPRING BOOT)

## Architecture

The application separates concerns across layers. The API layer handles HTTP requests and responses converting between JSON and Java objects. The business logic layer implements operations and applies rules. The data access layer abstracts database interactions through repository interfaces.

Spring automatically creates and wires objects through dependency injection. Services declare dependencies through @Autowired and Spring provides instances at runtime, enabling loose coupling and easy testing.

## REST API and Endpoints

We designed endpoints for each major entity. Every entity has standard operations: list with pagination, retrieve by ID, create, update, and delete. Specialized endpoints handle AI functionality: PUT /api/rooms/{id}/ai-assessment, PUT /api/guests/{id}/ai-profile, PUT /api/reservations/{id}/ai-notes.

## Authentication and Authorization

We implemented JWT token-based authentication. Users send username and password to the login endpoint. The server validates credentials and returns a JWT token if valid. The token encodes user ID and role, digitally signed for verification.

We defined three roles: ADMIN for full system access, STAFF for operational access, and CLEANER for housekeeping access. Endpoints are protected with @PreAuthorize annotations specifying required roles.

---

# 5. DOCUMENT DATABASE (MONGODB)

## Design

We created three collections: rooms, reservations, and bills. We denormalize data embedding guest and room information directly in reservation documents. A reservation document contains the complete guest object (ID, name, email) and room object (number, type) rather than just ID references.

We accept this trade-off because MongoDB serves as a cache for fast reads, not the authoritative data source. Data synchronizes from MySQL periodically, so eventual consistency is acceptable.

## Implementation

Spring Data MongoDB repositories follow the same interface as JPA repositories, providing automatic CRUD methods and custom queries. The service layer uses these repositories identically to MySQL repositories, with only the underlying implementation differing.

---

# 6. GRAPH DATABASE (NEO4J)

## Model Design

We modeled guests, rooms, and reservations as nodes. Guest nodes contain ID and contact information. Room nodes contain number and type. Reservation nodes contain dates and status.

We created two relationship types: STAYED_IN relationships connect guests to reservations, BOOKED_ROOM relationships connect reservations to rooms. This graph enables efficient relationship queries finding guests who stayed multiple times or the most popular rooms.

## Implementation

Spring Data Neo4j repositories provide the same interface as JPA and MongoDB repositories. Guest loyalty analysis queries the graph finding guests with multiple STAYED_IN relationships. Room popularity analysis counts BOOKED_ROOM relationships.

---

# 7. DATA MIGRATION

The migration service synchronizes data from MySQL, the authoritative source, into MongoDB and Neo4j. This one-way sync ensures consistency while serving different purposes.

The migration reads all MySQL records, transforms them to MongoDB and Neo4j formats, and writes to respective databases. A verification step checks that counts match, confirming all data migrated. The migration is idempotent, running multiple times safely by overwriting previous data rather than duplicating.

---

# 8. AI INTEGRATION AND DATA ENRICHMENT

## System Overview

We implemented an AI assistant answering staff questions about hotel operations in Danish. The assistant combines live hotel data with language model inference and domain knowledge, then automatically saves responses to the database.

We use Ollama running Mistral 7B supporting Danish. LangChain provides abstractions for working with language models. Chroma stores vector embeddings for retrieval augmented generation. The Python service communicates with the Java backend through REST API calls.

## Implementation

The system uses carefully designed prompts setting context and injecting live data. When staff ask "What is the status of room 101?", the system fetches current room data from the API, injects it into the prompt, and the LLM generates an answer based on actual data.

We implemented RAG to address language model limitations. The system maintains a vector database of hotel knowledge. When answering questions, the system retrieves similar documents and injects them into the prompt, providing current, authoritative information.

## Automatic Persistence

Originally AI responses were displayed but not saved. We implemented automatic persistence where responses are stored in the database. After the LLM generates a response, the system identifies which entity was discussed by extracting information from the question. It calls the appropriate API endpoint to save the response.

This persistence enables staff to query "Which rooms have AI assessments?" and retrieve historical data. AI insights build over time and patterns emerge.

---

# 9. DATABASE COMPARISON AND TRADE-OFFS

## Design Choices

We chose MySQL as the primary database because the hotel domain is highly structured with clear relationships. ACID guarantees are essential for billing accuracy. The normalized schema prevents duplication.

MongoDB serves as a cache for responsive dashboards. The same data exists in MySQL but pre-joined in MongoDB, providing instant display without joins.

Neo4j addresses analytical questions about guest patterns and loyalty that are expensive in relational systems.

## Transaction Handling

MySQL provides ACID transactions spanning multiple tables. Complex operations either complete entirely or rollback, maintaining consistency. This is critical for billing.

MongoDB provides ACID within single documents. Updating a reservation is atomic, but coordinating updates across collections requires application-level logic.

Neo4j supports ACID at the query level where a single Cypher query updating multiple nodes and relationships is atomic.

## Performance Considerations

Simple lookups by ID are fastest in MongoDB since a single document contains all data. Complex joins across multiple tables are slowest in MySQL but optimized in Neo4j through relationship traversal.

Dashboard queries favor MongoDB. Transactional queries favor MySQL. Analytical queries favor Neo4j.

## Trade-Offs Made

Storage versus speed: MongoDB uses more disk space but reads faster. We accepted the storage cost for better user experience.

Consistency versus availability: MySQL prioritizes strong consistency. MongoDB prioritizes availability with eventual consistency. We chose consistency for financial operations.

Complexity versus flexibility: Operating multiple databases increases operational burden but provides flexibility to query data different ways. We chose flexibility accepting increased complexity.

---

# 10. CHALLENGES AND LEARNING

## Challenges Encountered

Designing the same domain for three database types required understanding how each technology approaches data. The relational mindset focuses on eliminating duplication, the document mindset embraces embedding for performance, the graph mindset sees everything as relationships.

Data synchronization between databases without inconsistencies required careful design. We implemented periodic refresh and verification to detect drift.

The AI integration required thinking differently about data flow. LLM responses needed to be persistent. This meant adding new fields to entities, creating new endpoints, and coordinating between Python and Java services.

Authentication across Java and Python services required careful token handling. The Python service needed to call the Java API as the staff user.

## Design Mistakes and Fixes

We initially used inconsistent data types with some IDs as INT and others as BIGINT. This caused mapping issues and required standardizing everything to BIGINT. The initial schema lacked indexes on frequently-searched columns. Hardcoding database passwords in the application was a security risk. Moving passwords to environment variables required careful handling.

## Key Learnings

Database choice is contextual, not absolute. No single database handles all access patterns optimally. Polyglot persistence is practical and increasingly common.

Normalization prevents duplication but requires joins that slow complex queries. Denormalization enables speed but creates maintenance challenges.

API design influences everything downstream. Well-designed REST APIs make integrating new features straightforward.

Transactional consistency and availability present trade-offs. Strong consistency is essential for financial operations but limits scalability.

AI integration requires more than the language model. Data flow, persistence, and user experience matter significantly.

---

# 11. CONCLUSION

We developed a comprehensive hotel management system using three databases for different purposes. MySQL provides transactional consistency for core operations ensuring billing accuracy. MongoDB enables responsive dashboards through denormalized caching. Neo4j supports relationship analysis for loyalty and analytics.

The REST API provides 20+ endpoints handling CRUD operations on all major entities. Pagination and sorting handle large datasets efficiently. Role-based access control ensures staff see appropriate information.

An AI assistant answers staff questions in Danish, combining live hotel data with language model inference. Responses are automatically persisted to the database.

The system demonstrates practical use of polyglot persistence, showing that different databases excel at different tasks. The AI integration shows how to combine machine learning with transactional systems while maintaining data consistency.

---

# 12. REFERENCES

MySQL 8.0 Reference Manual. (2024). https://dev.mysql.com/doc/
MongoDB Manual. (2024). https://docs.mongodb.com/manual/
Neo4j Documentation. (2024). https://neo4j.com/docs/
Spring Boot Reference Documentation. (2024). https://spring.io/projects/spring-boot
Spring Data JPA. (2024). https://spring.io/projects/spring-data-jpa
Spring Security. (2024). https://spring.io/projects/spring-security
Ollama Documentation. (2024). https://github.com/ollama/ollama
LangChain Documentation. (2024). https://python.langchain.com/

---

# 13. APPENDICES

## Appendix A: MySQL Database Schema

Complete database schema with 14 tables. Located in: `/dumps/01_mysql_schema.sql`

Tables: user_account, guest, room, room_type, reservation, bill, bill_item, season_rate, cleaner, room_cleaning_task, room_cleaning_assignment, reservation_guest, extra_service, inventory_item.

All tables include PRIMARY KEY, FOREIGN KEY constraints, indexes on frequently searched columns (email, reference_no, room_status), and CASCADE rules for referential integrity.

## Appendix B: MySQL Data Sample

Sample data from MySQL export including guest records, reservations, room inventory, and bills. Located in: `/dumps/02_mysql_data_sample.sql`

Current data: 150 guests, 110 rooms, 120 reservations, 120 bills with complete billing history and relationships.

## Appendix C: Neo4j Graph Export

Neo4j node exports showing complete graph structure with 315+ nodes including Guest, Room, Reservation, and Bill nodes.

Located in: `/dumps/03_neo4j_nodes.txt` and `/dumps/04_neo4j_relationships.txt`

Relationships: STAYED_IN (guest to reservation), BOOKED_ROOM (reservation to room), HAS_BILL (reservation to bill).

## Appendix D: MongoDB Document Structure

MongoDB collection structure examples showing denormalized document format for fast reads without joins.

Located in: `/dumps/05_mongodb_structure.txt`

Collections: reservations (with embedded guest and room), rooms, bills (with embedded line items).

## Appendix E: API Endpoints

Core REST endpoints for hotel management operations:
- POST /api/login (authentication)
- GET /api/guests (list with pagination)
- POST /api/guests (create guest)
- PUT /api/guests/{id} (update guest)
- PUT /api/guests/{id}/ai-profile (AI enrichment)
- GET /api/reservations (list with pagination)
- POST /api/reservations (create reservation)
- GET /api/rooms (list with pagination)
- PUT /api/rooms/{id}/ai-assessment (AI assessment)
- POST /api/migrate (trigger data migration)

## Appendix F: Deployment Configuration

Docker Compose configuration running: Spring Boot (port 8080), MySQL (port 3307), MongoDB (port 27017), Neo4j (ports 7474, 7687).

Automatic initialization scripts create schema, populate test data (150+ records per entity), set up user roles with privileges, create audit triggers, and add AI enrichment columns.

---

Document Version: 4.0
Last Updated: May 26, 2026
Status: Final Report - Ready for Submission

---