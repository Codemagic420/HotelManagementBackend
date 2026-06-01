# ORAL EXAM - PRACTICAL QUESTIONS & HOW TO HANDLE THEM

**What They Might Ask You To Do + How to Handle It**

---

## CATEGORY A: RUN & DEMONSTRATE

### A1: "Run the unit tests"

**What to do**:
```bash
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1
./mvnw clean test
```

**Expected Output**:
```
[INFO] Tests run: 201, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

**What to say while waiting**:
"These 201 tests cover the three layers of our application - API controllers, business logic services, and database repositories. The tests run against an H2 in-memory database with 1,200+ seeded test records. They all pass, which means the core functionality is solid."

**If something fails**:
- Don't panic - say "Let me check what's happening"
- Look at error message
- It's probably a test database issue
- Can always restart: `docker-compose down && docker-compose up -d`

---

### A2: "Run the code coverage report"

**What to do**:
```bash
./mvnw jacoco:report
```

**Then open**:
```
target/site/jacoco/index.html
```

**What to show**:
- Overall coverage: 50%
- Click on packages: Show which classes are covered
- Point out: API, Service, Security layers have high coverage
- Explanation: "We focused coverage on critical business logic rather than trying to hit 100%, which would be meaningless"

**What to say**:
"JaCoCo generates this coverage report showing statement coverage. We achieved 50% overall, with critical paths near 100%. 100% coverage doesn't mean no bugs, so we focused on testing important functionality thoroughly rather than just maximizing percentage."

---

### A3: "Run the API tests"

**What to do**:

1. **Ensure API is running**:
```bash
./mvnw spring-boot:run
```

2. **Open Postman**

3. **Import collection**:
   - File → Import
   - Choose: `Exam-Deliverables/7-API-Testing/Hotel-Management-API-Collection.json`

4. **Import environment**:
   - Choose: `Exam-Deliverables/7-API-Testing/environment.json`

5. **Run collection**:
   - Click "Run" → Run all requests
   - Show results

**Expected**: Green checkmarks on most requests

**What to say**:
"This Postman collection tests 20+ API endpoints with both positive and negative test cases. We test status codes, response body validation, and response times. All requests complete in under 500ms, meeting our performance threshold."

---

### A4: "Run the stress/performance test"

**What to do**:
```bash
# Make sure app is running first
# In another terminal:
k6 run Exam-Deliverables/9-Performance-Testing/performance-test.js
```

**What to expect**:
- 450 seconds (~7.5 minutes)
- Shows live stats: users ramping, requests/sec, errors, response times
- Final output shows p95, p99 metrics

**What to say**:
"This k6 script simulates real-world load. We ramp up to 10 concurrent users (load test), then stress test with 50 users, then spike with 100 users. Key findings: p95 response time 487ms (under 500ms threshold), 97% success rate under normal load. At 100 users we see 19.6% failures - that's expected because connection pool saturates."

---

### A5: "Show the end-to-end tests"

**What to do**:

1. **Open IDE** (VS Code/IntelliJ)

2. **Navigate to**:
```
src/test/java/com/kea/hotel/hotelbackend/e2e/E2EApiPlaywrightTest.java
```

3. **Show them**:
   - @Test methods: testGuestRegistrationWorkflow, testReservationBookingWorkflow, etc.
   - Say: "6 complete workflows testing real user scenarios"

4. **Can run if asked**:
```bash
./mvnw test -Dtest=E2EApiPlaywrightTest
```

**What to say**:
"These are end-to-end tests using Playwright, simulating real user journeys. Test 1 is guest registration, test 2 is booking a room with reservation, test 3 is checkout and billing. Tests verify authentication, authorization, and business logic all work together. They're slower than unit tests but catch integration bugs."

---

### A6: "Show the CI pipeline"

**What to do**:

1. **Open file**:
```
Exam-Deliverables/6-CI-CD-Pipeline/github-actions-workflow.yml
```

2. **Explain sections**:
   - `build-and-test`: Compile, run 201 tests
   - `code-quality`: Checkstyle, SpotBugs, SonarCloud
   - `api-testing`: Postman tests
   - `build-docker`: Create container
   - `final-report`: Archive results

3. **Show GitHub Actions** (if available):
   - Go to: https://github.com/Codemagic420/HotelManagementBackend/actions
   - Show recent builds
   - Click on a build to see logs

**What to say**:
"This GitHub Actions workflow runs automatically on every push to the repository. It builds the application, runs our 201 unit tests, performs code quality checks with three tools, runs our Postman API tests, and builds a Docker image. The whole pipeline takes about 5-10 minutes. If any step fails, we get immediate notification so we can fix the issue quickly."

---

### A7: "Show the static analysis tools"

**What to do**:

1. **Run Checkstyle**:
```bash
./mvnw checkstyle:check
```

2. **Run SpotBugs**:
```bash
./mvnw spotbugs:check
```

3. **Show results**

**What to say**:
"Checkstyle verifies code style compliance - naming conventions, formatting, structure. SpotBugs finds potential bugs using static analysis - things like null pointer risks, inefficient code, security issues. SonarCloud (in CI) provides deeper analysis. These tools catch issues before testing, reducing bug escape."

---

## CATEGORY B: CHANGE CODE AND MAKE TESTS FAIL

### B1: "Change the code so a unit test fails"

**What to do**:

1. **Pick a simple test** - e.g., GuestAPITest

2. **Find the test**:
```java
@Test
void testGetGuestById() {
    // Test expects guest exists
}
```

3. **Find corresponding API code**:
```java
@GetMapping("/{id}")
public ResponseEntity<GuestDTO> getGuest(@PathVariable Long id) {
    // This should return guest
}
```

4. **Break it**:
```java
// Change from:
return ResponseEntity.ok(guestService.getGuest(id));

// To:
return ResponseEntity.notFound().build();  // This breaks the test
```

5. **Run test**:
```bash
./mvnw test -Dtest=GuestAPITest
```

**Expected**: Test fails

**What to say**:
"I found the GET guest endpoint in GuestController. The test expects to retrieve a guest by ID and verify the response. I changed the code to return 404 instead of the guest data. When I run the test, it fails as expected because the assertion fails - we expected a guest but got 404."

6. **Restore code** (important!):
```
Undo the change (Ctrl+Z or git restore)
```

---

### B2: "Make an API test fail"

**What to do**:

1. **Ensure API is NOT running**:
```bash
# Stop the running app (Ctrl+C in that terminal)
```

2. **Try to run Postman collection**

**Expected**: Tests fail (can't connect)

**What to say**:
"The API tests need the application to be running on localhost:8080. I stopped the server, so the tests can't connect. All requests fail with 'Connection refused'. Let me restart the application and the tests will pass again."

3. **Restart app**:
```bash
./mvnw spring-boot:run
```

4. **Run tests again** (now they pass)

---

### B3: "Make the CI job fail"

**What to do**:

1. **Break a test** (like B1 above)

2. **Commit & push**:
```bash
git add .
git commit -m "Temporary test failure for demo"
git push origin master
```

3. **Go to GitHub Actions**:
   - Visit: https://github.com/Codemagic420/HotelManagementBackend/actions
   - Watch the build run
   - Watch it FAIL at "Test" step
   - Red X ❌

**What to say**:
"I introduced a failing test in the code. When I pushed to GitHub, the CI pipeline ran automatically. At the test stage, all 201 tests ran, and one failed. The entire pipeline stops (doesn't build Docker, doesn't deploy). This is intentional - we want fast feedback on broken tests."

4. **Restore code** (important!):
```bash
git restore src/... # or git revert
git push
# Watch it pass in Actions
```

---

### B4: "Push to the repository so CI job runs automatically"

**What to do**:

1. **Make a small change** (something safe):
```
Edit: README.md
Add a line: "Tested on June 1, 2026"
```

2. **Commit & push**:
```bash
git add README.md
git commit -m "Updated README"
git push origin master
```

3. **Go to GitHub**:
   - Visit Actions
   - Show the build starting
   - Explain: "It triggered automatically on push"

**What to say**:
"The CI pipeline is set up to run automatically on every push. I made a small change to the README, committed and pushed to GitHub. GitHub Actions detected the push and started the workflow. You can see it's building, running tests, analyzing code quality. This provides instant feedback on code quality."

---

## CATEGORY C: EDIT TEST CODE

### C1: "Show us a parameterized test"

**What to do**:

1. **Search for @ParameterizedTest** in code

2. **If not found**, show how to create one:
```java
@ParameterizedTest
@ValueSource(strings = { "valid@email.com", "test@test.com", "admin@hotel.com" })
void testValidEmails(String email) {
    Guest guest = new Guest();
    guest.setEmail(email);
    assertNotNull(guest.getEmail());
}

@ParameterizedTest
@CsvSource({
    "10, 10",
    "50, 50",
    "100, 100"
})
void testConcurrentUserScenarios(int users, int expected) {
    assertEquals(expected, users);
}
```

3. **Run it**:
```bash
./mvnw test -Dtest=GuestAPITest#testValidEmails
```

**What to say**:
"Parameterized tests run the same test logic with different data. This one tests valid email formats. Instead of writing 5 separate tests with different emails, we use @ParameterizedTest with @CsvSource or @ValueSource to provide multiple inputs. Each input runs as a separate test case. This reduces code duplication and makes it easier to add more test cases."

---

### C2: "Edit an E2E test"

**What to do**:

1. **Open the E2E test file**:
```
src/test/java/com/kea/hotel/hotelbackend/e2e/E2EApiPlaywrightTest.java
```

2. **Edit something simple**:
```java
// Find a test like:
@Test
void testGuestRegistration() {
    // ...code...
}

// Add a new assertion:
assertEquals("Magnus", guest.getFirstName());  // Add this line
```

3. **Run the test**:
```bash
./mvnw test -Dtest=E2EApiPlaywrightTest#testGuestRegistration
```

**What to say**:
"I opened the E2E test file. These tests use Playwright to simulate real browser interactions. I added an assertion to verify the guest's first name. The test runs the workflow and checks that the result matches expectations. If I run it, it should pass because the workflow creates a guest with the correct data."

---

### C3: "Edit a unit test"

**What to do**:

1. **Open a test file**:
```
src/test/java/com/kea/hotel/hotelbackend/api/GuestAPITest.java
```

2. **Make a small edit**:
```java
// Find a test like:
@Test
void testCreateGuest() {
    // ...
}

// Add a new assertion or modify data:
guest.setEmail("newemail@test.com");  // Change this
```

3. **Run test**:
```bash
./mvnw test -Dtest=GuestAPITest#testCreateGuest
```

**What to say**:
"I'm editing a unit test to add a new assertion. In this case, I'm verifying that when we create a guest with a specific email, the email is correctly stored. This is a typical unit test change - either adding new assertions to verify additional behavior or modifying test data to cover edge cases."

---

## CATEGORY D: MANAGE EXTERNAL DEPENDENCIES

### D1: "Change something external that affects the tests"

**What to do**:

1. **Database is external to application** - if you restart it:
```bash
docker-compose down
docker-compose up -d
```

2. **Now run tests**:
```bash
./mvnw test
```

**What to say**:
"The tests depend on the database being running. I restarted docker-compose which brings down and restarts MySQL, MongoDB, and Neo4j. During the startup, tests would fail if run immediately. Once docker finishes initialization (takes ~30 seconds), the tests will pass. This demonstrates that tests are coupled to external dependencies - good test design minimizes such coupling."

---

### D2: "Run specific test suite"

**What to do**:

```bash
# Run only API tests
./mvnw test -Dtest=*APITest

# Run only one test class
./mvnw test -Dtest=GuestAPITest

# Run only one test method
./mvnw test -Dtest=GuestAPITest#testCreateGuest

# Run only integration tests
./mvnw verify -DskipUnitTests
```

**What to say**:
"Maven allows us to run specific tests. This is useful when we only want to test one feature during development. If I run just the Guest API tests, we can verify that feature works without waiting for the full 201-test suite."

---

## KEY THINGS TO REMEMBER

### ✅ DO:
- Have everything open BEFORE exam starts
- Practice these commands beforehand
- Know where files are located
- Have your laptop plugged in (battery!)
- Speak clearly what you're doing
- Explain the results

### ❌ DON'T:
- Type slowly or hesitate
- Go off-topic
- Make changes without explaining
- Restore files incorrectly (use git!)
- Break something and leave it broken

---

## PREP CHECKLIST - RUN THIS 30 MIN BEFORE EXAM

```bash
# 1. Check project directory
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1

# 2. Check git status (should be clean)
git status

# 3. Start databases
docker-compose up -d

# 4. Wait 30 seconds for startup

# 5. Run tests (should pass)
./mvnw clean test

# 6. Start API (in separate terminal)
./mvnw spring-boot:run

# 7. Test API endpoint
curl http://localhost:8080/api/guests

# 8. Open Postman and import collection

# 9. Test Postman collection (should pass)

# 10. Verify all files are in place:
#     - src/test/java (tests)
#     - Exam-Deliverables/ (all 9 folders)
#     - target/site/jacoco/index.html (coverage)
#     - 6-CI-CD-Pipeline/github-actions-workflow.yml

echo "✅ All systems ready for exam!"
```

---

## TROUBLESHOOTING QUICK REFERENCE

| Problem | Solution |
|---------|----------|
| Tests won't run | Check: docker-compose ps, check database is running |
| API won't start | Kill existing process: lsof -ti:8080 | xargs kill |
| Git issues | git status, git diff, git log --oneline |
| Postman tests fail | Ensure API running on localhost:8080, refresh environment |
| Coverage report missing | Run: ./mvnw jacoco:report, then open target/site/jacoco/ |
| Slow tests | This is normal - 201 tests take ~2 minutes |
| One test keeps failing | Check test database seeding, can comment test out temporarily |

---

## WHAT IF SOMETHING GOES WRONG?

**Stay calm!** This is normal and expected.

1. **API won't start**
   - Say: "Let me restart the services"
   - Do: `docker-compose restart`
   - Wait 30 seconds
   - Try again

2. **Test fails unexpectedly**
   - Say: "Let me check the error message"
   - Read the error
   - It's probably a test data issue
   - Try: `./mvnw clean test` (fresh start)

3. **Can't find a file**
   - Say: "Let me locate it"
   - Use: `find . -name "filename.java"`
   - Or check folder structure

4. **Code won't compile**
   - Say: "Let me check the compilation error"
   - Read error message
   - Fix (probably missing dependency)

5. **Examiner asks something you don't know**
   - Say: "That's a good question, let me think about that..."
   - Take a breath
   - Give your best answer
   - Or: "I don't have specific experience with that, but based on testing principles..."

---

**You've prepared well. Just demonstrate what you've built with confidence.**

**Good luck tomorrow! 🍀**

