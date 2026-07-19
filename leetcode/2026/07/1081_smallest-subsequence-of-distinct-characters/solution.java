/**
 * LeetCode Daily Challenge - 2026-07-19
 * Problem: 1081. Smallest Subsequence of Distinct Characters
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/
 *
 * Note: This question is the same as 316. Remove Duplicate Letters:
 * https://leetcode.com/problems/remove-duplicate-letters/
 *
 * Problem Description:
 * Given a string s, return the lexicographically smallest subsequence of s
 * that contains all the distinct characters of s exactly once.
 *
 * Approach: Greedy Monotonic Stack
 *
 * Key Insight:
 * We want to construct the result character by character, making greedy
 * choices: when we see a character c, if c is smaller than the last char
 * in our result AND that last char will appear again later, we should
 * "pop" that last char now (and add it back later) so c can appear earlier
 * in the result — producing a smaller lexicographic order.
 *
 * Algorithm:
 *   1. `last[c]` stores the last index where character c appears in s.
 *   2. `inStack[c]` tracks whether c is currently in the result.
 *   3. Iterate through s:
 *      a. Skip if c is already in the result (must appear exactly once).
 *      b. While stack is non-empty, c < stack top, AND stack top appears
 *         later (last[top] > current index): pop and mark as removed.
 *      c. Push c onto stack and mark as in-stack.
 *   4. The stack (StringBuilder) now contains the answer.
 *
 * Time Complexity:  O(n) — each character is pushed/popped at most once.
 * Space Complexity: O(1) — arrays of size 26 for last/inStack, plus
 *                    StringBuilder of at most 26 characters (lowercase alphabet).
 */
class Solution {
    public String smallestSubsequence(String s) {
        int n = s.length();
        int[] last = new int[26];
        boolean[] inStack = new boolean[26];

        // Record the last occurrence index of each character
        for (int i = 0; i < n; i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int idx = c - 'a';

            // If character is already in the result, skip
            if (inStack[idx]) {
                continue;
            }

            // Greedy: while the current character is smaller than the top
            // of stack, and the top character will appear later, pop it
            while (sb.length() > 0
                   && c < sb.charAt(sb.length() - 1)
                   && last[sb.charAt(sb.length() - 1) - 'a'] > i) {
                char removed = sb.charAt(sb.length() - 1);
                inStack[removed - 'a'] = false;
                sb.deleteCharAt(sb.length() - 1);
            }

            sb.append(c);
            inStack[idx] = true;
        }

        return sb.toString();
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: s = "bcabc" → "abc"
        // Process: b→(b), c→(bc), a<(bc)且b,c后出现→pop掉bc→(a), b→(ab), c→(abc)
        String r1 = sol.smallestSubsequence("bcabc");
        System.out.println("Example 1: " + r1 + " (expected \"abc\") — "
                + (r1.equals("abc") ? "PASS" : "FAIL"));

        // Example 2: s = "cbacdcbc" → "acdb"
        // Process: c→(c), b<(c)且c后出现→pop c→(b), a<(b)且b后出现→pop b→(a),
        //          c→(ac), d>(c)→(acd), c已在栈中→skip, b<(d)但d不再出现→append→(acdb),
        //          最后一个c已在栈中→skip
        String r2 = sol.smallestSubsequence("cbacdcbc");
        System.out.println("Example 2: " + r2 + " (expected \"acdb\") — "
                + (r2.equals("acdb") ? "PASS" : "FAIL"));

        // Single character: s = "a" → "a"
        String r3 = sol.smallestSubsequence("a");
        System.out.println("Single char 'a': " + r3 + " (expected \"a\") — "
                + (r3.equals("a") ? "PASS" : "FAIL"));

        // All same: s = "aaaa" → "a" (all distinct chars exactly once)
        String r4 = sol.smallestSubsequence("aaaa");
        System.out.println("All same 'aaaa': " + r4 + " (expected \"a\") — "
                + (r4.equals("a") ? "PASS" : "FAIL"));

        // Already sorted: s = "abc" → "abc"
        String r5 = sol.smallestSubsequence("abc");
        System.out.println("Already sorted 'abc': " + r5 + " (expected \"abc\") — "
                + (r5.equals("abc") ? "PASS" : "FAIL"));

        // Reverse sorted: s = "cba" → "cba" (each appears once, can't reorder)
        String r6 = sol.smallestSubsequence("cba");
        System.out.println("Reverse sorted 'cba': " + r6 + " (expected \"cba\") — "
                + (r6.equals("cba") ? "PASS" : "FAIL"));

        // All distinct, lexicographically tricky: s = "cdadabcc" → "adbc"
        String r7 = sol.smallestSubsequence("cdadabcc");
        System.out.println("Complex 'cdadabcc': " + r7 + " (expected \"adbc\") — "
                + (r7.equals("adbc") ? "PASS" : "FAIL"));

        // All 26 letters once, order preserved: s = "abcdefghijklmnopqrstuvwxyz"
        String r8 = sol.smallestSubsequence("abcdefghijklmnopqrstuvwxyz");
        System.out.println("All 26 letters sorted: " + r8
                + " (expected \"abcdefghijklmnopqrstuvwxyz\") — "
                + (r8.equals("abcdefghijklmnopqrstuvwxyz") ? "PASS" : "FAIL"));

        // Mixed: s = "ecbacba" → "eacb"
        String r9 = sol.smallestSubsequence("ecbacba");
        System.out.println("Mixed 'ecbacba': " + r9 + " (expected \"eacb\") — "
                + (r9.equals("eacb") ? "PASS" : "FAIL"));
    }
}
