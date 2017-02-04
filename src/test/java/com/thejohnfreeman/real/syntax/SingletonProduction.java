package com.thejohnfreeman.real.syntax;

public class SingletonProduction
    implements ListNode
{
    private final DigitNode _digit;

    public SingletonProduction(DigitNode digit) {
        _digit = digit;
    }

    public DigitNode digit() {
        return _digit;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return _digit.toString();
    }
}
