package com.jfreeman.lazy;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class TaggableTest {
    @Test
    public void testDebug() {
        final String str = Constant.of(1).debug().toString();
        assertThat(str, startsWith(
            "[com.jfreeman.lazy.TaggableTest.testDebug() @ TaggableTest.java:12]"));
    }

    @Test
    public void testWrappedClassNameUsed() {
        final String str = Constant.of(1).tag().toString();
        assertThat(str, containsString("Constant"));
    }

    @Test
    public void testTag() {
        final String str = Constant.of(1).tag("one").toString();
        assertThat(str, startsWith("[one]"));
    }

    @Test
    public void testTags() {
        final String str = Constant.of(1).tag("one", "two").toString();
        assertThat(str, startsWith("[one, two]"));
    }

    @Test
    public void testTagChain() {
        final String str = Constant.of(1).tag("one").tag("two").toString();
        assertThat(str, startsWith("[one, two]"));
    }
}