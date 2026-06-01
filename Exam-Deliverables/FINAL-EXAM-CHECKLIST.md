# 📋 FINAL EXAM CHECKLIST - JUNE 1, 2026

**Exam Time**: 10:30  
**Exam Location**: EK København  
**Duration**: Max 30 minutes (10 min presentation + 15 min questions + 5 min grading)  
**Status**: ✅ **READY TO GO**

---

## 🎯 PRE-EXAM TONIGHT (May 31, 2026)

### 1. PREPARE YOUR PRESENTATION

- [ ] Read: `Exam-Deliverables/PRESENTATION-GUIDE.md` (10 min presentation flow)
- [ ] Practice explaining (10 minutes out loud, in your own words)
- [ ] Time yourself - aim for 8-9 minutes (leave time for questions)
- [ ] Focus on:
  - What the application does (1 min)
  - Architecture (databases, APIs) (2 min)
  - Testing strategy (2 min)
  - Implementation details (2 min)
  - Performance & CI/CD (1.5 min)

### 2. STUDY THEORETICAL QUESTIONS

- [ ] Read: `Exam-Deliverables/THEORETICAL-QA-GUIDE.md`
- [ ] Pick 3-4 of the 25 questions that interest you
- [ ] Practice explaining them in simple words (not from text)
- [ ] Note down key phrases for each topic:
  - Testing vs Debugging
  - Static vs Dynamic Testing
  - Verification vs Validation
  - Regression Testing
  - Test Pyramid
  - Unit Testing approaches
  - TDD
  - Acceptance Testing

### 3. FAMILIARIZE WITH PRACTICAL SCENARIOS

- [ ] Read: `Exam-Deliverables/PRACTICAL-QUESTIONS-GUIDE.md`
- [ ] These are things you might be asked to DEMONSTRATE:
  - Run tests
  - Change code and break tests
  - Run API tests
  - Show CI pipeline
  - Run performance tests

### 4. VERIFY EVERYTHING WORKS

Run this verification script now (takes ~5 min):

```bash
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1

# 1. Check git is clean
git status
# Should show: "On branch master, nothing to commit, working tree clean"

# 2. Check docker is ready
docker-compose ps
# Should show: MySQL, MongoDB, Neo4j all running

# 3. Run tests
./mvnw clean test
# Should show: Tests run: 201, Failures: 0, Errors: 0

# 4. Verify files exist
ls Exam-Deliverables/1-Review/SRS.md
ls Exam-Deliverables/1-Review/Review-Report.md
ls Exam-Deliverables/2-Risk-Assessment/Risk-Assessment-Report.md
ls Exam-Deliverables/3-Black-Box-Testing/Black-Box-Test-Design.md
ls Exam-Deliverables/6-CI-CD-Pipeline/github-actions-workflow.yml
ls Exam-Deliverables/7-API-Testing/Hotel-Management-API-Collection.json
ls Exam-Deliverables/8-E2E-Testing/E2EApiPlaywrightTest.java
ls Exam-Deliverables/9-Performance-Testing/performance-test.js
# All should exist ✅

echo "✅ Pre-exam verification complete!"
```

### 5. SET UP WORKSPACE

Open these files/apps and keep them open:

**Terminal 1**: Leave ready for commands
```bash
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1
```

**Terminal 2 (optional)**: Ready to start API
```bash
./mvnw spring-boot:run
```

**VS Code/IDE**: Open with project loaded
- Show: src/test/java directory
- Ready to navigate test files
- Have PRESENTATION-GUIDE.md visible in tab

**Browser**: Bookmarks ready
- http://localhost:8080/swagger-ui.html (API docs)
- target/site/jacoco/index.html (coverage report)
- https://github.com/Codemagic420/HotelManagementBackend (CI/CD)

**Postman**: Imported with collection
- Hotel-Management-API-Collection.json loaded
- environment.json selected

**PDF Reader**: Have these open
- Exam-Deliverables/1-Review/SRS.md
- Exam-Deliverables/1-Review/Review-Report.md
- Exam-Deliverables/2-Risk-Assessment/Risk-Assessment-Report.md
- Exam-Deliverables/3-Black-Box-Testing/Black-Box-Test-Design.md

---

## 🌅 MORNING OF EXAM (June 1, 2026)

### WAKE UP EARLY
- [ ] Get up 90+ minutes before exam (at 8:50 for 10:30 exam)
- [ ] Eat breakfast
- [ ] Shower, get ready

### 1 HOUR BEFORE EXAM (9:30)

Run this final check in terminal:

```bash
cd C:\Users\magnu\IdeaProjects\HotelManagementBackend1

# 1. Ensure docker is running
docker-compose ps
# If not running: docker-compose up -d
# Wait 30 seconds for startup

# 2. Verify databases are ready
curl http://localhost:3306  # MySQL
curl http://localhost:27017 # MongoDB  
curl http://localhost:7687  # Neo4j
# (Errors are OK, just checking connectivity)

# 3. Quick test run
./mvnw test -Dtest=GuestAPITest --quiet
# Should complete in <30 seconds with green check

# 4. Start API in background (keep it running)
./mvnw spring-boot:run &

# Wait 10 seconds for startup

# 5. Test API endpoint
curl http://localhost:8080/api/guests
# Should return list of guests (JSON array)

echo "✅ All systems GO for exam!"
```

### 30 MIN BEFORE EXAM (10:00)

- [ ] Close unnecessary apps (email, Slack, etc.)
- [ ] Mute notifications
- [ ] Full screen the IDE/browser showing your project
- [ ] Check battery level - should be 100%
- [ ] Have water bottle nearby
- [ ] Use bathroom
- [ ] Take a deep breath 😌

### MENTALLY PREPARE

- [ ] Remember: You built this. You know this.
- [ ] You have 201 passing tests - that's impressive
- [ ] You have 9 complete deliverables
- [ ] You have studied the material
- [ ] The examiners WANT you to succeed
- [ ] If you don't know something, that's OK - just say so

---

## 🎓 DURING EXAM (10:30)

### PRESENTATION (Max 10 minutes)

**Your Structure** (use PRESENTATION-GUIDE.md):

```
[0:00-1:00]   Introduction (what is the app?)
[1:00-3:00]   Architecture & Requirements
[3:00-5:00]   Testing Strategy
[5:00-7:30]   Testing Implementation (show code)
[7:30-9:30]   Performance & CI/CD
[9:30-10:00]  Conclusion & Risk Assessment
```

**Key Things to Do**:
- ✅ Speak clearly, not too fast
- ✅ Let them SEE things (point at screen)
- ✅ Show actual code, not just talk
- ✅ Show reports (SRS, Risk, Black-box)
- ✅ Demonstrate tests running if possible
- ✅ Connect everything to your project

**Key Things NOT to Do**:
- ❌ Don't read from paper word-for-word
- ❌ Don't apologize for things that work
- ❌ Don't spend 10 minutes on intro
- ❌ Don't show PowerPoint (you don't have one!)
- ❌ Don't ramble or go off-topic

### QUESTIONS (Max 15 minutes)

**If Asked Theory Question**:
1. Take a breath
2. Think for 2 seconds
3. Start talking (even if uncertain)
4. Connect to your project if possible
5. If don't know, say "That's a good question. Based on what I understand..."

**If Asked Practical Question**:
1. Ask for clarification if needed
2. Do it step by step
3. Explain as you go
4. Don't rush
5. If something breaks, calmly troubleshoot

**Topics They'll Likely Ask**:

✅ Very Likely:
- Your test structure (unit vs integration vs E2E)
- Your black-box design approach
- How you achieved coverage
- Your risk assessment results
- Your performance test results
- How you used mocking/test doubles
- Your CI pipeline

⚠️ Might Ask:
- V-model vs Agile
- Testing pyramid
- When unit test becomes integration
- TDD approach
- Acceptance testing

🎲 Random (from 25 list):
- Any of the theoretical questions from THEORETICAL-QA-GUIDE.md

---

## 📦 WHAT TO BRING

### PHYSICAL
- [ ] Laptop (fully charged or with charger)
- [ ] Mouse (if you prefer over trackpad)
- [ ] Power adapter
- [ ] One sheet of paper with notes (if desired)
- [ ] Pen (not allowed for exam, but for notes)

### DIGITAL (On Laptop)
- [ ] Project open in IDE (VS Code/IntelliJ)
- [ ] Tests ready to run: `./mvnw clean test`
- [ ] API ready to run: `./mvnw spring-boot:run`
- [ ] Postman with collection imported
- [ ] Browser bookmarks ready (Swagger, JaCoCo, GitHub)
- [ ] All deliverable files accessible
- [ ] PRESENTATION-GUIDE.md open in editor

### NOT ALLOWED
- ❌ PowerPoint slides
- ❌ Pre-made presentation videos
- ❌ Extra handouts beyond what you submitted
- ❌ Your phone (put away)
- ❌ Notes beyond one sheet

---

## 📱 WHAT TO EXPECT - EXACT TIMELINE

### 10:30-10:40 (10 minutes) - YOUR PRESENTATION
You present while they listen. Show your work.

### 10:40-10:55 (15 minutes) - THEIR QUESTIONS
They ask 3-4 questions. Mix of theory and practical.

Examples:
- "Explain your unit testing approach"
- "Run the tests and show me they pass"
- "What is regression testing?"
- "Show me a parameterized test"
- "How did you design your black-box tests?"

### 10:55-11:00 (5 minutes) - GRADING
They discuss and assign grade. They tell you immediately.

### POSSIBLE GRADES
- 12 (Excellent - 90-100%)
- 10 (Very Good - 80-89%)
- 7 (Good - 70-79%)
- 4 (Fair - 60-69%)
- 02 (Failing - <60%)

---

## ✅ FINAL VERIFICATION CHECKLIST

### Application
- [ ] Runs on localhost:8080
- [ ] API endpoints respond
- [ ] Swagger documentation accessible
- [ ] Database connections work

### Tests
- [ ] 201 unit tests run and pass
- [ ] Postman collection importable
- [ ] E2E tests runnable
- [ ] Performance test script ready

### Documentation
- [ ] SRS.md present and clear
- [ ] Review Report readable
- [ ] Black-box design shows 67 test cases
- [ ] Risk assessment shows 3 phases
- [ ] CI/CD YAML file viewable
- [ ] Performance report accessible

### Knowledge
- [ ] Can explain your architecture
- [ ] Can explain testing strategy
- [ ] Can run tests on command
- [ ] Know answer to 10+ theoretical questions
- [ ] Can demonstrate code changes
- [ ] Know your project strengths and limitations

---

## 💡 QUICK TIPS FOR SUCCESS

### PRESENTATION
1. **Speak clearly** - Pronounce words properly
2. **Show, don't tell** - Point at code and output
3. **Be confident** - You know this stuff
4. **Keep pace** - Not too fast, not too slow
5. **Bridge gaps** - "Let me show you why..."

### DURING QUESTIONS
1. **Listen carefully** - Make sure you understand
2. **Think before speaking** - 2 seconds is OK
3. **Be honest** - "I don't know" is better than wrong answer
4. **Ask for clarification** - "Do you mean...?"
5. **Connect to project** - Always relate back

### IF THINGS GO WRONG
1. **Laptop crashes** → Stay calm, restart
2. **API won't start** → Restart docker and app
3. **Test fails** → Read error, debug, explain
4. **You forget something** → Go back to slides/code
5. **You don't know answer** → Make educated guess

---

## 🎯 SUCCESS CRITERIA

You'll be graded on:

### Content (40%)
- [ ] Know your project well
- [ ] Understand testing concepts
- [ ] Can explain decisions
- [ ] Show evidence (code, tests, reports)

### Delivery (30%)
- [ ] Clear communication
- [ ] Good pace
- [ ] Professional demeanor
- [ ] Handles questions well

### Technical (30%)
- [ ] Tests actually pass
- [ ] Application actually runs
- [ ] Code shows good practices
- [ ] Deliverables complete

**You've likely covered all of these. Just demonstrate it calmly.**

---

## 🚀 YOU'VE GOT THIS!

**Remember**:
- ✅ 201 tests passing = solid foundation
- ✅ 67 black-box test cases = comprehensive design
- ✅ 9 complete deliverables = all work done
- ✅ You studied this = you know it
- ✅ Examiners want you to succeed = they're on your side

**What to do now**:
1. Read PRESENTATION-GUIDE.md
2. Practice your 10-min presentation
3. Read THEORETICAL-QA-GUIDE.md and pick questions to practice
4. Read PRACTICAL-QUESTIONS-GUIDE.md to know what they might ask
5. Run the verification script tonight
6. Sleep well
7. Come fresh in the morning

**The exam is designed to let you show what you know. It's not a trap.**

You built a professional-grade testing project. Just show them what you did and why.

---

## 📞 QUICK REFERENCE - MOST IMPORTANT COMMANDS

Keep these in your head for tomorrow:

```bash
# Run all tests
./mvnw clean test

# Run specific test
./mvnw test -Dtest=GuestAPITest

# Generate coverage
./mvnw jacoco:report
# Open: target/site/jacoco/index.html

# Start API
./mvnw spring-boot:run

# Check test files
ls -la src/test/java/

# Check deliverables
ls -la Exam-Deliverables/

# Check docker
docker-compose ps
```

---

**Good luck, Magnus! You're ready! 💪**

Go show them what you built.

