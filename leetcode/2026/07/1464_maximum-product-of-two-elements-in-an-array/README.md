# LeetCode #1464: Maximum Product of Two Elements in an Array

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/
- **Date**: 2026-07-27

## Problem

Given the array of integers `nums`, choose two different indices `i` and `j` of that array. Return the maximum value of `(nums[i]-1)*(nums[j]-1)`.

### Examples

```
Input: nums = [3,4,5,2]
Output: 12
Explanation: (4-1)*(5-1) = 3*4 = 12

Input: nums = [1,5,4,5]
Output: 16
Explanation: (5-1)*(5-1) = 4*4 = 16

Input: nums = [3,7]
Output: 12
Explanation: (3-1)*(7-1) = 2*6 = 12
```

### Constraints

- `2 <= nums.length <= 500`
- `1 <= nums[i] <= 10^3`

## Approach

The expression `(a-1)*(b-1)` is monotonic in both `a` and `b` for `a,b >= 1`. Therefore, the maximum product is achieved by picking the two largest elements in the array.

1. Find the two maximum elements in a single pass
2. Return `(max1 - 1) * (max2 - 1)`

- **Time**: O(n)
- **Space**: O(1)
