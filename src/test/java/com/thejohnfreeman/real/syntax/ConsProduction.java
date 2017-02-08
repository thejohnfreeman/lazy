package com.thejohnfreeman.real.syntax;

public class ConsProduction
    implements ListNode
{
    private final ListNode _head;
    private final DigitNode _tail;

    public ConsProduction(final ListNode head, final DigitNode tail) {
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
    public void accept(final NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return _head.toString() + _tail;
    }
}
