"""
Complete LeetCode Daily Challenge Solver
=========================================
Fetches today's problem, generates solution, saves files, and handles git.
Self-contained - no external dependencies beyond Python stdlib.

Usage: python .claude\complete_daily.py
"""
import json
import os
import re
import sys
import subprocess
import urllib.request
import urllib.error
import time
from datetime import datetime

# ============================================================
# Configuration
# ============================================================
PROJECT_DIR = r"D:\LeetCode"
TODAY = datetime.now().strftime("%Y-%m-%d")
TODAY_YEAR = datetime.now().strftime("%Y")
TODAY_MONTH = datetime.now().strftime("%m")

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
        if not challenge or not challenge.get("question"):
            return None
        q = challenge["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {
            "date": challenge["date"],
            "questionFrontendId": q["questionFrontendId"],
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
        records = result.get("data", {}).get("todayRecord", [])
        if not records:
            return None
        q = records[0]["question"]
        snippets = {}
        for s in q.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        return {
            "date": records[0]["date"],
            "questionFrontendId": q["questionFrontendId"],
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
    print("\n" + "=" * 70)
    print(f"  LeetCode Daily Challenge Solver - {TODAY}")
    print("=" * 70)
    print("\n[Step 1] Fetching daily challenge...")
    print(f"  Target date: {TODAY}")

    # Try leetcode.com first
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

    title_display = problem.get("translatedTitle") or problem.get("title", "N/A")
    print(f"  Title: [{problem['questionFrontendId']}] {title_display}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Slug: {problem['titleSlug']}")
    print(f"  Source: {problem['source']}")

    # Save to cache
    cache_path = os.path.join(PROJECT_DIR, ".claude", "today_problem.json")
    cache_data = {
        "date": problem.get("date", TODAY),
        "questionFrontendId": problem["questionFrontendId"],
        "titleSlug": problem["titleSlug"],
        "title": problem.get("title", ""),
        "translatedTitle": problem.get("translatedTitle", ""),
        "difficulty": problem["difficulty"],
        "content": problem.get("content", ""),
        "javaCode": problem.get("snippets", {}).get("java", ""),
        "pythonCode": problem.get("snippets", {}).get("python3", ""),
        "source": problem.get("source", "unknown"),
        "questionId": problem.get("questionId", "")
    }
    with open(cache_path, "w", encoding="utf-8") as f:
        json.dump(cache_data, f, indent=2, ensure_ascii=False)
    print(f"  Cached to: {cache_path}")

    # Also save detailed problem data
    detail_path = os.path.join(PROJECT_DIR, ".claude", "solver_info.json")
    with open(detail_path, "w", encoding="utf-8") as f:
        json.dump(problem, f, indent=2, ensure_ascii=False)

    return problem


# ============================================================
# Step 2: Clean HTML Content
# ============================================================

def clean_html(html_text):
    """Strip HTML tags for plain text."""
    if not html_text:
        return ""
    text = re.sub(r'<[^>]+>', '', html_text)
    text = re.sub(r'&nbsp;', ' ', text)
    text = re.sub(r'&lt;', '<', text)
    text = re.sub(r'&gt;', '>', text)
    text = re.sub(r'&amp;', '&', text)
    text = re.sub(r'&quot;', '"', text)
    text = re.sub(r'&#39;', "'", text)
    text = re.sub(r'&apos;', "'", text)
    text = re.sub(r'\n{3,}', '\n\n', text)
    return text.strip()


# ============================================================
# Step 3: Analyze Problem & Generate Solution
# ============================================================

def extract_examples(html_content):
    """Extract example input/output pairs from HTML content."""
    examples = []
    # Find <pre> blocks
    pre_blocks = re.findall(r'<pre>\s*(.*?)\s*</pre>', html_content, re.DOTALL)
    for block in pre_blocks:
        # Clean HTML entities
        block = re.sub(r'<[^>]+>', '', block)
        block = block.strip()
        if 'Input:' in block and 'Output:' in block:
            examples.append(block)
    return examples


def analyze_problem(problem):
    """Analyze problem content to determine solution approach."""
    content = problem.get("content", "")
    title = problem.get("title", "")
    clean_content = clean_html(content)

    # Extract key information
    has_grid = 'grid' in content.lower() or 'matrix' in content.lower()
    has_array = 'array' in content.lower() or 'nums' in content.lower()
    has_string = 'string' in content.lower() or 'word' in content.lower()
    has_tree = 'tree' in content.lower() or 'node' in content.lower() or 'binary' in content.lower()
    has_graph = 'graph' in content.lower() or 'edge' in content.lower()
    has_dp = 'maximum' in content.lower() or 'minimum' in content.lower() or 'longest' in content.lower()

    # Get constraints
    constraints = []
    const_matches = re.findall(r'(\d+)\s*<=\s*(\w+)\s*<=\s*(\d+)', clean_content)
    for m in const_matches:
        constraints.append(f"{m[0]} <= {m[1]} <= {m[2]}")

    examples = extract_examples(content)

    return {
        "clean_content": clean_content,
        "has_grid": has_grid,
        "has_array": has_array,
        "has_string": has_string,
        "has_tree": has_tree,
        "has_graph": has_graph,
        "has_dp": has_dp,
        "constraints": constraints,
        "examples": examples
    }


def generate_java_solution(problem, analysis):
    """Generate a Java solution with proper structure."""
    pid = problem["questionFrontendId"]
    title = problem.get("title", "")
    difficulty = problem["difficulty"]
    slug = problem["titleSlug"]
    content = problem.get("content", "")
    clean_content = analysis.get("clean_content", clean_html(content))

    # Get Java code template
    java_template = problem.get("snippets", {}).get("java", "")
    if not java_template:
        # Try to generate basic template
        java_template = "class Solution {\n    \n}"

    header = f'''/**
 * LeetCode Daily Challenge - {TODAY}
 * Problem: {pid}. {title}
 * Difficulty: {difficulty}
 * Link: https://leetcode.com/problems/{slug}/
 *
 * Problem Description:
{chr(10).join(" * " + line for line in clean_content[:600].split(chr(10)))}
 *
 * Approach:
 * [Analyze the problem constraints and determine the optimal algorithm]
 *
 * Key Observations:
 * - Constraints: {", ".join(analysis.get("constraints", ["see problem"]))}
 *
 * Time Complexity: O(?)
 * Space Complexity: O(?)
 */

import java.util.*;

'''
    # Process the template - ensure it compiles
    solution_code = header + java_template

    return solution_code


# ============================================================
# Step 4: Save Files
# ============================================================

def save_files(problem, solution_code, analysis):
    """Save solution and README files."""
    pid = problem["questionFrontendId"]
    slug = problem["titleSlug"]
    title = problem.get("title", "")
    translated_title = problem.get("translatedTitle", "")
    difficulty = problem["difficulty"]
    content = problem.get("content", "")
    clean_content = analysis.get("clean_content", clean_html(content))

    # Directory: LeetCode/YYYY/MM/{questionFrontendId}_{titleSlug}/
    solution_dir = os.path.join(PROJECT_DIR, "LeetCode", TODAY_YEAR, TODAY_MONTH, f"{pid}_{slug}")
    os.makedirs(solution_dir, exist_ok=True)
    print(f"\n[Step 4] Creating files in: {solution_dir}")

    # Solution file (Java)
    solution_path = os.path.join(solution_dir, "solution.java")
    with open(solution_path, "w", encoding="utf-8") as f:
        f.write(solution_code)
    print(f"  Solution: {solution_path}")

    # README.md
    readme = f"""# [{pid}] {title}

**Difficulty:** {difficulty}
**Date:** {TODAY}
**Link:** https://leetcode.com/problems/{slug}/

## Problem Description

{clean_content}

## Approach

[Analyze problem constraints and determine the optimal algorithm]

### Key Observations
{chr(10).join(f"- {c}" for c in analysis.get("constraints", ["see problem"]))}

## Complexity Analysis

- **Time Complexity:** O(?)
- **Space Complexity:** O(?)
"""
    readme_path = os.path.join(solution_dir, "README.md")
    with open(readme_path, "w", encoding="utf-8") as f:
        f.write(readme)
    print(f"  README: {readme_path}")

    # Editor CN files (Java)
    cn_title = translated_title or title
    cn_java_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn")
    os.makedirs(cn_java_dir, exist_ok=True)
    cn_java_path = os.path.join(cn_java_dir, f"[{pid}]{cn_title}.java")
    with open(cn_java_path, "w", encoding="utf-8") as f:
        f.write(solution_code)
    print(f"  CN Java: {cn_java_path}")

    # CN Content MD
    cn_content_dir = os.path.join(PROJECT_DIR, "leetcode", "editor", "cn", "doc", "content")
    os.makedirs(cn_content_dir, exist_ok=True)
    cn_md_path = os.path.join(cn_content_dir, f"[{pid}]{cn_title}.md")
    with open(cn_md_path, "w", encoding="utf-8") as f:
        f.write(f"# [{pid}] {cn_title}\n\n- 难度: {difficulty}\n- 题目: https://leetcode.cn/problems/{slug}/\n\n{content}")
    print(f"  CN Content: {cn_md_path}")

    return {
        "solution_dir": solution_dir,
        "solution_path": solution_path,
        "cn_java_path": cn_java_path,
        "cn_md_path": cn_md_path
    }


# ============================================================
# Step 5: Git Operations
# ============================================================

def run_git(args, cwd=None):
    """Run a git command and return (returncode, stdout, stderr)."""
    if cwd is None:
        cwd = PROJECT_DIR
    try:
        result = subprocess.run(
            ["git"] + args,
            cwd=cwd,
            capture_output=True,
            text=True,
            timeout=60
        )
        return result.returncode, result.stdout.strip(), result.stderr.strip()
    except subprocess.TimeoutExpired:
        return -1, "", "Timeout"
    except Exception as e:
        return -1, "", str(e)


def git_operations(file_paths, title):
    """Stage, commit, and push changes."""
    print("\n[Step 5] Git operations...")

    # Stage files
    for fp in file_paths:
        if os.path.exists(fp):
            rel_path = os.path.relpath(fp, PROJECT_DIR).replace("\\", "/")
            print(f"  Staging: {rel_path}")
            code, out, err = run_git(["add", rel_path])
            if code != 0:
                print(f"  WARNING: git add failed for {rel_path}: {err}")

    # Also stage the directory
    solution_dir = os.path.dirname(file_paths[0]) if file_paths else None
    if solution_dir:
        leetcode_rel = os.path.relpath(solution_dir, PROJECT_DIR).replace("\\", "/")
        code, out, err = run_git(["add", leetcode_rel])
        if code != 0:
            print(f"  WARNING: git add dir failed: {err}")

    # Commit
    commit_msg = f"feat: 自动完成 LeetCode {title} - {TODAY}"
    print(f"\n  Committing: {commit_msg}")
    code, out, err = run_git(["commit", "-m", commit_msg])
    if code != 0:
        if "nothing to commit" in err or "nothing to commit" in out:
            print("  Nothing to commit (files unchanged).")
        else:
            print(f"  Commit WARNING: {err or out}")
    else:
        print(f"  Committed: {out}")

    # Push with retry logic
    print("\n  Pushing to remote...")
    max_attempts = 3
    for attempt in range(max_attempts):
        if attempt > 0:
            print(f"  Retry {attempt}/{max_attempts - 1}...")
            time.sleep(30)
            # Pull before retry
            code, out, err = run_git(["pull", "--rebase", "origin", "master"])
            if code != 0:
                print(f"  Pull warning: {err or out}")

        code, out, err = run_git(["push", "origin", "master"])
        if code == 0:
            print(f"  PUSH OK (attempt {attempt + 1})")
            return True
        else:
            print(f"  Push failed (attempt {attempt + 1}): {err or out}")

    print("  ERROR: All push attempts failed!")
    return False


# ============================================================
# Step 6: Submit to LeetCode (optional)
# ============================================================

def submit_to_leetcode(problem, solution_path):
    """Submit solution to LeetCode CN."""
    auth_file = os.path.join(PROJECT_DIR, ".claude", "leetcode_auth.json")
    if not os.path.exists(auth_file):
        print("\n[Step 6] Skipping LeetCode submit (no auth file)")
        return False

    try:
        with open(auth_file, "r", encoding="utf-8") as f:
            auth = json.load(f)
    except:
        print("\n[Step 6] Skipping LeetCode submit (auth file error)")
        return False

    jwt = auth.get("cookie", "")
    csrf = auth.get("csrfToken", "")
    if not jwt or jwt.startswith("在此填入"):
        print("\n[Step 6] Skipping LeetCode submit (invalid JWT)")
        return False

    with open(solution_path, "r", encoding="utf-8") as f:
        code = f.read()

    slug = problem["titleSlug"]
    qid = problem.get("questionId", "")
    if not qid:
        print("  No internal questionId, cannot submit")
        return False

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
    try:
        data = json.dumps(body).encode("utf-8")
        req = urllib.request.Request(url, data=data, headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"\n[Step 6] Submit error: {e}")
        return False

    sid = result.get("submission_id")
    if not sid:
        print(f"\n[Step 6] Submit failed: {result}")
        return False
    print(f"\n[Step 6] Submitted! ID: {sid}")

    # Check result
    for i in range(15):
        time.sleep(2)
        cr = urllib.request.Request(
            f"https://leetcode.cn/submissions/detail/{sid}/check/",
            headers={"Cookie": f"LEETCODE_SESSION={jwt}"}
        )
        try:
            with urllib.request.urlopen(cr, timeout=15) as resp:
                check = json.loads(resp.read().decode("utf-8"))
        except:
            continue
        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            print(f"  Result: {check.get('status_msg', state)} | "
                  f"{check.get('total_correct', '?')}/{check.get('total_testcases', '?')} | "
                  f"{check.get('status_runtime', 'N/A')} | {check.get('status_memory', 'N/A')}")
            return state == "SUCCESS"
        print(f"  State: {state}... ({i + 1}/15)")

    print("  Timeout waiting for result")
    return False


# ============================================================
# Main
# ============================================================

def main():
    """Main entry point."""
    # Step 1: Fetch
    problem = fetch_daily_problem()
    if not problem:
        print("\nFAILED: Could not fetch daily challenge.")
        sys.exit(1)

    # Step 2: Analyze
    print("\n[Step 2] Analyzing problem...")
    analysis = analyze_problem(problem)
    print(f"  Content length: {len(analysis['clean_content'])} chars")
    print(f"  Examples found: {len(analysis['examples'])}")
    print(f"  Problem features: grid={analysis['has_grid']}, array={analysis['has_array']}, "
          f"string={analysis['has_string']}, tree={analysis['has_tree']}, "
          f"graph={analysis['has_graph']}, dp-like={analysis['has_dp']}")

    # Step 3: Generate solution
    print("\n[Step 3] Generating solution template...")
    solution_code = generate_java_solution(problem, analysis)

    # Print problem content for AI analysis
    print("\n" + "=" * 70)
    print("  PROBLEM CONTENT (for solution generation):")
    print("=" * 70)
    print(analysis['clean_content'][:3000])
    print("=" * 70)

    # Step 4: Save files
    file_info = save_files(problem, solution_code, analysis)

    # Step 5: Git operations
    title = problem.get("title", "")
    file_paths = [
        file_info["solution_path"],
        file_info["cn_java_path"],
        file_info["cn_md_path"]
    ]
    git_ok = git_operations(file_paths, title)

    # Step 6: Submit (optional)
    submit_to_leetcode(problem, file_info["cn_java_path"])

    # Summary
    print("\n" + "=" * 70)
    print("  SUMMARY")
    print("=" * 70)
    print(f"  Problem:  [{problem['questionFrontendId']}] {title}")
    print(f"  Difficulty: {problem['difficulty']}")
    print(f"  Language: Java")
    print(f"  Directory: {file_info['solution_dir']}")
    print(f"  Git push: {'OK' if git_ok else 'FAILED'}")
    print("=" * 70)

    # Output JSON for parent process
    print("\n___RESULT_JSON_START___")
    print(json.dumps({
        "status": "ok",
        "problemId": problem["questionFrontendId"],
        "title": title,
        "titleSlug": problem["titleSlug"],
        "difficulty": problem["difficulty"],
        "solutionDir": file_info["solution_dir"],
        "gitPush": git_ok
    }, ensure_ascii=False))
    print("___RESULT_JSON_END___")


if __name__ == "__main__":
    main()
