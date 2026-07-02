"""
One-click LeetCode Daily Challenge Solver
Fetches problem, generates solution, submits, and commits.
"""
import json, os, sys, time, base64, urllib.request, urllib.error
from datetime import datetime

PROJECT_DIR = r"D:\LeetCode"
AUTH_FILE = os.path.join(PROJECT_DIR, ".claude", "leetcode_auth.json")

def load_auth():
    with open(AUTH_FILE, "r", encoding="utf-8") as f:
        return json.load(f)

def api_call(query, variables=None):
    """Make a GraphQL call to leetcode.cn"""
    body = {"query": query}
    if variables:
        body["variables"] = variables
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0"
    }
    req = urllib.request.Request(
        "https://leetcode.cn/graphql/",
        data=json.dumps(body).encode("utf-8"),
        headers=headers
    )
    with urllib.request.urlopen(req, timeout=30) as resp:
        return json.loads(resp.read().decode("utf-8"))

def get_daily():
    """Fetch today's daily challenge"""
    result = api_call("{todayRecord{date question{questionFrontendId titleSlug translatedTitle difficulty}}}")
    records = result.get("data", {}).get("todayRecord", [])
    if not records:
        print("ERROR: No daily record!")
        sys.exit(1)
    r = records[0]
    q = r["question"]
    return {
        "date": r["date"],
        "frontendId": q["questionFrontendId"],
        "titleSlug": q["titleSlug"],
        "translatedTitle": q["translatedTitle"],
        "difficulty": q["difficulty"]
    }

def get_detail(slug):
    """Fetch question detail with internal questionId"""
    query = """
    query($slug: String!) {
        question(titleSlug: $slug) {
            questionId
            content
            codeSnippets { lang langSlug code }
            sampleTestCase
        }
    }
    """
    result = api_call(query, {"slug": slug})
    q = result["data"]["question"]
    java_code = ""
    for s in q.get("codeSnippets", []):
        if s["langSlug"] == "java":
            java_code = s["code"]
            break
    return {
        "internalId": q["questionId"],
        "content": q.get("content", ""),
        "javaTemplate": java_code,
        "sampleTestCase": q.get("sampleTestCase", "")
    }

def main():
    print("=" * 60)
    print("LeetCode Daily Challenge Solver")
    print(f"Time: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print("=" * 60)

    # Step 1: Fetch daily
    info = get_daily()
    print(f"\n[Daily Challenge]")
    print(f"  Date: {info['date']}")
    print(f"  ID: {info['frontendId']}")
    print(f"  Title: {info['translatedTitle']}")
    print(f"  Slug: {info['titleSlug']}")
    print(f"  Difficulty: {info['difficulty']}")

    # Step 2: Fetch details
    detail = get_detail(info["titleSlug"])
    print(f"\n[Details]")
    print(f"  Internal ID: {detail['internalId']}")
    print(f"  Sample Test: {detail.get('sampleTestCase', 'N/A')}")

    # Step 3: Save problem data for the LLM
    result = {**info, **detail}
    output_path = os.path.join(PROJECT_DIR, ".claude", "problem_data.json")
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(result, f, ensure_ascii=False, indent=2)

    print(f"\n[Saved] Problem data -> {output_path}")

    # Print Java template
    print(f"\n[Java Template]")
    print(detail["javaTemplate"])
    print(f"\n[Content Preview]")
    # Strip HTML tags for readability
    content = detail.get("content", "")
    # Simple HTML tag removal
    import re
    clean = re.sub(r'<[^>]+>', '', content)
    clean = re.sub(r'\n{3,}', '\n\n', clean)
    print(clean[:1500])

    return info, detail

if __name__ == "__main__":
    main()
