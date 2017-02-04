package com.thejohnfreeman.real.syntax;

public class FloatingPointProduction
    implements StartNode
{
    private final ListNode _left;
    private final ListNode _right;

    public FloatingPointProduction(final ListNode left, final ListNode right) {
        _left = left;
        _right = right;
    }

    public ListNode left() {
        return _left;
    }

    public ListNode right() {
        return _right;
    }

    @Override
    public void accept(final NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return _left + "." + _right;
    }
}
