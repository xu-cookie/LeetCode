"""
Fetch LeetCode Daily Challenge and save to JSON file.
Tries leetcode.com first, then falls back to leetcode.cn.
Usage: python .claude/fetch_daily.py
Output: .claude/today_problem.json
"""
import json
import urllib.request
import urllib.error
import os
import sys
from datetime import datetime

TODAY = datetime.now().strftime("%Y-%m-%d")

def graphql_call(url, query, variables=None, timeout=30):
    """Make a GraphQL API call."""
    body = {"query": query}
    if variables:
        body["variables"] = variables
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
    }
    data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request(url, data=data, headers=headers, method="POST")
    with urllib.request.urlopen(req, timeout=timeout) as resp:
        return json.loads(resp.read().decode("utf-8"))


def fetch_leetcode_com():
    """Fetch daily challenge from leetcode.com (English, updates midnight UTC)."""
    query = """
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
    try:
        result = graphql_call("https://leetcode.com/graphql", query)
        challenge = result["data"]["activeDailyCodingChallengeQuestion"]
        if not challenge or not challenge.get("question"):
            return None
        q = challenge["question"]

        java_code = ""
        python_code = ""
        for s in q.get("codeSnippets", []):
            if s.get("langSlug") == "java":
                java_code = s.get("code", "")
            elif s.get("langSlug") == "python3":
                python_code = s.get("code", "")

        return {
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
    except Exception as e:
        print(f"  [leetcode.com] Error: {e}")
        return None


def fetch_leetcode_cn():
    """Fetch daily challenge from leetcode.cn (Chinese)."""
    query = """
    query todayRecord {
        todayRecord {
            date
            question {
                questionFrontendId
                titleSlug
                translatedTitle
                title
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
    try:
        result = graphql_call("https://leetcode.cn/graphql/", query)
        records = result.get("data", {}).get("todayRecord", [])
        if not records:
            print("Error: No todayRecord in response")
            return None

        challenge = records[0]
        question = challenge.get("question", {})

        java_code = ""
        python_code = ""
        for snippet in question.get("codeSnippets", []):
            if snippet.get("langSlug") == "java":
                java_code = snippet.get("code", "")
            elif snippet.get("langSlug") == "python3":
                python_code = snippet.get("code", "")

        return {
            "date": challenge.get("date", TODAY),
            "questionFrontendId": question.get("questionFrontendId", ""),
            "titleSlug": question.get("titleSlug", ""),
            "translatedTitle": question.get("translatedTitle", ""),
            "title": question.get("title", ""),
            "difficulty": question.get("difficulty", ""),
            "questionId": question.get("questionId", ""),
            "content": question.get("content", ""),
            "javaCode": java_code,
            "pythonCode": python_code,
            "source": "leetcode.cn"
        }
    except Exception as e:
        print(f"  [leetcode.cn] Error: {e}")
        return None


def fetch_daily_problem():
    """Try leetcode.com first, fallback to leetcode.cn."""
    print(f"Fetching LeetCode daily challenge... (target date: {TODAY})")

    # Try leetcode.com first (usually updates at midnight UTC, earlier availability)
    print("  Trying leetcode.com...")
    problem = fetch_leetcode_com()
    if problem:
        problem_date = problem.get("date", "")
        if problem_date == TODAY:
            print(f"  Got today's problem from leetcode.com!")
            return problem
        else:
            print(f"  leetcode.com returned date '{problem_date}', not today '{TODAY}'")

    # Fallback to leetcode.cn
    print("  Trying leetcode.cn...")
    problem = fetch_leetcode_cn()
    if problem:
        problem_date = problem.get("date", "")
        if problem_date == TODAY:
            print(f"  Got today's problem from leetcode.cn!")
            return problem
        else:
            print(f"  leetcode.cn returned date '{problem_date}', not today '{TODAY}'")

    # If neither returned today's problem, return whatever we got (stale is better than nothing)
    if problem:
        print(f"  WARNING: Returning problem dated {problem_date} (not today)")
        return problem

    return None


def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_file = os.path.join(script_dir, "today_problem.json")

    problem = fetch_daily_problem()

    if problem is None:
        print("FAILED to fetch daily problem from both sources.")
        sys.exit(1)

    # Save to JSON file
    with open(output_file, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)

    print(f"\nSuccess! Problem saved to: {output_file}")
    title_display = problem.get("translatedTitle") or problem.get("title", "N/A")
    print(f"  Problem #{problem['questionFrontendId']}: {title_display}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Slug: {problem['titleSlug']}")
    print(f"  Source: {problem.get('source', 'unknown')}")
    if problem.get("questionId"):
        print(f"  Internal ID: {problem['questionId']}")

    # Print compact JSON for capture by parent process
    # Use ascii-only to avoid GBK encoding errors on Windows
    print("\n___PROBLEM_JSON_START___")
    try:
        print(json.dumps(problem, ensure_ascii=False))
    except UnicodeEncodeError:
        # Fallback: strip non-ASCII characters to avoid GBK issues
        safe = json.dumps(problem, ensure_ascii=False)
        safe = safe.encode('ascii', errors='replace').decode('ascii')
        print(safe)
    print("___PROBLEM_JSON_END___")

    sys.exit(0)


if __name__ == "__main__":
    main()
