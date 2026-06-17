"""
Fetch LeetCode Daily Challenge and save to JSON file.
Usage: python .claude/fetch_daily.py
Output: .claude/today_problem.json
"""
import json
import urllib.request
import urllib.error
import os
import sys

def fetch_daily_problem():
    """Fetch today's LeetCode daily challenge from leetcode.cn GraphQL API."""

    # GraphQL query for today's problem (full details in one query)
    # Uses todayRecord (leetcode.cn) instead of activeDailyCodingChallengeQuestion (leetcode.com)
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

    body = json.dumps({"query": query, "variables": {}}).encode("utf-8")

    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    }

    url = "https://leetcode.cn/graphql/"

    try:
        req = urllib.request.Request(url, data=body, headers=headers, method="POST")
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        error_body = e.read().decode("utf-8")
        print(f"HTTP Error {e.code}: {error_body}")
        return None
    except Exception as e:
        print(f"Request failed: {e}")
        return None

    # Parse the response
    try:
        records = result.get("data", {}).get("todayRecord", [])
        if not records:
            print("Error: No todayRecord in response")
            print(f"Raw response: {json.dumps(result, indent=2, ensure_ascii=False)[:500]}")
            return None

        challenge = records[0]  # todayRecord is a list, take first entry
        question = challenge.get("question", {})

        # Find Java code snippet
        java_code = ""
        code_snippets = question.get("codeSnippets", [])
        for snippet in code_snippets:
            if snippet.get("langSlug") == "java":
                java_code = snippet.get("code", "")
                break

        problem = {
            "date": challenge.get("date", ""),
            "questionFrontendId": question.get("questionFrontendId", ""),
            "titleSlug": question.get("titleSlug", ""),
            "translatedTitle": question.get("translatedTitle", ""),
            "title": question.get("title", ""),
            "difficulty": question.get("difficulty", ""),
            "questionId": question.get("questionId", ""),
            "content": question.get("content", ""),
            "javaCode": java_code,
        }

        return problem

    except Exception as e:
        print(f"Error parsing response: {e}")
        print(f"Raw response: {json.dumps(result, indent=2, ensure_ascii=False)[:500]}")
        return None


def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_file = os.path.join(script_dir, "today_problem.json")

    print("Fetching LeetCode daily challenge from leetcode.cn...")
    problem = fetch_daily_problem()

    if problem is None:
        print("FAILED to fetch daily problem.")
        sys.exit(1)

    # Save to JSON file
    with open(output_file, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)

    print(f"\nSuccess! Problem saved to: {output_file}")
    print(f"  Problem #{problem['questionFrontendId']}: {problem['translatedTitle']} ({problem['title']})")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Slug: {problem['titleSlug']}")
    print(f"  Internal ID: {problem['questionId']}")

    # Print compact JSON for capture
    print("\n___PROBLEM_JSON_START___")
    print(json.dumps(problem, ensure_ascii=False))
    print("___PROBLEM_JSON_END___")

    sys.exit(0)


if __name__ == "__main__":
    main()
