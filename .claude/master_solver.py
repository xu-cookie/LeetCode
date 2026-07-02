"""
Master Solver: Fetches today's LeetCode daily challenge, generates Java solution,
submits to LeetCode CN, and prints git commands.
Run: python .claude\master_solver.py
"""
import json, urllib.request, urllib.error, os, sys, re, time, base64
from datetime import datetime

TODAY = "2026-06-29"
PROJECT_DIR = r"D:\LeetCode"

def graphql_call(url, query, timeout=30):
    body = {"query": query}
    headers = {"Content-Type": "application/json",
               "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"}
    data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request(url, data=data, headers=headers, method="POST")
    with urllib.request.urlopen(req, timeout=timeout) as resp:
        return json.loads(resp.read().decode("utf-8"))

def fetch_leetcode_com():
    query = """
    query questionOfToday {
        activeDailyCodingChallengeQuestion {
            date link
            question {
                questionFrontendId title titleSlug difficulty content
                codeSnippets { lang langSlug code }
                exampleTestcaseList
            }
        }
    }
    """
    try:
        result = graphql_call("https://leetcode.com/graphql", query)
        challenge = result["data"]["activeDailyCodingChallengeQuestion"]
        if not challenge or not challenge.get("question"): return None
        q = challenge["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        # Fetch internal questionId from leetcode.cn
        sid = None
        try:
            cnq = graphql_call("https://leetcode.cn/graphql/",
                "query($s:String!){question(titleSlug:$s){questionId}}",
                variables=None)
        except: pass
        return {"date": challenge.get("date", TODAY),
                "questionFrontendId": q["questionFrontendId"],
                "titleSlug": q["titleSlug"], "title": q["title"],
                "difficulty": q["difficulty"], "content": q.get("content", ""),
                "snippets": snippets, "source": "leetcode.com"}
    except Exception as e:
        print(f"  [leetcode.com] Error: {e}")
        return None

def fetch_leetcode_cn():
    query = """
    query todayRecord {
        todayRecord { date question {
            questionFrontendId titleSlug translatedTitle title
            difficulty questionId content
            codeSnippets { lang langSlug code }
        }}
    }
    """
    try:
        result = graphql_call("https://leetcode.cn/graphql/", query)
        records = result.get("data", {}).get("todayRecord", [])
        if not records: return None
        q = records[0]["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {"date": records[0]["date"],
                "questionFrontendId": q["questionFrontendId"],
                "titleSlug": q["titleSlug"],
                "translatedTitle": q.get("translatedTitle", ""),
                "title": q.get("title", ""), "difficulty": q.get("difficulty", ""),
                "questionId": q.get("questionId", ""),
                "content": q.get("content", ""), "snippets": snippets,
                "source": "leetcode.cn"}
    except Exception as e:
        print(f"  [leetcode.cn] Error: {e}")
        return None

def fetch_internal_id(slug):
    try:
        query = "query($s:String!){question(titleSlug:$s){questionId}}"
        result = graphql_call("https://leetcode.cn/graphql/", query)
        return result.get("data",{}).get("question",{}).get("questionId","")
    except: return ""

def save_files(problem):
    pid = problem["questionFrontendId"]
    slug = problem["titleSlug"]
    title_cn = problem.get("translatedTitle", problem["title"])
    title_en = problem["title"]
    difficulty = problem["difficulty"]
    content = problem.get("content", "")

    # Create directory structure: LeetCode/YYYY/MM/{id}_{slug}/
    year = TODAY[:4]; month = TODAY[5:7]
    leetcode_dir = os.path.join(PROJECT_DIR, "LeetCode", year, month, f"{pid}_{slug}")
    os.makedirs(leetcode_dir, exist_ok=True)

    # 1. Save Java solution template
    java_template = problem.get("snippets", {}).get("java", "")
    java_file = os.path.join(leetcode_dir, "solution.java")
    # The actual solution will be written separately
    print(f"\nDirectory: {leetcode_dir}")
    print(f"Java template: {java_template[:200]}...")

    # 2. Save README.md
    clean = re.sub(r'<[^>]+>', '', content)
    clean = re.sub(r'&[a-z]+;', ' ', clean)
    readme = f"""# [{pid}] {title_en}

**Difficulty:** {difficulty}
**Date:** {TODAY}
**Link:** https://leetcode.com/problems/{slug}/

## Problem Description

{clean[:2000]}

## Approach

[TODO]

## Complexity
- Time: O(?)
- Space: O(?)
"""
    with open(os.path.join(leetcode_dir, "README.md"), "w", encoding="utf-8") as f:
        f.write(readme)

    # 3. Save CN editor files
    cn_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn")
    os.makedirs(cn_dir, exist_ok=True)
    cn_java = os.path.join(cn_dir, f"[{pid}]{title_cn}.java")
    cn_content_dir = os.path.join(cn_dir, "doc", "content")
    os.makedirs(cn_content_dir, exist_ok=True)
    cn_md = os.path.join(cn_content_dir, f"[{pid}]{title_cn}.md")

    # Write content markdown
    with open(cn_md, "w", encoding="utf-8") as f:
        f.write(f"# [{pid}] {title_cn}\n\n- 难度: {difficulty}\n- 题目: https://leetcode.cn/problems/{slug}/\n\n{content}")

    return {"pid": pid, "slug": slug, "title_cn": title_cn, "title_en": title_en,
            "difficulty": difficulty, "content": content, "java_template": java_template,
            "dir": leetcode_dir, "java_file": java_file, "cn_java": cn_java,
            "cn_md": cn_md, "snippets": problem.get("snippets", {}),
            "questionId": problem.get("questionId", fetch_internal_id(slug))}

def submit_solution(slug, qid, code_file):
    auth_file = os.path.join(PROJECT_DIR, ".claude", "leetcode_auth.json")
    if not os.path.exists(auth_file):
        print("WARNING: No auth file, cannot submit to LeetCode")
        return False
    with open(auth_file, "r", encoding="utf-8") as f:
        auth = json.load(f)
    jwt = auth.get("cookie", "")
    csrf = auth.get("csrfToken", "")
    if not jwt or jwt.startswith("在此填入"):
        print("WARNING: Invalid JWT, cannot submit")
        return False

    with open(code_file, "r", encoding="utf-8") as f:
        code = f.read()
    body = {"lang": "java", "question_id": qid, "typed_code": code}
    headers = {"Content-Type": "application/json",
               "Cookie": f"LEETCODE_SESSION={jwt}",
               "Origin": "https://leetcode.cn",
               "Referer": f"https://leetcode.cn/problems/{slug}/",
               "User-Agent": "Mozilla/5.0"}
    if csrf and csrf not in ("在此填入你的 CSRF Token", ""):
        headers["Cookie"] += f"; csrftoken={csrf}"
        headers["x-csrftoken"] = csrf

    url = f"https://leetcode.cn/problems/{slug}/submit/"
    try:
        req = urllib.request.Request(url, data=json.dumps(body).encode("utf-8"), headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"Submit error: {e}")
        return False

    sid = result.get("submission_id")
    if not sid:
        print(f"Submit failed: {result}")
        return False
    print(f"Submitted! ID: {sid}")

    for i in range(15):
        time.sleep(2)
        cr = urllib.request.Request(f"https://leetcode.cn/submissions/detail/{sid}/check/",
            headers={"Cookie": f"LEETCODE_SESSION={jwt}"})
        try:
            with urllib.request.urlopen(cr, timeout=15) as resp:
                check = json.loads(resp.read().decode("utf-8"))
        except: continue
        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            print(f"Result: {check.get('status_msg', state)} | "
                  f"{check.get('total_correct','?')}/{check.get('total_testcases','?')} | "
                  f"{check.get('status_runtime','N/A')} | {check.get('status_memory','N/A')}")
            return state == "SUCCESS"
        print(f"  State: {state}... ({i+1}/15)")
    print("Timeout")
    return False

def main():
    print("=" * 60)
    print(f"LeetCode Daily Challenge Solver - {TODAY}")
    print("=" * 60)

    print("\n[1] Fetching daily challenge...")
    problem = fetch_leetcode_com()
    if not problem:
        print("Trying leetcode.cn...")
        problem = fetch_leetcode_cn()
    if not problem:
        print("FAILED to fetch!")
        sys.exit(1)

    print(f"  Date: {problem['date']}")
    print(f"  Problem: #{problem['questionFrontendId']} {problem['title']}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Slug: {problem['titleSlug']}")
    print(f"  Internal ID: {problem.get('questionId', 'N/A')}")

    # Save to cache
    cache_path = os.path.join(PROJECT_DIR, ".claude", "today_problem.json")
    with open(cache_path, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)
    print(f"  Cached to: {cache_path}")

    # Save files
    print("\n[2] Creating directory structure...")
    info = save_files(problem)
    print(f"  Dir: {info['dir']}")
    print(f"  CN Java: {info['cn_java']}")
    print(f"  CN MD: {info['cn_md']}")

    # Print content summary
    c = problem.get("content", "")
    clean = re.sub(r'<[^>]+>', '', c)
    print(f"\n[3] Problem Summary (first 2000 chars):")
    print(clean[:2000])

    # Output git commands
    print(f"\n[4] Git commands:")
    pid = problem["questionFrontendId"]
    title_cn = info["title_cn"]
    print(f"  git add LeetCode/{TODAY[:4]}/{TODAY[5:7]}/")
    print(f"  git add leetcode/editor/cn/[{pid}]{title_cn}.java")
    print(f"  git add leetcode/editor/cn/doc/content/[{pid}]{title_cn}.md")
    print(f'  git commit -m "每日一题: {pid} {title_cn} ({problem["difficulty"]})"')
    print(f"  git push")

    # Save problem info for solver
    solver_info = os.path.join(PROJECT_DIR, ".claude", "solver_info.json")
    with open(solver_info, "w", encoding="utf-8") as f:
        json.dump(info, f, indent=2, ensure_ascii=False)
    print(f"\nSolver info saved to: {solver_info}")

    return problem, info

if __name__ == "__main__":
    main()
