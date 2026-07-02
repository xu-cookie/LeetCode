# LeetCode Daily Auto Setup
# No admin required - uses Windows Startup folder

$ErrorActionPreference = "Stop"

$taskName = "LeetCodeDaily"
$scriptPath = "D:\LeetCode\daily_leetcode.ps1"
$startupDir = [Environment]::GetFolderPath("Startup")
$vbsPath = Join-Path $startupDir "LeetCodeDaily.vbs"

# Remove old VBS if exists
if (Test-Path $vbsPath) {
    Remove-Item $vbsPath -Force
    Write-Host "Removed old startup script"
}

# Create VBS launcher - runs PS script silently, no window
$vbsContent = @"
Set WshShell = CreateObject("WScript.Shell")
WshShell.Run "powershell.exe -WindowStyle Hidden -ExecutionPolicy Bypass -File `"$scriptPath`"", 0, False
Set WshShell = Nothing
"@

Set-Content -Path $vbsPath -Value $vbsContent
Write-Host "Startup script created: $vbsPath"

# Also remove old Task Scheduler task if exists (ignore errors)
$null = schtasks /delete /tn $taskName /f 2>&1

Write-Host ""
Write-Host "=== SETUP COMPLETE ==="
Write-Host "Method   : Windows Startup folder"
Write-Host "VBS file : $vbsPath"
Write-Host "PS script: $scriptPath"
Write-Host ""
Write-Host "How it works:"
Write-Host "  1. You log in to Windows"
Write-Host "  2. Windows runs LeetCodeDaily.vbs (hidden)"
Write-Host "  3. VBS launches daily_leetcode.ps1 (hidden)"
Write-Host "  4. Script waits random 0-30 min, then does the work"
Write-Host ""
Write-Host "Manual test:"
Write-Host "  powershell -ExecutionPolicy Bypass -File `"$scriptPath`""
Write-Host ""
Write-Host "To disable: delete $vbsPath"
