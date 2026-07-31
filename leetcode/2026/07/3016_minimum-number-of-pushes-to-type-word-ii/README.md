# [3016. Minimum Number of Pushes to Type Word II](https://leetcode.com/problems/minimum-number-of-pushes-to-type-word-ii/)

**Difficulty**: Medium

## Problem Description

You are given a string `word` containing lowercase English letters.

Telephone keypads have keys mapped with distinct collections of lowercase English letters, which can be used to form words by pushing them. For example, the key `2` is mapped with `["a","b","c"]`, we need to push the key one time to type `"a"`, two times to type `"b"`, and three times to type `"c"`.

It is allowed to remap the keys numbered `2` to `9` to distinct collections of letters. The keys can be remapped to any amount of letters, but each letter must be mapped to exactly one key. You need to find the minimum number of times the keys will be pushed to type the string `word`.

Return the **minimum** number of pushes needed to type `word` after remapping the keys.

### Examples

**Example 1:**
```
Input: word = "abcde"
Output: 5
Explanation: Each of the 5 distinct letters gets assigned to position 1 on a separate key (1 push each).
Total = 1 + 1 + 1 + 1 + 1 = 5.
```

**Example 2:**
```
Input: word = "xyzxyzxyzxyz"
Output: 12
Explanation: 3 letters (x, y, z), each appears 4 times.
Each gets position 1 on keys 2/3/4 → 4*1 + 4*1 + 4*1 = 12.
```

**Example 3:**
```
Input: word = "aabbccddeeffgghhiiiiii"
Output: 24
Explanation: 9 letters total. The most frequent letter 'i' (6 times) gets position 1.
The other 8 letters each get position 1 (first 7) or position 2 (8th).
Total = 6*1 + 2*1 + 2*1 + 2*1 + 2*1 + 2*1 + 2*1 + 2*1 + 2*2 = 24.
```

### Constraints

- `1 <= word.length <= 10^5`
- `word` consists of lowercase English letters.

## Approach

### Key Insight

Unlike **#3014** (Part I) where all letters were guaranteed distinct, here letters **can repeat**. We must assign the most frequently occurring letters to the first position of keys (costing 1 push per occurrence), and less frequent letters to later positions (costing more pushes per occurrence).

Since we have exactly 8 keys (2-9), there are 8 "slots" at position 1, 8 slots at position 2, and so on. By the rearrangement inequality, sorting letters by frequency descending and assigning them greedily to the best available slots is optimal.

### Algorithm

1. **Count frequencies**: Count occurrences of each of the 26 lowercase letters
2. **Sort descending**: Sort the frequency array so the most frequent letters come first
3. **Assign costs**: For the i-th most frequent letter (0-indexed):
   - Pushes per occurrence = `i / 8 + 1`
   - Total contribution = `frequency × pushes_per_occurrence`
4. **Sum and return**

### Comparison: Part I vs Part II

| Aspect | Part I (#3014) | Part II (#3016) |
|--------|---------------|-----------------|
| Letters | All distinct | Can repeat |
| Word length | ≤ 26 | ≤ 10^5 |
| Key insight | Every letter appears once → use formula | Must sort by frequency |
| Complexity | O(1) formula | O(n) with frequency counting |

### Example Walkthrough

**word = "aabbccddeeffgghhiiiiii"**:
- Frequencies: a=2, b=2, c=2, d=2, e=2, f=2, g=2, h=2, i=6
- Sorted: [6, 2, 2, 2, 2, 2, 2, 2, 2]
- Assignments:
  | Index | Freq | Position | Pushes/occurrence | Total |
  |-------|------|----------|-------------------|-------|
  | 0 | 6 | 1 | 1 | 6 |
  | 1 | 2 | 1 | 1 | 2 |
  | 2 | 2 | 1 | 1 | 2 |
  | 3 | 2 | 1 | 1 | 2 |
  | 4 | 2 | 1 | 1 | 2 |
  | 5 | 2 | 1 | 1 | 2 |
  | 6 | 2 | 1 | 1 | 2 |
  | 7 | 2 | 1 | 1 | 2 |
  | 8 | 2 | 2 | 2 | 4 |
- **Total = 24** ✓

## Complexity Analysis

- **Time Complexity**: O(n + 26 log 26) ≈ O(n), where n is the word length. Counting takes O(n), sorting 26 elements is O(1).
- **Space Complexity**: O(1) — fixed-size array of 26 integers.

## Code

See [Solution.java](./Solution.java) for the complete implementation with test cases.
