# 1081. Smallest Subsequence of Distinct Characters

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/
- **Note**: Same as [#316 Remove Duplicate Letters](https://leetcode.com/problems/remove-duplicate-letters/)
- **Date Solved**: 2026-07-19

## Problem Summary

Given a string `s`, return the **lexicographically smallest subsequence** of `s` that contains all the distinct characters of `s` exactly once.

A **subsequence** is a sequence that can be derived by deleting some characters without changing the order of remaining elements.

## Examples

| Input      | Output | Explanation |
|------------|--------|-------------|
| `"bcabc"`  | `"abc"`| Remove first 'b' → `"abc"` |
| `"cbacdcbc"` | `"acdb"` | Choose 'a','c','d','b' in optimal order |

## Approach: Greedy Monotonic Stack

### Key Insight

Build the result character by character with a monotonic stack. When we encounter a character `c` that is **smaller** than the current top of the stack AND the top character **appears again later**, we should "pop" it — placing `c` earlier produces a smaller lexicographic result.

### Algorithm

1. **Preprocessing**: Compute `last[c]` — the index of each character's last occurrence in `s`.
2. **Iterate** through `s` with index `i`:
   - **Skip** if `c` is already in the result (each distinct char appears exactly once).
   - **While** the stack is non-empty, `c < stackTop`, and `last[stackTop] > i`: pop the stack and mark as removed.
   - **Push** `c` onto the stack and mark as in-stack.
3. The stack (StringBuilder) contains the answer.

### Visualization: `s = "bcabc"`

| Step | i | c | Stack (before) | Action | Stack (after) |
|------|---|---|----------------|--------|---------------|
| 1 | 0 | b | `[]` | push b | `[b]` |
| 2 | 1 | c | `[b]` | b < c, no pop; push c | `[bc]` |
| 3 | 2 | a | `[bc]` | a < c, last[c]=3>2 → pop c; a < b, last[b]=4>2 → pop b; push a | `[a]` |
| 4 | 3 | b | `[a]` | a < b, no pop; push b | `[ab]` |
| 5 | 4 | c | `[ab]` | b < c, no pop; push c | `[abc]` |

Result: `"abc"` ✓

## Complexity

- **Time Complexity**: `O(n)` — each character is pushed and popped at most once.
- **Space Complexity**: `O(1)` — arrays of size 26 + StringBuilder of at most 26 characters.

## Tags

`monotonic stack`, `greedy`, `string`, `lexicographic order`
