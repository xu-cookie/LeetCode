/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode deleteMiddle(ListNode head) {
        // 只有一个节点，删除后链表为空
        if (head == null || head.next == null) {
            return null;
        }

        // 快慢指针：fast 每次走两步，slow 每次走一步
        // prev 记录 slow 的前一个节点，用于删除
        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        // 删除中间节点 slow
        prev.next = slow.next;

        return head;
    }
}
