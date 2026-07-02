# JWT Expiry Checker
# Shows popup only when running interactively (user present)
# Always writes status to log file
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$authFile = Join-Path $scriptDir "leetcode_auth.json"
$logFile = Join-Path $scriptDir "leetcode_daily.log"

if (-not (Test-Path $authFile)) { exit 0 }

$auth = Get-Content $authFile -Raw | ConvertFrom-Json
$jwt = $auth.cookie

if (-not $jwt -or $jwt -eq "...") { exit 0 }

# Decode JWT payload
try {
    $parts = $jwt.Split('.')
    if ($parts.Count -lt 2) { exit 0 }

    $payload = $parts[1]
    $payload = $payload.Replace('-', '+').Replace('_', '/')
    while ($payload.Length % 4 -ne 0) { $payload += '=' }

    $bytes = [Convert]::FromBase64String($payload)
    $json = [System.Text.Encoding]::UTF8.GetString($bytes)
    $data = $json | ConvertFrom-Json
    $expiryUnix = [long]$data.expired_time_
    if (-not $expiryUnix) { exit 0 }
} catch {
    exit 0
}

$expiryDate = [DateTimeOffset]::FromUnixTimeSeconds($expiryUnix).DateTime.ToLocalTime()
$now = Get-Date
$remainingDays = [math]::Floor(($expiryDate - $now).TotalDays)
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

# Check if running interactively (user is logged in and session is active)
$interactive = [Environment]::UserInteractive

if ($remainingDays -le 0) {
    "$timestamp - [JWT] EXPIRED! Cookie has expired. Please update leetcode_auth.json" | Out-File -Append -FilePath $logFile -Encoding UTF8
    if ($interactive) {
        Add-Type -AssemblyName System.Windows.Forms
        $msg = "Your LeetCode cookie has EXPIRED. Please update: D:\LeetCode\.claude\leetcode_auth.json`n`nHow to get a new cookie:`n1. Open leetcode.cn in browser and login`n2. F12 -> Application -> Cookies -> LEETCODE_SESSION`n3. Copy the value to the JSON file"
        [System.Windows.Forms.MessageBox]::Show($msg, "LeetCode - Cookie Expired!", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Error)
    }

} elseif ($remainingDays -le 3) {
    "$timestamp - [JWT] Expires in $remainingDays day(s) ($($expiryDate.ToString('yyyy-MM-dd')))" | Out-File -Append -FilePath $logFile -Encoding UTF8
    if ($interactive) {
        Add-Type -AssemblyName System.Windows.Forms
        $msg = "Your LeetCode cookie will expire on $($expiryDate.ToString('yyyy-MM-dd')).`nPlease update: D:\LeetCode\.claude\leetcode_auth.json`n`nHow to get a new cookie:`n1. Open leetcode.cn in browser and login`n2. F12 -> Application -> Cookies -> LEETCODE_SESSION`n3. Copy the value to the JSON file"
        [System.Windows.Forms.MessageBox]::Show($msg, "LeetCode - Cookie Expiring in $remainingDays day(s)", [System.Windows.Forms.MessageBoxButtons]::OK, [System.Windows.Forms.MessageBoxIcon]::Warning)
    }

} else {
    "$timestamp - [JWT] OK - expires in $remainingDays days ($($expiryDate.ToString('yyyy-MM-dd')))" | Out-File -Append -FilePath $logFile -Encoding UTF8
}
