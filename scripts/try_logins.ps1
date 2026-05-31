$creds = @(
  @{ u='admin'; p='admin123' },
  @{ u='admin'; p='admin' },
  @{ u='admin'; p='password' },
  @{ u='app'; p='secret' }
)
foreach($c in $creds){
  Write-Output "TRY LOGIN: $($c.u):$($c.p)"
  try{
    $body = @{ username = $c.u; password = $c.p } | ConvertTo-Json
    $resp = Invoke-RestMethod -Method Post -Uri 'http://localhost:8080/api/auth/login' -Body $body -ContentType 'application/json' -ErrorAction Stop
    Write-Output 'SUCCESS:'
    $resp | ConvertTo-Json -Depth 5 | Write-Output
  } catch {
    Write-Output 'FAILED:'
    Write-Output $_.Exception.Message
  }
  Write-Output '---'
  Start-Sleep -Milliseconds 200
}
