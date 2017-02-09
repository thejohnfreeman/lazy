package com.thejohnfreeman.lazy;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

@SuppressWarnings("PMD.TooManyMethods")
public class ToStringTest
{
    @Test
    public void testConstant() {
        final Lazy<Integer> one = Lazy.delay(1);
        assertEquals("1", one.toString());
    }

    @Test
    public void testThunk1Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, a -> a + 1);
        assertEquals("(_1_) -> ...", two.toString());
    }

    @Test
    public void testThunk1Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, a -> a + 1);
        two.force();
        assertEquals("2", two.toString());
    }

    @Test
    public void testThunk2Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, one, (a, b) -> a + b);
        assertEquals("(_2_) -> ...", two.toString());
    }

    @Test
    public void testThunk2Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, one, (a, b) -> a + b);
        two.force();
        assertEquals("2", two.toString());
    }

    @Test
    public void testThunk3Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> three = Lazy.delay(
            one, one, one, (a, b, c) -> a + b + c);
        assertEquals("(_3_) -> ...", three.toString());
    }

    @Test
    public void testThunk3Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> three = Lazy.delay(
            one, one, one, (a, b, c) -> a + b + c);
        three.force();
        assertEquals("3", three.toString());
    }

    @Test
    public void testThunk4Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> four = Lazy.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        assertEquals("(_4_) -> ...", four.toString());
    }

    @Test
    public void testThunk4Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> four = Lazy.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        four.force();
        assertEquals("4", four.toString());
    }

    @Test
    public void testThunk5Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> five = Lazy.delay(
            one, one, one, one, one, (a, b, c, d, e) -> a + b + c + d + e);
        assertEquals("(_5_) -> ...", five.toString());
    }

    @Test
    public void testThunk5Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> five = Lazy.delay(
            one, one, one, one, one, (a, b, c, d, e) -> a + b + c + d + e);
        five.force();
        assertEquals("5", five.toString());
    }

    @Test
    public void testThunk6Unforced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> six = Lazy.delay(
            one, one, one, one, one, one,
            (a, b, c, d, e, f) -> a + b + c + d + e + f);
        assertEquals("(_6_) -> ...", six.toString());
    }

    @Test
    public void testThunk6Forced() {
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> six = Lazy.delay(
            one, one, one, one, one, one,
            (a, b, c, d, e, f) -> a + b + c + d + e + f);
        six.force();
        assertEquals("6", six.toString());
    }

    @Test
    public void testLateBoundUnbound() {
        final LateBound<Integer> unbound = Lazy.delay();
        assertEquals("(???) -> ???", unbound.toString());
    }

    @Test
    public void testLateBoundForced() {
        final LateBound<Integer> bound = Lazy.delay();
        bound.bind(Lazy.delay(1));
        assertEquals("1", bound.toString());
    }

    @Test
    public void testLateBoundUnforced() {
        final LateBound<Integer> bound = Lazy.delay();
        final Lazy<Integer> one = Lazy.delay(1);
        final Lazy<Integer> two = Lazy.delay(one, a -> a + 1);
        bound.bind(two);
        assertEquals("(_1_) -> ...", bound.toString());
    }

    @Test
    public void testLateBoundTransitiveUnbound() {
        final LateBound<Integer> outer = Lazy.delay();
        final LateBound<Integer> inner = Lazy.delay();
        outer.bind(inner);
        assertEquals("(???) -> ???", outer.toString());
    }

    @Test
    public void testLateBoundTransitiveForced() {
        final LateBound<Integer> outer = Lazy.delay();
        final LateBound<Integer> inner = Lazy.delay();
        outer.bind(inner);
        final Lazy<Integer> one = Lazy.delay(1);
        inner.bind(one);
        assertEquals("1", outer.toString());
    }

    @Test
    public void testCollectionThunkUnforced() {
        final Collection<Lazy<String>> lazies = ImmutableSet.of(
            Lazy.delay("alpha"), Lazy.delay("beta"));
        final Lazy<List<String>> list = Lazy.sequence(lazies);
        assertEquals("(List[2]) -> ...", list.toString());
    }

    @Test
    public void testCollectionThunkForced() {
        final Collection<Lazy<String>> lazies = new LinkedHashSet<>();
        lazies.add(Lazy.delay("alpha"));
        lazies.add(Lazy.delay("beta"));
        final Lazy<List<String>> list = Lazy.sequence(lazies);
        list.force();
        assertEquals("[alpha, beta]", list.toString());
    }
}
