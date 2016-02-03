package com.jfreeman.real.pass;

import com.jfreeman.real.syntax.ConsProduction;
import com.jfreeman.real.syntax.DigitNode;
import com.jfreeman.real.syntax.FloatingPointProduction;
import com.jfreeman.real.syntax.IntegerProduction;
import com.jfreeman.real.syntax.NodeVisitor;
import com.jfreeman.real.syntax.SingletonProduction;

/**
 * @author jfreeman
 */
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
