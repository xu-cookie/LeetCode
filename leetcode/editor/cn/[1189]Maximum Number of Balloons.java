/**
 * LeetCode Daily Challenge - 2026-06-22
 * Problem: 1189. Maximum Number of Balloons
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/maximum-number-of-balloons/
 *
 * Problem Description:
 * Given a string text, you want to use the characters of text to form as many
 * instances of the word "balloon" as possible. You can use each character in
 * text at most once. Return the maximum number of instances that can be formed.
 *
 * Approach:
 * Count frequency of characters in text. The word "balloon" needs:
 *   b: 1, a: 1, l: 2, o: 2, n: 1
 * Answer = min(count[b], count[a], count[l]/2, count[o]/2, count[n])
 *
 * Time Complexity: O(n) - single pass over the string
 * Space Complexity: O(1) - fixed-size frequency array of 26 ints
 */
class Solution {
    public int maxNumberOfBalloons(String text) {
        // Frequency array for 26 lowercase English letters
        int[] count = new int[26];

        // Count occurrences of each character
        for (char c : text.toCharArray()) {
            count[c - 'a']++;
        }

        // Calculate max number of "balloon" instances
        int ans = count['b' - 'a'];                    // b: need 1
        ans = Math.min(ans, count['a' - 'a']);         // a: need 1
        ans = Math.min(ans, count['l' - 'a'] / 2);     // l: need 2
        ans = Math.min(ans, count['o' - 'a'] / 2);     // o: need 2
        ans = Math.min(ans, count['n' - 'a']);         // n: need 1

        return ans;
    }
}
