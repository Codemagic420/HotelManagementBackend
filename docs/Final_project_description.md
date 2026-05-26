# FINAL PROJECT REPORT - STRUCTURE & REQUIREMENTS

## Overview
The final report will have a big influence on the exam grade. It is the only information accessible to the external censor prior to the oral exam.

---

## REPORT STRUCTURE

### Cover Page
- Title
- Full names of all students in the group
- Group number
- Date of delivery

### Front Matter
- List of Figures
- List of Appendices
- Table of Contents (paginated index – the whole document must be paginated)

---

## 1. Introduction

### 1.1 System Overview and Cloud Architecture
This section must include:
- **Architecture diagram** showing the system deployed in the cloud
- **Diagram** showing the local development deployment using docker-compose

### 1.2 Explanation of Choices for Databases and Programming Languages
- Justification for MySQL, MongoDB, and Neo4j
- Programming languages chosen (Java, Python, etc.)
- Tools selected and why
- Technology stack rationale

---

## 2. Relational Database (MySQL)

### 2.1 Introduction to Relational Databases
- Explanation of relational database concepts
- How ACID properties ensure data integrity
- Overview of MySQL 8.0 capabilities

### 2.2 Database Design

#### 2.2.1 Entity/Relationship Model
- **Conceptual model** (entities and relationships at highest level)
- **Logical model** (attributes, cardinalities, primary/foreign key relationships)
- **Physical model** (implementation in MySQL with specific data types)
- Include ER diagrams for each stage

#### 2.2.2 Normalization Process
- Explanation of 1NF, 2NF, 3NF
- How the database achieves 3NF
- Redundancy elimination
- Data integrity assurance

### 2.3 Physical Data Model

#### 2.3.1 Data Types
- Types used (INT, VARCHAR, DECIMAL, DATE, ENUM, BOOLEAN, LONGTEXT, TIMESTAMP, etc.)
- Rationale for each choice
- Data type consistency across tables

#### 2.3.2 Primary and Foreign Keys
- Primary key strategy (AUTO_INCREMENT, etc.)
- Foreign key relationships
- Referential integrity enforcement
- Cascade rules (ON DELETE CASCADE, etc.)

#### 2.3.3 Indexes
- Indexes created (idx_guest_email, idx_ref_no, idx_room_status, etc.)
- Performance optimization reasoning
- Index usage in queries

#### 2.3.4 Constraints and Referential Integrity
- NOT NULL constraints
- UNIQUE constraints
- CHECK constraints
- FOREIGN KEY constraints
- Data validation mechanisms

### 2.4 Stored Objects
- **Stored Procedures**: Description and purpose
  - sp_CalculateFinalBill
  - Other procedures implemented
- **Functions**: What they do
  - fn_GetRoomRate
  - Other functions
- **Views**: Their role in the system
  - vw_HousekeepingList
  - Other views
- **Triggers**: Automation logic
  - tr_AfterCheckout
  - tr_AuditLog
  - Other triggers
- **Events**: Scheduled tasks (if any)

### 2.5 Transactions
- Explanation of transaction structure
- ACID properties in practice
- Implementation details
- Isolation levels used
- Transaction handling in the application

### 2.6 Auditing
- Explanation of audit structure implemented with triggers
- What events are audited
- Audit table structure
- How audit trails are maintained
- Access to audit data

### 2.7 Security

#### 2.7.1 Users and Privileges
- User accounts created (admin, appuser, staff, cleaner, etc.)
- Privilege levels per role
- SQL permissions granted (SELECT, INSERT, UPDATE, DELETE, EXECUTE)
- Principle of least privilege implementation

#### 2.7.2 SQL Injection Prevention
- What SQL injection is
- How it is prevented in the project
- Prepared statements usage
- Parameterized queries
- Security validation

### 2.8 CRUD Application for RDBMS

#### Data Layer
- **Models/Entities**: Class structure
  - Guest.java
  - Room.java
  - Reservation.java
  - Bill.java
  - Other entities
- **Repositories**: Data access pattern
  - GuestRepository
  - RoomRepository
  - ReservationRepository
  - Other repositories
  - Spring Data JPA interface usage
- **ORM Details**: Hibernate mapping, annotations, relationships

#### Service Layer
- Business logic implementation
- CRUD operations
- Transaction management
- Error handling

#### Controller Layer
- REST endpoint design
- HTTP methods mapping
- Response formats
- Error responses

#### Graphical Schema
- Backend module diagram showing Model → Repository → Service → Controller flow
- Dependency injection visualization
- Layered architecture diagram

#### Other Relevant Topics
- **Transactions**: How transactions are managed in service layer
- **Calling Stored Procedures**: Integration with MySQL procedures
- **Security**: Authentication and authorization filters
- **Integration Testing**: How CRUD operations are tested
- **AI Integration**: How AI enrichment fields are used and updated

---

## 3. Document Database (MongoDB)

### 3.1 Introduction to Document Databases
- NoSQL concepts and document-based storage
- BSON format explanation
- Benefits and trade-offs of document databases
- Flexibility and schema-less design

### 3.2 Database Design
- **Collections**: Purpose and structure
  - rooms collection
  - reservations collection
  - bills collection
  - Other collections
- **Document structure** with examples (real data)
- **Embeddings**: How related data is embedded in documents
- **Graphical representation**: Visual schema showing collections and relationships

### 3.3 Features Description

#### Indexes
- Indexes created
- Performance impact
- Query optimization

#### Transactions
- Transaction support in MongoDB
- ACID compliance in document databases
- Implementation approach

#### Primary Keys
- _id field usage
- Distributed ID generation

#### Constraints
- Validation rules
- Data constraints (if implemented)

#### Stored Objects
- **Note**: If stored objects (procedures, functions) are not available in MongoDB, explain how they were replaced
- Alternative implementations used (aggregation pipelines, application logic, etc.)

### 3.4 CRUD Application for Document Database
- Focus on differences from RDBMS implementation
- Spring Data MongoDB repository pattern
- Document insertion, retrieval, update, delete
- Query examples
- Index usage in queries
- Performance characteristics

---

## 4. Graph Database (Neo4j)

### 4.1 Introduction to Graph Databases
- Node and relationship concepts
- Property graph model
- Benefits for relationship queries
- Cypher query language basics

### 4.2 Database Design
- **Nodes**: Types of nodes, properties, counts
  - :Guest nodes
  - :Room nodes
  - :Reservation nodes
  - Other node types
- **Relationships**: Types of relationships, patterns, counts
  - STAYED_IN relationships
  - BOOKED_ROOM relationships
  - Other relationships
- **Graphical representation**: Database model diagram (not data screenshot)
  - Shows structure and patterns
  - Relationship directions

### 4.3 Features Description

#### Indexes
- Indexes on node properties
- Performance optimization

#### Transactions
- Transaction handling in Neo4j
- ACID properties

#### Primary Keys
- ID strategy
- Uniqueness constraints

#### Constraints
- Uniqueness constraints
- Property existence constraints

#### Stored Objects
- **Note**: If stored objects are not available, explain replacements
- Cypher procedures/functions
- Application-level logic

### 4.4 CRUD Application for Graph Database
- Spring Data Neo4j repository pattern
- Node creation, retrieval, update, delete
- Relationship management
- Cypher query examples
- Graph traversal patterns
- Performance for relationship queries

---

## 5. Migration

### Description of the Migration Application
- **Purpose**: How data moves from relational to document and graph databases
- **DataMigrator Implementation**: Code structure and approach
- **Endpoint**: Migration trigger (e.g., POST /api/migrate)
- **Data Mapping Table**:
  | MySQL | MongoDB | Neo4j |
  |-------|---------|-------|
  | GUEST table | Embedded in documents | :Guest node |
  | ROOM table | rooms collection | :Room node |
  | RESERVATION table | reservations collection | :Reservation node |
  | Foreign keys (JOINs) | Embedded documents | Relationships |
- **Migration Process**: Step-by-step explanation
- **Data Consistency**: How data integrity is maintained
- **Testing**: Migration verification steps and results

---

## 6. Discussion

### Similarities and Differences Between Database Types

#### Data Modeling
- Relational: Normalized tables, foreign keys
- Document: Denormalized documents, embeddings
- Graph: Nodes with relationships, property graph model
- Trade-offs and when to use each

#### Transactions
- ACID in MySQL: Full support
- ACID in MongoDB: Document-level transactions
- ACID in Neo4j: Transaction support
- Implications for application design

#### Queries
- SQL for MySQL
- MongoDB query language
- Cypher for Neo4j
- Performance comparison
- Query complexity examples

#### Performance
- Read/write characteristics of each
- Scaling capabilities
- Index performance
- Real-world performance metrics

#### Constraints and Integrity
- Relational: Database-level enforcement
- Document: Application-level enforcement
- Graph: Relationship constraints
- Data validation approaches

#### Which Database Fits the Domain Best — and Why
- Hotel management system requirements
- Guest, room, reservation relationships
- Strengths and weaknesses for this domain
- Final recommendation with justification

#### Trade-offs and Compromises
- Storage vs. query speed
- Consistency vs. availability
- Normalization vs. denormalization
- Complexity vs. flexibility

---

## 7. Reflection

### What Was Difficult About:

#### Modeling the Same Domain in Three Databases
- Conceptual challenges
- Design decisions that differed
- Complexity management
- Learning curve for each database type

#### Implementing Transactions
- ACID compliance across different systems
- Concurrency control
- Rollback mechanisms
- Testing transaction scenarios

#### Implementing Auditing
- Tracking changes across systems
- Audit trail completeness
- Performance impact
- Compliance requirements

#### Implementing Security
- User management across systems
- Privilege escalation prevention
- Encryption and data protection
- Security testing

### Design Mistakes Made and Fixed
- List specific mistakes
- How they were identified
- Solution approach
- Lessons learned

### What Would Be Done Differently Next Time
- Architectural improvements
- Technology choices
- Development approach
- Process improvements

### What You Learned About Databases in Practice
- Real-world challenges vs. theory
- Performance considerations
- Scalability lessons
- Team coordination
- DevOps and deployment lessons

---

## 8. Conclusion

### High-Level Summary
- Brief overview of what was accomplished
- Main achievements
- System capabilities
- Project scope completion

### Results
- Functional components delivered
- Performance metrics (if available)
- User satisfaction/acceptance
- Test results summary

### Main Insights
- Key learnings
- Surprising findings
- Best practices discovered
- Recommendations for future work

---

## 9. References

**Important**: All major design choices, technical claims, and comparisons must be supported by references.

### Format
- Standard format: APA, IEEE, or Harvard
- Consistent citation style throughout

### Source Types (Preferred Order)
1. Official documentation (MySQL, MongoDB, Neo4j, Spring Boot)
2. Academic or technical publications
3. Textbooks on database design and architecture
4. Industry best practice guides
5. Technical blogs (with credibility verification)

### Required References (Examples)
- MySQL 8.0 Official Documentation
- MongoDB Documentation
- Neo4j Documentation
- Spring Boot Official Guide
- Spring Data JPA Documentation
- Database design textbooks
- Software architecture references

### Citation Examples
- Within text: (MySQL Documentation, 2024)
- In-text claims require citations
- All diagrams should have sources

---

## 10. Appendix (Optional)

### Possible Appendix Sections
- Complete ER diagrams
- Database schema dumps
- Code listings (selected important parts)
- Test data samples
- Performance test results
- Additional architectural diagrams
- Security audit results
- Migration logs and reports
- Configuration files (sanitized)

---

## DOCUMENT FORMATTING REQUIREMENTS

### General
- **Pagination**: Entire document must be paginated
- **Font**: Professional font (Arial, Calibri, or Times New Roman)
- **Size**: 11-12pt body text
- **Spacing**: 1.5 line spacing
- **Margins**: Standard (1 inch or 2.5cm)

### Headings
- Consistent hierarchy
- Clear visual distinction
- Automatic TOC generation

### Figures and Tables
- All figures must be in the List of Figures
- Captions and references
- Numbered sequentially
- High quality and readable

### Appendices
- Listed in Table of Contents
- Labeled (Appendix A, B, C, etc.)
- Referenced in main text

### Professional Appearance
- Consistent formatting throughout
- No spelling or grammar errors
- Professional language
- Clear organization

---

## GRADING IMPACT NOTES

⚠️ **CRITICAL**: This report is the only information accessible to external examiners before the oral exam. It heavily influences the final grade.

### Key Success Factors
- ✅ Complete coverage of all required sections
- ✅ Clear explanations and visualizations
- ✅ Proper citations and references
- ✅ Professional presentation
- ✅ Demonstrates deep understanding
- ✅ Shows design thinking and trade-offs
- ✅ Includes evidence of testing and verification

---

**Document Created**: May 26, 2026  
**Status**: Structure Template Ready for Content Development
