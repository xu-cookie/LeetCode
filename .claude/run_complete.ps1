# ============================================================
# Complete LeetCode Daily Challenge Runner - 2026-06-29
# Does: fetch -> submit -> git commit -> git push
# ============================================================
$ErrorActionPreference = "Continue"
Set-Location D:\LeetCode

$logFile = "D:\LeetCode\.claude\leetcode_daily.log"
$ts = Get-Date -Format "yyyy-MM-dd HH:mm:ss"

"$ts - Starting complete daily workflow" | Out-File -Append -FilePath $logFile -Encoding UTF8

# Step 1: Fetch internal questionId and submit
Write-Host "[1/3] Submitting to LeetCode CN..."
$submitResult = python .claude\final_submit.py 2>&1
$submitExit = $LASTEXITCODE
"$ts - Submit exit code: $submitExit" | Out-File -Append -FilePath $logFile -Encoding UTF8
Write-Host $submitResult

# Step 2: Git add
Write-Host "[2/3] Git add..."
git add LeetCode/2026/06/1846_maximum-element-after-decreasing-and-rearranging/
git add leetcode/editor/cn/"[1846]减小和重新排列数组后的最大元素.java"
git add "leetcode/editor/cn/doc/content/[1846]减小和重新排列数组后的最大元素.md"

# Step 3: Git commit and push
Write-Host "[3/3] Git commit and push..."
$commitMsg = '每日一题: 1846 减小和重新排列数组后的最大元素 (Java解法+贪心+排序)'
git commit -m $commitMsg

$pushResult = git push 2>&1
$pushExit = $LASTEXITCODE
if ($pushExit -eq 0) {
    "$ts - Git push OK" | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Host "PUSH SUCCESS"
} else {
    "$ts - Git push FAILED: $pushResult" | Out-File -Append -FilePath $logFile -Encoding UTF8
    Write-Host "PUSH FAILED: $pushResult"
}

"$ts - Complete workflow ended" | Out-File -Append -FilePath $logFile -Encoding UTF8
