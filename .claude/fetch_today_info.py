"""
Fetch today's LeetCode daily challenge and print info.
Usage: python .claude\fetch_today_info.py
"""
import json
import urllib.request

TODAY = "2026-06-28"

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
        }
    }
}
"""

body = {"query": query}
headers = {
    "Content-Type": "application/json",
    "User-Agent": "Mozilla/5.0"
}

req = urllib.request.Request(
    "https://leetcode.com/graphql",
    data=json.dumps(body).encode("utf-8"),
    headers=headers,
    method="POST"
)

with urllib.request.urlopen(req, timeout=30) as resp:
    data = json.loads(resp.read().decode("utf-8"))

challenge = data["data"]["activeDailyCodingChallengeQuestion"]
q = challenge["question"]

print(f"日期:       {challenge['date']}")
print(f"题目编号:   {q['questionFrontendId']}")
print(f"标题:       {q['title']}")
print(f"Slug:       {q['titleSlug']}")
print(f"难度:       {q['difficulty']}")
print(f"链接:       https://leetcode.com{q.get('link', '/problems/' + q['titleSlug'])}")
print()
print("--- 题目描述 ---")
# Strip HTML tags for cleaner output
import re
content = q.get("content", "")
content = re.sub(r'<[^>]+>', '', content)
content = re.sub(r'&nbsp;', ' ', content)
content = re.sub(r'&lt;', '<', content)
content = re.sub(r'&gt;', '>', content)
content = re.sub(r'&amp;', '&', content)
content = re.sub(r'\n{3,}', '\n\n', content)
print(content)
