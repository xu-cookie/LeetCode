"""
Complete submission workflow for today's LeetCode daily challenge.
Run: python .claude\submit_today.py

This script:
1. Fetches the internal questionId from LeetCode CN
2. Submits the Java solution to LeetCode CN
"""
import json
import urllib.request
import urllib.error
import os
import sys
import time
import base64
from datetime import datetime

PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CLAUDE_DIR = os.path.join(PROJECT_DIR, ".claude")

# Load today's problem from cache
CACHE_FILE = os.path.join(CLAUDE_DIR, "today_problem.json")
if not os.path.exists(CACHE_FILE):
    print("ERROR: today_problem.json not found. Run fetch_daily.py first.")
    sys.exit(1)

with open(CACHE_FILE, "r", encoding="utf-8") as f:
    problem = json.load(f)

title_slug = problem["titleSlug"]
frontend_id = problem["questionFrontendId"]
title = problem["title"]

print(f"Problem: [{frontend_id}] {title}")
print(f"Slug: {title_slug}")

# Step 1: Fetch internal questionId from LeetCode CN
internal_id = problem.get("questionId", "")
if not internal_id:
    print("\n[1/3] Fetching internal questionId from LeetCode CN...")
    query = {
        "query": "query($slug: String!) { question(titleSlug: $slug) { questionId } }",
        "variables": {"slug": title_slug}
    }
    headers = {"Content-Type": "application/json", "User-Agent": "Mozilla/5.0"}
    try:
        req = urllib.request.Request(
            "https://leetcode.cn/graphql/",
            data=json.dumps(query).encode("utf-8"),
            headers=headers
        )
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
        q = result.get("data", {}).get("question", {})
        internal_id = q.get("questionId", "")
        if internal_id:
            problem["questionId"] = internal_id
            with open(CACHE_FILE, "w", encoding="utf-8") as f:
                json.dump(problem, f, indent=2, ensure_ascii=False)
            print(f"  Internal questionId: {internal_id}")
        else:
            print("ERROR: Could not get questionId from CN API")
            sys.exit(1)
    except Exception as e:
        print(f"ERROR fetching questionId: {e}")
        sys.exit(1)
else:
    print(f"\n[1/3] Internal questionId from cache: {internal_id}")

# Step 2: Load auth
print("\n[2/3] Loading auth...")
AUTH_FILE = os.path.join(CLAUDE_DIR, "leetcode_auth.json")
if not os.path.exists(AUTH_FILE):
    print("ERROR: Auth file not found")
    sys.exit(1)

with open(AUTH_FILE, "r", encoding="utf-8") as f:
    auth = json.load(f)

jwt = auth.get("cookie", "")
csrf = auth.get("csrfToken", "")

if not jwt or jwt.startswith("在此填入"):
    print("ERROR: Invalid JWT in auth file")
    sys.exit(1)

# Check JWT expiry
try:
    parts = jwt.split(".")
    if len(parts) >= 2:
        payload = parts[1]
        payload = payload.replace("-", "+").replace("_", "/")
        while len(payload) % 4 != 0:
            payload += "="
        decoded = json.loads(base64.b64decode(payload).decode("utf-8"))
        exp_unix = decoded.get("expired_time_")
        if exp_unix:
            exp_date = datetime.fromtimestamp(exp_unix)
            days_left = (exp_date - datetime.now()).days
            if days_left <= 0:
                print(f"ERROR: JWT expired on {exp_date.strftime('%Y-%m-%d')}")
                sys.exit(1)
            print(f"  JWT valid, expires: {exp_date.strftime('%Y-%m-%d')} ({days_left} days)")
except Exception:
    pass

print("  Auth loaded OK")

# Step 3: Submit
print("\n[3/3] Submitting solution...")

# Find the Java solution file
java_file = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn", f"[{frontend_id}]{title}.java")
if not os.path.exists(java_file):
    print(f"ERROR: Java file not found: {java_file}")
    sys.exit(1)

with open(java_file, "r", encoding="utf-8") as f:
    code = f.read()

print(f"  Code file: {java_file}")
print(f"  Code length: {len(code)} chars")

body = {
    "lang": "java",
    "question_id": internal_id,
    "typed_code": code
}
data = json.dumps(body).encode("utf-8")

headers = {
    "Content-Type": "application/json",
    "Cookie": f"LEETCODE_SESSION={jwt}",
    "Origin": "https://leetcode.cn",
    "Referer": f"https://leetcode.cn/problems/{title_slug}/",
    "User-Agent": "Mozilla/5.0"
}

if csrf and csrf not in ("在此填入你的 CSRF Token", ""):
    headers["Cookie"] += f"; csrftoken={csrf}"
    headers["x-csrftoken"] = csrf

url = f"https://leetcode.cn/problems/{title_slug}/submit/"
print(f"  Submit URL: {url}")

try:
    req = urllib.request.Request(url, data=data, headers=headers)
    with urllib.request.urlopen(req, timeout=30) as resp:
        result = json.loads(resp.read().decode("utf-8"))
except urllib.error.HTTPError as e:
    error_body = e.read().decode("utf-8")
    print(f"\nHTTP Error {e.code}: {error_body}")
    sys.exit(1)
except Exception as e:
    print(f"\nRequest failed: {e}")
    sys.exit(1)

submission_id = result.get("submission_id")
if not submission_id:
    print(f"\nSubmit failed: {result}")
    sys.exit(1)

print(f"  Submission ID: {submission_id}")

# Poll for result
print("\n  Polling for result...")
for i in range(15):
    time.sleep(2)
    check_req = urllib.request.Request(
        f"https://leetcode.cn/submissions/detail/{submission_id}/check/",
        headers={"Cookie": f"LEETCODE_SESSION={jwt}", "User-Agent": "Mozilla/5.0"}
    )
    try:
        with urllib.request.urlopen(check_req, timeout=15) as resp:
            check = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"  Check failed: {e}")
        continue

    state = check.get("state")
    if state not in ("PENDING", "STARTED"):
        status_msg = check.get("status_msg", state)
        runtime = check.get("status_runtime", "N/A")
        memory = check.get("status_memory", "N/A")
        total = check.get("total_testcases", "?")
        correct = check.get("total_correct", "?")
        print(f"\n{'='*50}")
        print(f"  Result: {status_msg}")
        print(f"  Passed: {correct}/{total}")
        print(f"  Runtime: {runtime}")
        print(f"  Memory: {memory}")
        print(f"{'='*50}")

        # Log
        log_file = os.path.join(CLAUDE_DIR, "leetcode_daily.log")
        ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        with open(log_file, "a", encoding="utf-8") as log:
            log.write(f"{ts} - [{frontend_id}] {title} | Result: {status_msg} | Passed: {correct}/{total} | Time: {runtime} | Mem: {memory}\n")

        if state == "SUCCESS":
            print("\nSUCCESS!")
            sys.exit(0)
        else:
            print(f"\nSubmission completed but not accepted: {status_msg}")
            if check.get("runtime_error"):
                print(f"  Runtime error: {check['runtime_error']}")
            if check.get("compile_error"):
                print(f"  Compile error: {check['compile_error']}")
            sys.exit(1)

    print(f"  State: {state}... (attempt {i+1}/15)")

print("\nTimeout waiting for result")
sys.exit(1)
