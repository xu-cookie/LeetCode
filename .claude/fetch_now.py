"""
Quick fetch: Gets today's LeetCode daily challenge and caches it.
Run: python .claude/fetch_now.py
Prints problem data to stdout for parent process consumption.
"""
import json
import urllib.request
import urllib.error
import os
import sys
from datetime import datetime

TODAY = datetime.now().strftime("%Y-%m-%d")
PROJECT_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CACHE_FILE = os.path.join(PROJECT_DIR, ".claude", "today_problem.json")

QUERY = """
query questionOfToday {
    activeDailyCodingChallengeQuestion {
        date
        link
        question {
            questionFrontendId
            title
            titleSlug
            difficulty
            content
            codeSnippets {
                lang
                langSlug
                code
            }
        }
    }
}
"""

def fetch():
    body = {"query": QUERY}
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
    }
    data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request("https://leetcode.com/graphql", data=data, headers=headers, method="POST")
    with urllib.request.urlopen(req, timeout=30) as resp:
        result = json.loads(resp.read().decode("utf-8"))

    challenge = result["data"]["activeDailyCodingChallengeQuestion"]
    if not challenge or not challenge.get("question"):
        print("ERROR: No question in response")
        sys.exit(1)

    q = challenge["question"]

    java_code = ""
    python_code = ""
    for s in q.get("codeSnippets", []):
        if s.get("langSlug") == "java":
            java_code = s.get("code", "")
        elif s.get("langSlug") == "python3":
            python_code = s.get("code", "")

    problem = {
        "date": challenge.get("date", TODAY),
        "questionFrontendId": q["questionFrontendId"],
        "titleSlug": q["titleSlug"],
        "title": q["title"],
        "translatedTitle": "",
        "difficulty": q["difficulty"],
        "content": q.get("content", ""),
        "javaCode": java_code,
        "pythonCode": python_code,
        "source": "leetcode.com"
    }

    # Save cache
    os.makedirs(os.path.dirname(CACHE_FILE), exist_ok=True)
    with open(CACHE_FILE, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)

    print(f"OK: [{problem['questionFrontendId']}] {problem['title']} ({problem['difficulty']})")
    print(f"Date: {problem['date']} | Slug: {problem['titleSlug']}")
    print(f"Cached to: {CACHE_FILE}")

    # Print problem data for parent
    print("\n___PROBLEM_JSON_START___")
    print(json.dumps(problem, ensure_ascii=False))
    print("___PROBLEM_JSON_END___")

    return problem

if __name__ == "__main__":
    try:
        fetch()
    except Exception as e:
        print(f"ERROR: {e}")
        # Try leetcode.cn fallback
        try:
            print("Trying leetcode.cn fallback...")
            cn_query = """
            query todayRecord {
                todayRecord {
                    date
                    question {
                        questionFrontendId
                        title
                        translatedTitle
                        titleSlug
                        difficulty
                        questionId
                        content
                        codeSnippets {
                            lang
                            langSlug
                            code
                        }
                    }
                }
            }
            """
            body = {"query": cn_query}
            headers = {
                "Content-Type": "application/json",
                "User-Agent": "Mozilla/5.0"
            }
            data = json.dumps(body).encode("utf-8")
            req = urllib.request.Request("https://leetcode.cn/graphql/", data=data, headers=headers, method="POST")
            with urllib.request.urlopen(req, timeout=30) as resp:
                result = json.loads(resp.read().decode("utf-8"))

            records = result.get("data", {}).get("todayRecord", [])
            if not records:
                print("ERROR: No records from CN API")
                sys.exit(1)

            q = records[0]["question"]
            java_code = ""
            python_code = ""
            for s in q.get("codeSnippets", []):
                if s.get("langSlug") == "java":
                    java_code = s.get("code", "")
                elif s.get("langSlug") == "python3":
                    python_code = s.get("code", "")

            problem = {
                "date": records[0].get("date", TODAY),
                "questionFrontendId": q["questionFrontendId"],
                "titleSlug": q["titleSlug"],
                "title": q.get("title", ""),
                "translatedTitle": q.get("translatedTitle", ""),
                "difficulty": q.get("difficulty", ""),
                "content": q.get("content", ""),
                "javaCode": java_code,
                "pythonCode": python_code,
                "source": "leetcode.cn",
                "questionId": q.get("questionId", "")
            }

            with open(CACHE_FILE, "w", encoding="utf-8") as f:
                json.dump(problem, f, indent=2, ensure_ascii=False)

            print(f"OK (CN): [{problem['questionFrontendId']}] {problem.get('translatedTitle', problem['title'])} ({problem['difficulty']})")
            print("\n___PROBLEM_JSON_START___")
            print(json.dumps(problem, ensure_ascii=False))
            print("___PROBLEM_JSON_END___")
        except Exception as e2:
            print(f"CN fallback also failed: {e2}")
            sys.exit(1)
