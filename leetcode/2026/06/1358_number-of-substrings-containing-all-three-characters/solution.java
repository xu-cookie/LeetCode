/**
 * Problem: 1358. Number of Substrings Containing All Three Characters
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/number-of-substrings-containing-all-three-characters/
 * Date Solved: 2026-06-30
 *
 * Approach: Last Occurrence Tracking (Sliding Window variation)
 *
 * For each position i (the right end of a substring), we track the most recent
 * occurrences of 'a', 'b', and 'c' (initialized to -1). For substrings ending
 * at position i to be valid, their start index must be ≤ the earliest of these
 * three last-occurrence positions. So the number of valid substrings ending at
 * i is min(last_a, last_b, last_c) + 1 (or 0 if any character hasn't appeared).
 *
 * This works because: if we need all three characters, the substring must start
 * at or before the position of the character that appeared least recently. For
 * example, if last_a=0, last_b=3, last_c=5, then any start from 0 to
 * min(0,3,5)=0 would include all three chars.
 *
 * Time Complexity:  O(N) — single pass through the string
 * Space Complexity: O(1) — only three integer variables for tracking
 */
class Solution {
    public int numberOfSubstrings(String s) {
        int[] last = new int[]{-1, -1, -1}; // last position of 'a', 'b', 'c'
        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
            // If all three characters have appeared at least once,
            // the start can be any position from 0 to min(last[0], last[1], last[2])
            int minLast = Math.min(last[0], Math.min(last[1], last[2]));
            if (minLast != -1) {
                total += minLast + 1; // minLast + 1 possible starting positions
            }
        }

        return total;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        String s1 = "abcabc";
        int res1 = sol.numberOfSubstrings(s1);
        System.out.println("Example 1: " + res1 + " (expected 10) — " + (res1 == 10 ? "PASS" : "FAIL"));

        // Example 2
        String s2 = "aaacb";
        int res2 = sol.numberOfSubstrings(s2);
        System.out.println("Example 2: " + res2 + " (expected 3) — " + (res2 == 3 ? "PASS" : "FAIL"));

        // Example 3
        String s3 = "abc";
        int res3 = sol.numberOfSubstrings(s3);
        System.out.println("Example 3: " + res3 + " (expected 1) — " + (res3 == 1 ? "PASS" : "FAIL"));

        // Additional test: edge case with minimum length
        String s4 = "aaa";
        int res4 = sol.numberOfSubstrings(s4);
        System.out.println("Edge 'aaa': " + res4 + " (expected 0) — " + (res4 == 0 ? "PASS" : "FAIL"));

        // Additional test: all three at the very end
        String s5 = "aaaaaaaaaaabc";
        int res5 = sol.numberOfSubstrings(s5);
        System.out.println("Edge 'aaaaaaaaaaabc': " + res5 + " (expected 11) — " + (res5 == 11 ? "PASS" : "FAIL"));
    }
}
