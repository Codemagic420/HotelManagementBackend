# ORAL EXAM - THEORETICAL Q&A GUIDE

**25 Potential Questions with Answers**  
**Study Tips**: Read the answers, then try to explain in YOUR OWN WORDS without reading

---

## SECTION 1: Introduction to Software Testing (5 questions)

### Q1: What are the differences between testing and debugging?

**Short Answer**:
- **Testing**: Finding bugs (failure detection) - systematic, planned activity
- **Debugging**: Fixing bugs (fault analysis) - investigative, reactive activity

**Explanation**:
Testing verifies that software meets requirements. It's done by test team, proactive, runs test cases. If tests fail, you found a bug.

Debugging investigates WHY it failed and fixes the code. Done by developers, reactive, uses debugger tools.

**Example**: You write a test for login. It fails (password not accepted). That's testing finding the bug. Dev debugs to find the password hashing is broken. That's debugging.

---

### Q2: What is static testing and what is dynamic testing?

**Short Answer**:
- **Static Testing**: Analyzing code without running it (reviews, code analysis tools)
- **Dynamic Testing**: Testing by running the code (unit tests, integration tests)

**Explanation**:
Static = reading code, walkthroughs, inspections, code reviews, static analysis tools (Checkstyle, SpotBugs).
- Finds issues early (logic errors, style violations)
- No test execution needed
- Can catch 50%+ of defects before running tests

Dynamic = executing the program with test cases.
- Runs actual behavior
- Verifies outputs
- Finds runtime issues

**Example in Your Project**:
- Static: Checkstyle/SpotBugs in CI pipeline, code reviews
- Dynamic: Running 201 unit tests, performance tests

---

### Q3: What is verification and what is validation?

**Short Answer**:
- **Verification**: "Are we building it right?" - Does code meet specification?
- **Validation**: "Are we building the right thing?" - Does system meet user needs?

**Explanation**:
Verification = checking against requirements (did we code what was specified?)
- Code reviews
- Unit tests
- Matches SRS

Validation = checking against real world (is this what user actually needs?)
- User acceptance testing
- System testing
- Real scenarios

**Example in Your Project**:
- Verification: Unit tests verify getGuest() returns correct data per SRS requirement
- Validation: End-to-end tests verify actual user can book room workflow

---

### Q4: What is regression testing?

**Short Answer**:
Regression testing checks that old features still work after making changes. When you fix a bug or add a feature, you run existing tests to ensure you didn't break anything.

**Explanation**:
After any code change:
1. Fix bug X
2. Regression test: Run all existing tests
3. Verify nothing else broke

**Example**: You fix the billing calculation. Regression test runs all 201 unit tests to ensure guest creation, reservation, etc still work.

**In Your Project**: Your CI pipeline runs all 201 tests on every push - that's automated regression testing.

---

### Q5: Explain at least four general testing principles.

**Short Answer** (Pick 4):

1. **Testing shows presence of defects, not absence**
   - Tests can show bugs exist, but can't prove system is perfect
   - 100% testing is impossible

2. **Exhaustive testing is impossible**
   - Can't test every combination of inputs
   - Must use risk-based approach (test important parts first)
   - Your black-box design uses techniques to prioritize test cases

3. **Early testing saves money**
   - Finding bug in requirements = cheap
   - Finding bug in production = expensive
   - Your SRS review happened before coding

4. **Tests are biased towards developers**
   - Developers tend to test happy path
   - Independent testing finds more bugs
   - Your team had dedicated QA perspective

5. **Testing is context-dependent**
   - E-commerce needs different tests than hotel system
   - Your tests designed for hospitality domain

6. **Pesticide paradox**
   - Same tests get boring, find fewer bugs
   - Need new test cases periodically
   - Your 67 black-box cases cover different angles

**Recommendation**: Pick 4 you understand best, practice explaining them naturally.

---

## SECTION 2: The V-Model and Testing in Agile (5 questions)

### Q1: Describe the V-model

**Short Answer**:
The V-model shows testing activities aligned with development phases:
- Left side (V going down): Requirements → Design → Code
- Right side (V going up): Unit Test → Integration Test → System Test → Acceptance Test
- Each development phase has corresponding test phase

**Explanation**:
```
Requirements                         Acceptance Testing
        \                           /
         Design              System Testing
             \              /
              Code  Integration Testing
                \ /
              Unit Testing
```

Left side = what to build  
Right side = verify it works

**Example in Your Project**:
- Requirements: SRS with 40+ requirements
- Design: Database schema + API design
- Code: Java implementation
- Unit Tests: 201 tests verify code
- Integration: H2 database tests
- System: API tests with Postman
- Acceptance: E2E tests simulating user workflows

---

### Q2: According to ISTQB, what parts of V-model correspond to verification and which to validation?

**Short Answer**:
- **Verification (left & bottom)**: Requirements, Design, Code, Unit Testing = building correctly
- **Validation (right & top)**: Integration, System, Acceptance Testing = building the right thing

**Explanation**:
As you go UP the right side of V, you get closer to actual user perspective:
- Unit test = verifies code works per specs
- Integration test = verifies components work together
- System test = verifies whole system works
- Acceptance test = verifies user gets what they wanted

---

### Q3: Explain what types of tests fall normally under system testing

**Short Answer**:
System testing verifies the complete integrated system against requirements:
- **Functional testing**: All features work
- **Non-functional testing**: Performance, security, usability, reliability
- **End-to-end testing**: Complete workflows
- **Regression testing**: Previous features still work
- **Smoke testing**: Critical path works

**In Your Project**:
- Functional: API tests verify all endpoints work (Postman)
- Non-functional: Performance tests, security (JWT), load testing
- E2E: 6 workflows (guest → reservation → billing)
- Smoke: Quick tests that core features work

---

### Q4: Explain the test pyramid and discuss its applicability

**Short Answer**:
```
        Top (10%)    - UI/E2E Tests (slow, expensive)
       Middle (30%) - Integration Tests (medium)
      Base (60%)   - Unit Tests (fast, cheap)
```

**Explanation**:
- **Base (Unit Tests)**: Many, fast, run often, check small pieces
- **Middle (Integration)**: Fewer, slower, verify pieces work together
- **Top (E2E/UI)**: Few, slowest, most expensive, simulate real user

**Why This Works**:
- Costs: 1 unit test < 1 integration test < 1 E2E test
- Speed: Unit tests run in seconds, E2E in minutes
- Feedback: Failures at base level are easier to debug
- Risk: Cover edge cases at base, happy path at top

**In Your Project**:
- Base: 201 unit tests (80%) - fast feedback in CI
- Middle: Integration tests, repository tests (15%)
- Top: E2E tests (5%), Performance tests

**Applicability**: 
✅ Works well for backend APIs like yours
⚠️ For front-end heavy systems, pyramid inverts (more UI tests needed)

---

### Q5: Explain the difficulties of performing acceptance testing in a Scrum setting

**Short Answer**:
Scrum moves fast (2-week sprints), but acceptance testing needs:
1. **Time**: Acceptance tests require user involvement - hard in fast sprints
2. **Definition**: What is "acceptance"? If unclear, tests fail
3. **Late timing**: User usually only available at sprint end (after development done)
4. **Change**: Requirements change mid-sprint, tests need updates
5. **Resource**: Product owner needed to define/approve tests

**Real World Difficulty**:
```
Sprint: 2 weeks
- Week 1: Dev builds feature
- End of Week 2: "Ready for acceptance?" → Tests fail → Feature not done
→ Sprint goal missed
```

**Solutions Mentioned**:
- Acceptance criteria defined BEFORE sprint
- Product owner involved daily
- Automated acceptance tests (like your E2E tests)
- Continuous integration (your CI pipeline)

**In Your Project**: 
You used automated E2E tests so acceptance can be checked anytime (not just at sprint end).

---

## SECTION 3: Test Management (3 questions)

### Q1: What is test monitoring and control?

**Short Answer**:
- **Monitoring**: Track test progress (how many tests ran, passed, failed?)
- **Control**: Take action if metrics go wrong (add more tests, fix code)

**Explanation**:
Monitoring = measuring, Control = responding

**Monitoring Examples**:
- 201 tests passing
- 50% code coverage
- p95 response time 487ms
- 0 critical defects

**Control Examples**:
If coverage drops below 40%:
- Write more tests
- Improve test design

If p95 > 500ms:
- Optimize slow queries
- Increase connection pool

---

### Q2: What is incident management?

**Short Answer**:
Incident management is how you handle defects found during testing:
1. **Report**: Document the bug with steps to reproduce
2. **Prioritize**: How critical is it? (Blocker vs Minor)
3. **Assign**: Who fixes it?
4. **Verify**: Is it actually fixed? (Regression test)
5. **Close**: Mark as resolved

**Example**:
Incident: Login fails with special characters
- Priority: High (authentication broken)
- Assign to: Backend dev
- Fix: Escape special chars in password validation
- Verify: Run login tests again
- Close: Incident resolved

**In Your Project**: Your git commit history shows this process (bug reports → fixes → tests verify).

---

### Q3: What is configuration management?

**Short Answer**:
Configuration management tracks what version of code/tests you're using:
- Version control (Git)
- Release versions
- Test environment configs
- Build artifacts

**Example**:
Which tests run on which version?
```
Version 1.0:
- 201 unit tests
- No E2E tests

Version 1.1:
- 201 unit tests
- 6 E2E tests added
```

**In Your Project**: 
Git tracks all versions. master branch = latest release.

---

## SECTION 4: White Box Test Design (1 question)

### Q1: Explain the pros and cons of coverage as a test progress measuring indicator

**Short Answer**:

**PROS**:
- Objective metric (50% vs 100%)
- Shows code being exercised
- Identifies untested code
- Helps find gaps

**CONS**:
- High coverage ≠ good tests
- 100% coverage doesn't mean no bugs
- Can be meaningless (test that does nothing but runs code)
- Doesn't measure quality

**Example**:
```
// Code
if (age > 18) {
    grantAccess();
}

// Bad test: 100% coverage but wrong
assertTrue(age > 18);  // Covers line, but doesn't assert anything

// Good test:
if (age >= 18) {
    grantAccess();
    assert(accessGranted == true);  // Actually verifies behavior
}
```

**In Your Project**: 50% coverage is OK because it's on critical paths (API layer, Services, Security), not just line coverage.

---

## SECTION 5: Unit Testing (5 questions)

### Q1: Discuss whether private methods must be unit-tested or not

**Short Answer**:
**NO**, private methods should NOT be unit-tested directly. Instead, test them indirectly through public methods.

**Explanation**:
Private methods are implementation details. They change frequently. Public methods are the contract.

**Example**:
```java
public class BillCalculator {
    public double calculateBill(reservation) {
        double roomCost = getRoomCost();  // Private helper
        double extras = getExtraCosts();   // Private helper
        return roomCost + extras;
    }
    
    private double getRoomCost() { }
    private double getExtraCosts() { }
}
```

❌ DON'T test getRoomCost() directly
✅ DO test calculateBill() which calls it internally

**Benefits**:
- Flexibility to refactor private methods
- Focus on public contract
- Tests are more stable

**In Your Project**: Your unit tests verify public API methods, not private helpers.

---

### Q2: Discuss when a unit test can be considered an integration test

**Short Answer**:
A unit test becomes an integration test when it:
- Uses real dependencies (not mocks)
- Accesses database
- Calls external services
- Involves multiple classes working together

**Example**:
```java
// UNIT test (mocked repository)
@Test
void testCreateGuest() {
    when(repository.save(...)).thenReturn(guest);
    service.createGuest(guest);
    verify(repository).save(...);
}

// INTEGRATION test (real database)
@Test
void testCreateGuestWithDatabase() {
    guestRepository.save(guest);
    Guest found = guestRepository.findById(guest.getId());
    assertEquals(guest.getEmail(), found.getEmail());
}
```

Unit test = isolated, fast  
Integration test = collaborative, slower

**In Your Project**: You have 201 unit tests (mocked) + integration tests using H2 database.

---

### Q3: What are the advantages and disadvantages of the classical approach to unit testing?

**Short Answer**:

**Classical (Detroit School)**:
- Only mock external dependencies (database, API, files)
- Use real objects for internal dependencies
- Focus on behavior, not structure

**ADVANTAGES**:
- Tests are stable (less mocking = fewer brittle tests)
- Finds integration bugs early
- Real interaction patterns tested
- Simpler to understand

**DISADVANTAGES**:
- Tests slower (use real objects)
- Setup more complex
- Failures harder to debug (many interactions)
- Test runs depend on other objects

**Example from Your Project**:
```java
// Classical: Mock ONLY the database
@Test
void testCreateGuest() {
    GuestRepository mockRepo = mock(GuestRepository.class);
    GuestService service = new GuestService(mockRepo);  // Real service
    service.createGuest(guestData);  // Uses real validation
    verify(mockRepo).save(...);
}
```

---

### Q4: What are the advantages and disadvantages of the London approach to unit testing?

**Short Answer**:

**London School (Mockist)**:
- Mock ALL dependencies (even internal ones)
- Focus on how object interacts with others
- "Spy on" every interaction

**ADVANTAGES**:
- Very fast tests (all mocks)
- Good for TDD (design by interaction)
- Failures isolated to one unit
- Tests parallel easily

**DISADVANTAGES**:
- Over-mocking creates false confidence
- Tests brittle (change implementation = change tests)
- Misses integration bugs
- Complex to maintain

**Example**:
```java
// London: Mock everything
@Test
void testCreateGuest() {
    GuestRepository mockRepo = mock(GuestRepository.class);
    EmailService mockEmail = mock(EmailService.class);
    GuestService service = new GuestService(mockRepo, mockEmail);
    
    service.createGuest(guestData);
    
    verify(mockRepo).save(...);
    verify(mockEmail).sendWelcome(...);  // Verify interaction
}
```

**Your Project**: Mix of both approaches - mocks for external, real objects for services.

---

### Q5: Discuss whether mocking should be used for external (unmanaged) dependencies

**Short Answer**:
**YES**, mock external dependencies. **NO**, don't mock dependencies you control.

**Explanation**:
External (unmanaged) = things you don't own (APIs, databases, email services)
Internal (managed) = your code

**Why Mock External**:
- Can't control them in tests
- Slow (network calls, DB queries)
- May not be available during testing
- Might have costs (API calls)

**Example**:
```
✅ Mock: External PaymentGateway API
✅ Mock: External Email Service
❌ Don't mock: Your own UserRepository (you manage it)
❌ Don't mock: Your own BillingService (you manage it)
```

**In Your Project**:
- Mock: External authentication (if you had external OAuth)
- Real: Your repositories (you control schema)
- Mock: Email if you had email service

---

## SECTION 6: Test-Driven Development (2 questions)

### Q1: What are the advantages and disadvantages of Test-Driven Development?

**Short Answer**:

**TDD = Write test → Write code → Refactor (Red → Green → Refactor)**

**ADVANTAGES**:
- Forces thinking about requirements first
- Code is testable by nature
- High code coverage automatically
- Refactoring is safe (tests catch breaks)
- Acts as design/documentation
- Finds edge cases early

**DISADVANTAGES**:
- Slower initially (write tests first)
- Requires discipline
- Not all code is testable (UI, legacy)
- Requires good test design skills
- Can lead to over-engineering

**Example**:
```
1. Write test (RED): testCreateGuest() fails
2. Write minimal code to pass (GREEN): Add createGuest()
3. Refactor: Improve implementation, test still passes
```

**In Your Project**: 
While not pure TDD (you built code first), you later wrote 201 tests ensuring design is sound.

---

### Q2: Explain the TDD cadence

**Short Answer**:
The TDD rhythm (Red → Green → Refactor) repeated continuously:

1. **RED**: Write a failing test
   - Test doesn't compile or fails
   - Run it, see the error

2. **GREEN**: Write minimal code to pass
   - Just enough to make test pass
   - Code may be ugly/inefficient
   - Test passes!

3. **REFACTOR**: Improve code
   - Clean it up
   - Keep tests passing
   - No behavior change

**Example**:
```
RED:       Write test for calculateBill()
           Test fails (method doesn't exist)

GREEN:     Add calculateBill() { return 0; }
           Test passes (but fake)

REFACTOR:  Add real calculation logic
           Test still passes
           Code is clean
```

**Cycle Time**: 5-10 minutes per cycle

**In Your Project**: 
Your 201 tests were written after code, but they follow the REFACTOR phase (ensuring code is clean and works).

---

## SECTION 7: Integration Testing (1 question)

### Q1: Discuss potential issues when writing tests that involve a database

**Short Answer**:
Database tests are hard because:

1. **State Isolation**
   - Each test leaves data behind
   - Next test sees old data
   - Solution: Clear data before/after each test (@Transactional)

2. **Speed**
   - Database queries are slow
   - Tests become slow
   - Solution: Use H2 in-memory (your project does this)

3. **Schema Synchronization**
   - Database schema ≠ test schema
   - Migrations might fail
   - Solution: Use same schema in tests

4. **External State**
   - Database has real data
   - Hard to control
   - Solution: Use test database

5. **Concurrency**
   - Tests running in parallel might conflict
   - Deadlocks possible
   - Solution: Use transactions, sequential runs

**Example Issues**:
```
Test 1: Create guest
Test 2: Expects guest not to exist
→ FAILS because Test 1's guest still in DB

Solution: Clear database between tests
```

**In Your Project**:
- H2 in-memory database (fresh for each test run)
- @Transactional (auto-rollback)
- 1,200+ test data seeded consistently

---

## SECTION 8: Acceptance Testing (2 questions)

### Q1: Explain what is an acceptance test

**Short Answer**:
An acceptance test verifies that the system meets user needs and is acceptable for delivery.

**Characteristics**:
- Written from user perspective ("I want to...")
- Tests complete scenarios/workflows
- Checks business value is delivered
- Often called "behavior-driven" or "end-to-end"
- User/product owner involved

**Example**:
```
User Story: "As a guest, I want to book a room"
Acceptance Test: 
1. Login as guest
2. Search available rooms
3. Select room for dates
4. Confirm booking
5. Get confirmation number
→ If this works, user acceptance is met
```

**In Your Project**: 
Your E2E tests (6 workflows) are acceptance tests - they verify complete guest journey works.

---

### Q2: Explain how an acceptance test can be documented

**Short Answer**:
Acceptance tests documented in multiple formats:

1. **Gherkin Format** (Given-When-Then):
```
Given: I am logged in as a guest
When: I search for available rooms on 2026-06-01
And: I select a room
Then: The room is marked as unavailable
And: A reservation is created
```

2. **User Story Format**:
```
As a Guest,
I want to book a room,
So that I have accommodation
Acceptance Criteria:
- Search returns available rooms
- Booking creates reservation
- Confirmation email sent
```

3. **Test Code** (Automated):
```java
@Test
void guestCanBookRoom() {
    // Given
    Guest guest = loginAs("guest@email.com");
    
    // When
    Reservation res = guest.searchAndBook("2026-06-01", "2026-06-03");
    
    // Then
    assertNotNull(res.getConfirmationNumber());
}
```

4. **Test Case Document** (Manual):
```
Test: Book Room
Steps:
1. Login
2. Search rooms
3. Book
Expected: Reservation created
```

**In Your Project**:
- Documentation: Exam-Deliverables/3-Black-Box-Testing/ has test cases
- Code: E2E tests are automated acceptance tests
- Format: Both Given-When-Then and code

---

## QUICK REFERENCE CARD

Print this and review before exam:

```
SECTION 1 - INTRO TO TESTING
Q1: Testing finds bugs, Debugging fixes them
Q2: Static (code analysis), Dynamic (run tests)
Q3: Verification (right code), Validation (code right thing)
Q4: Regression = rerun old tests after changes
Q5: Pick 4 principles: Defects exist, exhaustive impossible, early is cheap, context matters

SECTION 2 - V-MODEL & AGILE
Q1: V-model: Requirements→Design→Code then Unit→Integration→System→Acceptance
Q2: Left side = Verification, Right side = Validation
Q3: System testing = Functional, Non-functional, E2E, Regression, Smoke
Q4: Test pyramid: 60% unit, 30% integration, 10% E2E
Q5: Scrum difficulties: Time pressure, unclear requirements, late user involvement

SECTION 3 - TEST MANAGEMENT
Q1: Monitoring = track progress, Control = take action
Q2: Incident = report→prioritize→assign→verify→close
Q3: Configuration = version control, track what code/tests with each version

SECTION 4 - WHITE BOX
Q1: Coverage pros (objective, finds gaps), Coverage cons (doesn't mean no bugs)

SECTION 5 - UNIT TESTING
Q1: Don't test private methods directly
Q2: Unit test becomes integration when uses real dependencies
Q3: Classical: Mock external only (stable, finds bugs), Mockist: Mock all (fast, brittle)
Q4: Classical advantages/disadvantages (opposite of Mockist)
Q5: Mock external (APIs, DB), don't mock internal (your code)

SECTION 6 - TDD
Q1: Advantages (testable, coverage), Disadvantages (slow, needs discipline)
Q2: Red→Green→Refactor cycle (5-10 min per cycle)

SECTION 7 - INTEGRATION
Q1: Issues: State isolation, speed, schema sync, external state, concurrency

SECTION 8 - ACCEPTANCE
Q1: Tests user needs from user perspective
Q2: Document as: Gherkin, User Story, Test Code, or Test Case
```

---

## STUDY TIPS

1. **Read answer once**, then try to explain WITHOUT reading
2. **Connect to your project**: "In our project, we did X to address Y"
3. **Use examples**: Concrete examples help your explanations
4. **Practice out loud**: Explain to someone (or mirror)
5. **Time yourself**: Each answer should take ~1-2 minutes
6. **Focus on understanding**, not memorizing
7. **If stuck**: Say what you think, ask for clarification

**Good luck! You know this stuff! 💪**

