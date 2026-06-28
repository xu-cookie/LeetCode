"""
LeetCode Daily Challenge - 2026-06-28
Problem: 1846. Maximum Element After Decreasing and Rearranging
Difficulty: Medium
Link: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/

Problem Description:
You are given an array of positive integers `arr`. Perform some operations (possibly none)
on `arr` so that it satisfies these conditions:
- The value of the first element in `arr` must be `1`.
- The absolute difference between any 2 adjacent elements must be ≤ 1.

There are 2 types of operations:
- Decrease the value of any element to a smaller positive integer.
- Rearrange the elements of `arr` to be in any order.

Return the maximum possible value of an element in `arr` after operations.

Approach:
1. Sort the array — rearrangement is free, so sort to make adjacency easier.
2. Since we can decrease elements and first must be 1, greedily build:
   - Start with `cur = 0` (representing the max value so far)
   - For each `x` in sorted arr: `cur = min(x, cur + 1)`
   - This means: if `x` is large enough, we can increase by 1 from previous;
     if `x` is too small, we're capped by `x` itself.
3. The final value of `cur` is the maximum element achievable.

This works because:
- Sorting puts elements in non-decreasing order
- We can always decrease a large number to fit the "adjacent diff ≤ 1" rule
- The greedy choice `cur + 1` maximizes the final value
- `min(x, cur + 1)` ensures we never exceed available values

Time Complexity: O(n log n) due to sorting
Space Complexity: O(1) extra space (or O(n) if sorting creates a copy)
"""

from typing import List


class Solution:
    def maximumElementAfterDecrementingAndRearranging(self, arr: List[int]) -> int:
        arr.sort()
        cur = 0  # current maximum value we can achieve
        for x in arr:
            cur = min(x, cur + 1)
        return cur


if __name__ == "__main__":
    sol = Solution()

    # Example 1
    arr1 = [2, 2, 1, 2, 1]
    result1 = sol.maximumElementAfterDecrementingAndRearranging(arr1)
    expected1 = 2
    print(f"Example 1: arr={arr1}, result={result1}, expected={expected1} "
          f"-> {'PASS' if result1 == expected1 else 'FAIL'}")

    # Example 2
    arr2 = [100, 1, 1000]
    result2 = sol.maximumElementAfterDecrementingAndRearranging(arr2)
    expected2 = 3
    print(f"Example 2: arr={arr2}, result={result2}, expected={expected2} "
          f"-> {'PASS' if result2 == expected2 else 'FAIL'}")

    # Example 3
    arr3 = [1, 2, 3, 4, 5]
    result3 = sol.maximumElementAfterDecrementingAndRearranging(arr3)
    expected3 = 5
    print(f"Example 3: arr={arr3}, result={result3}, expected={expected3} "
          f"-> {'PASS' if result3 == expected3 else 'FAIL'}")

    # Test 4: Single element
    arr4 = [1]
    result4 = sol.maximumElementAfterDecrementingAndRearranging(arr4)
    expected4 = 1
    print(f"Test 4: arr={arr4}, result={result4}, expected={expected4} "
          f"-> {'PASS' if result4 == expected4 else 'FAIL'}")

    # Test 5: All same values
    arr5 = [5, 5, 5, 5]
    result5 = sol.maximumElementAfterDecrementingAndRearranging(arr5)
    expected5 = 4  # [1, 2, 3, 4] after decreasing
    print(f"Test 5: arr={arr5}, result={result5}, expected={expected5} "
          f"-> {'PASS' if result5 == expected5 else 'FAIL'}")

    # Test 6: Large gaps
    arr6 = [10, 10, 10]
    result6 = sol.maximumElementAfterDecrementingAndRearranging(arr6)
    expected6 = 3  # [1, 2, 3]
    print(f"Test 6: arr={arr6}, result={result6}, expected={expected6} "
          f"-> {'PASS' if result6 == expected6 else 'FAIL'}")

    # Test 7: Already perfect
    arr7 = [1, 1, 2, 3, 4]
    result7 = sol.maximumElementAfterDecrementingAndRearranging(arr7)
    expected7 = 4
    print(f"Test 7: arr={arr7}, result={result7}, expected={expected7} "
          f"-> {'PASS' if result7 == expected7 else 'FAIL'}")

    # Test 8: Many duplicates of 1
    arr8 = [1, 1, 1, 1, 1]
    result8 = sol.maximumElementAfterDecrementingAndRearranging(arr8)
    expected8 = 1  # all 1's, max stays 1
    print(f"Test 8: arr={arr8}, result={result8}, expected={expected8} "
          f"-> {'PASS' if result8 == expected8 else 'FAIL'}")
