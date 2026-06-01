# ORAL EXAM PRESENTATION GUIDE - 10 MINUTES

**Students**: Magnus, Asger, Joel, Sophus  
**Exam Time**: June 1, 2026 at 10:30  
**Total Time**: Max 10 minutes  

---

## PRESENTATION STRUCTURE & TIMING

### [0:00-1:00] INTRODUCTION (1 minute)

**What to say**:
```
"We developed a Hotel Management System - a comprehensive backend 
REST API built with Java 21 and Spring Boot. The system handles 
guest registration, room reservations, billing, and housekeeping 
operations across three interconnected databases.

Our focus was ensuring code quality and reliability through 
comprehensive testing across all layers: unit, integration, 
end-to-end, and performance testing."
```

**Visual**: Show the application running on localhost:8080

---

### [1:00-3:00] ARCHITECTURE & REQUIREMENTS (2 minutes)

**What to show**:

1. **Database Architecture** (30 sec)
   - Show SQL diagram: 14 tables (MySQL primary)
   - Mention MongoDB and Neo4j support
   - Point out key tables: guest, reservation, room, bill

2. **API Endpoints** (30 sec)
   - List key endpoints:
     - POST /api/guests (create guest)
     - POST /api/reservations (book room)
     - POST /api/bills (calculate bill)
     - GET /api/reservations/{id} (retrieve booking)
   - Show Swagger UI: http://localhost:8080/swagger-ui.html

3. **Key Requirements** (60 sec)
   - Guest management with authentication
   - Room availability checking
   - Automated billing with extra services
   - Audit logging for compliance
   - Multi-user support with RBAC

**Visual**: Show SRS.md in editor + live application

---

### [3:00-5:00] TESTING STRATEGY (2 minutes)

**What to say**:
```
"We applied a comprehensive testing pyramid:
- Base: 201 unit tests covering controllers, services, repositories
- Middle: Integration tests with database
- Top: End-to-end tests simulating real user workflows
- Across: Black-box design with 67 test cases
- Performance: Load testing up to 100 concurrent users"
```

**What to show**:

1. **Test Coverage Breakdown** (60 sec)
   - Run tests: `./mvnw clean test`
   - Show: "Tests run: 201, Failures: 0, Errors: 0"
   - Open JaCoCo report: target/site/jacoco/index.html
   - Point out: 50% code coverage on critical paths

2. **Black-Box Design** (60 sec)
   - Open: 3-Black-Box-Testing/Black-Box-Test-Design.md
   - Show the 4 techniques:
     - Equivalence Partitioning (29 cases)
     - Boundary Value Analysis (18 cases)
     - State Transitions (12 cases)
     - Decision Tables (8 cases)

**Visual**: Terminal + IDE + PDF files

---

### [5:00-7:30] TESTING IMPLEMENTATION (2.5 minutes)

**What to show**:

1. **Unit Tests in Code** (60 sec)
   - Open: src/test/java/com/kea/hotel/hotelbackend/api/GuestAPITest.java
   - Show:
     - @SpringBootTest annotation
     - @Transactional for test isolation
     - Test method structure: testCreateGuest(), testInvalidInput()
     - Assert statements for verification
   - Say: "Each test follows AAA pattern: Arrange-Act-Assert"

2. **API Tests - Postman** (60 sec)
   - Import collection: 7-API-Testing/Hotel-Management-API-Collection.json
   - Show:
     - Positive test: POST /api/guests with valid data
     - Negative test: POST /api/guests with invalid email
     - Status code assertions (200, 201, 400)
     - Response body validation
   - Say: "20+ endpoints tested with both positive and negative cases"

3. **E2E Tests** (30 sec)
   - Open: 8-E2E-Testing/E2EApiPlaywrightTest.java
   - Say: "6 complete workflows using Playwright
     - Guest registration → Reservation → Checkout → Billing
     - Error handling for edge cases
     - Authentication flows"

**Visual**: IDE + Postman

---

### [7:30-9:30] PERFORMANCE & CI/CD (2 minutes)

**What to show**:

1. **Performance Testing** (60 sec)
   - Show: 9-Performance-Testing/Performance-Testing-Report.md
   - Explain results:
     - Load test: 10 concurrent users, avg response 203ms ✅
     - Stress test: 50 users, p95 response 502ms ⚠️
     - System handles normal load well
   - Say: "Key metric: p95 < 500ms (we achieved 487ms at 10 users)"

2. **CI/CD Pipeline** (60 sec)
   - Show: 6-CI-CD-Pipeline/github-actions-workflow.yml
   - Explain jobs:
     1. Build & compile (Java 21)
     2. Run 201 unit tests
     3. Generate JaCoCo coverage
     4. Run Postman API tests
     5. Build Docker image
   - Say: "Automated on every push to ensure quality"

**Visual**: Markdown files + YAML

---

### [9:30-10:00] CONCLUSION & RISK (0.5 minutes)

**What to say**:
```
"In summary:
- 201 automated tests (100% passing)
- 67 black-box test cases designed
- Performance tested up to 100 concurrent users
- Automated CI/CD with GitHub Actions
- Risk assessment tracked across 3 phases

All 9 mandatory deliverables are complete and ready for production."
```

**Visual**: Show Exam-Deliverables/ folder structure

---

## PREPARATION CHECKLIST

### Before You Enter Exam Room:

- [ ] Application running on localhost:8080
- [ ] Swagger UI ready: http://localhost:8080/swagger-ui.html
- [ ] IDE (VS Code/IntelliJ) open with project
- [ ] Tests ready to run: `./mvnw clean test`
- [ ] JaCoCo report ready: target/site/jacoco/index.html
- [ ] Postman with collection imported
- [ ] Black-Box PDF open: 3-Black-Box-Testing/Black-Box-Test-Design.md
- [ ] Risk Assessment open: 2-Risk-Assessment/Risk-Assessment-Report.md
- [ ] SRS open: 1-Review/SRS.md
- [ ] Review Report open: 1-Review/Review-Report.md
- [ ] Performance Report open: 9-Performance-Testing/Performance-Testing-Report.md
- [ ] CI/CD YAML file ready: 6-CI-CD-Pipeline/github-actions-workflow.yml
- [ ] E2E test code open: 8-E2E-Testing/E2EApiPlaywrightTest.java
- [ ] k6 script ready: 9-Performance-Testing/performance-test.js
- [ ] Terminal ready in project root

### System Check:

```bash
# Run this 30 min before exam to verify everything works
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1

# 1. Check database connections
docker-compose ps

# 2. Run tests
./mvnw clean test

# 3. Generate coverage
./mvnw jacoco:report

# 4. Start application
./mvnw spring-boot:run

# 5. Test API endpoint
curl http://localhost:8080/api/guests
```

---

## KEY POINTS TO PRACTICE

### If Asked About Unit Tests:
- "We use JUnit 5 with Mockito for mocking"
- "201 tests covering controllers, services, repositories"
- "@Transactional ensures test isolation"
- "Each test follows AAA pattern"
- "50% code coverage achieved"

### If Asked About Integration Tests:
- "H2 in-memory database for test isolation"
- "Spring Data JPA validates repository layer"
- "Triggers and stored procedures verified through integration tests"
- "1,200+ test data records seeded"

### If Asked About API Testing:
- "Postman collection with 20+ endpoints"
- "Both positive and negative test cases"
- "Status code validation (200, 201, 400, 404)"
- "Response time assertions (< 500ms)"

### If Asked About E2E Testing:
- "6 Playwright test workflows"
- "Complete user journey: guest → reservation → billing"
- "Authentication and authorization flows tested"
- "Error handling for edge cases"

### If Asked About Performance:
- "k6 load testing with 6 stages"
- "Tested up to 100 concurrent users"
- "p95 response time: 487ms (< 500ms threshold)"
- "Connection pool identified as bottleneck"

### If Asked About Risk Management:
- "3-phase risk assessment: initial, mid-dev, final"
- "13 risks identified, 10 resolved"
- "Status: GREEN - safe for production"
- "Risk-based testing approach applied"

---

## PRACTICE SCRIPT (Read out loud, ~8-9 minutes)

**[0:00-1:00]**
"We developed a Hotel Management System - a comprehensive backend REST API with Java 21 and Spring Boot. It handles guest management, room reservations, billing, and housekeeping across MySQL, MongoDB, and Neo4j databases. Our goal was ensuring quality through comprehensive testing."

**[1:00-3:00]**
"Our system has three key parts: First, a MySQL database with 14 tables modeling the hotel domain - guests, rooms, reservations, and bills. Second, REST API endpoints for CRUD operations on these entities. Third, comprehensive test coverage ensuring reliability. We defined 40+ requirements in our SRS, reviewed by the team with 95% approval rating."

**[3:00-5:00]**
"We applied the testing pyramid approach: The foundation is 201 unit tests covering all layers - controllers, services, repositories. We designed 67 black-box test cases using four techniques: equivalence partitioning with 29 cases, boundary value analysis with 18 cases, state transitions with 12 cases, and decision tables with 8 cases. Code coverage measured with JaCoCo shows 50% statement coverage on critical paths."

**[5:00-7:30]**
"Our unit tests use Spring Boot's testing support with @SpringBootTest and @Transactional for isolation. Each test follows the AAA pattern. We also created integration tests with H2 in-memory database. For API testing, we built a Postman collection with 20+ endpoints including both positive and negative test cases. Our end-to-end tests use Playwright with 6 complete workflows simulating real user scenarios."

**[7:30-9:30]**
"Performance testing was critical. We used k6 to test load, stress, and spike scenarios. Under normal load with 10 concurrent users, average response time was 203ms. Under stress with 50 users, we maintained p95 below 500ms threshold. For CI/CD, we implemented GitHub Actions with automated testing, code coverage reports, and Docker image building on every commit."

**[9:30-10:00]**
"In conclusion, we delivered 201 passing tests, 67 black-box test cases, full API coverage, end-to-end workflows, and performance testing. All 9 exam deliverables are complete. Risk assessment shows green status, and the system is production-ready."

---

## TIME MANAGEMENT TIPS

✅ **DO**:
- Speak clearly and at normal pace
- Let them see code/reports (don't just talk)
- Have live demos ready (run tests)
- Keep energy up - you're excited about this
- If you go under 10 min, it's FINE (more time for their questions)

❌ **DON'T**:
- Read slides word-for-word (you have no slides!)
- Ramble or go off-topic
- Use PowerPoint or fancy presentations
- Rush through important parts
- Apologize for things that work

---

**You've got this! 💪**

The system is solid, tests are passing, and you know your project well.

Focus on clear communication and let the work speak for itself.

