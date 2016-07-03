package com.jfreeman.lazy;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author jfreeman
 */
public class LazyHelpTest
{
    @Test
    public void testForceConstant() {
        final Lazy<Integer> x = LazyHelp.bind(1);
        assertEquals(1, (int) LazyHelp.force(x));
    }

    @Test
    public void testForceThunk() {
        final Lazy<Integer> x = LazyHelp.bind(1);
        final Lazy<Integer> y = LazyHelp.bind(x, x_ -> x_ * 2);
        assertEquals(2, (int) LazyHelp.force(y));
    }

    @Test
    public void testUnbound() {
        final LateBound<Integer> x = LazyHelp.bindLater();
        assertFalse(x.isBound());
        assertFalse(x.isForced());
    }

    @Test(expected=IllegalStateException.class)
    public void testForceUnbound() {
        final LateBound<Integer> x = LazyHelp.bindLater();
        x.force();
    }

    @Test
    public void testGetDependenciesUnbound() {
        final LateBound<Integer> x = LazyHelp.bindLater();
        assertEquals(null, x.getDependencies());
    }

    @Test
    public void testBoundConstant() {
        final LateBound<Integer> x = LazyHelp.bindLater();
        x.bind(LazyHelp.bind(1));
        assertTrue(x.isBound());
        assertEquals(1, (int) LazyHelp.force(x));
        assertTrue(x.isForced());
    }

    @Test
    public void testBoundThunk() {
        final LateBound<Integer> x = LazyHelp.bindLater();
        final Lazy<Integer> a = LazyHelp.bind(1);
        final Lazy<Integer> b = LazyHelp.bind(a, a_ -> a_ + 1);
        x.bind(b);
        assertEquals(2, (int) LazyHelp.force(x));
    }

    @Test
    public void testForceLongChain() {
        final int N = 1_000_000;
        Lazy<Integer> x = LazyHelp.bind(0);
        for (int i = 0; i < N; ++i) {
            x = LazyHelp.bind(x, x_ -> x_ + 1);
        }
        assertEquals(N, (int) LazyHelp.force(x));
    }
}