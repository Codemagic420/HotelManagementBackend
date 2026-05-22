# k6 Load Test for HotelManagementBackend

This repository includes a basic `k6` load test script at `scripts/load-test.js`.

Purpose
- Provide a lightweight smoke/stress test for core read endpoints: `/api/rooms`, `/v3/api-docs`, and `/actuator/health`.

Install k6 (Windows)
- Using Chocolatey (requires admin PowerShell):

  choco install k6

- Using Scoop:

  scoop install k6

- Using winget:

  winget install k6

Run the test (PowerShell)

```powershell
# set target base URL (defaults to http://localhost:8080)
$env:BASE_URL = 'http://localhost:8080'
# optional: override vus and duration
$env:K6_VUS = '50'
$env:K6_DURATION = '4m'

# run
k6 run scripts/load-test.js
```

Save results to JSON

```powershell
k6 run --out json=artifacts/perf/result.json scripts/load-test.js
```

Notes
- The script defaults to safe read-only endpoints to avoid mutating the test database.
- Adjust `K6_VUS` and `K6_DURATION` to match desired load profiles.
- CI integration sample can be added using the `loadimpact/k6` Docker image.
