for ($i=0; $i -lt 30; $i++) {
    try {
        $r = Invoke-RestMethod -Uri 'http://localhost:8080/api/neo4j/diagnostics/status' -UseBasicParsing
        $r | ConvertTo-Json -Compress
        exit 0
    } catch {
        Start-Sleep -Seconds 2
    }
}
Write-Output 'NO_RESPONSE'
exit 1
