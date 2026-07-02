/**
 * LeetCode Daily Challenge - 2026-06-30
 * Problem: 1358. Number of Substrings Containing All Three Characters
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/number-of-substrings-containing-all-three-characters/
 *
 * Problem Description:
 * Given a string s consisting only of characters a, b and c, return the number
 * of substrings containing at least one occurrence of all these characters.
 *
 * Approach: Last Occurrence Tracking (Sliding Window Variation)
 *
 * For each position i (the right end of a substring), track the most recent
 * occurrences of 'a', 'b', and 'c'. For substrings ending at i to contain all
 * three characters, the start index must be ≤ min(last_a, last_b, last_c).
 * Thus, valid substrings ending at i = min(last_a, last_b, last_c) + 1
 * (or 0 if any character hasn't appeared yet).
 *
 * Example: s = "abcabc"
 * i=0 'a': last=[0,-1,-1], min=-1 → +0
 * i=1 'b': last=[0,1,-1], min=-1 → +0
 * i=2 'c': last=[0,1,2], min=0 → +1 (substring "abc")
 * i=3 'a': last=[3,1,2], min=1 → +2 (substrings "abca", "bca")
 * i=4 'b': last=[3,4,2], min=2 → +3
 * i=5 'c': last=[3,4,5], min=3 → +4
 * Total = 0+0+1+2+3+4 = 10 ✓
 *
 * Time Complexity: O(N) — single pass through the string
 * Space Complexity: O(1) — only three integer variables
 */
class Solution {
    public int numberOfSubstrings(String s) {
        int[] last = new int[]{-1, -1, -1}; // last[a], last[b], last[c]
        int total = 0;

        for (int i = 0; i < s.length(); i++) {
            last[s.charAt(i) - 'a'] = i;
            int minLast = Math.min(last[0], Math.min(last[1], last[2]));
            if (minLast != -1) {
                total += minLast + 1;
            }
        }

        return total;
    }
}
