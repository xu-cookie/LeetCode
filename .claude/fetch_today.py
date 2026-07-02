import requests
import json

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
headers = {
    "Content-Type": "application/json",
    "User-Agent": "Mozilla/5.0",
    "Referer": "https://leetcode.com"
}
response = requests.post(url, json={"query": query}, headers=headers, timeout=30)
data = response.json()
print(json.dumps(data, indent=2, ensure_ascii=False))
