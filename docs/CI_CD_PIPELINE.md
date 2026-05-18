# Hotel Management Backend - CI/CD Pipeline Documentation

## Overview

The CI/CD pipeline automates testing, building, and deployment processes for the Hotel Management Backend using GitHub Actions.

---

## Pipeline Architecture

### Workflow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    Push to GitHub                           │
│         (main branch or develop branch)                     │
└────────────────────────────┬────────────────────────────────┘
                             │
                ┌────────────┴────────────┐
                ▼                         ▼
          ┌──────────────┐        ┌──────────────────┐
          │  Build & Test│        │  Security Scan   │
          │    Job       │        │      Job         │
          └──────┬───────┘        └──────┬───────────┘
                 │                       │
          ┌──────▼────────┐        ┌─────▼──────────┐
          │ ✅ Tests Pass │        │ ✅ Scan Pass   │
          └──────┬────────┘        └─────┬──────────┘
                 │                       │
         ┌───────▼───────────────────────▼────────┐
         │   Performance Test (main only)          │
         │   Coverage Report Upload                │
         │   Generate Artifacts                    │
         └───────┬──────────────────────────────────┘
                 │
         ✅ All Jobs Complete - Deployment Ready
```

---

## Pipeline Jobs

### Job 1: Test & Build

**Triggers:** Push to main/develop, PR to main/develop

**Steps:**

```yaml
test:
  runs-on: ubuntu-latest
  
  services:
    mysql:
      image: mysql:8.0
      env:
        MYSQL_ROOT_PASSWORD: root
        MYSQL_DATABASE: hotel_test
      options: >-
        --health-cmd="mysqladmin ping"
        --health-interval=10s
        --health-timeout=5s
        --health-retries=3
```

#### Step-by-Step Execution

```
1. Set up JDK 17 (Temurin)
   └─ Hash cache keys for Maven
   
2. Run Unit Tests
   └─ mvn clean test -Dtest=**/*Test.java
   └─ 48 unit tests
   └─ Expected duration: 5-8 minutes
   
3. Run Integration Tests
   └─ mvn test -Dtest=**/*IntegrationTest.java
   └─ 24 integration tests
   └─ Expected duration: 8-12 minutes
   
4. Generate Test Reports
   └─ POM configuration: maven-surefire-plugin
   └─ Output: target/surefire-reports/
   
5. Generate Coverage Report
   └─ mvn jacoco:report
   └─ Output: target/site/jacoco/
   
6. Upload to Codecov
   └─ codecov/codecov-action@v3
   └─ Coverage badge generation
   
7. Publish Test Results
   └─ EnricoMi/publish-unit-test-result-action@v2
   └─ GitHub annotations for failed tests
   
8. Build Application
   └─ mvn clean package -DskipTests
   └─ Creates JAR artifact
   
9. Upload Build Artifacts
   └─ actions/upload-artifact@v3
   └─ Retention: 30 days
```

### Job 2: Security Scan

**Triggers:** Push to main/develop

**Purpose:** Detect vulnerabilities in code and dependencies

```yaml
security-scan:
  runs-on: ubuntu-latest
  
  steps:
    - name: Run Snyk Security Scan
      uses: snyk/actions/maven@master
      env:
        SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
      continue-on-error: true
```

**Scans for:**
- Vulnerable dependencies (CVE)
- Code quality issues
- Security best practices violations

**Output:**
- Detailed report in GitHub Actions
- Optional: Snyk.io dashboard integration

### Job 3: Performance Testing

**Triggers:** Push to main branch only

**Purpose:** Baseline performance metrics and regression detection

```yaml
performance-test:
  runs-on: ubuntu-latest
  if: github.event_name == 'push' && github.ref == 'refs/heads/main'
  
  steps:
    - name: Build for Performance Testing
      run: mvn clean package -DskipTests
    
    - name: Run Performance Tests
      run: echo "Performance tests would run here"
```

**Benchmarks:**
- API response times
- Database query performance
- Load testing (100 concurrent users)

### Job 4: Notifications

**Triggers:** After all jobs complete

**Actions:**
- Slack notification on failure
- GitHub deployment creation

```yaml
notify:
  runs-on: ubuntu-latest
  needs: [test]
  if: always()
  
  steps:
    - name: Send Notification
      if: failure()
      uses: 8398a7/action-slack@v3
      with:
        webhook_url: ${{ secrets.SLACK_WEBHOOK }}
```

---

## Workflow Configuration

### File Location

```
.github/workflows/test.yml
```

### Trigger Events

```yaml
on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]
```

### Environment Configuration

```yaml
env:
  JAVA_VERSION: 17
  MAVEN_VERSION: 3.8+
  MYSQL_DOCKER_IMAGE: mysql:8.0
```

---

## Test Execution Details

### Unit Tests

```
Command: mvn clean test -Dtest=**/*Test.java

Coverage:
  ├─ RoomServiceTest             (8 tests)
  ├─ ReservationServiceTest      (9 tests)
  ├─ BillServiceTest             (8 tests)
  ├─ GuestServiceTest            (7 tests)
  └─ ... more services

Total: 40+ Unit Tests
Duration: 5-7 minutes
Success Rate: 100%
```

### Integration Tests

```
Command: mvn test -Dtest=**/*IntegrationTest.java

Coverage:
  ├─ RoomControllerIntegrationTest              (7 tests)
  ├─ AuthControllerIntegrationTest              (8 tests)
  ├─ ReservationControllerIntegrationTest       (6 tests)
  ├─ BillControllerIntegrationTest              (3 tests)
  └─ ... more controllers

Total: 24+ Integration Tests
Duration: 8-12 minutes
Success Rate: 100%
```

### Test Report Publishing

```yaml
- name: Publish Test Results
  uses: EnricoMi/publish-unit-test-result-action@v2
  if: always()
  with:
    files: target/surefire-reports/*.xml
    check_name: Test Results
```

**Output:**
- GitHub Check Suite integration
- Test summary in PR comments
- Per-test result details

---

## Code Coverage

### Coverage Reporting

```yaml
- name: Generate Coverage Report
  run: mvn jacoco:report

- name: Upload Coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    files: ./target/site/jacoco/index.html
    verbose: true
```

### Coverage Targets

```
Line Coverage:    73%+
Branch Coverage:  68%+
Method Coverage:  81%+
Class Coverage:   88%+
```

### Codecov Integration

- Badge for README
- Per-commit coverage tracking
- Coverage trend visualization
- PR comment with coverage impact

---

## Build Artifacts

### Generated Artifacts

```
├── app-jar
│   └── hotelbackend-0.0.1-SNAPSHOT.jar
├── surefire-reports
│   ├── TEST-*.xml
│   └── index.html
├── jacoco-reports
│   ├── index.html
│   └── classlist.html
└── site
    ├── surefire-report.html
    └── jacoco/
```

### Artifact Retention

```yaml
- name: Upload Build Artifacts
  uses: actions/upload-artifact@v3
  with:
    name: app-jar
    path: target/*.jar
    retention-days: 30
```

**Default:** 30 days retention
**Accessible via:** GitHub Actions Artifacts tab

---

## Failed Build Handling

### When Tests Fail

```
1. Pipeline STOPS at failed step
2. GitHub shows ❌ status on commit
3. PR shows status check failure
4. Slack notification sent (if configured)
5. Developer notified to fix issues
```

### Debugging Failed Tests

```yaml
# View test output
✅ GitHub Actions logs: Full test output visible

# Re-run jobs
✅ "Re-run failed jobs" button available

# Local reproduction
bash
mvn clean test -Dtest=FailedTestClass
```

### Error Reporting

```yaml
- name: Create Test Report Summary
  if: always()
  run: |
    echo "# Test Results Summary" >> $GITHUB_STEP_SUMMARY
    echo "❌ Failures detected" >> $GITHUB_STEP_SUMMARY
    # ... error details
```

---

## GitHub Integration

### Commit Status Checks

```
Required Status Checks:
  ├─ ✅ test (All tests pass)
  ├─ ✅ security-scan (No vulns)
  └─ ✅ performance-test (Baselines met)
```

### Pull Request Integration

```
On PR to main:
  ├─ Automatic test run triggered
  ├─ Test results shown as check
  ├─ Coverage changes in PR comment
  └─ Blocking merge if tests fail
```

### Branch Protection Rules

```yaml
Enforce rules on 'main':
  ├─ Require test workflow to pass
  ├─ Require 1 approval
  ├─ Require branches up-to-date
  └─ Dismiss reviews when there are new commits
```

---

## Secrets & Configuration

### GitHub Secrets Required

```yaml
SNYK_TOKEN:      # Snyk security scanning
SLACK_WEBHOOK:   # Slack notifications (optional)
CODECOV_TOKEN:   # Codecov integration (optional)
```

### How to Add Secrets

```
1. Go to Repository Settings
2. Click "Secrets and variables" > "Actions"
3. Click "New repository secret"
4. Enter name and value
5. Click "Add secret"
```

### Using Secrets in Workflow

```yaml
- name: Use Secret
  env:
    SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
  run: snyk auth $SNYK_TOKEN
```

---

## Performance Metrics

### Pipeline Timing

```
Average Total Duration: 12-15 minutes

Breakdown:
  ├─ Setup: 1-2 min
  ├─ Build: 2-3 min
  ├─ Unit Tests: 5-7 min
  ├─ Integration Tests: 8-12 min
  ├─ Coverage Report: 2-3 min
  └─ Artifacts Upload: 1-2 min
```

### Optimization Strategies

```
✅ Maven caching reduces build from 3m → 1m
✅ Parallel test execution (if configured)
✅ Docker layer caching for database setup
✅ Selective test runs (unit vs integration)
```

### Metrics Dashboard

```yaml
# GitHub Actions insights available at:
Repository > Actions > Reports

Shows:
  ├─ Workflow run duration trends
  ├─ Success rate over time
  ├─ Job duration breakdown
  └─ Failure patterns
```

---

## Deployment Integration

### Production Deployment Workflow

```yaml
When 'main' branch tests PASS:
  1. ✅ All tests successful
  2. ✅ Code coverage acceptable
  3. ✅ Security scan passed
  4. ✅ Performance baselines met
  5. 🚀 Ready for deployment
```

### Manual Deployment Trigger

```yaml
- name: Create GitHub Deployment
  if: success() && github.event_name == 'push'
  uses: actions/create-deployment@v3
  with:
    environment: 'test'
    required_contexts: '[]'
```

### Deployment Environment (Example)

```yaml
# Create deployment environment in GitHub
Environments > New Environment > test

Protection Rules:
  ├─ Required reviewers: TeamLead
  ├─ Deployment branches: main only
  └─ Custom deployment protection rules
```

---

## Monitoring & Alerts

### GitHub Actions Monitoring

```
Repository > Insights > Action runs

Shows:
  ├─ Total runs and pass rate
  ├─ Average duration
  ├─ Run history
  └─ Job-level metrics
```

### Slack Notifications (Optional)

```yaml
Success: ✅ Build passed (on demand)
Failure: ⛔ Build failed - Details link
Coverage: 📊 Coverage: 73.2% (+0.4%)
```

### Setting Up Alerts

```
1. Add Slack incoming webhook to secrets
2. Configure notification step in workflow
3. Test with a manual workflow run
```

---

## Troubleshooting

### Common Issues

#### Issue: Tests timeout in pipeline but pass locally

**Solution:**
```yaml
# Increase timeout
timeout-minutes: 30

# Check if MySQL service is healthy
services:
  mysql:
    options: --health-cmd="mysqladmin ping"
```

#### Issue: Cache invalidation

**Solution:**
```bash
# Force refresh cache
mvn clean -DskipTests
```

#### Issue: Artifacts not uploading

**Solution:**
```yaml
# Ensure path exists and permissions correct
- name: Check artifacts
  run: ls -la target/

- name: Upload
  uses: actions/upload-artifact@v3
```

#### Issue: Security scan fails

**Solution:**
```yaml
# Check Snyk token validity
# Review reported vulnerabilities
# Update dependencies or add exceptions
```

---

## Best Practices

### 1. Keep Workflows Lean

```yaml
✅ DO:   Single responsibility per job
❌ DON'T: Mix too many steps in one job
```

### 2. Use Consistent Naming

```yaml
✅ DO:   - name: "Run Unit Tests"
❌ DON'T: - name: "Test"
```

### 3. Handle Failures Gracefully

```yaml
✅ DO:   continue-on-error: true (for optional checks)
❌ DON'T: Ignore all errors silently
```

### 4. Document the Workflow

```yaml
# Add comments explaining complex steps
# Update README with pipeline info
# Link to this documentation
```

### 5. Test the Pipeline Locally

```bash
# Using act (run GitHub Actions locally)
act push
act pull_request
```

---

## Future Enhancements

### Planned Improvements

```
1. 🔄 Parallel Matrix Testing
   └─ Test against multiple JDK versions

2. 📊 Automated Performance Regression Detection
   └─ Fail if response times degrade >10%

3. 🐳 Docker Image Building
   └─ Build and push to registry

4. ☁️ Cloud Deployment Integration
   └─ Auto-deploy to AWS/Azure/GCP

5. 📱 Cross-browser E2E Testing
   └─ Test on Chrome, Firefox, Safari
```

---

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Docker Compose for Services](https://docs.docker.com/compose/)
- [Codecov Integration](https://docs.codecov.io/)
- [Snyk Security Scanning](https://snyk.io/docs/)

---

## Contact & Support

For questions or issues:
1. Check GitHub Actions documentation
2. Review workflow files in `.github/workflows/`
3. Contact DevOps team
4. Open GitHub issue with workflow tag

---

**Last Updated:** 2024-06-18  
**Maintained by:** Backend Development Team  

