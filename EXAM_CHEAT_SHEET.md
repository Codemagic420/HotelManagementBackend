# Exam Cheat Sheet - Hotel Management Backend

## Quick Commands

### Run All Tests (186 tests)
```powershell
.\mvnw clean test
```

### Run Specific Test Suites
```powershell
# API tests only (63 tests)
.\mvnw test -Dtest="BillAPITest,GuestAPITest,RoomAPITest,ReservationAPITest"

# Service tests only (49 tests)
.\mvnw test -Dtest="BillServiceTest,GuestServiceTest,RoomServiceTest,ReservationServiceTest"

# Security tests only (34 tests)
.\mvnw test -Dtest="AuthenticationTest,JwtTokenProviderTest"
```

### Start the Backend
```powershell
.\mvnw spring-boot:run
```
Backend runs on: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html

### Login (get JWT token)
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
```

### Start Database (Docker)
```powershell
docker compose -f docker-compose.dev.yml up db -d
```

---

## k6 Performance Testing (Deliverable 9)

### Run k6 Load Test
```powershell
& "C:\Program Files\k6\k6.exe" run Exam-Deliverables/9-Performance-Testing/performance-test.js
```

### Run k6 with Grafana Cloud UI
```powershell
$env:K6_CLOUD_TOKEN = "your-token-here"
& "C:\Program Files\k6\k6.exe" run --out cloud Exam-Deliverables/9-Performance-Testing/performance-test.js
```

### k6 Test Stages
1. **Warm up** (30s) → ramp to 10 users
2. **Load test** (2m) → steady 10 users
3. **Stress test** (1m) → ramp to 50 users
4. **Spike test** (30s) → jump to 100 users
5. **Recovery** (1m) → ramp down to 10 users
6. **Cool down** (30s) → ramp to 0

### k6 Thresholds
- `p(95) < 500ms` — 95% of requests under 500ms
- `p(99) < 1000ms` — 99% of requests under 1 second
- `http_req_failed < 10%` — error rate under 10%

### Add k6 to PATH (optional)
```powershell
[Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Program Files\k6", "User")
```
Then restart PowerShell and use `k6 run ...` directly.

---

## Test Credentials
| Username | Password | Role |
|---|---|---|
| admin | admin123 | ADMIN |
| staff | staff123 | STAFF |
| cleaner1 | cleaner123 | CLEANER |

## Ports
| Service | Port |
- **Review report** — formal review by other group members

### What to say
> *"One group member wrote the SRS as user stories. The rest of us conducted a formal review — we checked for completeness, consistency, testability, and correctness. The review report documents our findings: what we found, severity levels, and whether items were resolved."*

### Potential exam questions
| Question | How to answer |
|---|---|
| *What criteria did you use to assign roles for your specification review?* | "We used roles: Author (wrote the SRS), Reviewer (checked for defects), Moderator (facilitated), and Scribe (documented findings)." |
| *Track your unit tests to the test cases found in the black-box design phase.* | "Each black-box test case has a TC ID (e.g., TC-B1). We annotated our unit tests with @DisplayName referencing those IDs." |
| *Show your use of parameterised tests.* | Point to BillAPITest: `@CsvSource({"99999","0","-1"})` — parameterised with different boundary values. |

---

## 🔴 DELIVERABLE 2: RISK ASSESSMENT

### What to show
- **Risk table(s)** — initial, mid-development, final
- **Risk matrices** — likelihood × impact heatmaps

### Example risks you could mention

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Database connection failure | Low | High | Docker health checks, retry logic |
| External weather API down | Medium | Low | Cache, graceful degradation |
| JWT token expiry | Low | Medium | Auto-refresh, proper expiry config |
| Concurrent booking conflicts | Medium | High | Database transactions, optimistic locking |
| Slow response under load | Medium | Medium | k6 testing, threshold monitoring |

### What to say
> *"We created an initial risk table before development, updated it mid-project, and created a final version. Each risk was plotted on a likelihood × impact matrix. High-risk items had mitigation strategies."*

---

## 🔴 DELIVERABLE 3: BLACK-BOX TEST DESIGN

### What to show in code

**Equivalence Partitioning** — `BillAPITest.java`:
```java
// Valid partition: existing ID → 200
// Invalid partitions: 0, -1, 99999 → 404
@ParameterizedTest
@CsvSource({"99999", "0", "-1"})
void testGetBill_InvalidId_NotFound(long billId) throws Exception {
    mockMvc.perform(get("/api/bills/" + billId))
            .andExpect(status().isNotFound());
}
```

**Boundary Value Analysis** — amounts:
```java
@CsvSource({
    "0.00, 0.00",      // Minimum boundary
    "1.00, 0.25",      // Just above minimum
    "9999.99, 2499.97" // Large value
})
```

**Decision Table** — bill status × discount combinations:
```java
@CsvSource({
    "PENDING, 0.00",
    "PENDING, 50.00",
    "PAID, 0.00"
})
```

**State Transition** — bill payment:
```
PENDING ──[pay]──→ PAID
```

### What to say
> *"We applied equivalence partitioning to group valid/invalid bill IDs. Boundary value analysis tested edge amounts (0, 1, 9999.99). The decision table covered status × discount combinations. State transition tests tracked bill payment from PENDING to PAID."*

---

## 🔴 DELIVERABLE 4: STATIC TESTING + WHITE-BOX

### Static testing tools in this project
- **SpotBugs** — Bug detection (in CI: `./mvnw spotbugs:check`)
- **Checkstyle** — Code style enforcement (in CI: `./mvnw checkstyle:check`)
- **SonarQube** — Code quality, smells, vulnerabilities (in CI: `./mvnw sonar:sonar`)

### Coverage tool: JaCoCo
```bash
./mvnw clean test jacoco:report
# Open target/site/jacoco/index.html
```

### What to say
> *"SpotBugs finds actual bugs (null pointer, infinite loops). Checkstyle enforces naming conventions and formatting. SonarQube provides a quality gate. JaCoCo measures line and branch coverage — we used it to identify untested code paths and design additional unit tests."*

### Potential exam questions
| Question | How to answer |
|---|---|
| *Did your coverage tool calculate statement coverage or decision coverage?* | "JaCoCo calculates both. Line coverage ≈ statement coverage. Branch coverage shows if boolean conditions are fully tested." |
| *What approach to unit testing did you use?* | "Classical approach — we test units with real dependencies where possible, only mocking external services like the weather API." |

---

## 🔴 DELIVERABLE 5: UNIT + INTEGRATION TESTING

### Unit tests (service layer) — `src/test/java/.../service/`
- `BillServiceTest.java` — Tests business logic for bill calculations
- `ReservationServiceTest.java` — Tests reservation creation, conflicts
- `RoomServiceTest.java` — Tests room availability, status updates

### Integration tests (API layer) — `src/test/java/.../api/`
- `BillAPITest.java` — Tests HTTP endpoints via MockMvc
- `GuestAPITest.java` — CRUD operations for guests
- `ReservationAPITest.java` — Reservation workflow
- `RoomAPITest.java` — Room listing and search

### Key features to highlight
```java
// AAA Pattern (Arrange-Act-Assert)
@DisplayName("TC-B1: Get all bills - Success")
void testGetBills_Success() throws Exception {
    // Arrange — setup in @BeforeEach (MockMvc)
    
    // Act — perform HTTP request
    ResultActions result = mockMvc.perform(get("/api/bills"));
    
    // Assert — verify response
    result.andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$", isA(java.util.List.class)));
}

// Parameterised tests (black-box test cases)
@ParameterizedTest(name = "Invalid bill ID: {0}")
@CsvSource({"99999", "0", "-1"})
void testGetBill_InvalidId_NotFound(long billId) { ... }

// Performance assertion
@Test
@DisplayName("GET /api/bills - Response time < 2 seconds")
void testGetBills_Performance() throws Exception {
    long startTime = System.currentTimeMillis();
    mockMvc.perform(get("/api/bills")).andExpect(status().isOk());
    long duration = System.currentTimeMillis() - startTime;
    assert duration < 2000 : "Response too slow: " + duration + "ms";
}
```

### H2 in-memory database for tests
```
application-test.properties:
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.jpa.hibernate.ddl-auto=create-drop
  → No external DB needed!
```

### What to say
> *"All black-box test cases (EP, BVA, decision tables) are implemented as parameterised tests in the API test classes. We follow AAA pattern. Tests use H2 in-memory database so they run without any external dependencies. The @ActiveProfiles('test') ensures MongoDB and Neo4j are excluded."*

### Potential exam questions
| Question | How to answer |
|---|---|
| *Discuss whether private methods must be unit-tested.* | "No — private methods are internal implementation. We test public behavior. If a private method has complex logic, it might be a sign to extract it into its own class." |
| *When is a unit test considered an integration test?* | "When it touches a real database, filesystem, or network. Our API tests (MockMvc) are integration tests because they test the full HTTP stack." |
| *Classical vs London approach?* | "We used classical — test with real objects, mock only shared/external dependencies (like weather API). London approach mocks everything except the unit under test." |
| *Mocking external dependencies?* | "Yes, we mock external APIs like the weather service because they're unmanaged and unreliable in tests." |
| *Testing the database?* | "We use H2 in-memory for tests with @SpringBootTest. The schema is auto-created from JPA entities + schema.sql." |
| *Testing the external API?* | "We mock it with Mockito for unit tests. The integration tests don't actually call the real external API." |

---

## 🔴 DELIVERABLE 6: CONTINUOUS TESTING (CI/CD)

### CI/CD Pipeline — `.github/workflows/ci-cd.yml`

```
git push → master/develop/examasger
   │
   ▼
┌─────────────────────────────────────────────┐
│  ① TEST JOB                                 │
│  • Starts MySQL, MongoDB, Neo4j containers  │
│  • Runs: ./mvnw clean test                  │
│  • All unit + integration + API tests       │
│  • Generates JaCoCo coverage                │
│  • Uploads to Codecov                       │
└─────────────────────────────────────────────┘
   │ (pass)
   ▼
┌─────────────────────────────────────────────┐
│  ② BUILD JOB (depends on: test)            │
│  • ./mvnw clean package -DskipTests         │
│  • Uploads JAR artifact                     │
└─────────────────────────────────────────────┘
   │
   ├── ③ SONARQUBE (on push only) → Static analysis
   ├── ④ QUALITY CHECK → SpotBugs + Checkstyle
   └── ⑤ DOCKER BUILD (master only) → Docker image
```

### Demo on exam day
```bash
# Make a small change to the code
echo "// trigger CI" >> src/main/java/.../HotelApplication.java

# Commit and push
git add .
git commit -m "trigger CI pipeline"
git push

# Open GitHub → Actions tab
# Show the pipeline running: test → build → quality → sonarqube
```

### To make a test fail
```java
// In BillAPITest.java, change status code assertion:
.andExpect(status().isNotFound())  // Change from isOk() → test fails
```
Then push → CI fails.

### What to say
> *"Our CI/CD pipeline runs automatically on every push. The test job starts the required databases, runs all unit tests, integration tests, and API tests, then generates coverage. If tests fail, the pipeline stops and we get notified. Build, quality checks, and Docker image follow after tests pass."*

---

## 🔴 DELIVERABLE 7: API TESTING (POSTMAN)

### Postman collection
- File: `Exam-Deliverables/7-API-Testing/Hotel-Management-API-Collection.json`
- Tests: Positive + negative tests for all endpoints

### How to run on exam day
```bash
# Via Newman (command-line)
newman run "Exam-Deliverables/7-API-Testing/Hotel-Management-API-Collection.json" \
  --env-var "base_url=http://localhost:8080"

# With HTML report
newman run "..." --env-var "base_url=..." \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export newman-report.html
```

### What the Postman tests check per endpoint
| Endpoint | Tests |
|---|---|
| POST /api/auth/login | 200 + token exists + token is string |
| POST /api/auth/login (invalid) | 401 |
| GET /api/guests | 200 + is array |
| GET /api/guests/1 | 200 + has guestId, firstName, lastName, email, phone |
| GET /api/guests/99999 | 404 |
| POST /api/guests (valid) | 201 + has guestId + firstName matches |
| POST /api/guests (missing email) | 400 |
| POST /api/guests (invalid email) | 400 |
| PUT /api/guests/1 | 200 + firstName updated |
| DELETE /api/guests/1 | 200 or 204 |
| GET /api/rooms | 200 + is array |
| GET /api/rooms/1 | 200 + has roomId, roomNumber, status |
| GET /api/rooms/99999 | 404 |
| POST /api/reservations | 201 + has reservationId + status PENDING |
| POST /api/reservations (invalid dates) | 400 |
| PUT /api/reservations/1/confirm | 200 + status CONFIRMED |
| GET /api/bills | 200 |
| GET /api/bills/1 | 200 + has totalAmount + is number |
| POST /api/bills | 201 + totalAmount > 0 |

### To make a Postman test fail on exam day
```javascript
// Edit a test script in Postman:
pm.test("Status code is 200", function () {
    pm.response.to.have.status(404);  // Change from 200 to 404
});
```

### What to say
> *"The Postman collection tests all endpoints with positive and negative cases. Each test checks HTTP status code, response body structure, field types, and content. We run them via Newman CLI for automation. The collection is in JSON format as required."*

---

## 🔴 DELIVERABLE 8: END-TO-END UI TESTING

### What to show
- Source code in `Exam-Deliverables/8-E2E-Testing/`
- Tests use Selenium WebDriver, Cypress, or Playwright

### What to say
> *"Our E2E tests automate the full user journey through the frontend — login, navigate to rooms, create reservation, view bills. They simulate real user interactions in a browser. The tests are in code (not Selenium IDE) and verify UI elements, navigations, and data display."*

---

## 🔴 DELIVERABLE 9: STRESS PERFORMANCE TESTING (k6)

### The k6 test script
- File: `Exam-Deliverables/9-Performance-Testing/performance-test.js`

### How to run
```bash
k6 run Exam-Deliverables/9-Performance-Testing/performance-test.js
```

### What the test does

| Stage | Duration | Users | Type |
|---|---|---|---|
| Ramp up | 30s | 0 → 10 | Warm-up |
| Load | 2 min | 10 | Normal traffic |
| Ramp up | 1 min | 10 → 50 | Stress |
| Spike | 30s | 50 → 100 | Spike |
| Recovery | 1 min | 100 → 10 | Recovery |
| Cool down | 30s | 10 → 0 | Shutdown |

### Endpoints tested under load
1. POST /api/auth/login — Authentication
2. GET /api/guests — Guest listing
3. GET /api/guests/1 — Single guest
4. POST /api/guests — Create guest
5. GET /api/rooms — Room listing
6. GET /api/rooms/{id} — Single room
7. GET /api/reservations — Reservation listing
8. POST /api/reservations — Create reservation
9. GET /api/bills — Bill listing
10. GET /api/bills/{id} — Single bill

### Thresholds (pass/fail criteria)
| Metric | Limit |
|---|---|
| p(95) response time | < 500ms |
| p(99) response time | < 1000ms |
| Error rate | < 10% |

### What to say
> *"k6 simulates up to 100 concurrent users going through the full workflow: login, manage guests, rooms, reservations, and bills. The test stages include load (steady traffic), stress (increasing), and spike (sudden surge). Thresholds ensure 95% of requests complete under 500ms and error rate stays under 10%."*

### Potential exam questions
| Question | How to answer |
|---|---|
| *Explain the design of your stress performance tests.* | "We used k6 with stages: ramp-up, steady load, stress ramp, spike, and recovery. Each virtual user authenticates, then performs a realistic workflow." |
| *Run a stress performance test.* | `k6 run Exam-Deliverables/9-Performance-Testing/performance-test.js` |

---

## 🟢 GENERAL EXAM QUESTIONS — PREPARED ANSWERS

### Introduction to Software Testing

| Question | Answer |
|---|---|
| *Differences between testing and debugging?* | **Testing** finds failures by executing software. **Debugging** identifies the root cause of a known failure and fixes it. Testing finds bugs; debugging fixes them. |
| *Static vs dynamic testing?* | **Static testing** reviews code without executing it (reviews, inspections, linters). **Dynamic testing** executes the software with test cases (unit tests, integration tests). |
| *Verification vs validation?* | **Verification:** "Are we building the product right?" (checks against specs — unit tests, reviews). **Validation:** "Are we building the right product?" (checks user needs — acceptance tests). |
| *What is regression testing?* | Re-running existing tests after code changes to ensure nothing broke. Our CI pipeline runs all tests on every push — that's regression testing. |
| *Four general testing principles?* | 1) Testing shows presence of defects, not their absence. 2) Exhaustive testing is impossible — use risk-based sampling. 3) Early testing saves time/money (shift-left). 4) Pesticide paradox — same tests find no new bugs; tests must evolve. |

### The V-model and Testing in Agile

| Question | Answer |
|---|---|
| *Describe the V-model.* | Left side: Requirements → Design → Implementation (verification phases). Right side: Unit tests → Integration tests → System tests → Acceptance tests (validation phases). Each phase on the left maps to a test level on the right. |
| *Verification vs validation in V-model?* | Left side (downward) = verification. Right side (upward) = validation. |
| *Types of system testing?* | Functional testing, performance testing (k6), security testing, usability testing, reliability testing, and stress/load testing. |
| *Explain the test pyramid.* | Bottom (many): Unit tests. Middle: Integration tests. Top (few): E2E/UI tests. Our project follows this: many service unit tests, fewer API integration tests, and a few E2E tests. |
| *Acceptance testing in Scrum?* | Done during sprint review. Product owner verifies user stories meet acceptance criteria. Challenges: stories may not be fully done by sprint end, changing priorities. |

### White Box Test Design

| Question | Answer |
|---|---|
| *Pros and cons of coverage as progress indicator?* | **Pros:** Shows untested code, helps design new tests. **Cons:** High coverage ≠ good tests; 100% coverage can still miss bugs; doesn't measure test quality. |

### Test-Driven Development

| Question | Answer |
|---|---|
| *Advantages and disadvantages of TDD?* | **Advantages:** Better design, fewer bugs, regression safety, documentation. **Disadvantages:** Slower initial development, learning curve, not suitable for all contexts (UI, legacy code). |
| *Explain TDD cadence.* | Red (write failing test) → Green (make it pass with minimal code) → Refactor (improve code while tests stay green). Repeat. |

### Integration Testing

| Question | Answer |
|---|---|
| *Issues when writing tests involving a database?* | State pollution (data from one test affects another), slow tests, external dependencies, setup/teardown complexity. |
| *Different possibilities for testing against a database?* | 1) H2 in-memory (our approach). 2) Testcontainers (real MySQL in Docker). 3) Mock the repository layer. |

### Acceptance Testing

| Question | Answer |
|---|---|
| *What is an acceptance test?* | Validates the system meets business requirements from the user's perspective. Often written as user stories or BDD scenarios (Given-When-Then). |
| *How can acceptance tests be documented?* | User stories with acceptance criteria, Gherkin (Given-When-Then), or checklists. |

---

## 🎯 EXAM DAY CHECKLIST

### Before the exam starts (have everything ready):
```
□ IDE open with the project loaded
□ Exam-Deliverables/ folder visible in explorer
□ Browser open with:
   □ http://localhost:8080/swagger-ui.html
   □ GitHub Actions tab (CI/CD pipeline page)
   □ GitHub repo main page
□ Terminal window open, ready to type commands
□ Postman (or Newman) ready with collection imported
□ k6 installed and verified (k6 --version)
□ App running (docker + spring-boot:run)
   □ Verify: curl http://localhost:8080/api/guests
□ All PDF deliverables open in PDF viewer
```

### During the exam (what to show in order):
```
1. PROJECT OVERVIEW (30 sec)
   → Show folder structure, briefly describe app

2. LIVE DEMOS (5 min)
   → Run Java tests: ./mvnw clean test
   → Show Swagger: http://localhost:8080/swagger-ui.html
   → Run Postman: newman run ...
   → Run k6: k6 run ... (or show results)
   → Show CI/CD: GitHub Actions tab

3. ANSWER QUESTIONS (15 min)
   → Use the cheat sheet above for prepared answers
   → For "make a test fail" questions:
      - Change assertion in test file
      - Run: ./mvnw test -Dtest="ThatTest"
   → For "push to trigger CI":
      - git add . && git commit -m "test" && git push
      - Show pipeline running on GitHub
```

### Common "make it fail" tricks
```bash
# Make a Java test fail
# Change in BillAPITest.java: .andExpect(status().isOk()) → .andExpect(status().isNotFound())

# Make Postman fail
# In collection: change expected status 200 → 404

# Make CI fail
# Push a failing test → pipeline shows red

# Make k6 fail
# Lower thresholds: p(95)<100ms → likely to fail under load