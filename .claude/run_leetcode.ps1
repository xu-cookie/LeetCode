# ============================================================
# LeetCode Daily Runner
# Triggered by Windows Task Scheduler at a random time
# ============================================================
Set-Location D:\LeetCode

$logFile = "D:\LeetCode\.claude\leetcode_daily.log"
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

"$timestamp - Starting daily LeetCode task" | Out-File -Append -FilePath $logFile -Encoding UTF8
"$timestamp - Working dir: $PWD" | Out-File -Append -FilePath $logFile -Encoding UTF8

# Ensure npm global path is available
$npmPath = Join-Path $env:APPDATA "npm"
if ($env:PATH -notlike "*$npmPath*") {
    $env:PATH = "$npmPath;$env:PATH"
}

# Read the prompt from external file (avoids PowerShell encoding issues with Chinese)
$promptFile = "D:\LeetCode\.claude\leetcode_prompt.txt"
if (-not (Test-Path $promptFile)) {
    "$timestamp - ERROR: Prompt file not found: $promptFile" | Out-File -Append -FilePath $logFile -Encoding UTF8
    exit 1
}

try {
    # Step 0: Fetch daily challenge data first
    $fetchScript = "D:\LeetCode\.claude\fetch_daily.py"
    $dailyResultFile = "D:\LeetCode\.claude\daily_result.json"
    "$timestamp - Running fetch_daily.py to get daily challenge..." | Out-File -Append -FilePath $logFile -Encoding UTF8
    $fetchOutput = python $fetchScript 2>&1
    "$timestamp - Fetch output: $fetchOutput" | Out-File -Append -FilePath $logFile -Encoding UTF8

    $prompt = Get-Content $promptFile -Raw -Encoding UTF8
    "$timestamp - Prompt loaded, running claude..." | Out-File -Append -FilePath $logFile -Encoding UTF8

    # Run Claude Code (non-interactive mode)
    claude -p $prompt

    $exitCode = $LASTEXITCODE
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    "$timestamp - Task completed, exit code: $exitCode" | Out-File -Append -FilePath $logFile -Encoding UTF8
} catch {
    $timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    "$timestamp - ERROR: $_" | Out-File -Append -FilePath $logFile -Encoding UTF8
    exit 1
}
