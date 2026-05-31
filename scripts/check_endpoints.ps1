$resp = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body (ConvertTo-Json @{ username='admin'; password='admin123' }) -ContentType 'application/json' -UseBasicParsing
$t = $resp.token
Write-Output ('TOKEN LENGTH: ' + $t.Length)
try {
  Write-Output 'CALL /api/guests'
  $g = Invoke-RestMethod -Uri 'http://localhost:8080/api/guests' -Headers @{ Authorization = 'Bearer ' + $t } -UseBasicParsing
  if ($g -is [System.Array]) { Write-Output ('Guests count: ' + $g.Length) } elseif ($g.content) { Write-Output ('Guests page size: ' + $g.content.Count) } else { Write-Output (ConvertTo-Json $g -Depth 5) }
} catch { Write-Output 'GUESTS ERROR:'; $_.Exception.Message }
try {
  Write-Output 'CALL /api/rooms'
  $r = Invoke-RestMethod -Uri 'http://localhost:8080/api/rooms' -Headers @{ Authorization = 'Bearer ' + $t } -UseBasicParsing
  if ($r -is [System.Array]) { Write-Output ('Rooms count: ' + $r.Length) } elseif ($r.content) { Write-Output ('Rooms page size: ' + $r.content.Count) } else { Write-Output (ConvertTo-Json $r -Depth 5) }
} catch { Write-Output 'ROOMS ERROR:'; $_.Exception.Message }
