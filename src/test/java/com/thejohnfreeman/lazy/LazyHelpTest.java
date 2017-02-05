package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LazyHelpTest
{
    @Test
    public void testForceConstant() {
        final Lazy<Integer> x = LazyHelp.delay(1);
        assertEquals(1, (int) LazyHelp.force(x));
    }

    @Test
    public void testForceThunk() {
        final Lazy<Integer> x = LazyHelp.delay(1);
        final Lazy<Integer> y = LazyHelp.delay(x, x_ -> x_ * 2);
        assertEquals(2, (int) LazyHelp.force(y));
    }

    @Test
    public void testUnbound() {
        final LateBound<Integer> x = LazyHelp.delay();
        assertFalse(x.isBound());
        assertFalse(x.isForced());
    }

    @Test
    public void testBoundConstant() {
        final LateBound<Integer> x = LazyHelp.delay();
        x.bind(LazyHelp.delay(1));
        assertTrue(x.isBound());
        assertEquals(1, (int) LazyHelp.force(x));
        assertTrue(x.isForced());
    }

    @Test
    public void testBoundThunk() {
        final LateBound<Integer> x = LazyHelp.delay();
        final Lazy<Integer> a = LazyHelp.delay(1);
        final Lazy<Integer> b = LazyHelp.delay(a, a_ -> a_ + 1);
        x.bind(b);
        assertEquals(2, (int) LazyHelp.force(x));
    }

    @Test
    public void testForceLongChain() {
        final int N = 1_000_000;
        Lazy<Integer> x = LazyHelp.delay(0);
        for (int i = 0; i < N; ++i) {
            x = LazyHelp.delay(x, x_ -> x_ + 1);
        }
        assertEquals(N, (int) LazyHelp.force(x));
    }
}
