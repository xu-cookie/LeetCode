"""
LeetCode Daily Challenge - 2026-07-18
Problem: 1979. Find Greatest Common Divisor of Array
Difficulty: Easy
Link: https://leetcode.com/problems/find-greatest-common-divisor-of-array/

Approach: One-Pass Min/Max + Euclidean GCD

Algorithm:
  1. Find min and max in a single pass through nums.
  2. Compute GCD(max, min) using math.gcd (Python 3.5+).
  3. Return the result.

Time Complexity:  O(n + log M) where n = len(nums), M = max(nums)
Space Complexity: O(1)
"""

import math
from typing import List


class Solution:
    def findGCD(self, nums: List[int]) -> int:
        # Step 1: Find min and max in a single pass
        min_val = nums[0]
        max_val = nums[0]

        for num in nums:
            if num < min_val:
                min_val = num
            elif num > max_val:
                max_val = num

        # Step 2: Compute GCD using Python's built-in math.gcd
        return math.gcd(max_val, min_val)


# Test harness
if __name__ == "__main__":
    sol = Solution()

    # Example 1
    r = sol.findGCD([2, 5, 6, 9, 10])
    print(f"Example 1: {r} (expected 2) — {'PASS' if r == 2 else 'FAIL'}")

    # Example 2
    r = sol.findGCD([7, 5, 6, 8, 3])
    print(f"Example 2: {r} (expected 1) — {'PASS' if r == 1 else 'FAIL'}")

    # Example 3
    r = sol.findGCD([3, 3])
    print(f"Example 3: {r} (expected 3) — {'PASS' if r == 3 else 'FAIL'}")

    # Edge cases
    r = sol.findGCD([1, 1000])
    print(f"[1,1000]: {r} (expected 1) — {'PASS' if r == 1 else 'FAIL'}")

    r = sol.findGCD([12, 18, 24])
    print(f"[12,18,24]: {r} (expected 12) — {'PASS' if r == 12 else 'FAIL'}")

    r = sol.findGCD([4, 8])
    print(f"[4,8]: {r} (expected 4) — {'PASS' if r == 4 else 'FAIL'}")

    print("\nAll tests completed.")
