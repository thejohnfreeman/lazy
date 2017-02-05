package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TaggableTest
{
    @Test
    public void testTag() {
        final String expected =
            "(???) -> xyz at TaggableTest.testTag() @ TaggableTest.java:14";
        // The origin is the next line.
        final Lazy<Object> lazy = LazyHelp.delay().tag("xyz");
        assertEquals(expected, lazy.toString());
    }

    @Test
    public void testTagWithExplicitStackLevel() {
        final String expected =
            "(???) -> xyz at TaggableTest.testTagWithExplicitStackLevel() @ TaggableTest.java:24";
        final Lazy<Object> lazy = LazyHelp
            // The origin is the next line.
            .delay().tag("xyz", /* stackLevel: */0);
        assertEquals(expected, lazy.toString());
    }

    private Lazy<Object> getTaggedLazy() {
        return LazyHelp.delay().tag("xyz", /* stackLevel: */1);
    }

    @Test
    public void testTagForCaller() {
        final String expected =
            "(???) -> xyz at TaggableTest.testTagForCaller() @ TaggableTest.java:37";
        // The origin is the next line.
        final Lazy<Object> lazy = getTaggedLazy();
        assertEquals(expected, lazy.toString());
    }

    @Test
    public void testForced() {
        final Lazy<Integer> one = LazyHelp.delay(1).tag("one");
        assertEquals("1", one.toString());
    }

    @Test
    public void testForce() {
        final Lazy<Integer> one = LazyHelp.delay(1).tag("one");
        LazyHelp.force(one);
        assertEquals(1, one.getValue().intValue());
    }
}
