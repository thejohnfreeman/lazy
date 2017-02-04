package com.thejohnfreeman.real.pass;

import com.thejohnfreeman.real.syntax.ConsProduction;
import com.thejohnfreeman.real.syntax.DigitNode;
import com.thejohnfreeman.real.syntax.FloatingPointProduction;
import com.thejohnfreeman.real.syntax.IntegerProduction;
import com.thejohnfreeman.real.syntax.NodeVisitor;
import com.thejohnfreeman.real.syntax.SingletonProduction;

public abstract class AbstractPassVisitor
    implements NodeVisitor
{
    protected abstract void annotate(FloatingPointProduction node);

    protected abstract void annotate(IntegerProduction node);

    protected abstract void annotate(ConsProduction node);

    protected abstract void annotate(SingletonProduction node);

    protected abstract void annotate(DigitNode node);

    @Override
    public void visit(FloatingPointProduction node) {
        node.left().accept(this);
        node.right().accept(this);
        annotate(node);
    }

    @Override
    public void visit(IntegerProduction node) {
        node.list().accept(this);
        annotate(node);
    }

    @Override
    public void visit(ConsProduction node) {
        node.head().accept(this);
        node.tail().accept(this);
        annotate(node);
    }

    @Override
    public void visit(SingletonProduction node) {
        node.digit().accept(this);
        annotate(node);
    }

    @Override
    public void visit(DigitNode node) {
        annotate(node);
    }
}
