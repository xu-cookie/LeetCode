# [1189] Maximum Number of Balloons

**Difficulty:** Easy
**Date:** 2026-06-22
**Link:** https://leetcode.com/problems/maximum-number-of-balloons/

## Problem Description

Given a string `text`, you want to use the characters of `text` to form as many instances of the word **"balloon"** as possible.

You can use each character in `text` **at most once**. Return the maximum number of instances that can be formed.

**Example 1:**
```
Input: text = "nlaebolko"
Output: 1
```

**Example 2:**
```
Input: text = "loonbalxballpoon"
Output: 2
```

**Example 3:**
```
Input: text = "leetcode"
Output: 0
```

**Constraints:**
- `1 <= text.length <= 10^4`
- `text` consists of lower case English letters only.

## Approach

**Character Frequency Counting**

The word "balloon" consists of the following letters with their required counts:
- `b`: 1
- `a`: 1
- `l`: 2
- `o`: 2
- `n`: 1

Algorithm:
1. Count the frequency of each character in the input string `text`.
2. For each required letter, compute how many full words can be formed using only that letter's supply.
3. The overall answer is the minimum across all five required letters.

Since `l` and `o` appear twice in "balloon", we divide their counts by 2 (integer division) before taking the minimum.

## Complexity Analysis

- **Time Complexity:** O(n) — we iterate through the string once to count character frequencies, then perform O(1) arithmetic operations.
- **Space Complexity:** O(1) — we use a fixed-size frequency array of 26 characters (or a hash map with at most 5 entries).
