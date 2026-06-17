/**
 * LeetCode 2130 - 链表最大孪生和 (Maximum Twin Sum of a Linked List)
 * 难度: Medium
 * 链接: https://leetcode.cn/problems/maximum-twin-sum-of-a-linked-list/
 * 日期: 2026-06-17
 *
 * 题目描述:
 * 在一个大小为 n 且 n 为偶数的链表中，对于 0 <= i <= (n/2) - 1 的 i，
 * 第 i 个节点（下标从 0 开始）的孪生节点为第 (n-1-i) 个节点。
 * 孪生和定义为一个节点和它孪生节点两者值之和。
 * 给你一个长度为偶数的链表的头节点 head，请你返回链表的 最大孪生和。
 *
 * 示例 1:
 *   输入: head = [5,4,2,1]
 *   输出: 6
 *   解释: 孪生节点对: (0,3)=5+1=6, (1,2)=4+2=6, 最大孪生和为6
 *
 * 示例 2:
 *   输入: head = [4,2,2,3]
 *   输出: 7
 *   解释: 孪生节点对: (0,3)=4+3=7, (1,2)=2+2=4, 最大孪生和为max(7,4)=7
 *
 * 示例 3:
 *   输入: head = [1,100000]
 *   输出: 100001
 *   解释: 只有一对孪生节点，(0,1)=1+100000=100001
 *
 * 约束:
 *   - 链表的节点数目是 [2, 10^5] 中的偶数
 *   - 1 <= Node.val <= 10^5
 *
 * -------------------------------------------
 * 解题思路: 快慢指针 + 反转链表
 *
 * 核心洞察: 孪生节点对 (i, n-1-i) 关于链表中心对称。
 * 因此，将链表从中点分成两半，反转后半部分，
 * 然后同时遍历两半，计算对应位置的和即可。
 *
 * 算法步骤:
 *   1. 使用快慢指针找到链表中点
 *      - slow每次走1步，fast每次走2步
 *      - fast到末尾时，slow正好在中点（后半部分的起点）
 *   2. 反转后半部分链表
 *      - 标准链表反转，prev/curr/next三指针
 *   3. 同时遍历前半部分和反转后的后半部分
 *      - 计算 twinSum = firstHalf.val + secondHalf.val
 *      - 更新最大值
 *   4. 返回最大孪生和
 *
 * 时间复杂度: O(n) — 遍历链表三次（找中点、反转、计算和）
 * 空间复杂度: O(1) — 只使用常数个指针变量
 *
 * 相关话题: 栈、链表、双指针
 */
class Solution {
    public int pairSum(ListNode head) {
        // 步骤1: 快慢指针找中点
        // 对于偶数长度链表，slow最终指向后半部分的第一个节点
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        // 此时 slow 指向后半部分的第一个节点（即索引 n/2 处）

        // 步骤2: 反转后半部分链表
        ListNode prev = null;
        ListNode curr = slow;
        while (curr != null) {
            ListNode nextTemp = curr.next; // 保存下一个节点
            curr.next = prev;              // 反转指针
            prev = curr;                   // prev 前进
            curr = nextTemp;               // curr 前进
        }
        // 此时 prev 指向反转后的后半部分链表的头节点

        // 步骤3: 同时遍历两半，计算最大孪生和
        int maxTwinSum = 0;
        ListNode firstHalf = head;       // 前半部分（未反转）
        ListNode secondHalf = prev;      // 后半部分（已反转）

        while (secondHalf != null) {
            int twinSum = firstHalf.val + secondHalf.val;
            maxTwinSum = Math.max(maxTwinSum, twinSum);
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }

        return maxTwinSum;
    }
}

//leetcode submit region end(Prohibit modification and deletion)

// 测试代码
class Test {
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 测试用例1: head = [5,4,2,1], 期望输出: 6
        ListNode head1 = buildList(new int[]{5, 4, 2, 1});
        System.out.println("测试1: " + sol.pairSum(head1) + " (期望: 6)");

        // 测试用例2: head = [4,2,2,3], 期望输出: 7
        ListNode head2 = buildList(new int[]{4, 2, 2, 3});
        System.out.println("测试2: " + sol.pairSum(head2) + " (期望: 7)");

        // 测试用例3: head = [1,100000], 期望输出: 100001
        ListNode head3 = buildList(new int[]{1, 100000});
        System.out.println("测试3: " + sol.pairSum(head3) + " (期望: 100001)");

        // 测试用例4: 两个相同值, head = [10,10], 期望输出: 20
        ListNode head4 = buildList(new int[]{10, 10});
        System.out.println("测试4: " + sol.pairSum(head4) + " (期望: 20)");
    }

    // 辅助方法: 从数组构建链表
    private static ListNode buildList(int[] values) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        for (int v : values) {
            curr.next = new ListNode(v);
            curr = curr.next;
        }
        return dummy.next;
    }
}

// 链表节点定义（LeetCode内置，此处用于本地测试）
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
