package com.thejohnfreeman.real.syntax;

public abstract class AbstractNodeVisitor
    implements NodeVisitor
{
    protected void defaultVisit(Node node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void visit(FloatingPointProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(IntegerProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(ConsProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(SingletonProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(DigitNode node) {
        defaultVisit(node);
    }
}
