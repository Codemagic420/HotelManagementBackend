$token = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc4MDI0Mjk1MywiZXhwIjoxNzgwMzI5MzUzfQ.c8vMH_pl-nUEdj5sOi678FTdMtlfMU6-OASwGZNA7GV_HGL_i2MonTZznmlYbCqTVeumpRlP0HhoCw5kPCkd0A'
try {
  $headers = @{ Authorization = "Bearer $token" }
  $r = Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/migrate' -Headers $headers -UseBasicParsing -ErrorAction Stop
  Write-Output 'MIGRATE RESPONSE:'
  $r | ConvertTo-Json -Depth 5 | Write-Output
} catch {
  Write-Output 'MIGRATE ERROR:'
  Write-Output $_.Exception.Message
}
