package com.thejohnfreeman.real.pass;

import com.thejohnfreeman.attribute.Attribute;
import com.thejohnfreeman.attribute.EarlyBoundAttribute;
import com.thejohnfreeman.attribute.LateBoundAttribute;
import com.thejohnfreeman.lazy.Lazy;
import com.thejohnfreeman.real.syntax.ConsProduction;
import com.thejohnfreeman.real.syntax.DigitNode;
import com.thejohnfreeman.real.syntax.FloatingPointProduction;
import com.thejohnfreeman.real.syntax.IntegerProduction;
import com.thejohnfreeman.real.syntax.ListNode;
import com.thejohnfreeman.real.syntax.Node;
import com.thejohnfreeman.real.syntax.SingletonProduction;

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

    public static double evaluate(final Node root) {
        final Visitor visitor = new Visitor();
        root.accept(visitor);
        return visitor._val.get(root).force();
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
        protected void annotate(final FloatingPointProduction node) {
            _val.put(node, Lazy.delay(
                _val.get(node.left()),
                _val.get(node.right()),
                (l, r) -> l + r
            ));
            _side.put(node.left(), Lazy.delay(Side.LEFT));
            _side.put(node.right(), Lazy.delay(Side.RIGHT));
        }

        @Override
        protected void annotate(final IntegerProduction node) {
            _val.put(node, _val.get(node.list()));
            _side.put(node.list(), Lazy.delay(Side.LEFT));
        }

        @Override
        protected void annotate(final ConsProduction node) {
            _len.put(node, Lazy.delay(
                _len.get(node.head()),
                len -> len + 1
            ));
            _val.put(node, Lazy.delay(
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
        protected void annotate(final SingletonProduction node) {
            _len.put(node, Lazy.delay(1));
            _val.put(node, Lazy.delay(
                _val.get(node.digit()),
                _side.get(node),
                (v, side) -> (side == Side.RIGHT) ? v / BASE : v
            ));
        }

        @Override
        protected void annotate(final DigitNode node) {
            _val.put(node, Lazy.delay((double) node.value()));
        }
    }
}
