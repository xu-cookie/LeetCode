# [1291. Sequential Digits](https://leetcode.com/problems/sequential-digits/)

**Difficulty:** Medium

## Problem Description

An integer has *sequential digits* if and only if each digit in the number is one more than the previous digit.

Return a **sorted** list of all the integers in the range `[low, high]` inclusive that have sequential digits.

### Example 1

```
Input: low = 100, high = 300
Output: [123,234]
```

### Example 2

```
Input: low = 1000, high = 13000
Output: [1234,2345,3456,4567,5678,6789,12345]
```

### Constraints

- `10 <= low <= high <= 10^9`

## Approach

Generate all possible sequential digit numbers by enumeration. There are only 45 possible sequential digit numbers total (9 starting digits × at most 9 lengths).

**Algorithm:**
1. For each possible starting digit (1 through 9):
2. Build the number by appending consecutive digits until we exceed 9 or `high`.
3. If the generated number is ≥ `low` and ≤ `high`, add it to the result.
4. Sort the result (natural generation order is already sorted, but explicit sort ensures correctness).

## Complexity Analysis

- **Time Complexity:** O(1) — at most 45 numbers are generated, regardless of input size.
- **Space Complexity:** O(1) — at most 45 numbers in the result list.

## Key Insight

Instead of iterating through the vast range `[low, high]` (which can be up to 10^9), we flip the problem: generate all 45 valid sequential numbers and filter by the range. This is the classic "generate candidates" optimization for problems with a small solution space.
