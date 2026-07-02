/**
 * LeetCode 每日一题 - 2026-06-29
 * 题目: 1846. 减小和重新排列数组后的最大元素
 * 难度: Medium
 * 链接: https://leetcode.cn/problems/maximum-element-after-decreasing-and-rearranging/
 *
 * 题目描述:
 * 给你一个正整数数组 arr 。请你对 arr 执行一些操作（也可以不进行任何操作），
 * 使得数组满足以下条件：
 * - arr 中第一个元素必须为 1 。
 * - 任意相邻两个元素的差的绝对值 小于等于 1 。
 *  即 abs(arr[i] - arr[i - 1]) <= 1 （0-indexed）。
 *
 * 你可以执行以下 2 种操作任意次：
 * - 减小 arr 中任意元素的值，使其变为一个更小的正整数。
 * - 重新排列 arr 中的元素，你可以以任意顺序重新排列。
 *
 * 返回执行以上操作后，arr 中可能的最大值。
 *
 * 解题思路（排序 + 贪心）:
 * 1. 对数组排序。由于可以自由重新排列，排序可以将元素按最优顺序放置。
 * 2. 初始化 cur = 0，表示当前位置能获得的最大值。
 * 3. 遍历排序后的数组，对于每个元素 x：
 *    - cur = min(x, cur + 1)
 *    - 如果 x 足够大，就在前一个值的基础上 +1
 *    - 如果 x 太小，就受限于 x 本身
 * 4. 最终 cur 即为可能达到的最大值。
 *
 * 核心洞察：
 * - 经过排序后，贪心地尝试构建 [1, 2, 3, ..., k]
 * - 每个位置的值受限于该位置可用的元素值
 * - cur+1 是贪心选择：总是希望下一个值尽可能大
 * - min(x, cur+1) 确保我们不会超过实际可用的值
 *
 * 时间复杂度: O(n log n) - 排序主导
 * 空间复杂度: O(1) - 原地算法
 */

import java.util.Arrays;

class Solution {
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        // 排序：将元素按非递减顺序排列
        Arrays.sort(arr);

        // cur 记录当前位置能达到的最大值
        // 初始为 0，第一个元素经 min(x, 0+1) 后必为 1
        int cur = 0;

        for (int x : arr) {
            // 贪心：尽量在前一个值基础上 +1，但不能超过 x
            cur = Math.min(x, cur + 1);
        }

        return cur;
    }

    // 测试用例
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 示例 1
        int[] arr1 = {2, 2, 1, 2, 1};
        System.out.println("示例 1: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr1)
                + " (预期: 2)");

        // 示例 2
        int[] arr2 = {100, 1, 1000};
        System.out.println("示例 2: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr2)
                + " (预期: 3)");

        // 示例 3
        int[] arr3 = {1, 2, 3, 4, 5};
        System.out.println("示例 3: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr3)
                + " (预期: 5)");

        // 边界测试：单个元素
        int[] arr4 = {1};
        System.out.println("单个元素: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr4)
                + " (预期: 1)");

        // 边界测试：全部相同
        int[] arr5 = {5, 5, 5, 5};
        System.out.println("全相同: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr5)
                + " (预期: 4)");

        // 边界测试：全1
        int[] arr6 = {1, 1, 1, 1, 1};
        System.out.println("全1: " +
                sol.maximumElementAfterDecrementingAndRearranging(arr6)
                + " (预期: 1)");
    }
}
