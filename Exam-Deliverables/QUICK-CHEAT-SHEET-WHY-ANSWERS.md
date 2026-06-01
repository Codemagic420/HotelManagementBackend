# EXAM QUICK CHEAT SHEET: "Why?" Answers

**Keep this next to you while practicing. Read these out loud until they feel natural.**

---

## 🎤 PRACTICE THESE EXPLANATIONS

### **Q: "Why 201 unit tests?"**

**SHORT ANSWER** (30 seconds):
```
Unit tests give fast feedback on code logic. 
We have 201 because they cover all critical layers:
API endpoints, business logic, database operations, security.
This is cost-effective: each test costs $1 to write and runs in milliseconds.
```

**MEDIUM ANSWER** (60 seconds):
```
Unit tests run in isolation, so we find bugs immediately while coding.
They test one piece of code at a time.

We have 201 because:
- 63 API tests verify all endpoints handle valid and invalid inputs
- 48 service tests verify business logic (billing, reservations, etc.)
- 34 security tests verify authentication and authorization
- 31 repository tests verify database operations

Combined, this covers all critical paths without testing every combination.
With instant feedback, developers fix bugs before they reach production.
```

**REAL EXAMPLE** (90 seconds):
```
Consider the billing calculation. 
Unit test verifies: calculateBill() with 2 nights + 1 extra service = correct total.

Without it, the bug isn't caught until:
- Customer is billed wrong amount
- Customer complains (angry review)
- Company loses money and reputation

Unit test catches it in development: Cost $0, Reputation impact $0.

That's why we have 201 unit tests - prevent expensive problems.
```

---

### **Q: "Why both positive AND negative API tests?"**

**SHORT ANSWER** (30 seconds):
```
Positive tests verify features work correctly.
Negative tests verify the system rejects bad input safely.
Together they prevent bugs and security issues.
```

**MEDIUM ANSWER** (60 seconds):
```
Positive test example: POST /guests with valid data → Status 201 Created
This verifies the happy path works.

Negative test example: POST /guests with invalid email → Status 400 Bad Request
This verifies validation catches errors.

If you only test positive, you don't know if:
- Invalid input is accepted (data corruption)
- Security validation works
- Error messages help users

Both are critical for a production system.
```

**REAL EXAMPLE** (90 seconds):
```
Positive test: Login with admin/admin123 → Returns JWT token
Verifies: Correct credentials work.

Negative test: Login with admin/wrongpassword → Status 401 Unauthorized
Verifies: Wrong password is rejected.

Negative test: Login with admin'; DROP TABLE--; → Status 400 Bad Request
Verifies: SQL injection is prevented.

Only testing positive would miss these security holes.
That's why we have negative tests.
```

---

### **Q: "Why 67 black-box test cases instead of random testing?"**

**SHORT ANSWER** (30 seconds):
```
Random testing is inefficient. We use systematic techniques:
equivalence partitioning, boundary value analysis, state transitions, decision tables.
This finds more bugs with fewer tests.
```

**MEDIUM ANSWER** (60 seconds):
```
Equivalence Partitioning: Group inputs by behavior. 
Test one valid email and one invalid email - no need to test 1000 emails.

Boundary Value Analysis: Test edges where bugs hide.
Age 17 (below minimum), 18 (at minimum), 100 (at maximum), 101 (above).
Off-by-one errors hide here.

State Transitions: Test invalid status changes.
Can't go CHECKED_OUT → CHECKED_IN (backwards).
Can't go PENDING → BILLED (skip intermediate states).

Decision Tables: Test condition combinations.
Extra services + weekend + repeat guest = 8 combinations.

Together: 67 strategically chosen cases catch more bugs than 1000 random tests.
```

---

### **Q: "Why E2E tests if you have unit tests?"**

**SHORT ANSWER** (30 seconds):
```
Unit tests test pieces in isolation. E2E tests verify pieces work together.
It's like testing individual car parts vs. testing the whole car driving.
```

**MEDIUM ANSWER** (60 seconds):
```
Unit test: loginService.authenticate() = works correctly ✅
API test: POST /login returns JWT token ✅
E2E test: User logs in, gets token, uses it to book a room ❌

Why E2E fails? Maybe:
- Token not passed to next request (session issue)
- Guest can create reservation but can't retrieve it (linking problem)
- Auth works but RBAC blocks the operation (authorization issue)

Unit and API tests pass, but user can't actually use the system.
E2E test catches this.
```

---

### **Q: "Why performance testing?"**

**SHORT ANSWER** (30 seconds):
```
A slow system loses customers. 
Performance testing identifies bottlenecks before they hit real users.
```

**MEDIUM ANSWER** (60 seconds):
```
A feature that works correctly but slowly still loses users.

Industry rule: If response time > 500ms, users leave.

Performance test simulates 100 concurrent users.
Results show:
- 10 users: 341ms ✅ Good
- 50 users: 502ms ⚠️ At limit  
- 100 users: 847ms ❌ Too slow

Finding: Database connection pool (20 default) is exhausted.
Solution: Increase to 40-50.

Without this test, we'd deploy and discover in production when real users complain.
That's too late and expensive.
```

---

### **Q: "Why 50% code coverage and not 100%?"**

**SHORT ANSWER** (30 seconds):
```
100% coverage doesn't mean no bugs. 
We focused on critical code paths (APIs, services, security).
Better to have 50% quality coverage than 100% meaningless coverage.
```

**MEDIUM ANSWER** (60 seconds):
```
Coverage = "Did we run this line?" not "Does it work correctly?"

Example of bad 100% coverage:
```java
if (age > 18) { grantAccess(); }

// 100% coverage test:
age = 25;
assertTrue(code ran);  // ✅ Line ran, coverage = 100%

// But doesn't verify: age=25 correctly enters if block!
// Bug: Code says > 18, actually checks > 21
// Coverage test passes, bug not caught!
```

We focused 50% coverage on:
- API endpoints (all critical)
- Service business logic (all)
- Security checks (100%)
- Validation (100%)

We skipped:
- Getters/setters (not business logic)
- Logging (doesn't affect behavior)
- Generated code (IDE stuff)

Result: 50% coverage finds more real bugs than 100% meaningless coverage.
```

---

### **Q: "Why did you use Mockito for unit tests?"**

**SHORT ANSWER** (30 seconds):
```
Mocks isolate the code we're testing. 
We test the API layer without running the database.
This makes tests fast and prevents false failures.
```

**MEDIUM ANSWER** (60 seconds):
```
Real database test:
- Need database running ✅
- Slow (disk I/O) ❌
- Depends on database state ❌
- Hard to test error cases (create a specific DB error) ❌

Mock database test:
- No database needed ✅
- Fast (memory) ✅
- Isolated, no state pollution ✅
- Easy to test errors (mock returns error) ✅

Example:
```java
// Without mock: Test fails because database is slow/unavailable
// With mock: Test passes in 10ms

@Test
void testGuestCreation() {
    when(repository.save(any())).thenReturn(guest);
    service.create(guestData);
    verify(repository).save(any());
}
```

We use mocks for unit tests. 
For integration tests (repository layer), we use real H2 database.
Both serve different purposes.
```

---

### **Q: "Why parameterized tests?"**

**SHORT ANSWER** (30 seconds):
```
Parameterized tests run the same test logic with different data.
Reduces code duplication and makes it easier to add test cases.
```

**MEDIUM ANSWER** (60 seconds):
```
Without parameterized tests:
```java
@Test
void testValidEmail1() { 
    assertValid("test@email.com");
}
@Test
void testValidEmail2() { 
    assertValid("guest@hotel.dk");
}
@Test
void testValidEmail3() { 
    assertValid("admin@system.com");
}
// 3 tests, 3 copies of same code = hard to maintain
```

With parameterized tests:
```java
@ParameterizedTest
@CsvSource({
    "test@email.com",
    "guest@hotel.dk",
    "admin@system.com"
})
void testValidEmails(String email) {
    assertValid(email);
}
// 1 test method, 3 test cases, easy to add more
```

Parameterized tests:
- Reduce code duplication
- Make tests easier to read
- Make it easy to add edge cases
- Follow DRY principle
```

---

## 📋 **QUICK REFERENCE TABLE**

Print this and memorize the pattern:

| Question | Key Word | Answer Framework |
|----------|----------|-------------------|
| **Why 201 tests?** | **Comprehensive** | Coverage of critical paths (APIs, services, DB, security) |
| **Why positive + negative?** | **Safe** | Verify feature works AND invalid input rejected safely |
| **Why 67 black-box?** | **Systematic** | Use techniques (partitioning, boundaries, transitions, tables) |
| **Why E2E?** | **Integration** | Unit tests miss coupling problems, need workflow tests |
| **Why performance?** | **Scalability** | Slow systems lose users, find bottlenecks before production |
| **Why 50% coverage?** | **Focused** | Quality coverage > meaningless 100% coverage |
| **Why mocks?** | **Isolation** | Fast tests, prevent DB dependency, easy error cases |
| **Why parameterized?** | **Efficient** | Reduce duplication, easy to maintain and extend |

---

## 🎯 **ANSWER FORMULA**

For any "Why?" question, use this formula:

1. **State the problem** we're solving:
   ```
   "Without this test, we wouldn't catch X problem"
   ```

2. **Explain the solution:**
   ```
   "This test type catches X by..."
   ```

3. **Give a real example:**
   ```
   "For example, in our project..."
   ```

4. **Connect to business impact:**
   ```
   "This prevents expensive consequence: ..."
   ```

---

## 💪 **PRACTICE TONIGHT**

Pick 5 questions. Answer each WITHOUT reading this guide.

Then read this guide and check if you covered:
- [ ] The PROBLEM being solved
- [ ] HOW the test solves it  
- [ ] A REAL EXAMPLE
- [ ] BUSINESS IMPACT

Repeat until answers feel natural (not memorized).

---

**Good luck! The examiners want to hear that you UNDERSTAND the why, not just know the what.** 🎓

