hvordan fungere# Exam Requirements

## Project Assignment Overview

The final project assignment is to develop the backend part of a web application and deploy the whole solution to the cloud. You need to implement 3 solutions with following databases:

➢ **Relational database** (MySQL is recommended)  
➢ **Document database** (MongoDB is recommended)  
➢ **Graph database** (Neo4j is recommended)  

**Recommended database cloud services:**
- **Azure MySQL server** (for relational database)
- **MongoDB Atlas** (for document database)
- **Neo4j AuraDB** (for graph database)

---

## Mandatory project deliverables: 

• Backend CRUD application 
• Relational database solution 
• Document database solution 
• Graph database solution 
• Migrator application (one-time migration from RDBMS) 
• Development environment using docker-compose 
• Cloud deployment using managed database services 
• Integration tests (application ↔ database) 
• AI-based data enrichment feature 

## Test data 

At the delivery time, the databases should contain some meaningful amount of realistic data. 
Aim for having at least 100 records for each entity – like 100 rows in each table, 100 documents 
in each collection, etc. It should be possible to generate the test data with the help of AI tools 
like ChatGPT. 

NOTE: You only need to generate the test data for your relational database as that is your 
starting point. The other databases will be seeded by the migrator application. 

## AI Integration (Data Enrichment) 

The system must integrate with an external AI service to generate derived data based on 
information stored in the database. To avoid costs, a local model can be used in the 
development environment. The deployed production version may have the AI feature disabled. 
The AI feature must: 

• Process and aggregate data queried from the database (e.g. reviews, descriptions, 
metadata) 
• Persist the AI-generated result back into the database 
• AI-generated data must be stored and treated as part of the domain model and must 
not be generated only transiently in memory. 

## Database user privileges 

Users will be defined at database level. There will be, at least: 

➢ A user for the application (with the minimum privileges it needs) 
➢ A user with full database admin privileges 
➢ A user with read-only privileges 
➢ A user with restricted reading privileges, which will be unable to see some data 

NOTE: We need to define a user for the CRUD application itself for connecting to the database 
server. In production, the application should not have admin rights, but it should only have the 
minimum rights it needs to perform its functionality. 

## MySQL solution (or another relational database) 

A CRUD application (create/read/update/delete) functionality to implement can be: 

➢ Login/logout. 
➢ Query data from the tables. 
➢ CRUD Functionality depending on the business logic 
➢ Typical API features like pagination, filtering, and sorting. You should not have any GET 
endpoints that query unlimited amount of data. 
➢ Security measures to prevent typical attacks and non-authorized access to data. 

## MongoDB solution (or another document database) 

The goal is to implement the same functionality (or at least most of it) like with the relational 
database.  

You add the document database as a parallel data source to the existing backend (and create 
parallel REST APIs or GraphQL API). 

To move from the relational model to document design, we usually need to de-normalize our 
relational model. For example, if we have tables like customers, orders, order_items, products 
we can have all this information in a customer document which will contain multiple levels of 
embedded documents – a customer document will contain an array of order documents. Each 
order can contain multiple order_item documents… 

## Neo4j solution (or another graph database) 

The goal is to implement the same functionality (or at least most of it) as with the relational and 
document database.  

You add the graph database as another data source to the existing backend (and create parallel 
REST APIs or GraphQL API).  

A graph database consists of nodes and relationships which makes it easy to traverse between 
nodes – for example if we want to know who a friend of a friend of a friend is… (social media 
app) or if we want to know what other products were purchased by customers who ordered or 
looked at a particular product (product recommendation). 

## Migrator application 

Application responsible for one-time migration from the relational database to MongoDB and 
Neo4j. 

It will simulate a situation where the company decides to migrate the production database with 
existing data into another database technology. That is why it will be aiming for one-time 
migration once your relational database is finished and seeded with test data that will 
represent the production data. 

## Summary 

➢ We start with creating a system which uses an RDBMS like MySQL. 
➢ Then we implement the same functionality with MongoDB (or similar DB). 
➢ Then we implement the same functionality with Neo4j (or similar DB). 
➢ Implement a migrator application for one-time migration from RDBMS. 

Implementing the same functionality with different database technologies simulates a scenario 
when a company decides to switch from one database technology to another which we will 
achieve using our migrator application. 

It will also help to compare the strengths and weaknesses of used database technologies. 

## Example of all 3 solutions in one Spring Boot project

(service layer is not visible) 

Apply layered architecture: controllers -> services -> models / repositories. If your backend 
supports generics, you can create a single set of controllers and services working with generic 
types and switch between the databases using a variable. Otherwise, you can make 3 sets of 
modules. Also, use DTO objects in the controllers. Do not expose the models to the outside. 

## Final Project Delivery 

The final group project will be uploaded individually to WISEflow. It will consist of a report and 
a series of artifacts. 

## Final Project Artifacts – public code repository 

➢ Relational database scripts (one or several), including: 
  • Database creation, including tables, keys, indexes, constraints, and referential 
integrity checks. 
  • Load of test data 
  • Stored procedures 
  • Triggers 
  • Views 
  • Events 
  • Creation of users and privileges 

➢ The source code of the CRUD application and the migrator - included as a link to an 
external public code repository (like GitHub). 

➢ For MongoDB system: 
  • dump file of the document database 
  • Script for loading the test data 
  • The source code of the CRUD application. 

➢ For Neo4j system: 
  • dump file of the graph database 
  • Script for loading the test data 
  • The source code of the CRUD application. 

➢ The source code of the migration application (link to GitHub or another public repo). 

➢ A brief installation procedure that specifies how to organize the code and import the 
databases in a test environment with full operational capabilities. For this, it is 
recommended to use a containerized solution with docker-compose.
