"""
Final submit script for 2026-06-29 LeetCode Daily Challenge
Problem: 1967. Number of Strings That Appear as Substrings in Word
Run: python .claude\final_submit.py

This script:
1. Fetches internal questionId from leetcode.cn
2. Submits the Java solution to leetcode.cn
3. Prints git commands for manual execution
"""
import json, urllib.request, urllib.error, os, sys, time, base64
from datetime import datetime

PROJECT_DIR = r"D:\LeetCode"
TODAY = "2026-06-29"

def load_auth():
    auth_file = os.path.join(PROJECT_DIR, ".claude", "leetcode_auth.json")
    if not os.path.exists(auth_file):
        print("ERROR: Auth file not found")
        return None, None
    with open(auth_file, "r", encoding="utf-8") as f:
        auth = json.load(f)
    jwt = auth.get("cookie", "")
    csrf = auth.get("csrfToken", "")
    if not jwt or jwt.startswith("在此填入"):
        print("ERROR: Invalid JWT")
        return None, None
    return jwt, csrf

def fetch_internal_id(slug):
    """Fetch internal questionId from leetcode.cn"""
    query = '{"query":"{question(titleSlug:\\"' + slug + '\\"){questionId}}","variables":{}}'
    try:
        req = urllib.request.Request(
            "https://leetcode.cn/graphql/",
            data=query.encode("utf-8"),
            headers={"Content-Type": "application/json",
                     "User-Agent": "Mozilla/5.0"}
        )
        with urllib.request.urlopen(req, timeout=15) as resp:
            data = json.loads(resp.read().decode("utf-8"))
        return data.get("data", {}).get("question", {}).get("questionId", "")
    except Exception as e:
        print(f"Error fetching internal ID: {e}")
        return ""

def submit_solution(slug, qid, code_file, jwt, csrf):
    """Submit Java solution to leetcode.cn"""
    if not os.path.exists(code_file):
        print(f"ERROR: Code file not found: {code_file}")
        return False
    with open(code_file, "r", encoding="utf-8") as f:
        code = f.read()
    print(f"Code length: {len(code)} chars")

    body = {"lang": "java", "question_id": qid, "typed_code": code}
    headers = {
        "Content-Type": "application/json",
        "Cookie": f"LEETCODE_SESSION={jwt}",
        "Origin": "https://leetcode.cn",
        "Referer": f"https://leetcode.cn/problems/{slug}/",
        "User-Agent": "Mozilla/5.0"
    }
    if csrf and csrf not in ("在此填入你的 CSRF Token", ""):
        headers["Cookie"] += f"; csrftoken={csrf}"
        headers["x-csrftoken"] = csrf

    url = f"https://leetcode.cn/problems/{slug}/submit/"
    print(f"Submitting to: {url}")
    try:
        req = urllib.request.Request(url, data=json.dumps(body).encode("utf-8"), headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        print(f"HTTP Error {e.code}: {e.read().decode('utf-8')}")
        return False
    except Exception as e:
        print(f"Request failed: {e}")
        return False

    sid = result.get("submission_id")
    if not sid:
        print(f"Submit failed: {result}")
        return False
    print(f"Submitted! ID: {sid}")

    for i in range(15):
        time.sleep(2)
        cr = urllib.request.Request(
            f"https://leetcode.cn/submissions/detail/{sid}/check/",
            headers={"Cookie": f"LEETCODE_SESSION={jwt}",
                     "User-Agent": "Mozilla/5.0"}
        )
        try:
            with urllib.request.urlopen(cr, timeout=15) as resp:
                check = json.loads(resp.read().decode("utf-8"))
        except:
            continue
        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            status_msg = check.get("status_msg", state)
            runtime = check.get("status_runtime", "N/A")
            memory = check.get("status_memory", "N/A")
            total = check.get("total_testcases", "?")
            correct = check.get("total_correct", "?")
            print(f"Result: {status_msg} | Passed: {correct}/{total} | Time: {runtime} | Mem: {memory}")

            log_file = os.path.join(PROJECT_DIR, ".claude", "leetcode_daily.log")
            ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            with open(log_file, "a", encoding="utf-8") as lf:
                lf.write(f"{ts} - [Submit] 1967. Number of Strings That Appear as Substrings in Word | {status_msg} | {correct}/{total} | Runtime: {runtime} | Memory: {memory}\n")

            return state == "SUCCESS"
        print(f"  State: {state}... ({i+1}/15)")
    print("Timeout")
    return False

def main():
    print("=" * 60)
    print(f"LeetCode Daily Challenge Submit - {TODAY}")
    print("Problem: 1967. Number of Strings That Appear as Substrings in Word")
    print("=" * 60)

    slug = "number-of-strings-that-appear-as-substrings-in-word"
    code_file = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn",
                             "[1967]作为子字符串出现在单词中的字符串数目.java")

    # Load auth
    jwt, csrf = load_auth()
    if not jwt:
        print("Cannot submit without valid auth")
        sys.exit(1)

    # Fetch internal questionId
    print("\n[1] Fetching internal questionId from leetcode.cn...")
    qid = fetch_internal_id(slug)
    if not qid:
        print("ERROR: Could not get internal questionId")
        sys.exit(1)
    print(f"Internal ID: {qid}")

    # Submit
    print("\n[2] Submitting solution...")
    success = submit_solution(slug, qid, code_file, jwt, csrf)

    # Git commands
    print("\n[3] Git commands:")
    print(f"  cd {PROJECT_DIR}")
    print(f"  git add leetcode/2026/06/29_number-of-strings-that-appear-as-substrings-in-word/")
    print(f"  git add leetcode/editor/cn/[1967]作为子字符串出现在单词中的字符串数目.java")
    print(f"  git add leetcode/editor/cn/doc/content/[1967]作为子字符串出现在单词中的字符串数目.md")
    print(f'  git commit -m "每日一题: 1967 作为子字符串出现在单词中的字符串数目 (Java解法+直接匹配)"')
    print(f"  git push")

    if success:
        print("\nSUCCESS! Solution accepted.")
    else:
        print("\nSubmission completed (check result above).")

if __name__ == "__main__":
    main()
