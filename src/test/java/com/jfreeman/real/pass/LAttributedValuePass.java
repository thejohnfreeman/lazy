package com.jfreeman.real.pass;

import com.jfreeman.attribute.Attribute;
import com.jfreeman.attribute.InheritedAttribute;
import com.jfreeman.attribute.SynthesizedAttribute;
import com.jfreeman.function.BiFunction;
import com.jfreeman.function.Function;
import com.jfreeman.function.QuadFunction;
import com.jfreeman.lazy.LazyHelp;
import com.jfreeman.real.syntax.ConsProduction;
import com.jfreeman.real.syntax.DigitNode;
import com.jfreeman.real.syntax.FloatingPointProduction;
import com.jfreeman.real.syntax.IntegerProduction;
import com.jfreeman.real.syntax.ListNode;
import com.jfreeman.real.syntax.Node;
import com.jfreeman.real.syntax.SingletonProduction;

/**
 * syn S.val :: double
 * inh L.side :: left | right
 * syn L.len :: int
 * syn L.val :: double
 * syn B.val :: double
 *
 * Parameterize by base:
 *
 * double BASE = 2.0
 *
 * S -> L1 '.' L2
 * S.val = L1.val + L2.val
 * L1.side = left
 * L2.side = right
 *
 * S -> L
 * S.val = L.val
 * L.side = left
 *
 * L -> L1 B
 * L.len = L1.len + 1
 * L.val = (L.side == left)
 *   ? L1.val * BASE + B.val
 *   : L1.val        + B.val / pow(BASE, L.len)
 * L1.side = L.side
 *
 * L -> B
 * L.len = 1
 * L.val = { double v = B.val; if (L.side == right) { v /= BASE; } return v; }
 *
 * B -> 0
 * B.val = 0.0
 *
 * B -> 1
 * B.val = 1.0
 *
 * @author jfreeman
 */
public class LAttributedValuePass
{
    public static double evaluate(Node root) {
        Visitor visitor = new Visitor();
        root.accept(visitor);
        return LazyHelp.force(visitor._val.get(root));
    }

    private static final double BASE = 2.0;

    private enum Side {
        LEFT, RIGHT;
    }

    private static class Visitor
        extends AbstractPassVisitor
    {
        private Attribute<Node, Double> _val
            = new SynthesizedAttribute<>();
        private Attribute<ListNode, Integer> _len
            = new SynthesizedAttribute<>();
        private Attribute<ListNode, Side> _side
            = new InheritedAttribute<>();

        @Override
        protected void annotate(FloatingPointProduction node) {
            _val.set(node, LazyHelp.bind(
                _val.get(node.left()),
                _val.get(node.right()),
                new BiFunction<Double, Double, Double>() {
                    @Override
                    public Double apply(Double l1, Double l2) {
                        return l1 + l2;
                    }
                }
            ));
            _side.set(node.left(), LazyHelp.bind(Side.LEFT));
            _side.set(node.right(), LazyHelp.bind(Side.RIGHT));
        }

        @Override
        protected void annotate(IntegerProduction node) {
            _val.set(node, _val.get(node.list()));
            _side.set(node.list(), LazyHelp.bind(Side.LEFT));
        }

        @Override
        protected void annotate(ConsProduction node) {
            _len.set(node, LazyHelp.bind(
                _len.get(node.head()),
                new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer len) {
                        return len + 1;
                    }
                }
            ));
            _val.set(node, LazyHelp.bind(
                _side.get(node),
                _val.get(node.head()),
                _val.get(node.tail()),
                _len.get(node),
                new QuadFunction<Side, Double, Double, Integer, Double>() {
                    @Override
                    public Double apply(
                        Side side, Double l1, Double b, Integer len)
                    {
                        return (side == Side.LEFT)
                               ? l1 * BASE + b
                               : l1 + b / Math.pow(BASE, len);
                    }
                }
            ));
            _side.set(node.head(), _side.get(node));
        }

        @Override
        protected void annotate(SingletonProduction node) {
            _len.set(node, LazyHelp.bind(1));
            _val.set(node, LazyHelp.bind(
                _val.get(node.digit()),
                _side.get(node),
                new BiFunction<Double, Side, Double>() {
                    @Override
                    public Double apply(Double v, Side side) {
                        if (side == Side.RIGHT) {
                            v /= BASE;
                        }
                        return v;
                    }
                }
            ));
        }

        @Override
        protected void annotate(DigitNode node) {
            _val.set(node, LazyHelp.bind((double) node.value()));
        }
    }
}
