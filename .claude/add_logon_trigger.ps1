$task = Get-ScheduledTask -TaskName "LeetCode_Daily_Trigger" -ErrorAction Stop

# Check if logon trigger already exists
$hasLogon = $false
foreach ($t in $task.Triggers) {
    if ($t.CimClass.CimClassName -eq "MSFT_TaskLogonTrigger") {
        $hasLogon = $true
        break
    }
}

if ($hasLogon) {
    Write-Host "Logon trigger already exists, nothing to do."
    exit 0
}

# Collect existing triggers into a mutable list and add logon trigger
$triggers = @()
foreach ($t in $task.Triggers) { $triggers += $t }
$triggers += New-ScheduledTaskTrigger -AtLogon

Set-ScheduledTask -TaskName "LeetCode_Daily_Trigger" -Trigger $triggers
Write-Host "Logon trigger added successfully!"
