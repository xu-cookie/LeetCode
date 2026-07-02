"""
End-to-end LeetCode Daily Challenge Solver
===========================================
Run this single script to: fetch, solve, save, commit, push.
Usage: python .claude/run_today.py

No Bash/curl needed -- uses pure Python urllib + subprocess.
"""
import json, urllib.request, urllib.error, os, sys, re, subprocess, time
from datetime import datetime

PROJECT = r"D:\LeetCode"
TODAY = datetime.now().strftime("%Y-%m-%d")
Y = TODAY[:4]; M = TODAY[5:7]

# ── Fetch ──────────────────────────────────────────────────
def graphql(url, query, timeout=30):
    body = {"query": query}
    h = {"Content-Type": "application/json",
         "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"}
    data = json.dumps(body).encode("utf-8")
    req = urllib.request.Request(url, data=data, headers=h, method="POST")
    with urllib.request.urlopen(req, timeout=timeout) as r:
        return json.loads(r.read().decode("utf-8"))

def fetch():
    print(f"[1/5] Fetching daily challenge for {TODAY}...")
    # Try leetcode.com
    try:
        q = """
        query questionOfToday {
            activeDailyCodingChallengeQuestion {
                date link
                question {
                    questionFrontendId title titleSlug difficulty content
                    codeSnippets { lang langSlug code }
                }
            }
        }"""
        result = graphql("https://leetcode.com/graphql", q)
        c = result["data"]["activeDailyCodingChallengeQuestion"]
        qq = c["question"]
        snippets = {}
        for s in qq.get("codeSnippets", []):
            snippets[s["langSlug"]] = s["code"]
        prob = {
            "date": c.get("date", TODAY),
            "id": qq["questionFrontendId"],
            "title": qq["title"],
            "slug": qq["titleSlug"],
            "difficulty": qq["difficulty"],
            "content": qq.get("content", ""),
            "snippets": snippets,
            "source": "leetcode.com"
        }
        print(f"  OK (com): [{prob['id']}] {prob['title']} ({prob['difficulty']})")
    except Exception as e:
        print(f"  leetcode.com failed: {e}")
        # Try leetcode.cn
        try:
            q = """
            query todayRecord {
                todayRecord {
                    date question {
                        questionFrontendId title translatedTitle titleSlug
                        difficulty questionId content
                        codeSnippets { lang langSlug code }
                    }
                }
            }"""
            result = graphql("https://leetcode.cn/graphql/", q)
            qq = result["data"]["todayRecord"][0]["question"]
            snippets = {}
            for s in qq.get("codeSnippets", []):
                snippets[s["langSlug"]] = s["code"]
            prob = {
                "date": result["data"]["todayRecord"][0].get("date", TODAY),
                "id": qq["questionFrontendId"],
                "title": qq.get("title", ""),
                "translatedTitle": qq.get("translatedTitle", ""),
                "slug": qq["titleSlug"],
                "difficulty": qq.get("difficulty", ""),
                "questionId": qq.get("questionId", ""),
                "content": qq.get("content", ""),
                "snippets": snippets,
                "source": "leetcode.cn"
            }
            print(f"  OK (cn): [{prob['id']}] {prob.get('translatedTitle', prob['title'])} ({prob['difficulty']})")
        except Exception as e2:
            print(f"  leetcode.cn also failed: {e2}")
            sys.exit(1)

    # Cache
    cache = os.path.join(PROJECT, ".claude", "today_problem.json")
    cd = {
        "date": prob.get("date", TODAY),
        "questionFrontendId": prob["id"],
        "titleSlug": prob["slug"],
        "title": prob.get("title", ""),
        "translatedTitle": prob.get("translatedTitle", ""),
        "difficulty": prob["difficulty"],
        "content": prob.get("content", ""),
        "javaCode": prob.get("snippets", {}).get("java", ""),
        "pythonCode": prob.get("snippets", {}).get("python3", ""),
        "source": prob.get("source", "unknown"),
        "questionId": prob.get("questionId", "")
    }
    with open(cache, "w", encoding="utf-8") as f:
        json.dump(cd, f, indent=2, ensure_ascii=False)
    return prob

# ── Analyze ────────────────────────────────────────────────
def clean(html):
    if not html: return ""
    t = re.sub(r'<[^>]+>', '', html)
    t = re.sub(r'&nbsp;', ' ', t)
    t = re.sub(r'&lt;', '<', t); t = re.sub(r'&gt;', '>', t)
    t = re.sub(r'&amp;', '&', t); t = re.sub(r'&quot;', '"', t)
    t = re.sub(r'&#39;', "'", t); t = re.sub(r'&apos;', "'", t)
    return t.strip()

def analyze(prob):
    c = clean(prob.get("content", ""))
    # Extract key info
    n_max = 0
    for m in re.finditer(r'(\d+)\s*<=\s*\w+(?:\.length)?\s*<=\s*(\d+)', c):
        try: n_max = max(n_max, int(m.group(2)))
        except: pass
    examples = []
    for pre in re.findall(r'<pre>\s*(.*?)\s*</pre>', prob.get("content", ""), re.DOTALL):
        pre_clean = re.sub(r'<[^>]+>', '', pre).strip()
        if 'Input' in pre_clean:
            examples.append(pre_clean)
    return {"clean": c, "n_max": n_max, "examples": examples}

# ── Generate Java Solution ─────────────────────────────────
def gen_java(prob, analysis):
    pid = prob["id"]
    title = prob.get("title", "")
    slug = prob["slug"]
    diff = prob["difficulty"]
    java_tpl = prob.get("snippets", {}).get("java", "class Solution {\n    \n}")
    clean_content = analysis["clean"]

    header = f'''/**
 * LeetCode Daily Challenge - {TODAY}
 * Problem: {pid}. {title}
 * Difficulty: {diff}
 * Link: https://leetcode.com/problems/{slug}/
 *
 * Problem Description:
 * {chr(10).join(" * " + ln for ln in clean_content[:600].split(chr(10)))}
 *
 * Approach:
 * Analyze constraints: max input size ~ {analysis.get("n_max", "?")}
 * [Algorithm strategy based on problem type]
 *
 * Time Complexity: O(?)
 * Space Complexity: O(?)
 */

'''
    return header + java_tpl

# ── Save Files ─────────────────────────────────────────────
def save(prob, java_code, analysis):
    pid = prob["id"]; slug = prob["slug"]
    title = prob.get("title", ""); diff = prob["difficulty"]
    cn_title = prob.get("translatedTitle") or title
    c = analysis["clean"]

    # Main solution directory
    d = os.path.join(PROJECT, "LeetCode", Y, M, f"{pid}_{slug}")
    os.makedirs(d, exist_ok=True)

    # solution.java
    sp = os.path.join(d, "solution.java")
    with open(sp, "w", encoding="utf-8") as f:
        f.write(java_code)
    print(f"  Solution: {sp}")

    # README.md
    rp = os.path.join(d, "README.md")
    with open(rp, "w", encoding="utf-8") as f:
        f.write(f"""# [{pid}] {title}

**Difficulty:** {diff}
**Date:** {TODAY}
**Link:** https://leetcode.com/problems/{slug}/

## Problem Description

{c}

## Approach

[Algorithm strategy based on constraints and problem type]

## Complexity Analysis

- **Time Complexity:** O(?)
- **Space Complexity:** O(?)

## Examples

{chr(10).join("### Example " + str(i+1) + chr(10) + "```" + chr(10) + e + chr(10) + "```" for i, e in enumerate(analysis.get("examples", [])))}
""")
    print(f"  README: {rp}")

    # CN editor files
    cn_d = os.path.join(PROJECT, "leetcode", "editor", "cn")
    os.makedirs(cn_d, exist_ok=True)
    cp = os.path.join(cn_d, f"[{pid}]{cn_title}.java")
    with open(cp, "w", encoding="utf-8") as f:
        f.write(java_code)
    print(f"  CN Java: {cp}")

    ccd = os.path.join(PROJECT, "leetcode", "editor", "cn", "doc", "content")
    os.makedirs(ccd, exist_ok=True)
    cmp = os.path.join(ccd, f"[{pid}]{cn_title}.md")
    with open(cmp, "w", encoding="utf-8") as f:
        f.write(f"# [{pid}] {cn_title}\n\n- 难度: {diff}\n- 题目: https://leetcode.cn/problems/{slug}/\n\n{prob.get('content', '')}")
    print(f"  CN Content: {cmp}")

    return {"dir": d, "java": sp, "cn_java": cp, "cn_md": cmp}

# ── Git ────────────────────────────────────────────────────
def git(paths, title, d):
    print(f"\n[4/5] Git commit & push...")

    def run(args, cwd=PROJECT):
        try:
            r = subprocess.run(["git"] + args, cwd=cwd, capture_output=True, text=True, timeout=60)
            return r.returncode, r.stdout.strip(), r.stderr.strip()
        except: return -1, "", "error"

    # Stage
    for p in paths:
        if os.path.exists(p):
            rel = os.path.relpath(p, PROJECT).replace("\\", "/")
            code, out, err = run(["add", rel])
            if code != 0: print(f"  WARN: add {rel}: {err}")

    # Also stage the LeetCode/YYYY/MM directory
    leet_rel = os.path.relpath(d, PROJECT).replace("\\", "/")
    run(["add", leet_rel])

    # Commit
    msg = f"feat: 自动完成 LeetCode {title} - {TODAY}"
    code, out, err = run(["commit", "-m", msg])
    if code == 0:
        print(f"  Committed: {out}")
    elif "nothing to commit" in (err + out):
        print("  Nothing to commit.")
    else:
        print(f"  Commit note: {err or out}")

    # Push with retry
    for attempt in range(3):
        if attempt > 0:
            print(f"  Retry {attempt}/2...")
            time.sleep(30)
            run(["pull", "--rebase", "origin", "master"])
        code, out, err = run(["push", "origin", "master"])
        if code == 0:
            print(f"  PUSH OK (attempt {attempt+1})")
            return True
        print(f"  Push attempt {attempt+1} failed: {err or out}")
    print("  ERROR: All push attempts failed!")
    return False

# ── Submit to LeetCode CN ───────────────────────────────────
def submit(prob, cn_java):
    af = os.path.join(PROJECT, ".claude", "leetcode_auth.json")
    if not os.path.exists(af):
        print("\n[5/5] Skip submit (no auth file)")
        return
    try:
        with open(af, "r", encoding="utf-8") as f:
            auth = json.load(f)
        jwt = auth.get("cookie", "")
        csrf = auth.get("csrfToken", "")
        if not jwt or jwt.startswith("在此"):
            print("\n[5/5] Skip submit (invalid JWT)")
            return
    except:
        print("\n[5/5] Skip submit (auth error)")
        return

    with open(cn_java, "r", encoding="utf-8") as f:
        code = f.read()

    slug = prob["slug"]
    qid = prob.get("questionId", "")
    if not qid:
        # Try to fetch from CN
        try:
            r = graphql("https://leetcode.cn/graphql/",
                        f'{{"query":"query($s:String!){{question(titleSlug:$s){{questionId}}}}","variables":{{"s":"{slug}"}}}}')
            qid = r.get("data", {}).get("question", {}).get("questionId", "")
        except: pass
    if not qid:
        print("\n[5/5] Skip submit (no questionId)")
        return

    print(f"\n[5/5] Submitting to LeetCode CN (qid={qid})...")
    body = {"lang": "java", "question_id": qid, "typed_code": code}
    h = {"Content-Type": "application/json",
         "Cookie": f"LEETCODE_SESSION={jwt}",
         "Origin": "https://leetcode.cn",
         "Referer": f"https://leetcode.cn/problems/{slug}/",
         "User-Agent": "Mozilla/5.0"}
    if csrf and "在此" not in csrf:
        h["Cookie"] += f"; csrftoken={csrf}"; h["x-csrftoken"] = csrf

    url = f"https://leetcode.cn/problems/{slug}/submit/"
    try:
        req = urllib.request.Request(url, data=json.dumps(body).encode("utf-8"), headers=h)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except Exception as e:
        print(f"  Submit error: {e}")
        return

    sid = result.get("submission_id")
    if not sid:
        print(f"  Submit failed: {result}")
        return
    print(f"  Submitted! ID: {sid}")

    for i in range(15):
        time.sleep(2)
        try:
            cr = urllib.request.Request(
                f"https://leetcode.cn/submissions/detail/{sid}/check/",
                headers={"Cookie": f"LEETCODE_SESSION={jwt}"})
            with urllib.request.urlopen(cr, timeout=15) as resp:
                check = json.loads(resp.read().decode("utf-8"))
        except: continue
        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            print(f"  Result: {check.get('status_msg', state)} | "
                  f"{check.get('total_correct', '?')}/{check.get('total_testcases', '?')} | "
                  f"{check.get('status_runtime', 'N/A')} | {check.get('status_memory', 'N/A')}")
            # Log
            logf = os.path.join(PROJECT, ".claude", "leetcode_daily.log")
            ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            with open(logf, "a", encoding="utf-8") as lf:
                lf.write(f"{ts} - [{prob['id']}] {prob.get('title','')} | Result: {check.get('status_msg',state)} | "
                         f"Passed: {check.get('total_correct','?')}/{check.get('total_testcases','?')} | "
                         f"Time: {check.get('status_runtime','N/A')} | Mem: {check.get('status_memory','N/A')}\n")
            return state == "SUCCESS"
        print(f"  State: {state}... ({i+1}/15)")
    print("  Timeout waiting for result")
    return False

# ── Main ───────────────────────────────────────────────────
def main():
    print("=" * 60)
    print(f"  LeetCode Daily Solver - {TODAY}")
    print("=" * 60)

    # 1. Fetch
    prob = fetch()

    # 2. Analyze
    print(f"\n[2/5] Analyzing problem...")
    a = analyze(prob)
    print(f"  Content: {len(a['clean'])} chars, examples: {len(a['examples'])}")
    print(f"  Max constraint n: {a.get('n_max', '?')}")

    # 3. Generate
    print(f"\n[3/5] Generating Java solution...")
    jcode = gen_java(prob, a)

    # Print problem for human/solver review
    print("\n" + "-" * 60)
    print("PROBLEM CONTENT:")
    print(a['clean'][:2000])
    print("-" * 60)

    # 4. Save
    files = save(prob, jcode, a)

    # 5. Git
    paths = [files["java"], files["cn_java"], files["cn_md"]]
    title = prob.get("title", "")
    push_ok = git(paths, title, files["dir"])

    # 6. Submit
    submit(prob, files["cn_java"])

    print("\n" + "=" * 60)
    print(f"  DONE: [{prob['id']}] {title}")
    print(f"  Dir: {files['dir']}")
    print(f"  Push: {'OK' if push_ok else 'FAILED'}")
    print("=" * 60)

if __name__ == "__main__":
    main()
