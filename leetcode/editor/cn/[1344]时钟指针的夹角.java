/**
 * LeetCode 1344 - 时钟指针的夹角 (Angle Between Hands of a Clock)
 * 难度: Medium
 * 链接: https://leetcode.cn/problems/angle-between-hands-of-a-clock/
 * 日期: 2026-06-18
 *
 * 题目描述:
 * 给你两个数 hour 和 minutes。请你返回在时钟上，由给定时间的时针和分针组成的较小角的角度（60单位制）。
 * 答案与标准答案误差在 10^-5 以内的会被视为正确。
 *
 * 示例 1:
 *   输入: hour = 12, minutes = 30
 *   输出: 165
 *
 * 示例 2:
 *   输入: hour = 3, minutes = 30
 *   输出: 75
 *
 * 示例 3:
 *   输入: hour = 3, minutes = 15
 *   输出: 7.5
 *
 * 约束:
 *   - 1 <= hour <= 12
 *   - 0 <= minutes <= 59
 *
 * -------------------------------------------
 * 解题思路: 数学计算
 *
 * 核心洞察:
 *   - 分针每分钟走 6° (360° / 60)
 *   - 时针每小时走 30° (360° / 12)，同时每分钟还要额外走 0.5° (30° / 60)
 *   - 计算两针的角度差，取较小角 (min(diff, 360-diff))
 *
 * 算法步骤:
 *   1. 计算分针角度: minuteAngle = minutes * 6.0
 *   2. 计算时针角度: hourAngle = (hour % 12) * 30.0 + minutes * 0.5
 *      - hour % 12 处理 12 点的情况（12 点和 0 点时针位置相同）
 *   3. 计算角度差: diff = |hourAngle - minuteAngle|
 *   4. 返回较小角: min(diff, 360.0 - diff)
 *
 * 时间复杂度: O(1)
 * 空间复杂度: O(1)
 *
 * 相关话题: 数学
 */
class Solution {
    public double angleClock(int hour, int minutes) {
        // 分针每分钟走 6°
        double minuteAngle = minutes * 6.0;

        // 时针每小时走 30°，每分钟额外走 0.5°
        // hour % 12 将 12 映射为 0
        double hourAngle = (hour % 12) * 30.0 + minutes * 0.5;

        // 计算两针之间的夹角
        double diff = Math.abs(hourAngle - minuteAngle);

        // 返回较小角
        return Math.min(diff, 360.0 - diff);
    }
}

//leetcode submit region end(Prohibit modification and deletion)

// 测试代码
class Test {
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 测试用例1: hour = 12, minutes = 30, 期望输出: 165
        double result1 = sol.angleClock(12, 30);
        System.out.println("测试1: " + result1 + " (期望: 165) "
            + (Math.abs(result1 - 165) < 1e-5 ? "✓" : "✗"));

        // 测试用例2: hour = 3, minutes = 30, 期望输出: 75
        double result2 = sol.angleClock(3, 30);
        System.out.println("测试2: " + result2 + " (期望: 75) "
            + (Math.abs(result2 - 75) < 1e-5 ? "✓" : "✗"));

        // 测试用例3: hour = 3, minutes = 15, 期望输出: 7.5
        double result3 = sol.angleClock(3, 15);
        System.out.println("测试3: " + result3 + " (期望: 7.5) "
            + (Math.abs(result3 - 7.5) < 1e-5 ? "✓" : "✗"));

        // 边界测试: hour = 1, minutes = 0, 期望输出: 30
        double result4 = sol.angleClock(1, 0);
        System.out.println("测试4: " + result4 + " (期望: 30) "
            + (Math.abs(result4 - 30) < 1e-5 ? "✓" : "✗"));

        // 边界测试: hour = 6, minutes = 0, 期望输出: 180
        double result5 = sol.angleClock(6, 0);
        System.out.println("测试5: " + result5 + " (期望: 180) "
            + (Math.abs(result5 - 180) < 1e-5 ? "✓" : "✗"));

        // 边界测试: hour = 12, minutes = 0, 期望输出: 0
        double result6 = sol.angleClock(12, 0);
        System.out.println("测试6: " + result6 + " (期望: 0) "
            + (Math.abs(result6 - 0) < 1e-5 ? "✓" : "✗"));
    }
}
