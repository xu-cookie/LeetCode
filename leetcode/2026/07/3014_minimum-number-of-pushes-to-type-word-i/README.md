# [3014. Minimum Number of Pushes to Type Word I](https://leetcode.com/problems/minimum-number-of-pushes-to-type-word-i/)

**Difficulty**: Easy

## Problem Description

You are given a string `word` containing **distinct** lowercase English letters.

Telephone keypads have keys mapped with distinct collections of lowercase English letters, which can be used to form words by pushing them. For example, the key `2` is mapped with `["a","b","c"]`, we need to push the key one time to type `"a"`, two times to type `"b"`, and three times to type `"c"`.

It is allowed to remap the keys numbered `2` to `9` to distinct collections of letters. The keys can be remapped to any amount of letters, but each letter must be mapped to exactly one key. You need to find the minimum number of times the keys will be pushed to type the string `word`.

Return the **minimum** number of pushes needed to type `word` after remapping the keys.

### Examples

**Example 1:**
```
Input: word = "abcde"
Output: 5
Explanation: The remapped keypad provides the minimum cost.
"a" -> one push on key 2
"b" -> one push on key 3
"c" -> one push on key 4
"d" -> one push on key 5
"e" -> one push on key 6
Total cost is 1 + 1 + 1 + 1 + 1 = 5.
```

**Example 2:**
```
Input: word = "xycdefghij"
Output: 12
Explanation:
"x" -> one push on key 2
"y" -> two pushes on key 2
"c" -> one push on key 3
"d" -> two pushes on key 3
"e" -> one push on key 4
"f" -> one push on key 5
"g" -> one push on key 6
"h" -> one push on key 7
"i" -> one push on key 8
"j" -> one push on key 9
Total cost is 1 + 2 + 1 + 2 + 1 + 1 + 1 + 1 + 1 + 1 = 12.
```

### Constraints

- `1 <= word.length <= 26`
- `word` consists of lowercase English letters.
- All letters in `word` are distinct.

## Approach

### Key Insight

We have 8 keys (2-9) that can be freely remapped. Since all letters in `word` are **distinct** (each appears exactly once), the optimal strategy is to spread letters across keys so that as many letters as possible are on the first position of their key (1 press), then the second position (2 presses), and so on.

### Algorithm

For a word of length `n`:
- First 8 letters → assign to position 1 on each key → 1 press each
- Next 8 letters → assign to position 2 on each key → 2 presses each
- Next 8 letters → assign to position 3 on each key → 3 presses each
- Remaining 2 letters → assign to position 4 → 4 presses each

**Formula:**
```
groups = n / 8
remainder = n % 8
total = (groups + 1) × (4 × groups + remainder)
```

Alternatively, the i-th letter (0-indexed) needs `i / 8 + 1` pushes.

### Example Walkthrough

**word = "abcde"** (n=5):
- groups = 5/8 = 0, remainder = 5
- total = (0+1) × (4×0 + 5) = 1 × 5 = **5** ✓

**word = "xycdefghij"** (n=10):
- groups = 10/8 = 1, remainder = 2
- total = (1+1) × (4×1 + 2) = 2 × 6 = **12** ✓

## Complexity Analysis

- **Time Complexity**: O(1) — closed-form formula; no iteration needed
- **Space Complexity**: O(1) — only a few integer variables

## Code

See [Solution.java](./Solution.java) for the complete implementation with test cases.
