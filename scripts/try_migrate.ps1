$creds = @('admin:admin123','admin:admin','admin:password','app:secret')
foreach($c in $creds){
  $b = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($c))
  $h = "Authorization: Basic $b"
  $f = "migrate_$($c -replace ':','_').txt"
  Write-Output "TRY $c"
  & curl.exe -i -H $h -X POST http://localhost:8080/api/migrate > $f 2>&1
  Write-Output '--- RESPONSE ---'
  if (Test-Path $f) { Get-Content $f -Raw } else { Write-Output '(no file)' }
  Write-Output "EXIT=$LASTEXITCODE"
  Write-Output '================'
  Start-Sleep -Milliseconds 200
}
