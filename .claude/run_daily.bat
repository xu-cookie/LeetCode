@echo off
REM Script to run the LeetCode daily challenge workflow
cd /d D:\LeetCode
python .claude\run_all.py
echo.
echo After the script runs and fetches the problem, the solution needs to be written.
echo Check .claude\daily_result.json for problem details.
pause