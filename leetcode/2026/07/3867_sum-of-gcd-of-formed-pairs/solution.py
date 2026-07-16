"""
LeetCode Daily Challenge - 2026-07-16
Problem: 3867. Sum of GCD of Formed Pairs (数对的最大公约数之和)
Difficulty: Medium
Link: https://leetcode.com/problems/sum-of-gcd-of-formed-pairs/

Problem Description:
You are given an integer array nums of length n.

Construct an array prefixGcd where for each index i:
- Let mx_i = max(nums[0], nums[1], ..., nums[i]).
- prefixGcd[i] = gcd(nums[i], mx_i).

After constructing prefixGcd:
- Sort prefixGcd in non-decreasing order.
- Form pairs by taking the smallest unpaired element and the largest unpaired element.
- Repeat this process until no more pairs can be formed.
- For each formed pair, compute the gcd of the two elements.
- If n is odd, the middle element in the prefixGcd array remains unpaired and should be ignored.

Return an integer denoting the sum of the GCD values of all formed pairs.

Approach:
1. Build prefixGcd array by tracking running maximum
   - For each i: if nums[i] > running_max, update running_max
   - prefixGcd[i] = gcd(nums[i], running_max)
2. Sort prefixGcd
3. Pair smallest with largest: gcd(sorted[i], sorted[n-1-i]) for i < n//2
4. Sum all pair GCDs

Time Complexity: O(n log n) - dominated by sorting
Space Complexity: O(n) for prefixGcd array
"""

import math


class Solution:
    def gcdSum(self, nums: list[int]) -> int:
        n = len(nums)

        # Step 1: Build prefixGcd array
        prefix_gcd = [0] * n
        running_max = 0
        for i, num in enumerate(nums):
            if num > running_max:
                running_max = num
            prefix_gcd[i] = math.gcd(num, running_max)

        # Step 2: Sort non-decreasing
        prefix_gcd.sort()

        # Step 3: Pair smallest with largest, sum gcd of each pair
        ans = 0
        half = n // 2
        for i in range(half):
            ans += math.gcd(prefix_gcd[i], prefix_gcd[n - 1 - i])

        return ans
