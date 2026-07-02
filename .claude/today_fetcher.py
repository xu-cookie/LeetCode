import requests, json

url = "https://leetcode.com/graphql"
query = """
query questionOfToday {
    activeDailyCodingChallengeQuestion {
        date
        userStatus
        link
        question {
            title
            titleSlug
            difficulty
            questionFrontendId
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
headers = {"Content-Type": "application/json", "User-Agent": "Mozilla/5.0", "Referer": "https://leetcode.com"}
r = requests.post(url, json={"query": query}, headers=headers, timeout=30)
data = r.json()
with open(r"D:\LeetCode\.claude\today_data.json", "w", encoding="utf-8") as f:
    json.dump(data, f, indent=2, ensure_ascii=False)

q = data.get("data", {}).get("activeDailyCodingChallengeQuestion", {})
print("Date:", q.get("date"))
print("Link:", q.get("link"))
question = q.get("question", {})
print("Title:", question.get("title"))
print("TitleSlug:", question.get("titleSlug"))
print("Difficulty:", question.get("difficulty"))
print("FrontendID:", question.get("questionFrontendId"))
for s in question.get("codeSnippets", []):
    print(f"Snippet: {s['lang']} ({s['langSlug']})")
