# LeetCode #628: Maximum Product of Three Numbers

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/maximum-product-of-three-numbers/
- **Date**: 2026-07-26

## Problem

Given an integer array `nums`, find three numbers whose product is maximum and return the maximum product.

### Examples

```
Input: nums = [1,2,3]
Output: 6

Input: nums = [1,2,3,4]
Output: 24

Input: nums = [-1,-2,-3]
Output: -6
```

### Constraints

- `3 <= nums.length <= 10^4`
- `-1000 <= nums[i] <= 1000`

## Approach

The maximum product of three numbers comes from one of two cases:

1. **Three largest numbers**: `max1 × max2 × max3`
2. **Two smallest × largest**: `min1 × min2 × max1` — two negative numbers multiply to a positive, which may exceed case 1

We find all five needed values in a single O(n) pass, avoiding the O(n log n) cost of sorting.

- **Time**: O(n)
- **Space**: O(1)
