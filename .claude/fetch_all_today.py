import json, urllib.request, urllib.error, os, sys, re
from datetime import datetime

TODAY = "2026-06-29"
PROJECT_DIR = r"D:\LeetCode"

def graphql_call(url, query, timeout=30):
    body = {"query": query}
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
    }
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
        if not challenge or not challenge.get("question"):
            return None
        q = challenge["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {
            "date": challenge.get("date", TODAY),
            "questionFrontendId": q["questionFrontendId"],
            "titleSlug": q["titleSlug"], "title": q["title"],
            "translatedTitle": "", "difficulty": q["difficulty"],
            "content": q.get("content", ""), "snippets": snippets,
            "exampleTests": q.get("exampleTestcaseList", []),
            "link": challenge.get("link", ""), "source": "leetcode.com"
        }
    except Exception as e:
        print(f"  [leetcode.com] Error: {e}")
        return None

def fetch_leetcode_cn():
    query = """
    query todayRecord {
        todayRecord {
            date question {
                questionFrontendId titleSlug translatedTitle title
                difficulty questionId content
                codeSnippets { lang langSlug code }
            }
        }
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
        return {
            "date": records[0]["date"], "questionFrontendId": q["questionFrontendId"],
            "titleSlug": q["titleSlug"], "translatedTitle": q.get("translatedTitle", ""),
            "title": q.get("title", ""), "difficulty": q.get("difficulty", ""),
            "questionId": q.get("questionId", ""), "content": q.get("content", ""),
            "snippets": snippets, "source": "leetcode.cn"
        }
    except Exception as e:
        print(f"  [leetcode.cn] Error: {e}")
        return None

def main():
    print(f"Fetching daily challenge for {TODAY}...")
    problem = fetch_leetcode_com()
    if not problem:
        print("Trying leetcode.cn fallback...")
        problem = fetch_leetcode_cn()
    if not problem:
        print("FAILED!"); sys.exit(1)

    out = os.path.join(PROJECT_DIR, ".claude", "today_problem.json")
    with open(out, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)

    title = problem.get("translatedTitle") or problem.get("title", "N/A")
    print(f"DONE: #{problem['questionFrontendId']} {title}")
    print(f"Difficulty: {problem['difficulty']}")
    print(f"Slug: {problem['titleSlug']}")
    print(f"Source: {problem['source']}")
    if problem.get("questionId"):
        print(f"InternalID: {problem['questionId']}")
    print(f"Snippets: {list(problem.get('snippets',{}).keys())}")

    c = problem.get("content","")
    clean = re.sub(r'<[^>]+>', '', c)
    print("\n===PROBLEM CONTENT===")
    print(clean[:4000])

if __name__ == "__main__":
    main()
