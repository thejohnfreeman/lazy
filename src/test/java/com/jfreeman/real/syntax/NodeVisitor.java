package com.jfreeman.real.syntax;

public interface NodeVisitor
{
    void visit(FloatingPointProduction node);

    void visit(IntegerProduction node);

    void visit(ConsProduction node);

    void visit(SingletonProduction node);

    void visit(DigitNode node);
}
