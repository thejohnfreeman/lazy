package com.thejohnfreeman.real.syntax;

/**
 * No point splitting this into multiple productions. Arguably no point in
 * having a node at all (just use an int), but we will for demonstration.
 *
 * @author thejohnfreeman
 */
public class DigitNode
    implements Node
{
    private final int _value;

    public DigitNode(final int value) {
        _value = value;
    }

    public int value() {
        return _value;
    }

    @Override
    public void accept(final NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.valueOf(_value);
    }
}
