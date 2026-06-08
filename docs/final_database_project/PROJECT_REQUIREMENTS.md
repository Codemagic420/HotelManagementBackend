# Final Database Project Requirements

## Overview
Databases should be complex enough to cover the curriculum of the whole course.

## Domain Model Complexity

### Main Entity Requirements
The relational database should have at least **10 main entities** (join tables do not count).

The other 2 solutions must be able to **store the same data and query the same information** – of course, the design and structure will be different.

### Architecture Note
We are working with a **"monolithic" design**. We deliberately avoid more complex architectures such as microservices. Instead, we want to focus on dealing with complex databases from the software developer perspective.

---

## Frontend Substitution
- Focus on **database and backend server** only
- Use **Postman**, **Swagger UI**, etc. to simulate API interaction
- Frontend is optional but should NOT be covered in the report (not relevant for this course)

---

## API Documentation

Most frameworks allow you to use **Swagger** to create API documentation. You should have **3 sets of endpoints** – one set for each database:

### Example Pattern:
- `…/mysql/products`
- `…/mongodb/products`
- `…/neo4j/products`

This provides an overview of the available database resources and actions.

---

## Project Components

The project will consist of the following:

1. **CRUD Application (Backend)**
2. **Relational Database**
3. **Document Database**
4. **Graph Database**
5. **Migrator Application**

### Technology Stack (Recommendations)
You are free to choose whichever tools you consider appropriate:

- **Relational Database**: MySQL (recommended, used in lectures)
- **Document Database**: MongoDB (recommended, used in lectures)
- **Graph Database**: Neo4j (recommended, used in lectures)
- **Backend**: JavaScript/TypeScript (Node.js/Express), Java/Spring Boot, C#/ASP.NET, Python/Django/FastAPI, PHP/Laravel, etc.

**NOTE**: You must develop your own backend. **Firebase, Supabase, or similar platforms are NOT allowed as your API** (you can use them as database services, but not as API).

---

## Mandatory Project Deliverables

- ✅ Backend CRUD application
- ✅ Relational database solution
- ✅ Document database solution
- ✅ Graph database solution
- ✅ Migrator application (one-time migration from RDBMS)
- ✅ Development environment using `docker-compose`
- ✅ Cloud deployment using managed database services
- ✅ Integration tests (application ↔ database)
- ✅ AI-based data enrichment feature

---

## Test Data Requirements

At delivery time, the databases should contain **meaningful amounts of realistic data**.

### Target
- At least **100 records for each entity**
  - 100+ rows in each table
  - 100+ documents in each collection
  - Similar for graph database

### Generation Method
- Test data can be generated with help of **AI tools like ChatGPT**

### Starting Point
- **Note**: You only need to generate test data for your **relational database** (starting point)
- The other databases will be seeded by the **migrator application**

---

## AI Integration (Data Enrichment) - MANDATORY

The system **must integrate with an external AI service** to generate derived data based on information stored in the database.

### Cost Optimization
- A **local model can be used** in the development environment to avoid costs
- The deployed production version may have the AI feature disabled

### AI Feature Requirements

The AI feature must:

- **Process and aggregate data** queried from the database (e.g., reviews, descriptions, metadata)
- **Persist the AI-generated result** back into the database
- **AI-generated data must be stored** and treated as part of the domain model
  - Must NOT be generated only transiently in memory
  - Must be part of the persistent storage

---

## Database User Privileges

Users will be defined at **database level**. There will be **at least**:

- ✅ A user for the **application** (with minimum privileges needed)
- ✅ A user with **full database admin privileges**
- ✅ A user with **read-only privileges**
- ✅ A user with **restricted reading privileges** (unable to see some data)

### Production Note
In production, the application should **NOT have admin rights**. It should only have the **minimum rights it needs** to perform its functionality.

---

## MySQL Solution (or other Relational Database)

A CRUD application with the following functionality:

- ✅ **Login/logout**
- ✅ **Query data from tables**
- ✅ **CRUD Functionality** depending on business logic
- ✅ **Typical API features**:
  - Pagination
  - Filtering
  - Sorting
  - **IMPORTANT**: No GET endpoints that query unlimited amounts of data
- ✅ **Security measures** to prevent typical attacks and unauthorized access

---

## MongoDB Solution (or other Document Database)

### Goal
Implement the **same functionality** (or at least most of it) as with the relational database.

### Design Pattern
Add the document database as a **parallel data source** to the existing backend and create **parallel REST APIs** or **GraphQL API**.

### Data Model Transformation
When moving from relational to document design, **de-normalize** the relational model.

#### Example: De-normalization Pattern
- **Relational**: customers, orders, order_items, products (separate tables)
- **Document**: Customer document contains:
  - Customer information
  - Array of order documents (embedded)
  - Each order contains array of order_item documents (embedded)
  - Product information included in order_items

This represents one of the key differences in document database design.

---

## Neo4j Solution (or other Graph Database)

### Goal
Implement the **same functionality** (or at least most of it) as with relational and document databases.

### Design Pattern
Add the graph database as **another data source** to the existing backend and create **parallel REST APIs** or **GraphQL API**.

### Graph Database Concepts
A graph database consists of **nodes and relationships**, making it easy to traverse between nodes.

#### Use Cases
- **Social Networks**: Who is a friend of a friend of a friend?
- **Product Recommendations**: What other products were purchased by customers who ordered or looked at a particular product?
- **Relationship Queries**: Leveraging graph traversal for complex relationship patterns

---

## Migrator Application

The migrator application is responsible for **one-time migration** from the relational database to MongoDB and Neo4j.

### Purpose
It simulates a situation where the company decides to migrate the production database with existing data into another database technology.

### Execution
- One-time migration **after relational database is finished** and seeded with test data
- Test data represents production data
- Migration transforms and populates MongoDB and Neo4j with data from MySQL

### Workflow
1. Extract data from relational database
2. Transform to document format for MongoDB
3. Transform to graph format for Neo4j
4. Load into respective databases
5. Verify data integrity

---

## Project Summary

The project follows this progression:

1. ✅ Create a system using **RDBMS** (like MySQL)
2. ✅ Implement the **same functionality** with MongoDB (or similar)
3. ✅ Implement the **same functionality** with Neo4j (or similar)
4. ✅ Implement a **migrator application** for one-time migration from RDBMS

### Learning Outcomes
This approach:
- Simulates a real-world database migration scenario
- Helps compare **strengths and weaknesses** of different database technologies
- Forces you to think about data modeling from multiple perspectives
- Demonstrates practical database technology switching

---

## Architecture Pattern Example

### Spring Boot Example (Service Layer Pattern)

```
Controllers (REST endpoints)
    ↓
Services (Business logic)
    ↓
Models / Repositories (Data access)
    ↓
Databases (MySQL, MongoDB, Neo4j)
```

### Implementation Options

**Option 1: Generics (if supported by framework)**
- Single set of controllers and services using generic types
- Switch between databases using a configuration variable

**Option 2: Multiple Modules**
- Separate module sets for each database
- Dedicated controllers, services, and repositories per database

### DTO Pattern
- Use **DTO objects** in controllers
- Do NOT expose domain models to the outside world
- Keeps API contract stable and independent of internal data structures

---

## Final Project Delivery

The final project will consist of a **report** and **artifacts** uploaded individually to WISEflow.

### Final Project Artifacts – Public Code Repository

#### Relational Database
- ✅ Database creation scripts including:
  - Tables, keys, indexes, constraints, referential integrity
  - Load of test data
  - Stored procedures
  - Triggers
  - Views
  - Events
  - User creation and privileges

#### Document Database (MongoDB)
- ✅ Dump file of the document database
- ✅ Script for loading test data
- ✅ Source code of the CRUD application

#### Graph Database (Neo4j)
- ✅ Dump file of the graph database
- ✅ Script for loading test data
- ✅ Source code of the CRUD application

#### CRUD Application & Migrator
- ✅ Source code link to external public repository (GitHub, etc.)
- ✅ Migrator application source code

#### Documentation
- ✅ Brief installation procedure specifying:
  - Code organization
  - Database import process
  - Full operational capabilities in test environment
  - **Recommended**: Use containerized solution with `docker-compose`

---

## Success Criteria Checklist

- [ ] At least 10 main entities in relational database
- [ ] Same data stored and queryable in all 3 databases
- [ ] CRUD operations working across all databases
- [ ] 100+ test records per entity
- [ ] API documentation (Swagger) for all 3 database sets
- [ ] Migrator application functional
- [ ] Docker-compose development environment
- [ ] Integration tests passing
- [ ] AI data enrichment feature implemented
- [ ] Database user privileges defined (app, admin, read-only, restricted)
- [ ] Security measures implemented (prevent SQL injection, unauthorized access, etc.)
- [ ] Pagination, filtering, sorting on API endpoints
- [ ] No unlimited data queries
- [ ] Cloud deployment with managed services
- [ ] Complete documentation and installation guide
- [ ] All artifacts in public repository
- [ ] Report covering all requirements

---

**Last Updated**: June 6, 2026  
**Project Status**: In Development  
**Reference**: HotelManagementBackend
