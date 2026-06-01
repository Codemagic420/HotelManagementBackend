# EXAM PREP: WHY WE CHOSE EACH TEST TYPE

**When they ask: "Why did you choose these tests?"**  
**Answer: Explain the BUSINESS REASON, not just the requirement**

---

## 🎯 THE BIG PICTURE FIRST

**Testing has a clear PURPOSE**:
1. **Find bugs early** (cheaper, faster)
2. **Prevent regressions** (don't break old features)
3. **Build confidence** (code works as designed)
4. **Document behavior** (tests show what code does)
5. **Enable refactoring** (safe to change code)

**Each test type serves different purposes.** That's why we chose them.

---

## 1️⃣ **UNIT TESTS (201 tests) - Why We Have So Many**

### **Problem We're Solving:**
- Developers frequently make mistakes in code logic
- A single bug in core logic spreads to many users
- Bugs are expensive if found in production
- We need FAST feedback while coding

### **Why Unit Tests Work:**
✅ Run in 2 minutes (fast feedback)  
✅ Test one piece of code in isolation  
✅ Easy to debug when they fail (knows exactly what broke)  
✅ Can run offline (don't need databases)  
✅ Cheap to write and maintain  

### **What They Catch:**
- ❌ Wrong calculations (e.g., billing math off by 1)
- ❌ Null pointer exceptions
- ❌ Wrong method logic (if/else conditions)
- ❌ Data validation failures
- ❌ Edge cases (boundaries, empty arrays)

### **Real Example from Your Project:**

```java
// This unit test catches a bug in password validation
@Test
void testPassword_MinimumLength() {
    // PROBLEM: Code might accept passwords < 8 chars
    // SOLUTION: Unit test ensures minimum length
    
    String shortPassword = "pass";
    assertThrows(IllegalArgumentException.class, 
        () -> userService.validatePassword(shortPassword));
}
```

**WHY THIS MATTERS:**
- If you don't test password validation, weak passwords get through
- Weak passwords = security breach = company lawsuit
- Unit test catches this in 100ms during development
- Cost if found in production: $100,000+

---

### **Why 201 Tests Specifically?**

**Each test covers a different scenario:**
- 63 API tests = test all endpoints with various inputs
- 48 service tests = test business logic
- 34 security tests = test authentication/authorization
- 31 repository tests = test database operations
- 4 E2E tests = test complete workflows

**201 is the RIGHT number because:**
- Covers all critical paths
- Covers edge cases (boundaries)
- Covers happy path AND error cases
- 50% code coverage on important code

Not 100% because:
- 100% coverage = wasted time on meaningless tests
- Better to have 50% of GOOD tests than 100% of BAD tests

---

## 2️⃣ **INTEGRATION TESTS - Why They're Different**

### **Problem Unit Tests DON'T Solve:**
```
Unit test: ✅ userService.save(user) works
Integration test: ❌ But does it actually save to DATABASE?
```

Unit tests use **mocks** (fake objects). Integration tests use **REAL dependencies**.

### **Why Integration Tests Matter:**
✅ Test that components work TOGETHER (not just alone)  
✅ Test actual database operations  
✅ Test real transactions  
✅ Catch coupling problems  

### **What They Catch (That Unit Tests Miss):**
- ❌ Database constraint violations (UNIQUE, NOT NULL)
- ❌ Transaction issues (rollback not working)
- ❌ Data type mismatches (saving int to VARCHAR)
- ❌ Trigger problems (stored procedures not firing)
- ❌ Foreign key violations

### **Real Example from Your Project:**

```java
// UNIT TEST (uses mock database)
@Test
void testSaveGuest_WithMock() {
    when(repository.save(any())).thenReturn(guest);
    service.save(guest);
    verify(repository).save(any());  // ✅ Passes
}

// But what if actual database rejects it?
// Mock doesn't catch constraint violations!
```

```java
// INTEGRATION TEST (uses real H2 database)
@Test
@Transactional
void testSaveGuest_ToRealDatabase() {
    Guest guest = new Guest();
    guest.setEmail("duplicate@test.com");
    
    guestRepository.save(guest);
    guestRepository.save(guest);  // ❌ UNIQUE constraint!
    // Real database catches this, mock wouldn't
}
```

### **In Your Project:**

You have 31 repository integration tests because:
- Must verify actual database schema works
- Must verify constraints are enforced
- Must verify relationships (FKs) work
- Must verify triggers fire correctly

---

## 3️⃣ **BLACK-BOX TESTS (67 test cases) - Why Technique Matters**

### **Problem We're Solving:**
"We can't test EVERY possible input. How do we choose wisely?"

**Solution: Systematic techniques to find bugs efficiently**

---

### **EQUIVALENCE PARTITIONING (29 cases)**

**Idea**: Group inputs into categories. One test per category.

```
Input: Guest email
Categories:
  - Valid email (test@hotel.com)          ✅ Should work
  - Invalid format (not-an-email)         ❌ Should reject
  - Empty string ("")                     ❌ Should reject

Why: All valid emails behave the same way. 
All invalid emails behave the same way.
No need to test 1000 valid emails - one is enough!
```

**Why This Matters:**
- Tests fewer cases (29 instead of 1000)
- But finds the same bugs
- Efficient use of time

**In Your Project:**
```
Guest email partitions:
  1. Valid emails (gmail.com, hotmail.com, hotel.dk)
  2. Invalid formats (missing @, no domain)
  3. SQL injection attempts ('; DROP TABLE--)
  4. Empty/null
```

---

### **BOUNDARY VALUE ANALYSIS (18 cases)**

**Idea**: Test the EDGES/BOUNDARIES of ranges.

```
Input: Guest age
Valid range: 18-100 years old

Boundaries to test:
  - 17 (below minimum)        ❌ Should reject
  - 18 (minimum valid)        ✅ Should accept
  - 50 (middle/normal)        ✅ Should accept
  - 100 (maximum valid)       ✅ Should accept
  - 101 (above maximum)       ❌ Should reject

Why: Bugs hide at boundaries!
Off-by-one errors: age >= 18 vs age > 18
```

**Real Bug This Catches:**

```java
// WRONG CODE (off-by-one):
if (age >= 18) { ... }  // ✅ Correct
if (age > 18) { ... }   // ❌ BUG: age=18 rejected!

// Boundary test catches this!
```

**In Your Project:**
```
Room capacity boundaries:
  - 0 people (invalid)
  - 1 person (minimum)
  - 2-4 people (normal ranges)
  - Max capacity (boundary)
  - Max+1 (should reject)
```

---

### **STATE TRANSITION TESTING (12 cases)**

**Idea**: Test state changes (status moving from one to another)

```
Reservation states:
  PENDING → CONFIRMED → CHECKED_IN → CHECKED_OUT → BILLED

Invalid transitions that code might allow:
  ❌ PENDING → BILLED (skip intermediate states)
  ❌ CHECKED_OUT → CHECKED_IN (go backwards)
  ❌ BILLED → CONFIRMED (already completed)
```

**Why This Matters:**
- State machines are complex
- Easy to allow invalid transitions
- Can cause major business logic problems

**Real Example:**

```
Bug: Reservation marked as CHECKED_OUT but still accepted new bookings
Why: Code didn't validate state transitions
Impact: Double-booking! Same room for two guests!
Cost: Angry guest, negative review, lost money
```

**In Your Project:**
```
Reservation state transitions:
✅ PENDING → CONFIRMED (guest confirms booking)
✅ CONFIRMED → CHECKED_IN (guest arrives)
✅ CHECKED_IN → CHECKED_OUT (guest leaves)
✅ CHECKED_OUT → BILLED (invoice created)

❌ PENDING → CHECKED_OUT (skipped confirmation!)
❌ BILLED → CHECKED_OUT (reversed!)
```

---

### **DECISION TABLES (8 cases)**

**Idea**: Test combinations of conditions (complex logic)

```
Bill calculation with multiple factors:

Conditions:
  - Has extra services? (Yes/No)
  - Is weekend? (Yes/No)
  - Is repeat guest? (Yes/No)
  
That's 2×2×2 = 8 combinations!

Test all 8:
  ✅ Extra+Weekend+Repeat
  ✅ Extra+Weekend+NoRepeat
  ✅ Extra+Weekday+Repeat
  ... (4 more)
```

**Why This Matters:**
- Complex logic with many conditions = easy to get wrong
- Missing one combination = missing a bug

**Real Bug This Catches:**

```java
// WRONG CODE:
if (hasExtra && isWeekend) { discount = 0.1; }
if (isRepeatGuest) { discount = 0.2; }

// BUG: What if both are true? Which discount applies?
// Decision table forces you to test all combinations!
```

---

## 4️⃣ **API TESTS (Postman - 20+ endpoints) - Why Positive AND Negative**

### **The Testing Balance:**

```
Happy Path (70%): Everything works perfectly
  ✅ Valid inputs → Expected outputs

Negative Tests (30%): User makes mistakes
  ❌ Invalid inputs → Expected rejection
```

### **Why Both Matter:**

**POSITIVE tests verify:**
- ✅ Feature works when used correctly
- ✅ User gets expected results
- ✅ Happy path performs well

**NEGATIVE tests verify:**
- ❌ Invalid input is rejected (not accepted silently)
- ❌ Error messages are helpful
- ❌ Doesn't crash or lose data
- ❌ Security isn't bypassed

### **Real Examples from Your Project:**

```json
POSITIVE TEST:
  Input: Valid guest data
  Expected: Status 201 Created, guest ID returned
  Why: Verify feature works

NEGATIVE TEST:
  Input: Missing email (required field)
  Expected: Status 400 Bad Request
  Why: Verify validation catches errors
```

### **Critical Negative Tests:**

```
Login tests:
  ✅ Valid credentials → 200 OK (positive)
  ❌ Wrong password → 401 Unauthorized (negative)
  ❌ Non-existent user → 401 Unauthorized (negative)
  ❌ SQL injection attempt → 400 Bad Request (negative)

Why: If you only test positive, you don't know if:
  - Invalid login is rejected
  - Password validation works
  - SQL injection is prevented
```

---

## 5️⃣ **E2E TESTS (6 Playwright workflows) - Why End-to-End**

### **Problem We're Solving:**

```
Unit test: ✅ loginService.authenticate() works
API test: ✅ POST /login returns token
E2E test: ❌ But can actual user login through the whole workflow?
```

Unit + API tests don't guarantee the **full journey works**.

### **Why E2E Tests Are Critical:**

✅ Test complete user workflow (not just individual APIs)  
✅ Test interactions between systems  
✅ Catch integration problems unit tests miss  
✅ Test from user perspective  

### **What They Catch (That Unit Tests Miss):**

- ❌ User can't login even though loginService works (session problem)
- ❌ Token generated but not passed to next request
- ❌ Guest can create reservation but then can't retrieve it
- ❌ Checkout updates reservation status but doesn't update room status
- ❌ Bill calculated but not saved to database

### **Real Example:**

```
Unit test: ✅ calculateBill() math is correct
API test: ✅ POST /bills returns 201
E2E test: ❌ Guest can't pay for their bill!

Why: Maybe the bill was created but:
  - Not linked to guest
  - Not linked to reservation
  - Not retrievable by ID
  - Session/auth problem

E2E test follows the full flow and catches this!
```

### **In Your Project:**

6 E2E workflows test:
1. Guest registration → get ID → retrieve guest
2. Guest → search rooms → create reservation → confirm
3. Reservation → check in → update status → check out
4. Checkout → create bill → calculate total → retrieve
5. Multiple guests on one reservation
6. Error handling throughout

**Why 6 is enough:**
- Tests main paths
- Tests error scenarios
- Tests complex workflows
- Doesn't test every combination (that's for black-box)

---

## 6️⃣ **PERFORMANCE TESTS (k6 load testing) - Why It Matters**

### **Problem We're Solving:**

```
Unit test: ✅ Code is correct
API test: ✅ API works
E2E test: ✅ Full workflow works
Performance test: ❌ But does it work with 100 users?
```

### **Why Performance Matters to Business:**

- **10 concurrent users**: Response 200ms ✅ Fast
- **50 concurrent users**: Response 500ms ⚠️ Slow but OK
- **100 concurrent users**: Response 2000ms ❌ Users leave (bounce rate ↑)
- **500ms rule**: If slower than 500ms, users leave

### **Real Business Impact:**

```
E-commerce site:
  - Old code: 100 users → 2 second response
  - Users leave before page loads
  - Lost sales: $10,000/day

  - New optimized code: 100 users → 300ms response
  - Users stay and buy
  - Gained sales: $10,000/day

  Performance testing found the bottleneck!
```

### **What Performance Tests Catch:**

- ❌ Connection pool exhaustion (too few DB connections)
- ❌ Memory leaks (heap grows over time)
- ❌ Slow queries (one bad SQL kills everyone)
- ❌ Thread pool too small (requests queue up)
- ❌ Cache not working (same query 1000x times)

### **In Your Project:**

```
Load test results:
  10 users: p95 = 341ms ✅ Good
  50 users: p95 = 502ms ⚠️ Marginal
  100 users: p95 = 847ms ❌ Poor
  
Finding: Connection pool (20 default) → increase to 40-50

After optimization:
  50 users: p95 = 312ms ✅ Much better!
```

**Why this matters:**
- Identifies bottlenecks before production
- Prevents "works in dev, fails in production"
- Shows scalability limits
- Drives optimization decisions

---

## 7️⃣ **CODE COVERAGE (JaCoCo 50%) - Why Not 100%?**

### **Common Misconception:**
"100% coverage = all bugs found"

### **Reality:**
```
Coverage = "Did we run this line of code?"
Quality ≠ "Does this line work correctly?"

Example:
// Code
if (age > 18) { }

// 100% coverage test (useless):
age = 25;  // ✅ Line runs, coverage = 100%

// But doesn't verify: age=25 correctly enters if block!
// Bug: Code says > 18, actually checks > 21
// Coverage test passes, bug not caught!
```

### **Why 50% Is Reasonable:**

✅ 50% coverage focused on **critical paths**  
✅ Better than 100% coverage on **everything**  
✅ Focuses effort on business logic (not getters/setters)  
✅ Industry standard for good coverage  

### **What We DIDN'T Cover (and why):**
- Getters/setters (not business logic)
- Logging statements (don't affect behavior)
- Exception handling for "never happens" cases
- IDE-generated code

**What We DID Cover:**
- API endpoints (all critical)
- Service layer business logic (all)
- Security checks (100%)
- Validation (100%)
- Database operations (80%+)

---

## 🎯 **CONNECTING IT ALL - The Testing Pyramid**

```
                  ╱╲
                 ╱  ╲  E2E Tests (6)
                ╱    ╲ (Slow, Expensive)
               ╱──────╲
              ╱        ╲
             ╱ Integration ╲ (Repository Tests)
            ╱──────────────╲ (Medium Speed)
           ╱                ╲
          ╱ Unit Tests      ╲ (201 tests)
         ╱__________________╲ (Fast, Cheap)
```

**Why this structure:**

```
Unit Tests (Base):
  - Many (201 tests)
  - Fast (2 minutes)
  - Cheap ($1 each)
  - Instant feedback
  
Integration Tests (Middle):
  - Fewer (31 repo tests)
  - Medium speed
  - Check database works
  
E2E Tests (Top):
  - Few (6 tests)
  - Slow (10 seconds each)
  - Expensive ($10 each)
  - But critical paths
  
Performance Tests (Outside):
  - Different purpose
  - Check scalability
  - Check bottlenecks
```

**Total: 201 + 31 + 6 + 67 black-box = Complete coverage**

---

## 💬 **HOW TO ANSWER IN EXAM**

### **If Asked: "Why did you have 201 unit tests?"**

**DON'T say**: "Because the requirement asked for it."

**DO say**:
```
"Unit tests are the foundation of our testing strategy. 
They run fast (2 minutes) so we get instant feedback during development. 
We have 201 tests because they cover all critical paths:
- 63 API tests cover all endpoints with valid/invalid inputs
- 48 service tests cover business logic
- 34 security tests verify authentication works
- 31 repository tests verify database operations work

This gives us confidence that when we deploy, 
the core functionality is solid."
```

---

### **If Asked: "Why both positive and negative API tests?"**

**DON'T say**: "We needed to test success and failure cases."

**DO say**:
```
"Positive tests verify the feature works when used correctly.
Negative tests verify the system rejects invalid input safely.

For example, if we only test successful login, 
we don't verify that:
- Wrong passwords are rejected (401)
- SQL injection attempts are blocked
- Invalid email formats are caught

Negative tests prevent security breaches and data corruption."
```

---

### **If Asked: "Why 67 black-box test cases?"**

**DON'T say**: "It was required by the exam."

**DO say**:
```
"We used four systematic techniques:

1. Equivalence Partitioning (29 cases):
   Groups similar inputs. Catch logic errors.
   Example: All invalid emails fail the same way.
   
2. Boundary Value Analysis (18 cases):
   Tests at limits/edges. Catch off-by-one errors.
   Example: Maximum occupancy exactly at boundary.
   
3. State Transitions (12 cases):
   Test status changes. Catch invalid state logic.
   Example: Can't checkout if never checked-in.
   
4. Decision Tables (8 cases):
   Test condition combinations. Catch complex logic bugs.
   Example: All combinations of extra-services + weekend + repeat-guest.

67 cases = complete coverage without testing every possible combination.
We're efficient: fewer tests, more bugs caught."
```

---

### **If Asked: "Why performance testing?"**

**DON'T say**: "The assignment required it."

**DO say**:
```
"Performance is a non-functional requirement. 
A working system that's slow loses customers.

We tested load (10 users), stress (50 users), 
and spike (100 users) conditions.

Results show:
- 10 users: 341ms response ✅ Good
- 50 users: 502ms response ⚠️ At threshold
- 100 users: 847ms response ❌ Too slow

This identified that connection pool (20 default) 
is our bottleneck. Recommendation: increase to 40-50.

Without this test, we'd deploy and discover the problem 
when real users hit it. That's too late and expensive."
```

---

## 📋 **QUICK REFERENCE: The "WHY" of Each Test Type**

| Test Type | Why We Have It | What It Catches | Cost/Benefit |
|-----------|---|---|---|
| **Unit (201)** | Fast feedback, isolate bugs | Logic errors, null pointers | Cheap, fast, easy to debug |
| **Integration (31)** | Verify DB works | Constraints, triggers, schema | Medium cost, catches integration bugs |
| **Black-box (67)** | Systematic edge cases | Boundary errors, state bugs | Efficient: few tests, many bugs |
| **API (Postman)** | Test actual endpoints | Invalid inputs, security | Mimics real users, finds real bugs |
| **E2E (6)** | Complete workflows | Integration failures, missing links | Slow but critical for confidence |
| **Performance (k6)** | Scalability | Bottlenecks, resource issues | Prevents production failures |
| **Coverage (50%)** | Code quality baseline | Dead code, missed paths | Balance: focused on critical code |

---

## 🎓 **BOTTOM LINE FOR EXAM**

**When they ask "Why this test?":**

✅ Explain the BUSINESS REASON (what bug does it prevent?)  
✅ Explain the TECHNICAL REASON (what code does it test?)  
✅ Connect to YOUR PROJECT (real example from your code)  
✅ Show you understand RISK (what happens if we skip it?)  

**Don't just list what you did. Explain WHY it matters.**

---

**This separates a good answer from a great answer.** 💪

Practice answering these "Why" questions out loud before the exam!

