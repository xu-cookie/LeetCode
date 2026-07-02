$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$logFile = Join-Path $scriptDir "leetcode_daily.log"
$ts = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

"$ts - === Daily Scheduler Start ===" | Out-File -Append -FilePath $logFile -Encoding UTF8

# Generate random time 8:00-20:00 (must be in the future)
$now = Get-Date
$hour = Get-Random -Minimum 8 -Maximum 21
$min  = Get-Random -Minimum 0 -Maximum 60
if ($min -eq 0)  { $min = Get-Random -Minimum 1  -Maximum 30 }
elseif ($min -eq 30) { $min = Get-Random -Minimum 31 -Maximum 60 }

$targetTime = Get-Date -Hour $hour -Minute $min -Second 0
if ($targetTime -le $now.AddMinutes(2)) {
    # Time already passed or too soon - schedule 3 minutes from now
    $targetTime = $now.AddMinutes(3)
}
$timeStr = $targetTime.ToString("HH:mm")
$dateStr = Get-Date -Format "yyyyMMdd"
$taskName = "LeetCode_$dateStr"

"$ts - Today: $dateStr, Target time: $timeStr" | Out-File -Append -FilePath $logFile -Encoding UTF8

# Create one-time task
schtasks /delete /tn $taskName /f 2>$null
$runScript = Join-Path $scriptDir "run_leetcode.ps1"
$result = schtasks /create /tn $taskName /tr "powershell -ExecutionPolicy Bypass -File `"$runScript`"" /sc once /st $timeStr /f 2>&1
"$ts - schtasks result: $result" | Out-File -Append -FilePath $logFile -Encoding UTF8
"$ts - Task $taskName created @ $timeStr" | Out-File -Append -FilePath $logFile -Encoding UTF8

# Cleanup old tasks (>30 days)
$cutoff = (Get-Date).AddDays(-30)
$allTasks = schtasks /query /fo csv /v 2>$null | ConvertFrom-Csv
if ($allTasks) {
    foreach ($task in $allTasks) {
        $tn = $task.TaskName
        if ($tn -match "^LeetCode_(\d{8})$") {
            try {
                $d = [datetime]::ParseExact($Matches[1], "yyyyMMdd", $null)
                if ($d -lt $cutoff) {
                    schtasks /delete /tn $tn /f 2>$null
                    "$ts - Cleaned: $tn" | Out-File -Append -FilePath $logFile -Encoding UTF8
                }
            } catch {}
        }
    }
}
"$ts - === Daily Scheduler End ===" | Out-File -Append -FilePath $logFile -Encoding UTF8
