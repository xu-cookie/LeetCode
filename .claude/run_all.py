"""
Complete LeetCode Daily Challenge workflow.
Run this script to: fetch -> solve -> submit -> commit
"""
import json
import urllib.request
import urllib.error
import os
import sys
import time
import base64
from datetime import datetime

PROJECT_DIR = r"D:\LeetCode"
AUTH_FILE = os.path.join(PROJECT_DIR, ".claude", "leetcode_auth.json")

def load_auth():
    if not os.path.exists(AUTH_FILE):
        print(f"ERROR: Auth file not found: {AUTH_FILE}")
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
                print(f"JWT expires: {exp_date.strftime('%Y-%m-%d')} ({days_left} days left)")
                if days_left <= 0:
                    print("ERROR: JWT expired!")
                    sys.exit(1)
    except Exception as e:
        print(f"Warning: Could not decode JWT: {e}")

    return jwt, csrf

def fetch_daily():
    """Step 1: Fetch today's daily challenge"""
    query = {
        "query": "{todayRecord{date question{questionFrontendId titleSlug translatedTitle difficulty}}}"
    }
    headers = {"Content-Type": "application/json", "User-Agent": "Mozilla/5.0"}
    url = "https://leetcode.cn/graphql/"

    try:
        req = urllib.request.Request(url, data=json.dumps(query).encode("utf-8"), headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"ERROR fetching daily: {e}")
        sys.exit(1)

    data = result.get("data", {}).get("todayRecord", [])
    if not data:
        print("ERROR: No daily record found")
        sys.exit(1)

    q = data[0].get("question", {})
    info = {
        "date": data[0].get("date", ""),
        "frontendId": q.get("questionFrontendId", ""),
        "titleSlug": q.get("titleSlug", ""),
        "translatedTitle": q.get("translatedTitle", ""),
        "difficulty": q.get("difficulty", "")
    }
    print(f"Daily: [{info['frontendId']}] {info['translatedTitle']} ({info['difficulty']})")
    return info

def fetch_question_detail(title_slug):
    """Step 2: Fetch question details including internal questionId"""
    query = {
        "query": "query($slug: String!) { question(titleSlug: $slug) { questionId content codeSnippets { lang langSlug code } } }",
        "variables": {"slug": title_slug}
    }
    headers = {"Content-Type": "application/json", "User-Agent": "Mozilla/5.0"}
    url = "https://leetcode.cn/graphql/"

    try:
        req = urllib.request.Request(url, data=json.dumps(query).encode("utf-8"), headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"ERROR fetching detail: {e}")
        sys.exit(1)

    q = result.get("data", {}).get("question", {})
    detail = {
        "internalId": q.get("questionId", ""),
        "content": q.get("content", ""),
        "codeSnippets": q.get("codeSnippets", [])
    }

    # Get Java code snippet
    for snippet in detail["codeSnippets"]:
        if snippet.get("langSlug") == "java":
            detail["javaTemplate"] = snippet.get("code", "")
            break

    print(f"Internal ID: {detail['internalId']}")
    return detail

def write_java_solution(info, java_template, solution_code):
    """Step 3: Write the Java solution file"""
    frontend_id = info["frontendId"]
    title = info["translatedTitle"]

    # Ensure directory exists
    cn_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn")
    os.makedirs(cn_dir, exist_ok=True)

    filename = f"[{frontend_id}]{title}.java"
    filepath = os.path.join(cn_dir, filename)

    with open(filepath, "w", encoding="utf-8") as f:
        f.write(solution_code)

    print(f"Solution written to: {filepath}")
    return filepath

def write_content_md(info, detail):
    """Step 4: Write problem description markdown"""
    frontend_id = info["frontendId"]
    title = info["translatedTitle"]

    content_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn", "doc", "content")
    os.makedirs(content_dir, exist_ok=True)

    filename = f"[{frontend_id}]{title}.md"
    filepath = os.path.join(content_dir, filename)

    # Clean HTML content for markdown
    content = detail.get("content", "")

    md_content = f"# [{frontend_id}] {title}\n\n"
    md_content += f"- 难度: {info['difficulty']}\n"
    md_content += f"- 题目: https://leetcode.cn/problems/{info['titleSlug']}/\n\n"
    md_content += content

    with open(filepath, "w", encoding="utf-8") as f:
        f.write(md_content)

    print(f"Content written to: {filepath}")
    return filepath

def submit_solution(slug, question_id, code_file, jwt, csrf):
    """Step 5: Submit to LeetCode"""
    if not os.path.exists(code_file):
        print(f"ERROR: Code file not found: {code_file}")
        return False

    with open(code_file, "r", encoding="utf-8") as f:
        code = f.read()

    print(f"Code length: {len(code)} chars")

    body = {
        "lang": "java",
        "question_id": question_id,
        "typed_code": code
    }
    data = json.dumps(body).encode("utf-8")

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
        req = urllib.request.Request(url, data=data, headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        error_body = e.read().decode("utf-8")
        print(f"HTTP Error {e.code}: {error_body}")
        return False
    except Exception as e:
        print(f"Request failed: {e}")
        return False

    submission_id = result.get("submission_id")
    if not submission_id:
        print(f"Submit failed: {result}")
        return False

    print(f"Submitted! Submission ID: {submission_id}")

    # Poll for result
    print("Polling for result...")
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
            print(f"Check failed: {e}")
            continue

        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            status_msg = check.get("status_msg", state)
            runtime = check.get("status_runtime", "N/A")
            memory = check.get("status_memory", "N/A")
            total = check.get("total_testcases", "?")
            correct = check.get("total_correct", "?")
            print(f"Result: {status_msg} | Passed: {correct}/{total} | Time: {runtime} | Mem: {memory}")
            return state == "SUCCESS"

        print(f"  State: {state}... (attempt {i+1}/15)")

    print("Timeout waiting for result")
    return False

def main():
    print("=" * 60)
    print("LeetCode Daily Challenge - Full Workflow")
    print(f"Date: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 60)

    # Load auth
    jwt, csrf = load_auth()

    # Fetch daily challenge info
    info = fetch_daily()

    # Fetch question detail
    detail = fetch_question_detail(info["titleSlug"])

    # Save all data for the LLM to use
    result = {**info, **detail}
    result_path = os.path.join(PROJECT_DIR, ".claude", "daily_result.json")
    with open(result_path, "w", encoding="utf-8") as f:
        json.dump(result, f, ensure_ascii=False, indent=2)

    print(f"\nAll data saved to: {result_path}")
    print("\nNow write the solution in the Java file, then run:")
    print(f"  python .claude\\run_all.py --submit-only")

    # Print the Java template for manual solution writing
    print("\n=== Java Template ===")
    print(detail.get("javaTemplate", "N/A"))
    print("=== End Template ===")

if __name__ == "__main__":
    main()
