"""
LeetCode Daily Challenge - 2026-06-27
Problem: 3020. Find the Maximum Number of Elements in Subset
Difficulty: Medium
Link: https://leetcode.com/problems/find-the-maximum-number-of-elements-in-subset/

Problem Description:
You are given an array of positive integers nums. You need to select a subset
which can be placed in an array following the pattern:
[x, x², x⁴, ..., x^(k/2), x^k, x^(k/2), ..., x⁴, x², x]
where k is a non-negative power of 2.

Approach:
The pattern is a palindrome centered at x^k. For k = 2^m, the exponents are:
2^0, 2^1, 2^2, ..., 2^m, ..., 2^2, 2^1, 2^0
The length of the pattern is 2m + 1.

Key observations:
1. For x = 1: 1^(anything) = 1, so the pattern is all 1's.
   The max length is the largest odd number ≤ count[1].
2. For x > 1: Build a chain x → x² → x⁴ → ... while each value exists in nums.
   For a pattern of depth m (center at x^(2^m)):
   - Need at least 2 copies of each intermediate value (one per side)
   - Need at least 1 copy of the center value
   - The length is 2m + 1
3. Chain length is at most ~5 because values ≤ 10^9 and squaring grows fast.

Time Complexity: O(n + u * log(maxVal)) where u = unique values, effectively O(n)
Space Complexity: O(n) for the frequency map
"""

from collections import Counter
from typing import List


class Solution:
    def maximumLength(self, nums: List[int]) -> int:
        cnt = Counter(nums)
        ans = 0

        # Special handling for x = 1
        # 1^(anything) = 1, so any odd-length palindrome of 1's works
        if 1 in cnt:
            c = cnt[1]
            # Pattern length must be 2m+1 (odd), use largest odd ≤ c
            ans = c if c % 2 == 1 else c - 1

        # Process other values as starting points of chains
        for x in cnt:
            if x == 1:
                continue

            # Build the chain: x, x², x⁴, x⁸, ...
            chain = []
            val = x
            while val in cnt:
                chain.append(val)
                val *= val
                if val > 10 ** 9:  # Constraint boundary
                    break

            # Depth 0 (just [x]): always gives 1 element
            ans = max(ans, 1)

            # Try each depth m >= 1
            # For depth i (center at chain[i]), need cnt[chain[j]] >= 2 for all j < i
            for i in range(1, len(chain)):
                ok = True
                for j in range(i):
                    if cnt[chain[j]] < 2:
                        ok = False
                        break
                if ok:
                    ans = max(ans, 2 * i + 1)

        return ans
