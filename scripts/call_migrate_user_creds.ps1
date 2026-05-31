$u='97fa6212'
$p='8lBHyzEMHOmqcL3BsENpPG94Uuxq62eKkkO86oTQV5U'
$pair = $u + ':' + $p
$b = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($pair))
$h1 = "Authorization: Basic $b"
Write-Output "--- TRY BASIC ($u) ---"
& curl.exe -i -H $h1 -X POST http://localhost:8080/api/migrate 2>&1 | Out-String | Write-Output
Write-Output "--- TRY BEARER ---"
$h2 = "Authorization: Bearer $p"
& curl.exe -i -H $h2 -X POST http://localhost:8080/api/migrate 2>&1 | Out-String | Write-Output
