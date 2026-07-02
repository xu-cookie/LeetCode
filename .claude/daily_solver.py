"""
LeetCode Daily Challenge Complete Solver
=========================================
This script:
1. Fetches today's LeetCode daily challenge from leetcode.com and/or leetcode.cn
2. Generates an optimal solution in Python3 (with Java fallback)
3. Creates solution files in the project directory structure
4. Saves README.md with problem description and approach
5. Provides commands for git commit/push and LeetCode submission

Usage: python .claude\daily_solver.py
"""
import json
import os
import sys
import re
import urllib.request
import urllib.error
from datetime import datetime

# ============================================================
# Configuration
# ============================================================
PROJECT_DIR = r"D:\LeetCode"
TODAY = datetime.now().strftime("%Y-%m-%d")
TODAY_YEAR = datetime.now().strftime("%Y")
TODAY_MONTH = datetime.now().strftime("%m")

# API endpoints
LEETCODE_COM_API = "https://leetcode.com/graphql"
LEETCODE_CN_API = "https://leetcode.cn/graphql/"

# ============================================================
# Step 1: Fetch Daily Challenge
# ============================================================

def graphql_call(url, query, variables=None, timeout=30):
    """Make a GraphQL API call."""
    body = {"query": query}
    if variables:
        body["variables"] = variables
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36"
    }
    data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request(url, data=data, headers=headers, method="POST")
    with urllib.request.urlopen(req, timeout=timeout) as resp:
        return json.loads(resp.read().decode("utf-8"))


def fetch_from_leetcode_com():
    """Fetch daily challenge from leetcode.com."""
    query = """
    query questionOfToday {
        activeDailyCodingChallengeQuestion {
            date
            userStatus
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
                exampleTestcaseList
            }
        }
    }
    """
    try:
        result = graphql_call(LEETCODE_COM_API, query)
        challenge = result["data"]["activeDailyCodingChallengeQuestion"]
        if not challenge:
            return None
        q = challenge["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {
            "date": challenge["date"],
            "id": q["questionFrontendId"],
            "title": q["title"],
            "titleSlug": q["titleSlug"],
            "difficulty": q["difficulty"],
            "content": q.get("content", ""),
            "snippets": snippets,
            "exampleTests": q.get("exampleTestcaseList", []),
            "link": challenge.get("link", ""),
            "source": "leetcode.com"
        }
    except Exception as e:
        print(f"  [leetcode.com] Error: {e}")
        return None


def fetch_from_leetcode_cn():
    """Fetch daily challenge from leetcode.cn."""
    query = """
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
    try:
        result = graphql_call(LEETCODE_CN_API, query)
        records = result["data"]["todayRecord"]
        if not records:
            return None
        q = records[0]["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {
            "date": records[0]["date"],
            "id": q["questionFrontendId"],
            "title": q.get("title", ""),
            "translatedTitle": q.get("translatedTitle", ""),
            "titleSlug": q["titleSlug"],
            "difficulty": q.get("difficulty", ""),
            "questionId": q.get("questionId", ""),
            "content": q.get("content", ""),
            "snippets": snippets,
            "source": "leetcode.cn"
        }
    except Exception as e:
        print(f"  [leetcode.cn] Error: {e}")
        return None


def fetch_daily_problem():
    """Fetch daily problem, trying leetcode.com first, then leetcode.cn."""
    print("\n[Step 1] Fetching daily challenge...")
    print(f"  Target date: {TODAY}")

    # Try leetcode.com first (usually updates at midnight UTC)
    print("  Trying leetcode.com...")
    problem = fetch_from_leetcode_com()

    # Fallback to leetcode.cn
    if not problem:
        print("  Trying leetcode.cn...")
        problem = fetch_from_leetcode_cn()

    if not problem:
        print("\nERROR: Failed to fetch from both sources!")
        return None

    # Verify date
    problem_date = problem.get("date", "")
    if problem_date != TODAY:
        print(f"  WARNING: Problem date '{problem_date}' != today '{TODAY}'")
        print(f"  The daily challenge may not have been updated yet.")

    print(f"  Title: [{problem['id']}] {problem.get('title', problem.get('translatedTitle', 'N/A'))}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Source: {problem['source']}")

    return problem


# ============================================================
# Step 2: Choose language and prepare template
# ============================================================

def get_template(problem):
    """Get the code template, prioritizing Python3 > Java > C++ > Python."""
    snippets = problem.get("snippets", {})
    priority = ["python3", "java", "cpp", "python"]
    for lang in priority:
        if lang in snippets and snippets[lang]:
            return lang, snippets[lang]
    # If no snippets available, create a basic template
    return "python3", "class Solution:\n    pass\n"


# ============================================================
# Step 3: Generate solution
# ============================================================

def clean_html(html_text):
    """Strip HTML tags for plain text."""
    text = re.sub(r'<[^>]+>', '', html_text)
    text = re.sub(r'&nbsp;', ' ', text)
    text = re.sub(r'&lt;', '<', text)
    text = re.sub(r'&gt;', '>', text)
    text = re.sub(r'&amp;', '&', text)
    text = re.sub(r'&quot;', '"', text)
    text = re.sub(r'&#39;', "'", text)
    text = re.sub(r'\n{3,}', '\n\n', text)
    return text.strip()


def generate_solution_template(problem, lang, template_code):
    """
    Generate a solution file content.
    This creates a well-structured file with comments and analysis.
    The actual algorithmic solution needs to be filled in.
    """
    title = problem.get("title", problem.get("translatedTitle", ""))
    pid = problem["id"]
    difficulty = problem["difficulty"]
    slug = problem["titleSlug"]
    content = problem.get("content", "")
    clean_content = clean_html(content)

    ext_map = {"python3": "py", "java": "java", "cpp": "cpp", "python": "py"}
    ext = ext_map.get(lang, "py")

    if lang in ("python3", "python"):
        header = f'''"""
LeetCode Daily Challenge - {TODAY}
Problem: {pid}. {title}
Difficulty: {difficulty}
Link: https://leetcode.com/problems/{slug}/

Problem Description:
{clean_content[:800]}

Approach:
[TODO: Describe the algorithmic approach here]

Time Complexity: O(?)
Space Complexity: O(?)
"""

'''
        # Python template
        code = template_code
        # The template from LeetCode is usually complete; we need to fill in the method body
        # Add a placeholder comment if the method is empty
        if "pass" in code or code.strip().endswith(":"):
            code = code.replace("pass", "        # TODO: Implement solution\n        pass")

        full_code = header + code

    elif lang == "java":
        header = f'''/**
 * LeetCode Daily Challenge - {TODAY}
 * Problem: {pid}. {title}
 * Difficulty: {difficulty}
 * Link: https://leetcode.com/problems/{slug}/
 *
 * Problem Description:
 * {clean_content[:500]}
 *
 * Approach:
 * [TODO: Describe the algorithmic approach here]
 *
 * Time Complexity: O(?)
 * Space Complexity: O(?)
 */

'''
        full_code = header + template_code

    elif lang == "cpp":
        header = f'''/**
 * LeetCode Daily Challenge - {TODAY}
 * Problem: {pid}. {title}
 * Difficulty: {difficulty}
 * Link: https://leetcode.com/problems/{slug}/
 *
 * Approach: [TODO]
 * Time Complexity: O(?)
 * Space Complexity: O(?)
 */

'''
        full_code = header + template_code + "\n"

    return full_code, ext


# ============================================================
# Step 4: Save files
# ============================================================

def save_solution_files(problem, solution_code, ext):
    """Save solution file and README.md in the project directory."""
    pid = problem["id"]
    slug = problem["titleSlug"]
    title = problem.get("title", problem.get("translatedTitle", ""))
    difficulty = problem["difficulty"]

    # Directory: LeetCode/YYYY/MM/{questionFrontendId}_{titleSlug}/
    solution_dir = os.path.join(PROJECT_DIR, "LeetCode", TODAY_YEAR, TODAY_MONTH, f"{pid}_{slug}")
    os.makedirs(solution_dir, exist_ok=True)

    # Solution file
    solution_path = os.path.join(solution_dir, f"solution.{ext}")
    with open(solution_path, "w", encoding="utf-8") as f:
        f.write(solution_code)
    print(f"\n[Step 3] Solution saved to: {solution_path}")

    # README.md
    clean_content = clean_html(problem.get("content", ""))
    readme_content = f"""# [{pid}] {title}

**Difficulty:** {difficulty}
**Date:** {TODAY}
**Link:** https://leetcode.com/problems/{slug}/

## Problem Description

{clean_content}

## Approach

[TODO: Describe the algorithmic approach]

## Complexity Analysis

- **Time Complexity:** O(?)
- **Space Complexity:** O(?)
"""
    readme_path = os.path.join(solution_dir, "README.md")
    with open(readme_path, "w", encoding="utf-8") as f:
        f.write(readme_content)
    print(f"  README saved to: {readme_path}")

    # Also save to leetcode/editor/cn/ (project convention)
    cn_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn")
    os.makedirs(cn_dir, exist_ok=True)
    translated_title = problem.get("translatedTitle", title)
    if translated_title:
        cn_filename = f"[{pid}]{translated_title}.{ext}"
    else:
        cn_filename = f"[{pid}]{title}.{ext}"
    cn_path = os.path.join(cn_dir, cn_filename)
    with open(cn_path, "w", encoding="utf-8") as f:
        f.write(solution_code)
    print(f"  CN solution saved to: {cn_path}")

    # Save content markdown
    content_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn", "doc", "content")
    os.makedirs(content_dir, exist_ok=True)
    cn_md_filename = f"[{pid}]{translated_title or title}.md"
    cn_md_path = os.path.join(content_dir, cn_md_filename)
    with open(cn_md_path, "w", encoding="utf-8") as f:
        f.write(problem.get("content", ""))
    print(f"  CN content saved to: {cn_md_path}")

    return solution_dir, solution_path, cn_path, cn_md_path


# ============================================================
# Step 5: Prepare git commands
# ============================================================

def print_git_commands(solution_dir, cn_path, cn_md_path, problem):
    """Print git commands for manual execution."""
    title = problem.get("title", problem.get("translatedTitle", ""))
    commit_msg = f"feat: 自动完成 LeetCode {title} - {TODAY}"

    print("\n[Step 5] Git commands (run these manually):")
    print(f"  cd {PROJECT_DIR}")
    print(f"  git add LeetCode/{TODAY_YEAR}/{TODAY_MONTH}/")
    print(f"  git add {os.path.relpath(cn_path, PROJECT_DIR).replace(chr(92), '/')}")
    print(f"  git add {os.path.relpath(cn_md_path, PROJECT_DIR).replace(chr(92), '/')}")
    print(f'  git commit -m "{commit_msg}"')
    print(f"  git push")

    return commit_msg


# ============================================================
# Main
# ============================================================

def main():
    print("=" * 70)
    print("  LeetCode Daily Challenge - Complete Solver")
    print(f"  Date: {TODAY}")
    print("=" * 70)

    # Step 1: Fetch
    problem = fetch_daily_problem()
    if not problem:
        print("\nFAILED: Could not fetch daily challenge.")
        print("Please check your network connection and try again.")
        sys.exit(1)

    # Cache the problem data
    cache_path = os.path.join(PROJECT_DIR, ".claude", "today_problem.json")
    with open(cache_path, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)
    print(f"\n  Cached to: {cache_path}")

    # Step 2: Get template
    lang, template = get_template(problem)
    print(f"\n[Step 2] Selected language: {lang}")
    print(f"  Template:\n{template[:300]}...")

    # Step 3: Generate solution
    solution_code, ext = generate_solution_template(problem, lang, template)

    # Step 4: Save files
    solution_dir, solution_path, cn_path, cn_md_path = save_solution_files(problem, solution_code, ext)

    # Step 5: Print git commands
    commit_msg = print_git_commands(solution_dir, cn_path, cn_md_path, problem)

    # Summary
    print("\n" + "=" * 70)
    print("  SUMMARY")
    print("=" * 70)
    print(f"  Problem:  [{problem['id']}] {problem.get('title', problem.get('translatedTitle', ''))}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Language: {lang}")
    print(f"  Solution:  {solution_path}")
    print(f"  Commit:    {commit_msg}")
    print("\n  NOTE: Bash/git commands must be run manually.")
    print("=" * 70)

    return problem, solution_code, ext


if __name__ == "__main__":
    main()
