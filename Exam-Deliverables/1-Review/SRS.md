# Software Requirements Specification (SRS)
**Hotel Management System**  
**Version**: 1.0  
**Date**: May 31, 2026  
**Status**: APPROVED

---

## 1. Introduction

### 1.1 Purpose
The Hotel Management System (HMS) is a comprehensive backend application designed to manage all aspects of hotel operations including guest management, room reservations, billing, cleaning assignments, and inventory tracking.

### 1.2 Scope
This system manages:
- Guest information and profiles
- Room availability and bookings
- Reservation lifecycle (pending → confirmed → checked-in → checked-out)
- Billing calculations with extra service charges
- Room cleaning and maintenance schedules
- Inventory management
- Staff and cleaner management
- Audit logging for compliance

### 1.3 Definitions and Acronyms
- **HMS**: Hotel Management System
- **API**: Application Programming Interface
- **JWT**: JSON Web Token (authentication)
- **CRUD**: Create, Read, Update, Delete operations
- **ER**: Entity-Relationship (database design)

---

## 2. Overall Description

### 2.1 Product Perspective
The HMS is a backend REST API that serves frontend applications and handles all business logic for hotel operations. It integrates with three databases (MySQL, MongoDB, Neo4j) and provides comprehensive reporting capabilities.

### 2.2 Product Functions
1. **Guest Management** - Register, update, delete guests
2. **Room Management** - Manage room inventory and status
3. **Reservation System** - Book rooms with date validation
4. **Billing System** - Calculate bills with room charges and extras
5. **Cleaning Management** - Assign and track room cleaning
6. **Inventory System** - Track hotel supplies
7. **Authentication** - Secure login with JWT tokens
8. **Audit Logging** - Track all changes for compliance

### 2.3 User Classes
1. **Administrator** - Full system access, manage users and settings
2. **Staff** - Access guest and reservation data
3. **Cleaner** - View and update cleaning assignments
4. **System** - Automated processes and batch operations

---

## 3. Specific Requirements

### 3.1 Functional Requirements

#### 3.1.1 Guest Management

**REQ-GUEST-001**: System shall allow creation of guest accounts
- Input: First name, last name, email, phone number
- Validation: Email must be unique and valid format
- Output: Guest ID, confirmation message

**REQ-GUEST-002**: System shall allow retrieval of guest information
- Query by: Guest ID or email address
- Return: Complete guest profile with contact information
- Permissions: Accessible by staff and admin

**REQ-GUEST-003**: System shall allow updating guest information
- Editable fields: Phone, email, name
- Validation: Same as creation
- Audit: Log all changes with timestamp and user

**REQ-GUEST-004**: System shall allow deletion of guest accounts
- Hard delete or soft delete (mark inactive)
- Cannot delete guests with active reservations
- Audit: Log deletion event

#### 3.1.2 Room Management

**REQ-ROOM-001**: System shall maintain room inventory
- Fields: Room number, type, status, cleaning status
- Status values: AVAILABLE, OCCUPIED, MAINTENANCE, CLEANING
- Search: By type, status, or floor

**REQ-ROOM-002**: System shall track room availability
- Calculate availability based on reservations
- Update status automatically on check-in/check-out
- Support bulk availability searches

**REQ-ROOM-003**: System shall manage room types
- Fields: Name, max occupancy, base price
- Price varies by season (peak/off-season)
- Support multiple room type definitions

#### 3.1.3 Reservation System

**REQ-RES-001**: System shall accept reservation requests
- Input: Guest ID, room ID, check-in date, check-out date, number of guests
- Validation: 
  - Room must be available for entire date range
  - Check-out date > check-in date
  - Check-in date must be in future
- Output: Reservation ID, confirmation

**REQ-RES-002**: System shall manage reservation status
- Status workflow: PENDING → CONFIRMED → CHECKED_IN → CHECKED_OUT → BILLED
- Allow status transitions only in order
- Log all transitions with timestamp

**REQ-RES-003**: System shall validate date conflicts
- No double-booking of same room
- Support same-day turnover with cleaning
- Handle overlapping date ranges

**REQ-RES-004**: System shall support group reservations
- Multiple guests per reservation
- Designate primary guest
- Track additional guest information

#### 3.1.4 Billing System

**REQ-BILL-001**: System shall calculate room charges
- Base price from room type and season
- Apply daily rate × number of nights
- Support discount codes

**REQ-BILL-002**: System shall track extra service charges
- Breakfast, parking, laundry, etc.
- Per-unit pricing
- Multiple extras per reservation

**REQ-BILL-003**: System shall generate bills
- Trigger: On checkout completion
- Include: All charges, taxes, payment terms
- Output: Printable bill

**REQ-BILL-004**: System shall track payment status
- Status: PENDING, PAID, PARTIAL, OVERDUE
- Support multiple payment methods
- Generate payment reminders

#### 3.1.5 Cleaning Management

**REQ-CLEAN-001**: System shall assign cleaning tasks
- Assigned to: Cleaner staff
- Priority: URGENT, HIGH, NORMAL, LOW
- Deadline: Specific time window

**REQ-CLEAN-002**: System shall track cleaning status
- Status: ASSIGNED, IN_PROGRESS, COMPLETED, INSPECTED
- Support comments and time logs
- Generate cleaning schedules

**REQ-CLEAN-003**: System shall manage room cleanliness
- Status: DIRTY, CLEAN, INSPECTED
- Auto-mark DIRTY on checkout
- Prevent guest check-in if not CLEAN

#### 3.1.6 Inventory Management

**REQ-INV-001**: System shall track inventory items
- Fields: Item name, unit price, quantity
- Status: ACTIVE, INACTIVE
- Category: Supplies, linens, amenities

**REQ-INV-002**: System shall manage stock levels
- Low stock alerts
- Reorder suggestions
- Historical usage reports

#### 3.1.7 Authentication & Authorization

**REQ-AUTH-001**: System shall implement user authentication
- Method: Username/password with JWT tokens
- Password: BCrypt hashing with strength 10
- Session: Stateless JWT-based

**REQ-AUTH-002**: System shall enforce role-based access control
- Roles: ADMIN, STAFF, CLEANER
- Permissions: Role-specific endpoint access
- Audit: Log all authentication attempts

**REQ-AUTH-003**: System shall manage user sessions
- Token expiration: Configurable (default 1 hour)
- Refresh tokens: Support token refresh
- Logout: Invalidate tokens

#### 3.1.8 Audit & Compliance

**REQ-AUDIT-001**: System shall log all data modifications
- Trigger: On any INSERT, UPDATE, DELETE
- Fields: Table, operation, old values, new values, user, timestamp
- Retention: Retain for 7 years minimum

**REQ-AUDIT-002**: System shall provide audit reports
- Query by: Date range, table, user, operation type
- Export: CSV, PDF formats
- Compliance: Meet data protection regulations

### 3.2 Non-Functional Requirements

#### 3.2.1 Performance

**REQ-PERF-001**: API Response Times
- 95% of requests: < 500ms
- 99% of requests: < 1000ms
- Database queries: < 100ms average

**REQ-PERF-002**: Throughput
- Support: Minimum 50 concurrent users
- Scale to: 200 concurrent users
- Request rate: 100+ requests/second

**REQ-PERF-003**: Database Performance
- Query optimization: Indexed critical columns
- Connection pooling: Configured and monitored
- Caching: Redis for frequently accessed data (v2)

#### 3.2.2 Reliability & Availability

**REQ-REL-001**: System Uptime
- Target: 99.5% uptime
- Maintenance window: 2 hours/month
- Disaster recovery: RTO < 4 hours, RPO < 1 hour

**REQ-REL-002**: Data Integrity
- Transactions: ACID compliance
- Backups: Daily automated backups
- Restore capability: Test monthly

**REQ-REL-003**: Error Handling
- Graceful degradation: System degrades gracefully
- Error codes: Standardized HTTP status codes
- Logging: Comprehensive error logging

#### 3.2.3 Security

**REQ-SEC-001**: Data Protection
- Encryption: TLS 1.2+ for data in transit
- At rest: Database encryption (optional for v1)
- PII: Comply with GDPR/privacy regulations

**REQ-SEC-002**: Access Control
- Authentication: Mandatory for all endpoints
- Authorization: Role-based access control
- API keys: Support for service-to-service communication

**REQ-SEC-003**: Input Validation
- SQL injection: Parameterized queries only
- XSS protection: HTML escaping
- Rate limiting: Prevent DDoS attacks (v2)

#### 3.2.4 Scalability

**REQ-SCALE-001**: Horizontal Scalability
- Stateless API: Support load balancing
- Database: Read replicas for scaling
- Caching: Distributed caching support

**REQ-SCALE-002**: Data Volume
- Support: 10,000+ guests
- Support: 100,000+ reservations
- Support: 1,000+ rooms

#### 3.2.5 Maintainability

**REQ-MAINT-001**: Code Quality
- Code style: Consistent with Java conventions
- Documentation: Comprehensive inline comments
- Testing: Minimum 50% code coverage

**REQ-MAINT-002**: API Documentation
- Swagger/OpenAPI: Auto-generated documentation
- Examples: Include request/response examples
- Versioning: API v1 with migration path to v2

#### 3.2.6 Compatibility

**REQ-COMPAT-001**: Database Support
- MySQL 8.0: Primary database
- MongoDB 7: Document storage
- Neo4j 5: Graph relationships (optional)

**REQ-COMPAT-002**: Platform Support
- Java 21: Required runtime
- Docker: Containerized deployment
- Cloud: Compatible with AWS, Azure, GCP

---

## 4. External Interface Requirements

### 4.1 API Interface

**Base URL**: `http://localhost:8080/api`

**Authentication**: JWT Bearer token in Authorization header

**Response Format**: JSON

**Endpoints**:
```
POST   /api/auth/login              - User login
GET    /api/guests                  - List guests
POST   /api/guests                  - Create guest
GET    /api/guests/{id}             - Get guest
PUT    /api/guests/{id}             - Update guest
DELETE /api/guests/{id}             - Delete guest

GET    /api/rooms                   - List rooms
POST   /api/rooms                   - Create room
GET    /api/rooms/{id}              - Get room
PUT    /api/rooms/{id}              - Update room

GET    /api/reservations            - List reservations
POST   /api/reservations            - Create reservation
GET    /api/reservations/{id}       - Get reservation
PUT    /api/reservations/{id}       - Update reservation
PUT    /api/reservations/{id}/confirm     - Confirm
PUT    /api/reservations/{id}/check-in   - Check-in
PUT    /api/reservations/{id}/check-out  - Check-out

GET    /api/bills                   - List bills
POST   /api/bills                   - Create bill
GET    /api/bills/{id}              - Get bill
```

### 4.2 Database Interface

**Primary**: MySQL 8.0 with 14 tables  
**Secondary**: MongoDB 7 for document storage  
**Tertiary**: Neo4j 5 for graph data  

### 4.3 External API Integration

**Weather Widget** (v2): 3rd party weather API for guest information
**Payment Gateway** (v2): Integration for online payments
**Email Service** (v2): SMTP for booking confirmations

---

## 5. System Features

### 5.1 Functional Features
- ✅ Complete CRUD for all entities
- ✅ Complex reservation workflow
- ✅ Automatic billing calculation
- ✅ Role-based access control
- ✅ Audit logging
- ✅ Multi-database support

### 5.2 Quality Features
- ✅ 201 automated tests
- ✅ 50% code coverage
- ✅ Swagger API documentation
- ✅ Docker containerization
- ✅ CI/CD pipeline
- ✅ Performance testing

---

## 6. Acceptance Criteria

### 6.1 Functional Acceptance
- [ ] All CRUD operations verified working
- [ ] Reservation workflow tested end-to-end
- [ ] Billing calculations accurate
- [ ] Authentication/authorization enforced
- [ ] Data integrity validated

### 6.2 Quality Acceptance
- [ ] All tests passing (201/201)
- [ ] Code coverage ≥ 50%
- [ ] API response times < 500ms (95th percentile)
- [ ] Zero critical security issues
- [ ] Documentation complete

### 6.3 Performance Acceptance
- [ ] Support 50+ concurrent users
- [ ] Database queries < 100ms average
- [ ] Bulk operations handle 1000+ records
- [ ] No memory leaks detected
- [ ] CPU usage < 80% under load

---

## 7. Revision History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 0.1 | May 1, 2026 | Requirements Team | Initial draft |
| 0.5 | May 15, 2026 | Dev Team | Refined based on design review |
| 1.0 | May 31, 2026 | Project Lead | Final version - Approved |

---

## 8. Approval & Sign-Off

**Project Lead**: _____________________ Date: May 31, 2026

**Product Owner**: _____________________ Date: May 31, 2026

**Quality Assurance**: _____________________ Date: May 31, 2026

**Status**: ✅ **APPROVED FOR DEVELOPMENT**

