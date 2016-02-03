package com.jfreeman.real.syntax;

/**
 * No point splitting this into multiple productions. Arguably no point in
 * having a node at all (just use an int), but we will for demonstration.
 *
 * @author jfreeman
 */
public class DigitNode
    implements Node
{
    private final int _value;

    public DigitNode(int value) {
        _value = value;
    }

    public int value() {
        return _value;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "" + _value;
    }
}
