package com.thejohnfreeman.real.syntax;

public abstract class AbstractNodeVisitor
    implements NodeVisitor
{
    protected void defaultVisit(final Node node) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void visit(final FloatingPointProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(final IntegerProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(final ConsProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(final SingletonProduction node) {
        defaultVisit(node);
    }

    @Override
    public void visit(final DigitNode node) {
        defaultVisit(node);
    }
}
