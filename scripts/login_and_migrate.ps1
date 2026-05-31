$u='97fa6212'
$p='8lBHyzEMHOmqcL3BsENpPG94Uuxq62eKkkO86oTQV5U'
try {
  $body = @{ username = $u; password = $p } | ConvertTo-Json
  $resp = Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/auth/login' -Body $body -ContentType 'application/json' -ErrorAction Stop
  Write-Output 'LOGIN RESPONSE:'
  $resp | ConvertTo-Json -Depth 5 | Write-Output
  $token = $resp.token
  if (-not $token) { Write-Output 'No token in response'; exit 1 }
  Write-Output 'Calling /api/migrate with Bearer token...'
  $h = "Authorization: Bearer $token"
  & curl.exe -i -H $h -X POST http://localhost:8080/api/migrate 2>&1 | Out-String | Write-Output
} catch {
  Write-Output 'ERROR during login or migrate:'
  Write-Output $_.Exception.Message
}
