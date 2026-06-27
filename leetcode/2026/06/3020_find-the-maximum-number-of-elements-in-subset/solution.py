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


if __name__ == "__main__":
    sol = Solution()

    # Example 1 from problem statement
    nums1 = [5, 4, 1, 2, 2]
    result1 = sol.maximumLength(nums1)
    expected1 = 3
    print(f"Example 1: nums={nums1}, result={result1}, expected={expected1} "
          f"-> {'PASS' if result1 == expected1 else 'FAIL'}")

    # Example 2 from problem statement
    nums2 = [1, 3, 2, 4]
    result2 = sol.maximumLength(nums2)
    expected2 = 1
    print(f"Example 2: nums={nums2}, result={result2}, expected={expected2} "
          f"-> {'PASS' if result2 == expected2 else 'FAIL'}")

    # Additional tests
    # Test 3: Full chain 2 -> 4 -> 16 -> 256
    nums3 = [2, 2, 4, 4, 16, 16, 256, 1]
    result3 = sol.maximumLength(nums3)
    expected3 = 7  # [2, 4, 16, 256, 16, 4, 2]
    print(f"Test 3: nums={nums3}, result={result3}, expected={expected3} "
          f"-> {'PASS' if result3 == expected3 else 'FAIL'}")

    # Test 4: Only single elements, no chains possible
    nums4 = [10, 20, 30]
    result4 = sol.maximumLength(nums4)
    expected4 = 1
    print(f"Test 4: nums={nums4}, result={result4}, expected={expected4} "
          f"-> {'PASS' if result4 == expected4 else 'FAIL'}")

    # Test 5: Five ones (odd count)
    nums5 = [1, 1, 1, 1, 1]
    result5 = sol.maximumLength(nums5)
    expected5 = 5
    print(f"Test 5: nums={nums5}, result={result5}, expected={expected5} "
          f"-> {'PASS' if result5 == expected5 else 'FAIL'}")

    # Test 6: Four ones (even count -> use f-1)
    nums6 = [1, 1, 1, 1]
    result6 = sol.maximumLength(nums6)
    expected6 = 3
    print(f"Test 6: nums={nums6}, result={result6}, expected={expected6} "
          f"-> {'PASS' if result6 == expected6 else 'FAIL'}")

    # Test 7: Mixed chains, choose the longer one
    nums7 = [2, 2, 4, 4, 16, 3, 3, 9]
    result7 = sol.maximumLength(nums7)
    expected7 = 5  # [2, 4, 16, 4, 2] is longer than [3, 9, 3]
    print(f"Test 7: nums={nums7}, result={result7}, expected={expected7} "
          f"-> {'PASS' if result7 == expected7 else 'FAIL'}")

    # Test 8: Chain from base 3
    nums8 = [3, 3, 9, 9, 81, 81]
    result8 = sol.maximumLength(nums8)
    expected8 = 5  # [3, 9, 81, 9, 3]
    print(f"Test 8: nums={nums8}, result={result8}, expected={expected8} "
          f"-> {'PASS' if result8 == expected8 else 'FAIL'}")

    # Test 9: Multiple copies of base value but no next value
    nums9 = [2, 2, 2, 2]
    result9 = sol.maximumLength(nums9)
    expected9 = 1  # Cannot form [2,4,2] without 4
    print(f"Test 9: nums={nums9}, result={result9}, expected={expected9} "
          f"-> {'PASS' if result9 == expected9 else 'FAIL'}")

    # Test 10: Edge case - two copies, next exists with one copy
    nums10 = [2, 2, 4]
    result10 = sol.maximumLength(nums10)
    expected10 = 3  # [2, 4, 2]
    print(f"Test 10: nums={nums10}, result={result10}, expected={expected10} "
          f"-> {'PASS' if result10 == expected10 else 'FAIL'}")

