package com.thejohnfreeman.lazy;

import org.junit.Test;

import com.thejohnfreeman.attribute.LateBoundAttribute;

public class ErrorsTest
{
    @Test(expected = IllegalStateException.class)
    public void testConstantForce() {
        final Lazy<Integer> one = Lazy.delay(1);
        one.forceThis();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstantDependencies() {
        final Lazy<Integer> one = Lazy.delay(1);
        one.getDependencies();
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundAttributePutTwice() {
        final LateBoundAttribute<Object, Integer> attribute =
            new LateBoundAttribute<>();
        attribute.put(this, Lazy.delay(1));
        attribute.put(this, Lazy.delay(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundBindTwice() {
        final LateBound<Integer> lazy = Lazy.delay();
        lazy.bind(Lazy.delay(1));
        lazy.bind(Lazy.delay(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundGetDependenciesBeforeBind() {
        final LateBound<Integer> lazy = Lazy.delay();
        lazy.getDependencies();
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundForceBeforeBind() {
        final LateBound<Integer> lazy = Lazy.delay();
        lazy.forceThis();
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundGetValueBeforeBind() {
        final LateBound<Integer> lazy = Lazy.delay();
        lazy.getValue();
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1GetValueBeforeForce() {
        final Lazy<Integer> two = Lazy.delay(Lazy.delay(1), a -> a + 1);
        two.getValue();
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1GetDependenciesAfterForce() {
        final Lazy<Integer> two = Lazy.delay(Lazy.delay(1), a -> a + 1);
        two.force();
        two.getDependencies();
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1ForceAfterForce() {
        final Lazy<Integer> two = Lazy.delay(Lazy.delay(1), a -> a + 1);
        two.force();
        two.forceThis();
    }
}
