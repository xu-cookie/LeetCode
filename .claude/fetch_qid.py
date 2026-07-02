"""Fetch internal questionId from LeetCode CN for a given slug."""
import json
import urllib.request
import sys
import os

slug = sys.argv[1] if len(sys.argv) > 1 else "maximum-number-of-balloons"

query = {
    "query": "query($slug: String!) { question(titleSlug: $slug) { questionId questionFrontendId title translatedTitle difficulty codeSnippets { lang langSlug code } } }",
    "variables": {"slug": slug}
}

headers = {
    "Content-Type": "application/json",
    "User-Agent": "Mozilla/5.0"
}

try:
    req = urllib.request.Request(
        "https://leetcode.cn/graphql/",
        data=json.dumps(query).encode("utf-8"),
        headers=headers
    )
    with urllib.request.urlopen(req, timeout=30) as resp:
        result = json.loads(resp.read().decode("utf-8"))
except Exception as e:
    print(f"ERROR: {e}")
    sys.exit(1)

q = result.get("data", {}).get("question", {})
if not q:
    print("ERROR: No question data returned")
    sys.exit(1)

internal_id = q.get("questionId", "")
frontend_id = q.get("questionFrontendId", "")
title = q.get("title", "")
translated = q.get("translatedTitle", "")

print(f"Internal ID: {internal_id}")
print(f"Frontend ID: {frontend_id}")
print(f"Title: {title}")
print(f"Translated: {translated}")

# Save to cache
cache_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "today_problem.json")
if os.path.exists(cache_path):
    with open(cache_path, "r", encoding="utf-8") as f:
        cache = json.load(f)
    cache["questionId"] = internal_id
    with open(cache_path, "w", encoding="utf-8") as f:
        json.dump(cache, f, indent=2, ensure_ascii=False)
    print(f"Updated {cache_path} with questionId: {internal_id}")

# Print just the ID for easy capture
print(f"\nQID={internal_id}")
