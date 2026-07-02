# ============================================================
# LeetCode Daily — clean setup
# Creates ONE Task Scheduler "At Logon" trigger
# Removes all old triggers (Startup shortcut, old one-shot tasks)
# ============================================================
Write-Host "=== LeetCode Daily Setup ===" -ForegroundColor Cyan

# ---- 1. Remove old Startup folder shortcut ----
$startupFolder = [Environment]::GetFolderPath('Startup')
$shortcutPath = Join-Path $startupFolder "LeetCode_Daily.lnk"
if (Test-Path $shortcutPath) {
    Remove-Item $shortcutPath -Force
    Write-Host "  Removed: Startup shortcut" -ForegroundColor Yellow
} else {
    Write-Host "  No Startup shortcut found" -ForegroundColor Green
}

# ---- 2. Remove ALL old LeetCode scheduled tasks ----
Write-Host "  Cleaning old scheduled tasks..."
$xml = schtasks /query /fo csv /v 2>$null
if ($xml) {
    $lines = $xml -split "`n"
    foreach ($line in $lines) {
        if ($line -match '^"\\?(LeetCode_.*?)"') {
            $taskName = $Matches[1]
            schtasks /delete /tn $taskName /f 2>$null
            Write-Host "    Deleted: $taskName" -ForegroundColor Yellow
        }
        if ($line -match '^"\\?(LeetCode_Daily).*?"') {
            $taskName = $Matches[1]
            schtasks /delete /tn $taskName /f 2>$null
            Write-Host "    Deleted: $taskName" -ForegroundColor Yellow
        }
    }
}

# ---- 3. Create new logon-triggered task ----
$ps1Path = "D:\LeetCode\daily_leetcode.ps1"
$taskName = "LeetCode_Daily"

Write-Host "  Creating logon trigger task..."
# /sc onlogon = trigger when current user logs in
# /delay 0002:00 = wait 2 min after logon (let desktop settle)
# /it = interactive (only runs when user is logged in)
$createResult = schtasks /create `
    /tn $taskName `
    /tr "powershell.exe -ExecutionPolicy Bypass -WindowStyle Hidden -File `"$ps1Path`"" `
    /sc onlogon `
    /delay 0002:00 `
    /it `
    /f 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "  Created: LeetCode_Daily (At Logon, 2min delay)" -ForegroundColor Green
} else {
    Write-Host "  ERROR creating task: $createResult" -ForegroundColor Red
    exit 1
}

# ---- 4. Verify ----
Write-Host ""
$verify = schtasks /query /tn $taskName /fo list 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "=== Setup Complete ===" -ForegroundColor Green
    Write-Host ""
    Write-Host "How it works:"
    Write-Host "  1. You log into Windows"
    Write-Host "  2. 2 min later, the script runs hidden"
    Write-Host "  3. It checks: already committed today? Then skip"
    Write-Host "  4. If not: random 0-2h delay, then fetch → solve → submit → push"
    Write-Host ""
    Write-Host "Test it: schtasks /run /tn LeetCode_Daily" -ForegroundColor DarkGray
} else {
    Write-Host "Verification failed: $verify" -ForegroundColor Red
    exit 1
}
