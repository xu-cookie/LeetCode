"""
LeetCode Daily Challenge - 2026-07-16
Problem: 3867. Sum of GCD of Formed Pairs (数对的最大公约数之和)
Difficulty: Medium
Link: https://leetcode.cn/problems/sum-of-gcd-of-formed-pairs/

Approach:
1. Build prefixGcd: gcd(nums[i], running_max)
2. Sort prefixGcd
3. Pair smallest with largest, sum gcd of each pair

Time Complexity: O(n log n)
Space Complexity: O(n)
"""

import math


class Solution:
    def gcdSum(self, nums: list[int]) -> int:
        n = len(nums)
        prefix_gcd = [0] * n
        mx = 0
        for i, num in enumerate(nums):
            if num > mx:
                mx = num
            prefix_gcd[i] = math.gcd(num, mx)

        prefix_gcd.sort()
        ans = 0
        half = n // 2
        for i in range(half):
            ans += math.gcd(prefix_gcd[i], prefix_gcd[n - 1 - i])
        return ans
