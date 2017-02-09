package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LazyTest
{
    @Test
    public void testForceConstant() {
        final Lazy<Integer> x = Lazy.delay(1);
        assertEquals(1, x.force().intValue());
    }

    @Test
    public void testForceThunk() {
        final Lazy<Integer> x = Lazy.delay(1);
        final Lazy<Integer> y = Lazy.delay(x, x_ -> x_ * 2);
        assertEquals(2, y.force().intValue());
    }

    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @Test
    public void testUnbound() {
        final LateBound<Integer> x = Lazy.delay();
        assertFalse(x.isBound());
        assertFalse(x.isForced());
    }

    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    @Test
    public void testBoundConstant() {
        final LateBound<Integer> x = Lazy.delay();
        x.bind(Lazy.delay(1));
        assertTrue(x.isBound());
        assertEquals(1, x.force().intValue());
        assertTrue(x.isForced());
    }

    @Test
    public void testBoundThunk() {
        final LateBound<Integer> x = Lazy.delay();
        final Lazy<Integer> a = Lazy.delay(1);
        final Lazy<Integer> b = Lazy.delay(a, a_ -> a_ + 1);
        x.bind(b);
        assertEquals(2, x.force().intValue());
    }

    @Test
    public void testForceLongChain() {
        final int N = 1_000_000;
        Lazy<Integer> x = Lazy.delay(0);
        for (int i = 0; i < N; ++i) {
            x = Lazy.delay(x, x_ -> x_ + 1);
        }
        assertEquals(N, x.force().intValue());
    }
}
