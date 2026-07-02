# ============================================================
# LeetCode Daily Scheduler
# 每天 8:00 由 Windows 任务计划程序触发
# 生成 8:00-20:00 之间的随机时间，创建今天的一次性任务
# 自动清理 30 天前的旧任务
# ============================================================

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$logFile = Join-Path $scriptDir "leetcode_daily.log"
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

# ---------- 0. 检查 JWT 是否即将过期 ----------
$jwtChecker = Join-Path $scriptDir "check_jwt_expiry.ps1"
if (Test-Path $jwtChecker) {
    & powershell -ExecutionPolicy Bypass -File $jwtChecker 2>&1 | Out-File -Append -FilePath $logFile -Encoding UTF8
}

# ---------- 1. 生成随机时间 (8:00 ~ 20:00) ----------
$randomHour = Get-Random -Minimum 8 -Maximum 21    # 8~20
$randomMinute = Get-Random -Minimum 0 -Maximum 60

# 避开整点和半点，减小服务器压力
if ($randomMinute -eq 0) {
    $randomMinute = Get-Random -Minimum 1 -Maximum 30
} elseif ($randomMinute -eq 30) {
    $randomMinute = Get-Random -Minimum 31 -Maximum 60
}

$timeString = "{0:D2}:{1:D2}" -f $randomHour, $randomMinute
$dateString = Get-Date -Format "yyyyMMdd"
$taskName = "LeetCode_$dateString"

Write-Output "$timestamp - 调度今日任务: $taskName @ $timeString" | Out-File -Append -FilePath $logFile -Encoding UTF8

# ---------- 2. 创建今天的一次性任务 ----------
# 先删除可能已存在的同名任务
schtasks /delete /tn $taskName /f 2>$null

$runScript = Join-Path $scriptDir "run_leetcode.ps1"
schtasks /create `
    /tn $taskName `
    /tr "powershell -ExecutionPolicy Bypass -File `"$runScript`"" `
    /sc once `
    /st $timeString `
    /f 2>&1 | Out-File -Append -FilePath $logFile -Encoding UTF8

Write-Output "$timestamp - 任务 $taskName 已创建" | Out-File -Append -FilePath $logFile -Encoding UTF8

# ---------- 3. 清理 30 天前的旧任务 ----------
$cutoff = (Get-Date).AddDays(-30)
$allTasks = schtasks /query /fo csv /v 2>$null | ConvertFrom-Csv

foreach ($task in $allTasks) {
    $tn = $task.TaskName
    if ($tn -match '^LeetCode_(\d{8})$') {
        try {
            $taskDate = [datetime]::ParseExact($Matches[1], 'yyyyMMdd', $null)
            if ($taskDate -lt $cutoff) {
                schtasks /delete /tn $tn /f 2>$null
                Write-Output "$timestamp - 清理过期任务: $tn" | Out-File -Append -FilePath $logFile -Encoding UTF8
            }
        } catch {
            # 解析失败则跳过
        }
    }
}

Write-Output "$timestamp - 调度与清理完成" | Out-File -Append -FilePath $logFile -Encoding UTF8
