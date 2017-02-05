package com.thejohnfreeman.lazy;

import org.junit.Test;

import com.thejohnfreeman.attribute.LateBoundAttribute;

public class ErrorsTest
{
    @Test(expected = IllegalStateException.class)
    public void testConstantForce() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        one.force();
    }

    @Test(expected = IllegalStateException.class)
    public void testConstantDependencies() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        one.getDependencies();
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundAttributePutTwice() {
        final LateBoundAttribute<Object, Integer> attribute =
            new LateBoundAttribute<>();
        attribute.put(this, LazyHelp.delay(1));
        attribute.put(this, LazyHelp.delay(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testLateBoundBindTwice() {
        final LateBound<Integer> lazy = LazyHelp.delay();
        lazy.bind(LazyHelp.delay(1));
        lazy.bind(LazyHelp.delay(2));
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1GetValueBeforeForce() {
        final Lazy<Integer> two = LazyHelp.delay(LazyHelp.delay(1), a -> a + 1);
        two.getValue();
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1GetDependenciesAfterForce() {
        final Lazy<Integer> two = LazyHelp.delay(LazyHelp.delay(1), a -> a + 1);
        LazyHelp.force(two);
        two.getDependencies();
    }

    @Test(expected = IllegalStateException.class)
    public void testThunk1ForceAfterForce() {
        final Lazy<Integer> two = LazyHelp.delay(LazyHelp.delay(1), a -> a + 1);
        LazyHelp.force(two);
        two.force();
    }
}
