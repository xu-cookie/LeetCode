"""
LeetCode Daily Challenge - 2026-06-22
Problem: 1189. Maximum Number of Balloons
Difficulty: Easy
Link: https://leetcode.com/problems/maximum-number-of-balloons/

Problem Description:
Given a string text, you want to use the characters of text to form as many
instances of the word "balloon" as possible. You can use each character in
text at most once. Return the maximum number of instances that can be formed.

Approach:
Count the frequency of each character in text, then compute how many times
the word "balloon" can be formed. The word "balloon" requires:
  b: 1, a: 1, l: 2, o: 2, n: 1
So the answer is min(count['b'], count['a'], count['l']//2, count['o']//2, count['n']).

Edge cases:
- text length between 1 and 10^4, all lowercase English letters.
- If any required character is missing, the result is 0.

Time Complexity: O(n) - single pass to count characters
Space Complexity: O(1) - fixed-size frequency array of 26 characters
"""

from collections import Counter


class Solution:
    def maxNumberOfBalloons(self, text: str) -> int:
        """
        Returns the maximum number of instances of the word "balloon"
        that can be formed using characters from text (each char at most once).
        """
        # Count frequency of each character
        freq = Counter(text)

        # The word "balloon" requires:
        # b: 1, a: 1, l: 2, o: 2, n: 1
        return min(
            freq.get('b', 0),
            freq.get('a', 0),
            freq.get('l', 0) // 2,
            freq.get('o', 0) // 2,
            freq.get('n', 0)
        )


# Test cases
if __name__ == "__main__":
    sol = Solution()
    # Example 1
    assert sol.maxNumberOfBalloons("nlaebolko") == 1, "Example 1 failed"
    # Example 2
    assert sol.maxNumberOfBalloons("loonbalxballpoon") == 2, "Example 2 failed"
    # Example 3
    assert sol.maxNumberOfBalloons("leetcode") == 0, "Example 3 failed"
    # Edge case: empty/not enough chars
    assert sol.maxNumberOfBalloons("ballon") == 0, "Missing one 'o'"
    assert sol.maxNumberOfBalloons("balloonballoon") == 2, "Two full words"
    print("All test cases passed!")
