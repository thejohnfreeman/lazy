package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

@SuppressWarnings("PMD.TooManyMethods")
public class ToStringTest
{
    @Test
    public void testConstant() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        assertEquals("1", one.toString());
    }

    @Test
    public void testThunk1Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, a -> a + 1);
        assertEquals("(_1_) -> ...", two.toString());
    }

    @Test
    public void testThunk1Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, a -> a + 1);
        LazyHelp.force(two);
        assertEquals("2", two.toString());
    }

    @Test
    public void testThunk2Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, one, (a, b) -> a + b);
        assertEquals("(_2_) -> ...", two.toString());
    }

    @Test
    public void testThunk2Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, one, (a, b) -> a + b);
        LazyHelp.force(two);
        assertEquals("2", two.toString());
    }

    @Test
    public void testThunk3Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> three = LazyHelp.delay(
            one, one, one, (a, b, c) -> a + b + c);
        assertEquals("(_3_) -> ...", three.toString());
    }

    @Test
    public void testThunk3Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> three = LazyHelp.delay(
            one, one, one, (a, b, c) -> a + b + c);
        LazyHelp.force(three);
        assertEquals("3", three.toString());
    }

    @Test
    public void testThunk4Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> four = LazyHelp.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        assertEquals("(_4_) -> ...", four.toString());
    }

    @Test
    public void testThunk4Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> four = LazyHelp.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        LazyHelp.force(four);
        assertEquals("4", four.toString());
    }

    @Test
    public void testThunk5Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> five = LazyHelp.delay(
            one, one, one, one, one, (a, b, c, d, e) -> a + b + c + d + e);
        assertEquals("(_5_) -> ...", five.toString());
    }

    @Test
    public void testThunk5Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> five = LazyHelp.delay(
            one, one, one, one, one, (a, b, c, d, e) -> a + b + c + d + e);
        LazyHelp.force(five);
        assertEquals("5", five.toString());
    }

    @Test
    public void testThunk6Unforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> six = LazyHelp.delay(
            one, one, one, one, one, one,
            (a, b, c, d, e, f) -> a + b + c + d + e + f);
        assertEquals("(_6_) -> ...", six.toString());
    }

    @Test
    public void testThunk6Forced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> six = LazyHelp.delay(
            one, one, one, one, one, one,
            (a, b, c, d, e, f) -> a + b + c + d + e + f);
        LazyHelp.force(six);
        assertEquals("6", six.toString());
    }

    @Test
    public void testLateBoundUnbound() {
        final LateBound<Integer> unbound = LazyHelp.delay();
        assertEquals("(???) -> ???", unbound.toString());
    }

    @Test
    public void testLateBoundForced() {
        final LateBound<Integer> bound = LazyHelp.delay();
        bound.bind(LazyHelp.delay(1));
        assertEquals("1", bound.toString());
    }

    @Test
    public void testLateBoundUnforced() {
        final LateBound<Integer> bound = LazyHelp.delay();
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, a -> a + 1);
        bound.bind(two);
        assertEquals("(_1_) -> ...", bound.toString());
    }

    @Test
    public void testLateBoundTransitiveUnbound() {
        final LateBound<Integer> outer = LazyHelp.delay();
        final LateBound<Integer> inner = LazyHelp.delay();
        outer.bind(inner);
        assertEquals("(???) -> ???", outer.toString());
    }

    @Test
    public void testLateBoundTransitiveForced() {
        final LateBound<Integer> outer = LazyHelp.delay();
        final LateBound<Integer> inner = LazyHelp.delay();
        outer.bind(inner);
        final Lazy<Integer> one = LazyHelp.delay(1);
        inner.bind(one);
        assertEquals("1", outer.toString());
    }

    @Test
    public void testCollectionThunkUnforced() {
        final Collection<Lazy<String>> lazies = ImmutableList.of(
            LazyHelp.delay("alpha"), LazyHelp.delay("beta"));
        final Lazy<Collection<String>> list = LazyHelp.sequence(lazies);
        assertEquals("(List[2]) -> ...", list.toString());
    }

    @Test
    public void testCollectionThunkForced() {
        // Use ArrayList just to show that it can pretty-print itself.
        final Collection<Lazy<String>> lazies = new ArrayList<>();
        lazies.add(LazyHelp.delay("alpha"));
        lazies.add(LazyHelp.delay("beta"));
        final Lazy<Collection<String>> list = LazyHelp.sequence(lazies);
        LazyHelp.force(list);
        assertEquals("[alpha, beta]", list.toString());
    }
}
