package com.jfreeman.real.syntax;

/**
 * @author jfreeman
 */
public class ConsProduction
    implements ListNode
{
    private final ListNode _head;
    private final DigitNode _tail;

    public ConsProduction(ListNode head, DigitNode tail) {
        _head = head;
        _tail = tail;
    }

    public ListNode head() {
        return _head;
    }

    public DigitNode tail() {
        return _tail;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "" + _head + _tail;
    }
}
