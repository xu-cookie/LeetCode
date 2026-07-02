$ErrorActionPreference = "Continue"
$projectDir = "D:\LeetCode"
Set-Location $projectDir

# ============ Logging ============
$logDir = "$projectDir\.claude"
if (-not (Test-Path $logDir)) { New-Item -ItemType Directory -Path $logDir -Force | Out-Null }
$logFile = "$logDir\daily_log.txt"

function Write-Log($msg) {
    $ts = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $line = "[$ts] $msg"
    Write-Host $line
    try { Add-Content -Path $logFile -Value $line -Encoding UTF8 } catch {}
}

Write-Log "=== Daily task started ==="

# ============ Lock file (prevent concurrent runs) ============
$lockFile = "$logDir\daily.lock"
if (Test-Path $lockFile) {
    try {
        $lockAge = (Get-Date) - (Get-Item $lockFile).LastWriteTime
        if ($lockAge.TotalMinutes -lt 60) {
            Write-Log "SKIP: Already running (lock age: $([math]::Round($lockAge.TotalMinutes))min)"
            exit 0
        }
        Write-Log "WARNING: Stale lock (${lockAge}h), overriding"
        Remove-Item $lockFile -Force
    } catch {
        Write-Log "SKIP: Cannot read lock, exiting"
        exit 0
    }
}
"" | Out-File -FilePath $lockFile -Encoding UTF8

# Cleanup lock on exit
$cleanupLock = {
    Remove-Item $lockFile -Force -ErrorAction SilentlyContinue
}

# ============ Check if today already committed ============
$today = Get-Date -Format "yyyy-MM-dd"
try {
    $existing = git log --since="$today 00:00:00" --until="$today 23:59:59" --oneline 2>$null
    if ($existing) {
        Write-Log "SKIP: Already committed today -> $existing"
        & $cleanupLock
        exit 0
    }
} catch {
    Write-Log "WARNING: git check failed, continuing anyway"
}

# ============ Random delay (spread server load) ============
$delaySeconds = Get-Random -Minimum 0 -Maximum 7200  # 0-2 hours
if ($delaySeconds -gt 0) {
    Write-Log "Random delay: ${delaySeconds}s"
    Start-Sleep -Seconds $delaySeconds
}

# ============ Git pull ============
Write-Log "Pulling latest from remote..."
try {
    git pull origin master -q 2>&1 | Out-Null
    Write-Log "Pull OK (exit: $LASTEXITCODE)"
} catch {
    Write-Log "WARNING: git pull error: $_"
}

# ============ Fetch daily problem ============
$fetchScript = "$projectDir\.claude\fetch_daily.py"
$problemJson = "$projectDir\.claude\today_problem.json"

if (Test-Path $fetchScript) {
    Write-Log "Fetching daily problem via Python..."
    try {
        $fetchResult = python $fetchScript 2>&1
        $fetchExit = $LASTEXITCODE
        Write-Log "Fetch exit: $fetchExit"
        if ($fetchExit -ne 0) {
            Write-Log "ERROR: Fetch failed: $fetchResult"
            & $cleanupLock
            exit 1
        }
        if (Test-Path $problemJson) {
            $problemData = Get-Content $problemJson -Raw -Encoding UTF8 | ConvertFrom-Json
            Write-Log "Today: [$($problemData.questionFrontendId)] $($problemData.translatedTitle) ($($problemData.difficulty))"
        }
        Write-Log "Fetch OK"
    } catch {
        Write-Log "ERROR: Fetch exception: $_"
        & $cleanupLock
        exit 1
    }
} else {
    Write-Log "ERROR: fetch_daily.py not found"
    & $cleanupLock
    exit 1
}

# ============ Run Claude Code ============
$promptFile = "$projectDir\.claude\leetcode_prompt.txt"
if (-not (Test-Path $promptFile)) {
    Write-Log "ERROR: Prompt file not found"
    & $cleanupLock
    exit 1
}

try {
    $claudePrompt = Get-Content $promptFile -Raw -Encoding UTF8
    Write-Log "Starting Claude Code..."

    $result = claude -p $claudePrompt `
        --permission-mode bypassPermissions `
        --dangerously-skip-permissions `
        --output-format text `
        2>&1

    $exitCode = $LASTEXITCODE
    Write-Log "Claude exit code: $exitCode"

    if ($exitCode -eq 0) {
        Write-Log "SUCCESS"
        try {
            git push origin master -q 2>&1 | Out-Null
            if ($LASTEXITCODE -eq 0) {
                Write-Log "PUSH OK"
            } else {
                Write-Log "WARNING: git push exit code: $LASTEXITCODE"
            }
        } catch {
            Write-Log "WARNING: git push error: $_"
        }
    } else {
        Write-Log "FAILED (exit: $exitCode)"
        Write-Log "Output (last 500 chars): $($result.Substring([Math]::Max(0, $result.Length - 500)))"
    }
} catch {
    Write-Log "ERROR: Claude execution exception: $_"
    & $cleanupLock
    exit 1
}

& $cleanupLock
Write-Log "=== Daily task ended ==="
exit $exitCode
