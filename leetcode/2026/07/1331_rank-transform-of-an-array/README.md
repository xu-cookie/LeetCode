# [1331. Rank Transform of an Array](https://leetcode.com/problems/rank-transform-of-an-array/)

**Difficulty:** Easy

## Problem Description

Given an array of integers `arr`, replace each element with its rank.

The rank represents how large the element is. The rank has the following rules:

- Rank is an integer starting from 1.
- The larger the element, the larger the rank. If two elements are equal, their rank must be the same.
- Rank should be as small as possible.

### Example 1

```
Input: arr = [40,10,20,30]
Output: [4,1,2,3]
Explanation: 40 is the largest element. 10 is the smallest. 20 is the second smallest. 30 is the third smallest.
```

### Example 2

```
Input: arr = [100,100,100]
Output: [1,1,1]
Explanation: Same elements share the same rank.
```

### Example 3

```
Input: arr = [37,12,28,9,100,56,80,5,12]
Output: [5,3,4,2,8,6,7,1,3]
```

### Constraints

- `0 <= arr.length <= 10^5`
- `-10^9 <= arr[i] <= 10^9`

## Approach

1. **Extract unique elements and sort:** Use a `TreeSet` to automatically sort and deduplicate the array elements. This gives us the ordering of elements by size.
2. **Map values to ranks:** Iterate over the sorted unique elements and assign each a rank (starting from 1). Store these mappings in a `HashMap`.
3. **Replace original elements:** Iterate over the original array and replace each element with its corresponding rank from the map.

## Complexity Analysis

- **Time Complexity:** O(n log n) — dominated by sorting the unique elements. Both building the map and constructing the result array run in O(n).
- **Space Complexity:** O(n) — for storing the sorted unique elements array and the rank HashMap, where n is the number of unique elements in the worst case.
