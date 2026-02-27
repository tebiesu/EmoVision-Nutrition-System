$ErrorActionPreference = "Stop"

$backendProcess = $null
$frontendProcess = $null

function Wait-HttpReady {
    param(
        [string]$Url,
        [int]$MaxSeconds = 90
    )
    $start = Get-Date
    while (((Get-Date) - $start).TotalSeconds -lt $MaxSeconds) {
        try {
            $resp = Invoke-WebRequest -UseBasicParsing -Uri $Url -TimeoutSec 3
            if ($resp.StatusCode -ge 200 -and $resp.StatusCode -lt 500) {
                return
            }
        } catch {
        }
        Start-Sleep -Milliseconds 800
    }
    throw "Timeout waiting for: $Url"
}

try {
    Write-Host "[e2e] starting backend..."
    $backendProcess = Start-Process -FilePath "cmd.exe" `
        -ArgumentList "/c", "cd /d ..\backend && run-local.cmd" `
        -PassThru -WindowStyle Hidden

    Wait-HttpReady -Url "http://127.0.0.1:38080/api/v1/health" -MaxSeconds 120

    Write-Host "[e2e] starting frontend..."
    $frontendProcess = Start-Process -FilePath "cmd.exe" `
        -ArgumentList "/c", "set VITE_API_PROXY=http://127.0.0.1:38080&& npm run dev -- --host 127.0.0.1 --port 43173" `
        -PassThru -WindowStyle Hidden

    Wait-HttpReady -Url "http://127.0.0.1:43173/login" -MaxSeconds 120

    Write-Host "[e2e] running playwright..."
    & npx playwright test --config=playwright.config.js
    exit $LASTEXITCODE
}
finally {
    if ($frontendProcess -and !$frontendProcess.HasExited) {
        Stop-Process -Id $frontendProcess.Id -Force -ErrorAction SilentlyContinue
    }
    if ($backendProcess -and !$backendProcess.HasExited) {
        Stop-Process -Id $backendProcess.Id -Force -ErrorAction SilentlyContinue
    }
}
