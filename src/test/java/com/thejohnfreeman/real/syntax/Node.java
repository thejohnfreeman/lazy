package com.thejohnfreeman.real.syntax;

public interface Node
{
    void accept(NodeVisitor visitor);

    /**
     * The string representation of a node is essentially a synthesized
     * attribute. We will use the conventional call-stack based implementation.
     *
     * @return human-readable string representation
     */
    @Override
    String toString();
}
