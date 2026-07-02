# ============================================================
# LeetCode Submission Helper
# 将解答提交到 LeetCode 并等待评测结果
# 参数: -Slug "problem-slug" -QuestionId "2095" -CodeFile "path/to/Solution.java"
# ============================================================

param(
    [Parameter(Mandatory=$true)][string]$Slug,
    [Parameter(Mandatory=$true)][string]$QuestionId,
    [Parameter(Mandatory=$false)][string]$CodeFile,
    [Parameter(Mandatory=$false)][string]$ProjectDir = "D:\LeetCode"
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$authFile = Join-Path $scriptDir "leetcode_auth.json"
$logFile = Join-Path $scriptDir "leetcode_daily.log"

# ---------- 查找代码文件（避免中文路径传参乱码）----------
if ((-not $CodeFile) -or (-not (Test-Path $CodeFile -PathType Leaf))) {
    $cnDir = Join-Path $ProjectDir "leetcode\editor\cn"
    if (Test-Path $cnDir) {
        # 枚举所有 .java 文件，用 -like 匹配（避免 Filter 通配符问题）
        $found = @(Get-ChildItem -Path $cnDir -File -ErrorAction SilentlyContinue | Where-Object {
            $_.Name -like "*$QuestionId*" -and $_.Extension -eq ".java"
        })
        if ($found.Count -gt 0) {
            $CodeFile = $found[0].FullName
        }
    }
}

Write-Output "代码文件: $CodeFile"

# ---------- 读取登录凭证 ----------
if (-not (Test-Path $authFile)) {
    $msg = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') - [Submit] 错误: 找不到 $authFile"
    Write-Output $msg | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Output $msg
    exit 1
}

$auth = Get-Content $authFile -Raw | ConvertFrom-Json
$cookie = $auth.cookie
$csrfToken = $auth.csrfToken

if ($cookie -eq "在此填入你的 LeetCode Cookie" -or $cookie -eq "") {
    $msg = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') - [Submit] 错误: 请先在 .claude/leetcode_auth.json 中填入 LeetCode Cookie"
    Write-Output $msg | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Output $msg
    exit 1
}

# ---------- 读取代码 ----------
if (-not (Test-Path $CodeFile)) {
    $msg = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') - [Submit] 错误: 找不到代码文件 $CodeFile"
    Write-Output $msg | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Output $msg
    exit 1
}

$code = Get-Content $CodeFile -Raw -Encoding UTF8

# ---------- 构建请求 ----------
$body = @{
    lang         = "java"
    question_id  = $QuestionId
    typed_code   = $code
} | ConvertTo-Json -Depth 1

$headers = @{
    "Content-Type" = "application/json"
    "Cookie"       = "LEETCODE_SESSION=$cookie; csrftoken=$csrfToken"
    "x-csrftoken"  = $csrfToken
    "Referer"      = "https://leetcode.cn/problems/$Slug/"
    "Origin"       = "https://leetcode.cn"
}

# ---------- 提交 ----------
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
Write-Output "$timestamp - [Submit] 正在提交 $Slug (ID: $QuestionId)..." | Out-File -Append -FilePath $logFile -Encoding UTF8

try {
    $response = Invoke-RestMethod -Uri "https://leetcode.cn/problems/$Slug/submit/" `
                                  -Method Post `
                                  -Headers $headers `
                                  -Body $body `
                                  -TimeoutSec 30
} catch {
    $msg = "$timestamp - [Submit] 提交失败: $_"
    Write-Output $msg | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Output $msg
    exit 1
}

$submissionId = $response.submission_id
Write-Output "$timestamp - [Submit] 提交成功, Submission ID: $submissionId" | Out-File -Append -FilePath $logFile -Encoding UTF8

# ---------- 轮询结果 ----------
$maxWait = 30
$waited = 0
$checkHeaders = @{ "Cookie" = "LEETCODE_SESSION=$cookie" }

do {
    Start-Sleep -Seconds 2
    $waited += 2

    try {
        $result = Invoke-RestMethod -Uri "https://leetcode.cn/submissions/detail/$submissionId/check/" `
                                    -Headers $checkHeaders `
                                    -TimeoutSec 15
    } catch {
        $msg = "$timestamp - [Submit] 查询结果失败: $_"
        Write-Output $msg | Out-File -Append -FilePath $logFile -Encoding UTF8
        exit 1
    }

} while (($result.state -eq "PENDING" -or $result.state -eq "STARTED") -and $waited -lt $maxWait)

# ---------- 输出结果 ----------
$timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$statusMsg = if ($result.status_msg) { $result.status_msg } else { $result.state }
$runtime = if ($result.status_runtime) { $result.status_runtime } else { "N/A" }
$memory = if ($result.status_memory) { $result.status_memory } else { "N/A" }
$passed = if ($result.total_correct -and $result.total_testcases) {
    "$($result.total_correct)/$($result.total_testcases)"
} else { "?" }

Write-Output "$timestamp - [Submit] 结果: $statusMsg | 通过: $passed | 耗时: $runtime | 内存: $memory" | Out-File -Append -FilePath $logFile -Encoding UTF8
Write-Output "LeetCode 提交结果: $statusMsg | 通过: $passed | 耗时: $runtime | 内存: $memory"
