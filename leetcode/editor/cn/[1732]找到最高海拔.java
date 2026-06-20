/**
 * LeetCode 1732 - 找到最高海拔 (Find the Highest Altitude)
 * 难度: Easy
 * 链接: https://leetcode.cn/problems/find-the-highest-altitude/
 * 日期: 2026-06-19
 *
 * 题目描述:
 * 有一个自行车手打算进行一场公路骑行，这条路线总共由 n + 1 个不同海拔的点组成。
 * 自行车手从海拔为 0 的点 0 开始骑行。
 * 给你一个长度为 n 的整数数组 gain，其中 gain[i] 是点 i 和点 i + 1 的净海拔高度差（0 <= i < n）。
 * 请你返回最高点的海拔。
 *
 * 示例 1:
 *   输入: gain = [-5,1,5,0,-7]
 *   输出: 1
 *   解释: 海拔依次为 [0,-5,-4,1,1,-6]。最高海拔为 1。
 *
 * 示例 2:
 *   输入: gain = [-4,-3,-2,-1,4,3,2]
 *   输出: 0
 *   解释: 海拔依次为 [0,-4,-7,-9,-10,-6,-3,-1]。最高海拔为 0。
 *
 * 约束:
 *   - n == gain.length
 *   - 1 <= n <= 100
 *   - -100 <= gain[i] <= 100
 *
 * -------------------------------------------
 * 解题思路: 前缀和 + 维护最大值
 *
 * 核心洞察:
 *   - 起始海拔为 0，每经过一段路海拔变化 gain[i]
 *   - 当前海拔 = 之前海拔 + gain[i]
 *   - 其实就是求前缀和数组中的最大值
 *
 * 算法步骤:
 *   1. 初始化 maxAltitude = 0, currentAltitude = 0
 *   2. 遍历 gain 数组:
 *      - currentAltitude += gain[i]
 *      - maxAltitude = max(maxAltitude, currentAltitude)
 *   3. 返回 maxAltitude
 *
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 *
 * 相关话题: 数组, 前缀和
 */
class Solution {
    public int largestAltitude(int[] gain) {
        int maxAltitude = 0;
        int currentAltitude = 0;

        for (int g : gain) {
            currentAltitude += g;
            maxAltitude = Math.max(maxAltitude, currentAltitude);
        }

        return maxAltitude;
    }
}

//leetcode submit region end(Prohibit modification and deletion)

// 测试代码
class Test {
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 测试用例1: gain = [-5,1,5,0,-7], 期望输出: 1
        int[] gain1 = {-5, 1, 5, 0, -7};
        int result1 = sol.largestAltitude(gain1);
        System.out.println("测试1: " + result1 + " (期望: 1) "
            + (result1 == 1 ? "✓" : "✗"));

        // 测试用例2: gain = [-4,-3,-2,-1,4,3,2], 期望输出: 0
        int[] gain2 = {-4, -3, -2, -1, 4, 3, 2};
        int result2 = sol.largestAltitude(gain2);
        System.out.println("测试2: " + result2 + " (期望: 0) "
            + (result2 == 0 ? "✓" : "✗"));

        // 边界测试: 全是正数
        int[] gain3 = {1, 2, 3, 4, 5};
        int result3 = sol.largestAltitude(gain3);
        System.out.println("测试3: " + result3 + " (期望: 15) "
            + (result3 == 15 ? "✓" : "✗"));

        // 边界测试: 全是负数
        int[] gain4 = {-10, -20, -30};
        int result4 = sol.largestAltitude(gain4);
        System.out.println("测试4: " + result4 + " (期望: 0) "
            + (result4 == 0 ? "✓" : "✗"));

        // 边界测试: 单个元素
        int[] gain5 = {100};
        int result5 = sol.largestAltitude(gain5);
        System.out.println("测试5: " + result5 + " (期望: 100) "
            + (result5 == 100 ? "✓" : "✗"));
    }
}
