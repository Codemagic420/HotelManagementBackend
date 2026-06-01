# PRECONDITION DOCUMENT - SOFTWARE QUALITY EXAM

**Date**: June 1, 2026  
**Submission Date**: May 31, 2026

---

## Group Members

- **Magnus** (Lead Developer, Testing Coordinator)
- **Asger** (Full Stack Developer, CI/CD Specialist)
- **Joel** (QA Engineer, Performance Testing)
- **Sophus** (Security & Integration Testing)

---

## Application Description

### Hotel Management System - Backend REST API

**Technology Stack**: Java 21 with Spring Boot 4.0.6

**Purpose**: Enterprise-grade hotel management system providing comprehensive backend APIs for guest management, room booking, reservations, billing, and housekeeping operations.

**Key Features**:
- Guest registration and profile management
- Room availability checking and reservations
- Automated billing calculation with extra services
- Housekeeping task assignments and room status tracking
- Multi-database support (MySQL, MongoDB, Neo4j)
- JWT-based authentication and role-based access control (RBAC)
- Comprehensive audit logging for compliance

**Database Architecture**:
- **Primary**: MySQL 8.0 (14 tables, relational model)
- **Document**: MongoDB 7 (NoSQL backup)
- **Graph**: Neo4j 5 (relationship analytics)

**API Endpoints**: 20+ REST endpoints with full CRUD operations

**Test Data**: 1,200+ seeded records across all tables

---

## Expected Testing Activities

### Phase 1: Project Presentation (Max 10 minutes)

- Overview of application architecture and technology stack
- Key business requirements addressed
- Database design and relationships
- API endpoint showcase

### Phase 2: Black-Box Testing Activities

- **Test Case Design**:
  - Equivalence Partitioning (29 test cases)
  - Boundary Value Analysis (18 test cases)
  - State Transition Testing (12 test cases)
  - Decision Tables (8 test cases)
  - **Total: 67 black-box test cases**

### Phase 3: White-Box Testing Activities

- **Code Coverage Analysis**:
  - JaCoCo coverage report (50% overall coverage)
  - Static code analysis with Checkstyle, SpotBugs, SonarCloud
  - Code quality metrics and compliance checks

- **Unit Testing**:
  - 201 automated unit tests (100% pass rate)
  - Test framework: JUnit 5 with Mockito
  - Coverage across: Controllers, Services, Repositories
  - Parameterized tests with multiple data providers
  - AAA pattern implementation (Arrange-Act-Assert)

- **Integration Testing**:
  - Spring Boot integration tests with @SpringBootTest
  - Database testing with H2 in-memory test database
  - Transaction management and rollback verification
  - Repository layer integration tests

- **Repository Testing**:
  - 31 repository tests for CRUD operations
  - Database constraint validation
  - Query result verification

### Phase 4: API Testing Activities

- **Postman Collection**: 20+ endpoint tests
- **Test Types**:
  - Positive test cases (valid inputs, expected outputs)
  - Negative test cases (invalid inputs, error handling)
  - Status code validation
  - Response payload validation
  - Performance response time checks (< 500ms threshold)
  - Authentication/Authorization testing
  - JWT token validation

### Phase 5: End-to-End Testing Activities

- **Playwright Test Suite**: 6 complete workflows
  - Guest registration → Reservation → Checkout → Billing workflow
  - Multi-guest booking scenarios
  - Error handling and edge cases
  - Authentication flows
  - Authorization validation
  - UI state verification

### Phase 6: Performance Testing Activities

- **k6 Load Testing Script**:
  - **Load Test**: Sustain 10 concurrent users for 2 minutes
  - **Stress Test**: Ramp load from 10 → 50 users
  - **Spike Test**: Sudden jump to 100 concurrent users
  - **Recovery Test**: Verify system recovery
  - **Metrics Tracked**:
    - Response time (p95 < 500ms, p99 < 1000ms)
    - Throughput (requests/second)
    - Error rates under load
    - Connection pool saturation
    - Memory usage patterns

### Phase 7: Continuous Integration Activities

- **GitHub Actions Pipeline**: 5 automated jobs
  - Build and compile verification
  - Automated test execution (201 tests)
  - JaCoCo coverage report generation
  - Static code analysis integration
  - Postman API test execution
  - Docker image building
  - Artifact archiving and reporting

### Phase 8: Risk Management Activities

- **Risk Assessment Across 3 Phases**:
  - Initial risks (5 identified)
  - Mid-development risks (10 tracked)
  - Final risks (13 total, 10 resolved)
  - Risk matrices and mitigation strategies
  - Risk-based testing approach
  - Traceability between risks and test coverage

### Phase 9: Test-Related Activities

- **Test Doubles & Mocking**:
  - Mockito for unit test isolation
  - Mock repositories and external services
  - Argument captors for verification
  - Spy usage for partial mocking

- **Database Testing**:
  - H2 in-memory database for test isolation
  - Schema validation through test execution
  - Trigger and stored procedure testing (via integration tests)
  - Transaction rollback verification

- **External System Testing**:
  - JWT token generation and validation
  - Authentication endpoint testing
  - Role-based access control verification
  - Third-party service integration (if applicable)

---

## Test Execution Plan for Exam

### Ready-to-Run Demonstrations

1. **Unit Tests** (201 tests)
   ```bash
   ./mvnw clean test
   ```
   Expected: Tests run: 201, Failures: 0, Errors: 0

2. **Code Coverage Report**
   ```bash
   ./mvnw jacoco:report
   ```
   View: `target/site/jacoco/index.html`

3. **Static Code Analysis**
   ```bash
   ./mvnw checkstyle:check
   ./mvnw spotbugs:check
   ```

4. **API Tests**
   - Import Postman collection: `7-API-Testing/Hotel-Management-API-Collection.json`
   - Run entire collection or specific test suite

5. **E2E Tests**
   - Run Playwright test: `8-E2E-Testing/E2EApiPlaywrightTest.java`
   - View test execution in browser

6. **Performance Tests**
   ```bash
   k6 run 9-Performance-Testing/performance-test.js
   ```

7. **CI Pipeline**
   - GitHub Actions workflow: `.github/workflows/ci-cd-pipeline.yml`
   - Trigger manual run or view recent builds

---

## Key Metrics & Success Criteria

| Metric | Target | Achieved |
|--------|--------|----------|
| **Unit Tests** | 100+ | ✅ 201 |
| **Test Pass Rate** | 100% | ✅ 100% |
| **Code Coverage** | 40%+ | ✅ 50% |
| **API Endpoints Tested** | 15+ | ✅ 20+ |
| **Black-Box Test Cases** | 50+ | ✅ 67 |
| **E2E Workflows** | 3+ | ✅ 6 |
| **p95 Response Time** | < 500ms | ✅ 487ms |
| **Load Test Users** | 10+ | ✅ 100 |

---

## Documentation Provided

All materials are available in: `Exam-Deliverables/`

1. **1-Review**: SRS.md, Review-Report.md
2. **2-Risk-Assessment**: Risk-Assessment-Report.md
3. **3-Black-Box-Testing**: Black-Box-Test-Design.md
4. **4-Static-Testing**: JaCoCo reports
5. **5-Unit-Integration-Testing**: 201 test files
6. **6-CI-CD-Pipeline**: GitHub Actions workflow
7. **7-API-Testing**: Postman collection + environment
8. **8-E2E-Testing**: Playwright test code
9. **9-Performance-Testing**: k6 script + report

---

**Prepared by**: Magnus, Asger, Joel, Sophus  
**Date**: May 31, 2026  
**Status**: Ready for Exam  

---
