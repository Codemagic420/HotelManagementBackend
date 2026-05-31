$auth = "admin:admin123"
$b = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($auth))
$h = "Authorization: Basic $b"
Write-Output '--- Neo4j diagnostics ---'
try{
  & curl.exe -s -H 'Accept: application/json' http://localhost:8080/api/neo4j/diagnostics/status | ConvertFrom-Json | ConvertTo-Json -Depth 5 | Write-Output
} catch { Write-Output 'Failed to call diagnostics'; $_.Exception.Message }
Write-Output '--- GET /api/guests (first page) ---'
try{
  & curl.exe -s -H "Authorization: Basic $b" http://localhost:8080/api/guests | ConvertFrom-Json | ConvertTo-Json -Depth 5 | Write-Output
} catch { Write-Output 'Failed to call /api/guests'; $_.Exception.Message }
Write-Output '--- GET /api/rooms (first page) ---'
try{
  & curl.exe -s -H "Authorization: Basic $b" http://localhost:8080/api/rooms | ConvertFrom-Json | ConvertTo-Json -Depth 5 | Write-Output
} catch { Write-Output 'Failed to call /api/rooms'; $_.Exception.Message }
