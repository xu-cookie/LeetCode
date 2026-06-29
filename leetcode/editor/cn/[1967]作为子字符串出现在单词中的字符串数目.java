/**
 * LeetCode 1967. 作为子字符串出现在单词中的字符串数目
 * 难度: 简单 (Easy)
 *
 * 题目描述:
 * 给你一个字符串数组 patterns 和一个字符串 word，统计 patterns 中有多少个字符串
 * 是 word 的子字符串。返回字符串数目。
 * 子字符串是字符串中的一个连续字符序列。
 *
 * 示例 1:
 * 输入: patterns = ["a","abc","bc","d"], word = "abc"
 * 输出: 3
 *
 * 示例 2:
 * 输入: patterns = ["a","b","c"], word = "aaaaabbbbb"
 * 输出: 2
 *
 * 示例 3:
 * 输入: patterns = ["a","a","a"], word = "ab"
 * 输出: 3
 *
 * 解题思路:
 * 遍历 patterns 数组，使用 String.contains() 方法判断每个 pattern 是否为 word
 * 的子串，统计计数即可。
 *
 * 时间复杂度: O(n * m) - n 为 patterns 长度，m 为 word 长度
 * 空间复杂度: O(1)
 */
class Solution {
    public int numOfStrings(String[] patterns, String word) {
        int count = 0;
        for (String pattern : patterns) {
            if (word.contains(pattern)) {
                count++;
            }
        }
        return count;
    }
}
