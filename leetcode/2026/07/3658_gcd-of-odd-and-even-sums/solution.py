"""
LeetCode #3658 - GCD of Odd and Even Sums
Difficulty: Easy
Link: https://leetcode.com/problems/gcd-of-odd-and-even-sums/
Date Solved: 2026-07-15

Approach:
  sumOdd  = 1 + 3 + 5 + ... + (2n-1) = n²
  sumEven = 2 + 4 + 6 + ... + 2n     = n(n+1)
  GCD(n², n(n+1)) = n · GCD(n, n+1) = n  (since n and n+1 are coprime)

Time:  O(1)
Space: O(1)
"""

import math


class Solution:
    def gcdOfOddEvenSums(self, n: int) -> int:
        return n


if __name__ == "__main__":
    sol = Solution()

    # Test Case 1
    assert sol.gcdOfOddEvenSums(4) == 4, f"Test 1 failed"
    print("Test 1 (n=4):", sol.gcdOfOddEvenSums(4))  # Expected: 4

    # Test Case 2
    assert sol.gcdOfOddEvenSums(5) == 5, f"Test 2 failed"
    print("Test 2 (n=5):", sol.gcdOfOddEvenSums(5))  # Expected: 5

    # Test Case 3
    assert sol.gcdOfOddEvenSums(1) == 1, f"Test 3 failed"
    print("Test 3 (n=1):", sol.gcdOfOddEvenSums(1))  # Expected: 1

    # Test Case 4
    assert sol.gcdOfOddEvenSums(10) == 10, f"Test 4 failed"
    print("Test 4 (n=10):", sol.gcdOfOddEvenSums(10))  # Expected: 10

    # Test Case 5
    assert sol.gcdOfOddEvenSums(2) == 2, f"Test 5 failed"
    print("Test 5 (n=2):", sol.gcdOfOddEvenSums(2))  # Expected: 2

    print("\nAll tests passed!")
