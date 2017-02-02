package com.jfreeman.real.pass;

import com.jfreeman.attribute.Attribute;
import com.jfreeman.attribute.EarlyBoundAttribute;
import com.jfreeman.attribute.LateBoundAttribute;
import com.jfreeman.lazy.LazyHelp;
import com.jfreeman.real.syntax.ConsProduction;
import com.jfreeman.real.syntax.DigitNode;
import com.jfreeman.real.syntax.FloatingPointProduction;
import com.jfreeman.real.syntax.IntegerProduction;
import com.jfreeman.real.syntax.ListNode;
import com.jfreeman.real.syntax.Node;
import com.jfreeman.real.syntax.SingletonProduction;

/**
 * <pre>
 * syn S.val :: double
 * inh L.side :: left | right
 * syn L.len :: int
 * syn L.val :: double
 * syn B.val :: double
 * </pre>
 *
 * <p>Parameterize by base:</p>
 *
 * <pre>
 * double BASE = 2.0
 * </pre>
 *
 * <pre>
 * S -> L1 '.' L2
 * S.val = L1.val + L2.val
 * L1.side = left
 * L2.side = right
 * </pre>
 *
 * <pre>
 * S -> L
 * S.val = L.val
 * L.side = left
 * </pre>
 *
 * <pre>
 * L -> L1 B
 * L.len = L1.len + 1
 * L.val = (L.side == left)
 *     ? L1.val * BASE + B.val
 *     : L1.val        + B.val / pow(BASE, L.len)
 * L1.side = L.side
 * </pre>
 *
 * <pre>
 * L -> B
 * L.len = 1
 * L.val = { double v = B.val; if (L.side == right) { v /= BASE; } return v; }
 * </pre>
 *
 * <pre>
 * B -> 0
 * B.val = 0.0
 * </pre>
 *
 * <pre>
 * B -> 1
 * B.val = 1.0
 * </pre>
 */
public class LAttributedValuePass
{
    private static final double BASE = 2.0;

    /** Just a namespace. */
    private LAttributedValuePass() {}

    public static double evaluate(Node root) {
        Visitor visitor = new Visitor();
        root.accept(visitor);
        return LazyHelp.force(visitor._val.get(root));
    }

    private enum Side {
        LEFT, RIGHT;
    }

    private static class Visitor
        extends AbstractPassVisitor
    {
        private final Attribute<Node, Double> _val
            = new EarlyBoundAttribute<>();
        private final Attribute<ListNode, Integer> _len
            = new EarlyBoundAttribute<>();
        private final Attribute<ListNode, Side> _side
            = new LateBoundAttribute<>();

        @Override
        protected void annotate(FloatingPointProduction node) {
            _val.put(node, LazyHelp.delay(
                _val.get(node.left()),
                _val.get(node.right()),
                (l, r) -> l + r
            ));
            _side.put(node.left(), LazyHelp.delay(Side.LEFT));
            _side.put(node.right(), LazyHelp.delay(Side.RIGHT));
        }

        @Override
        protected void annotate(IntegerProduction node) {
            _val.put(node, _val.get(node.list()));
            _side.put(node.list(), LazyHelp.delay(Side.LEFT));
        }

        @Override
        protected void annotate(ConsProduction node) {
            _len.put(node, LazyHelp.delay(
                _len.get(node.head()),
                len -> len + 1
            ));
            _val.put(node, LazyHelp.delay(
                _side.get(node),
                _val.get(node.head()),
                _val.get(node.tail()),
                _len.get(node),
                (side, h, t, len) -> (side == Side.LEFT)
                       ? h * BASE + t
                       : h + t / Math.pow(BASE, len)
            ));
            _side.put(node.head(), _side.get(node));
        }

        @Override
        protected void annotate(SingletonProduction node) {
            _len.put(node, LazyHelp.delay(1));
            _val.put(node, LazyHelp.delay(
                _val.get(node.digit()),
                _side.get(node),
                (v, side) -> (side == Side.RIGHT) ? v / BASE : v
            ));
        }

        @Override
        protected void annotate(DigitNode node) {
            _val.put(node, LazyHelp.delay((double) node.value()));
        }
    }
}
