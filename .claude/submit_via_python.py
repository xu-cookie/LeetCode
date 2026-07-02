"""LeetCode 提交脚本 - 使用 Python 避免各平台编码问题"""
import json
import urllib.request
import urllib.error
import sys
import os

def submit(slug, question_id, code_file, jwt, csrf_token=None):
    # 读取代码
    if not os.path.exists(code_file):
        print(f"错误: 找不到代码文件 {code_file}")
        return False

    with open(code_file, "r", encoding="utf-8") as f:
        code = f.read()

    print(f"代码长度: {len(code)} 字符")

    # 构建请求
    body = {
        "lang": "java",
        "question_id": question_id,
        "typed_code": code
    }
    data = json.dumps(body).encode("utf-8")

    headers = {
        "Content-Type": "application/json",
        "Cookie": f"LEETCODE_SESSION={jwt}",
        "Origin": "https://leetcode.cn",
        "Referer": f"https://leetcode.cn/problems/{slug}/"
    }

    if csrf_token and csrf_token not in ("在此填入你的 CSRF Token", ""):
        headers["Cookie"] += f"; csrftoken={csrf_token}"
        headers["x-csrftoken"] = csrf_token

    url = f"https://leetcode.cn/problems/{slug}/submit/"
    print(f"提交到: {url}")

    try:
        req = urllib.request.Request(url, data=data, headers=headers)
        with urllib.request.urlopen(req, timeout=30) as resp:
            result = json.loads(resp.read().decode("utf-8"))
    except urllib.error.HTTPError as e:
        error_body = e.read().decode("utf-8")
        print(f"HTTP 错误 {e.code}: {error_body}")
        return False
    except Exception as e:
        print(f"请求失败: {e}")
        return False

    submission_id = result.get("submission_id")
    if not submission_id:
        print(f"提交失败: {result}")
        return False

    print(f"提交成功! Submission ID: {submission_id}")

    # 轮询结果
    print("等待评测结果...")
    import time
    for _ in range(15):  # 最多等 30 秒
        time.sleep(2)
        check_req = urllib.request.Request(
            f"https://leetcode.cn/submissions/detail/{submission_id}/check/",
            headers={"Cookie": f"LEETCODE_SESSION={jwt}"}
        )
        try:
            with urllib.request.urlopen(check_req, timeout=15) as resp:
                check = json.loads(resp.read().decode("utf-8"))
        except Exception as e:
            print(f"查询失败: {e}")
            continue

        state = check.get("state")
        if state not in ("PENDING", "STARTED"):
            status_msg = check.get("status_msg", state)
            runtime = check.get("status_runtime", "N/A")
            memory = check.get("status_memory", "N/A")
            total = check.get("total_testcases", "?")
            correct = check.get("total_correct", "?")
            print(f"评测完成: {status_msg} | 通过: {correct}/{total} | 耗时: {runtime} | 内存: {memory}")

            # 写入日志
            log_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "leetcode_daily.log")
            from datetime import datetime
            ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            with open(log_file, "a", encoding="utf-8") as log:
                log.write(f"{ts} - [Submit] 结果: {status_msg} | 通过: {correct}/{total} | 耗时: {runtime} | 内存: {memory}\n")

            return state == "SUCCESS"

        print(f"  状态: {state}...")

    print("等待超时")
    return False

if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser(description="提交解答到 LeetCode CN")
    parser.add_argument("--slug", required=True, help="题目 slug，如 delete-the-middle-node-of-a-linked-list")
    parser.add_argument("--qid", required=True, help="LeetCode 内部 questionId（非前端题号），如 2216")
    parser.add_argument("--file", required=True, help="Java 解答文件路径")
    parser.add_argument("--auth-file", default=None, help="认证文件路径")
    args = parser.parse_args()

    # 从 auth 文件读取 JWT（避免命令行传长字符串）
    jwt = ""
    csrf = ""
    auth_path = args.auth_file
    if not auth_path:
        auth_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "leetcode_auth.json")

    if os.path.exists(auth_path):
        with open(auth_path, "r", encoding="utf-8") as f:
            auth = json.load(f)
        jwt = auth.get("cookie", "")
        csrf = auth.get("csrfToken", "")

    if not jwt or jwt.startswith("在此填入"):
        print("错误: 请先在 leetcode_auth.json 中填入有效的 Cookie")
        sys.exit(1)

    # Check JWT expiry before attempting submission
    try:
        parts = jwt.split(".")
        if len(parts) >= 2:
            payload = parts[1]
            payload = payload.replace("-", "+").replace("_", "/")
            while len(payload) % 4 != 0:
                payload += "="
            import base64
            decoded = json.loads(base64.b64decode(payload).decode("utf-8"))
            exp_unix = decoded.get("expired_time_")
            if exp_unix:
                from datetime import datetime
                exp_date = datetime.fromtimestamp(exp_unix)
                days_left = (exp_date - datetime.now()).days
                if days_left <= 0:
                    print(f"错误: JWT 已于 {exp_date.strftime('%Y-%m-%d')} 过期，请更新 leetcode_auth.json")
                    sys.exit(1)
                elif days_left <= 3:
                    print(f"警告: JWT 将在 {days_left} 天后过期 ({exp_date.strftime('%Y-%m-%d')})，请尽快更新")
    except Exception:
        pass  # JWT decode failed, try submission anyway

    success = submit(args.slug, args.qid, args.file, jwt, csrf)
    sys.exit(0 if success else 1)
