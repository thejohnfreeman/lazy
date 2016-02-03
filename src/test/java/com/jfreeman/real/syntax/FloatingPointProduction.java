package com.jfreeman.real.syntax;

/**
 * @author jfreeman
 */
public class FloatingPointProduction
    implements StartNode
{
    private final ListNode _left, _right;

    public FloatingPointProduction(ListNode left, ListNode right) {
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
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return _left + "." + _right;
    }
}
