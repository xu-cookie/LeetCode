# 3756. Concatenate Non-Zero Digits and Multiply by Sum II

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-ii/
- **Date Solved**: 2026-07-08

## Problem Summary

This is the follow-up to [#3754](../3754_concatenate-non-zero-digits-and-multiply-by-sum-i/). Instead of a single integer `n`, we are given:

- A digit string `s` of length `m` (1 ≤ m ≤ 10⁵)
- An array of queries `queries[i] = [l_i, r_i]` (up to 10⁵ queries)

For each query, extract substring `s[l_i..r_i]`, form `x` by concatenating all non-zero digits, compute `sum` of those digits, and return `(x * sum) % (10⁹ + 7)`.

## Examples

| Query | Substring  | Non-zero digits | x          | sum | Result (mod) |
|-------|-----------|-----------------|------------|-----|-------------|
| [0,7] | "10203004" | 1,2,3,4        | 1234       | 10  | 12340       |
| [1,3] | "020"     | 2               | 2          | 2   | 4           |
| [4,6] | "300"     | 3               | 3          | 3   | 9           |

| [0,9] | "9876543210" | 9,8,7,6,5,4,3,2,1 | 987654321 | 45 | 444444137 |

## Approach

**Prefix Sums + Modular Arithmetic**

Since we have up to 10⁵ queries on a string of length up to 10⁵, a naive per-query scan would be O(m·q), too slow. Instead, we precompute prefix arrays:

### Key Insight

For a substring `[l, r]`, the x-value in the range equals the full prefix x at `r` minus the contribution of the prefix before `l`:

```
prefixX[r] = A * 10^|B| + B
=> B = prefixX[r] - A * 10^|B|
```

where:
- `A` = x-value of non-zero digits in `s[0..l-1]`
- `B` = x-value of non-zero digits in `s[l..r]` (what we want)
- `|B|` = count of non-zero digits in the range

### Algorithm

1. **Precompute** three prefix arrays in a single pass:
   - `prefixX[i]`: concatenated x-value of non-zero digits in `s[0..i]` (mod MOD)
   - `prefixCnt[i]`: number of non-zero digits in `s[0..i]`
   - `prefixSum[i]`: sum of non-zero digits in `s[0..i]`

2. **Precompute** powers of 10 up to m: `pow10[i] = 10ⁱ % MOD`

3. **Answer each query** in O(1):
   ```
   cnt = prefixCnt[r] - prefixCnt[l-1]
   x = (prefixX[r] - prefixX[l-1] * 10^cnt) % MOD
   sum = prefixSum[r] - prefixSum[l-1]
   answer = (x * sum) % MOD
   ```

## Complexity

- **Time Complexity**: O(m + q) — one pass to build prefix arrays, one pass to answer queries
- **Space Complexity**: O(m) for the three prefix arrays and powers of 10
