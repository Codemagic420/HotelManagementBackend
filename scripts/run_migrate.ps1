$resp = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body (ConvertTo-Json @{ username='admin'; password='admin123' }) -ContentType 'application/json' -UseBasicParsing
Write-Output 'LOGIN RESPONSE:'
$resp | ConvertTo-Json -Depth 5
$t = $resp.token
Write-Output ('TOKEN LENGTH: ' + $t.Length)
try {
  Write-Output 'POST /api/migrate ->'
  Invoke-RestMethod -Uri 'http://localhost:8080/api/migrate' -Method Post -Headers @{ Authorization = 'Bearer ' + $t } -UseBasicParsing -TimeoutSec 120 | ConvertTo-Json -Depth 5 | Write-Output
} catch {
  Write-Output 'MIGRATE ERROR:'
  Write-Output $_.Exception.Message
}
try {
  Write-Output 'DIAGNOSTICS AFTER MIGRATE:'
  Invoke-RestMethod -Uri 'http://localhost:8080/api/neo4j/diagnostics/status' -Headers @{ Authorization = 'Bearer ' + $t } -UseBasicParsing | ConvertTo-Json -Depth 5 | Write-Output
} catch {
  Write-Output 'DIAG ERROR:'
  Write-Output $_.Exception.Message
}
