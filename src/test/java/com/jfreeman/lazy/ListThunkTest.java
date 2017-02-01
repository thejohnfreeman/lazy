package com.jfreeman.lazy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class ListThunkTest
{
    private <T> void assertListEquals(List<T> expected, List<Lazy<T>> lazies) {
        final Lazy<List<T>> lazyList = ListThunk.of(lazies);
        final List<T> actual = LazyHelp.force(lazyList);
        assertThat(actual, CoreMatchers.is(expected));
    }

    @Test
    public void testEmptyList() {
        final List<Lazy<Integer>> lazies = Collections.emptyList();
        assertListEquals(Collections.emptyList(), lazies);
    }

    @Test
    public void testSingletoneList() {
        final List<Lazy<Integer>> lazies = ImmutableList.of(LazyHelp.delay(1));
        assertListEquals(ImmutableList.of(1), lazies);
    }

    @Test
    public void testList() {
        final List<Lazy<Integer>> lazies = ImmutableList.of(
            LazyHelp.delay(1),
            LazyHelp.delay(2),
            LazyHelp.delay(3));
        assertListEquals(ImmutableList.of(1, 2, 3), lazies);
    }

    @Test
    public void testInterdependentList() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, x -> x + 1);
        final Lazy<Integer> three = LazyHelp.delay(two, x -> x + 1);
        final List<Lazy<Integer>> lazies = ImmutableList.of(one, two, three);
        assertListEquals(ImmutableList.of(1, 2, 3), lazies);
    }

    @Test
    public void testInterdependentSum() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, x -> x + 1);
        final Lazy<Integer> three = LazyHelp.delay(two, x -> x + 1);
        final List<Lazy<Integer>> lazies = ImmutableList.of(one, two, three);
        final Lazy<Integer> sum = ListThunk.of(lazies,
            l -> l.stream().mapToInt(Integer::intValue).sum());
        assertEquals(6, LazyHelp.force(sum).intValue());
    }

}
