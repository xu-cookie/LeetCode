/**
 * LeetCode 3614 - 用特殊操作处理字符串 II (Process String with Special Operations II)
 * Difficulty: Hard
 *
 * 问题描述:
 * 给定一个由小写英文字母和特殊字符 '*'、'#'、'%' 组成的字符串 s，以及一个整数 k。
 * 按照以下规则从左到右处理 s 构建新字符串 result：
 * - 小写字母：将其追加到 result 末尾
 * - '*'：如果存在，删除 result 的最后一个字符
 * - '#'：将当前 result 复制一份并追加到自身末尾（result = result + result）
 * - '%'：反转当前 result
 * 返回最终 result 的第 k 个字符（0-indexed）。如果 k 超出 result 范围，返回 '.'。
 *
 * 约束:
 * - 1 <= s.length <= 10^5
 * - s 仅包含小写字母和 '*', '#', '%'
 * - 0 <= k <= 10^15
 * - result 最终长度不超过 10^15
 *
 * 解题思路:
 * 由于 result 最终长度可达 10^15，无法直接模拟构建字符串。需要采用逆向追踪法：
 *
 * 1. 正向遍历：记录每次操作后 result 的长度（用 long 存储，不会溢出）
 *    - 字母: len++
 *    - '*': len = max(0, len-1)
 *    - '#': len *= 2
 *    - '%': len 不变
 *
 * 2. 边界判断：如果最终长度 <= k，返回 '.'
 *
 * 3. 逆向遍历：从最后一个操作开始，反向推导第 k 个字符的来源
 *    - '*' (逆向): 正向时删除了末尾字符，逆向时 k 位置不变（被删字符在末尾之后）
 *    - 字母 (逆向): 如果 k == curLen-1（即该字母被追加的位置），直接返回该字母；
 *                   否则 k 不变，继续向前查找
 *    - '%' (逆向): 反转的逆操作还是反转，k = curLen - 1 - k
 *    - '#' (逆向): 复制操作的逆操作是取模，k = k % (curLen/2)
 *
 * 时间复杂度: O(n)
 * 空间复杂度: O(n) — 存储长度数组
 */
class Solution {
    public char processStr(String s, long k) {
        int n = s.length();

        // ========== 第一遍：正向计算每次操作后的长度 ==========
        long[] lens = new long[n];
        long len = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c >= 'a' && c <= 'z') {
                len++;
            } else if (c == '*') {
                if (len > 0) {
                    len--;
                }
            } else if (c == '#') {
                // 防止溢出：long 最大值约 9.2e18，远大于题目上限 1e15
                // 但为了安全，如果翻倍会溢出则设为 Long.MAX_VALUE
                if (len > Long.MAX_VALUE / 2) {
                    len = Long.MAX_VALUE;
                } else {
                    len *= 2;
                }
            }
            // '%' 不改变长度
            lens[i] = len;
        }

        // ========== 边界判断 ==========
        if (lens[n - 1] <= k) {
            return '.';
        }

        // ========== 第二遍：逆向追踪第 k 个字符的来源 ==========
        for (int i = n - 1; i >= 0; i--) {
            char c = s.charAt(i);
            long curLen = lens[i];
            long prevLen = (i > 0) ? lens[i - 1] : 0;

            if (c >= 'a' && c <= 'z') {
                // 该字母被追加到 result 末尾（位置 curLen-1）
                if (k == curLen - 1) {
                    return c;
                }
                // 否则 k 在之前的字符串中，位置不变
            } else if (c == '%') {
                // 反转操作：新位置 = 总长度 - 1 - 旧位置
                // 逆操作同理
                k = curLen - 1 - k;
            } else if (c == '#') {
                // 复制操作：原字符串长度 = curLen / 2
                // 新字符串的前半部分对应原字符串，后半部分也对应原字符串
                // 逆操作：k = k % 原长度
                k = k % prevLen;
            }
            // '*' 操作：逆操作时 k 不变（正向时删除了末尾字符，
            // 逆向时该字符被加回去，位于 prevLen-1 处，不影响 k < curLen 的位置）
        }

        return '.'; // 理论上不会执行到这里
    }
}
