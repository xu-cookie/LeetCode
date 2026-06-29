/**
 * Problem: 1967. Number of Strings That Appear as Substrings in Word
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/number-of-strings-that-appear-as-substrings-in-word/
 * Date Solved: 2026-06-29
 *
 * Approach:
 * Traverse each string in the patterns array and use Java's built-in
 * String.contains() method to check whether it appears as a substring in word.
 * Maintain a counter to track the number of matching patterns.
 *
 * Since the constraints are small (patterns.length <= 100, each string <= 100,
 * word.length <= 100), the O(P * W) brute-force substring check is perfectly acceptable.
 *
 * Time Complexity:  O(P * W) where P is the total length of all patterns
 *                   and W is the length of word. In the worst case each
 *                   contains() call does O(W) work per character of each pattern.
 * Space Complexity: O(1) — only an integer counter is used.
 */
class Solution {
    public int numOfStrings(String[] patterns, String word) {
        int count = 0;
        for (String pattern : patterns) {
            if (word.contains(pattern)) {
                count++;
            }
        }
        return count;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        String[] patterns1 = {"a", "abc", "bc", "d"};
        String word1 = "abc";
        int res1 = sol.numOfStrings(patterns1, word1);
        System.out.println("Example 1: " + res1 + " (expected 3) — " + (res1 == 3 ? "PASS" : "FAIL"));

        // Example 2
        String[] patterns2 = {"a", "b", "c"};
        String word2 = "aaaaabbbbb";
        int res2 = sol.numOfStrings(patterns2, word2);
        System.out.println("Example 2: " + res2 + " (expected 2) — " + (res2 == 2 ? "PASS" : "FAIL"));

        // Example 3
        String[] patterns3 = {"a", "a", "a"};
        String word3 = "ab";
        int res3 = sol.numOfStrings(patterns3, word3);
        System.out.println("Example 3: " + res3 + " (expected 3) — " + (res3 == 3 ? "PASS" : "FAIL"));
    }
}
