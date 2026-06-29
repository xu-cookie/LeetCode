/**
 * LeetCode Daily Challenge - 2026-06-29
 * Problem: 1967. Number of Strings That Appear as Substrings in Word
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/number-of-strings-that-appear-as-substrings-in-word/
 *
 * Problem Description:
 * Given an array of strings patterns and a string word, return the number
 * of strings in patterns that exist as a substring in word.
 * A substring is a contiguous sequence of characters within a string.
 *
 * Approach (Direct String Matching):
 * 1. Iterate through each pattern string in patterns.
 * 2. Use String.contains() (which implements indexOf) to check if the
 *    pattern appears as a substring in word.
 * 3. Count and return the number of matches.
 *
 * Key Insight:
 * The problem is straightforward — Java's built-in String.contains()
 * method efficiently checks substring existence.
 *
 * Time Complexity: O(n * m) where n = patterns.length, m = word.length
 *   - For each pattern (n), contains() takes O(m) in worst case
 * Space Complexity: O(1) — only a counter variable
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

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        String[] patterns1 = {"a", "abc", "bc", "d"};
        String word1 = "abc";
        int result1 = sol.numOfStrings(patterns1, word1);
        System.out.println("Example 1: " + result1 + " (expected: 3) -> "
                + (result1 == 3 ? "PASS" : "FAIL"));

        // Example 2
        String[] patterns2 = {"a", "b", "c"};
        String word2 = "aaaaabbbbb";
        int result2 = sol.numOfStrings(patterns2, word2);
        System.out.println("Example 2: " + result2 + " (expected: 2) -> "
                + (result2 == 2 ? "PASS" : "FAIL"));

        // Example 3
        String[] patterns3 = {"a", "a", "a"};
        String word3 = "ab";
        int result3 = sol.numOfStrings(patterns3, word3);
        System.out.println("Example 3: " + result3 + " (expected: 3) -> "
                + (result3 == 3 ? "PASS" : "FAIL"));

        // Edge case: no matches
        String[] patterns4 = {"x", "y", "z"};
        String word4 = "hello";
        int result4 = sol.numOfStrings(patterns4, word4);
        System.out.println("Test 4 (no matches): " + result4 + " (expected: 0) -> "
                + (result4 == 0 ? "PASS" : "FAIL"));

        // Edge case: all match
        String[] patterns5 = {"he", "el", "ll", "lo"};
        String word5 = "hello";
        int result5 = sol.numOfStrings(patterns5, word5);
        System.out.println("Test 5 (all match): " + result5 + " (expected: 4) -> "
                + (result5 == 4 ? "PASS" : "FAIL"));

        // Edge case: duplicate patterns
        String[] patterns6 = {"a", "a", "a"};
        String word6 = "a";
        int result6 = sol.numOfStrings(patterns6, word6);
        System.out.println("Test 6 (duplicates): " + result6 + " (expected: 3) -> "
                + (result6 == 3 ? "PASS" : "FAIL"));
    }
}
