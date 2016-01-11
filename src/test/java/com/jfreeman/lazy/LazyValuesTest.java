package com.jfreeman.lazy;

import org.junit.Test;

import static org.junit.Assert.*;

import com.jfreeman.function.Function;

/**
 * @author jfreeman
 */
public class LazyValuesTest
{
    @Test
    public void testForceConstant() {
        final LazyValue<Integer> x = LazyValues.bind(1);
        assertEquals(1, (int) x.force());
    }

    @Test
    public void testForceThunk() {
        final LazyValue<Integer> x = LazyValues.bind(1);
        final LazyValue<Integer> y = LazyValues.bind(x, new Function<Integer, Integer>()
        {
            @Override
            public Integer apply(Integer x) {
                return x * 2;
            }
        });
        assertEquals(2, (int) y.force());
    }

    @Test
    public void testUnbound() {
        final LateBoundValue<Integer> x = LazyValues.bindLater();
        assertFalse(x.isBound());
        assertFalse(x.isForced());
    }

    @Test(expected=IllegalStateException.class)
    public void testForceUnbound() {
        final LateBoundValue<Integer> x = LazyValues.bindLater();
        x.force();
    }

    @Test(expected=IllegalStateException.class)
    public void testGetDependenciesUnbound() {
        final LateBoundValue<Integer> x = LazyValues.bindLater();
        x.getDependencies();
    }

    @Test
    public void testBoundConstant() {
        final LateBoundValue<Integer> x = LazyValues.bindLater();
        x.bind(LazyValues.bind(1));
        assertTrue(x.isBound());
        assertEquals(1, (int) x.force());
        assertTrue(x.isForced());
    }

    @Test
    public void testBoundThunk() {
        final LateBoundValue<Integer> x = LazyValues.bindLater();
        final LazyValue<Integer> a = LazyValues.bind(1);
        final LazyValue<Integer> b = LazyValues.bind(a, new Function<Integer, Integer>()
        {
            @Override
            public Integer apply(Integer a) {
                return a + 1;
            }
        });
        x.bind(b);
        assertEquals(2, (int) x.force());
    }

    private LazyValue<Integer> bindChain(int N) {
        LazyValue<Integer> x = LazyValues.bind(0);
        for (int i = 0; i < N; ++i) {
            x = LazyValues.bind(x, new Function<Integer, Integer>()
            {
                @Override
                public Integer apply(Integer x) {
                    return x + 1;
                }
            });
        }
        return x;
    }

    @Test(expected=StackOverflowError.class)
    public void testStackOverflow() {
        final int N = 1_000_000;
        assertEquals(N, (int) bindChain(N).force());
    }

    @Test
    public void testSafelyForce() {
        final int N = 1_000_000;
        assertEquals(N, (int) LazyValues.safelyForce(bindChain(N)));
    }
}