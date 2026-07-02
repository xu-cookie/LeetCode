# ============================================================
# LeetCode Daily — setup logon auto-start (no admin required)
# Uses Windows Startup folder instead of Task Scheduler
# ============================================================
Write-Host "=== LeetCode Daily Setup ===" -ForegroundColor Cyan

# Clean up old scheduled tasks (ignore errors)
schtasks /delete /tn "LeetCode_Daily_Trigger" /f 2>$null
Unregister-ScheduledTask -TaskName "LeetCode_Daily" -Confirm:$false -ErrorAction SilentlyContinue

# Find Startup folder
$startupFolder = [Environment]::GetFolderPath('Startup')
Write-Host "Startup folder: $startupFolder"

# Remove old shortcut if exists
$shortcutPath = Join-Path $startupFolder "LeetCode_Daily.lnk"
if (Test-Path $shortcutPath) {
    Remove-Item $shortcutPath -Force
    Write-Host "  Removed old shortcut"
}

# Create shortcut that launches the script silently
$WScriptShell = New-Object -ComObject WScript.Shell
$shortcut = $WScriptShell.CreateShortcut($shortcutPath)
$shortcut.TargetPath = "powershell.exe"
$shortcut.Arguments = '-ExecutionPolicy Bypass -WindowStyle Hidden -File "D:\LeetCode\daily_leetcode.ps1"'
$shortcut.WorkingDirectory = "D:\LeetCode"
$shortcut.WindowStyle = 7  # Minimized
$shortcut.Description = "LeetCode Daily Challenge Solver"
$shortcut.Save()

Write-Host "  Created: LeetCode_Daily.lnk in Startup folder" -ForegroundColor Green

# Verify
if (Test-Path $shortcutPath) {
    Write-Host ""
    Write-Host "=== Done! ===" -ForegroundColor Green
    Write-Host "The script will run automatically every time you log in."
    Write-Host "It runs hidden (no window popup), and skips if already done today."
} else {
    Write-Host "ERROR: Failed to create shortcut" -ForegroundColor Red
}
