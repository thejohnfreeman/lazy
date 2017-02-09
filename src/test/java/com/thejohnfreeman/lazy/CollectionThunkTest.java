package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CollectionThunkTest
{
    private <T> void assertCollectionEquals(
        final Collection<T> expected, final Collection<Lazy<T>> lazies) {
        final Lazy<List<T>> lazyList = Lazy.sequence(lazies);
        final Collection<T> actual = lazyList.force();
        assertThat(actual, CoreMatchers.is(expected));
    }

    @Test
    public void testEmptyList() {
        final Collection<Lazy<Integer>> lazies = Collections.emptyList();
        assertCollectionEquals(Collections.emptyList(), lazies);
    }

    @Test
    public void testSingletonList() {
        final Collection<Lazy<Integer>> lazies =
            ImmutableList.of(Lazy.delay(1));
        assertCollectionEquals(ImmutableList.of(1), lazies);
    }

    @Test
    public void testList() {
        final Collection<Lazy<Integer>> lazies = ImmutableList.of(
            Lazy.delay(1),
            Lazy.delay(2),
            Lazy.delay(3));
        assertCollectionEquals(ImmutableList.of(1, 2, 3), lazies);
    }

    @Test
    public void testInterdependentList() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, x -> x + 1);
        final Lazy<Integer> three = Lazy.delay(two, x -> x + 1);
        final Collection<Lazy<Integer>> lazies =
            ImmutableList.of(one, two, three);
        assertCollectionEquals(ImmutableList.of(1, 2, 3), lazies);
    }

    @Test
    public void testInterdependentSum() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, x -> x + 1);
        final Lazy<Integer> three = Lazy.delay(two, x -> x + 1);
        final Collection<Lazy<Integer>> lazies =
            ImmutableList.of(one, two, three);
        final Lazy<Integer> sum = Lazy.delay(lazies,
            l -> l.stream().mapToInt(Integer::intValue).sum());
        assertEquals(6, sum.force().intValue());
    }

    // The tests below are just to assert the type checker.

    @Test
    public void testFunctionOfSuperTypeOfListOfSameType() {
        final Collection<Lazy<Integer>> lazies = Collections.emptyList();
        // We can map functions that want a specific super type of Collection
        // containing the same element type, e.g.
        // a function over Iterable<Integer> should accept a Collection<Integer>.
        final Function<? super List<Integer>, String> function =
            Object::toString;
        final Lazy<String> value = Lazy.delay(lazies, function);
        assertNotNull(value);
    }

    @Test
    public void testFunctionOfListOfSuperType() {
        final Collection<Lazy<Integer>> lazies = Collections.emptyList();
        // We can map functions that want the same type of Collection
        // containing a super type of the element type, e.g.
        // a function over Collection<Object> should accept Collection<Integer>.
        final Function<List<? super Integer>, String> function =
            Collection::toString;
        final Lazy<String> value = Lazy.delay(lazies, function);
        assertNotNull(value);
    }

    @Test
    public void testFunctionOfSuperTypeOfListOfSuperType() {
        final Collection<Lazy<Integer>> lazies = Collections.emptyList();
        // We can map functions that want a specific super type of Collection
        // containing a super type of the element type, e.g.
        // a function over Iterable<Object> should accept Collection<Integer>.
        final Function<? super Iterable<? super Integer>, String> function =
            Iterable::toString;
        final Lazy<String> value = Lazy.delay(lazies, function);
        assertNotNull(value);
    }
}
