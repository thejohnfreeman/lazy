package com.jfreeman.lazy;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

public class ToStringTest
{
    @Test
    public void testConstant() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        assertEquals("1", one.toString());
    }

    @Test
    public void testThunkUnforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, a -> a + 1);
        assertEquals("(_1_) -> ...", two.toString());
    }

    @Test
    public void testThunkForced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, a -> a + 1);
        LazyHelp.force(two);
        assertEquals("2", two.toString());
    }

    @Test
    public void testBiThunkUnforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, one, (a, b) -> a + b);
        assertEquals("(_2_) -> ...", two.toString());
    }

    @Test
    public void testBiThunkForced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> two = LazyHelp.delay(one, one, (a, b) -> a + b);
        LazyHelp.force(two);
        assertEquals("2", two.toString());
    }

    @Test
    public void testTriThunkUnforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> three = LazyHelp.delay(
            one, one, one, (a, b, c) -> a + b + c);
        assertEquals("(_3_) -> ...", three.toString());
    }

    @Test
    public void testTriThunkForced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> three = LazyHelp.delay(
            one, one, one, (a, b, c) -> a + b + c);
        LazyHelp.force(three);
        assertEquals("3", three.toString());
    }

    @Test
    public void testQuadThunkUnforced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> four = LazyHelp.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        assertEquals("(_4_) -> ...", four.toString());
    }

    @Test
    public void testQuadThunkForced() {
        final Lazy<Integer> one = LazyHelp.delay(1);
        final Lazy<Integer> four = LazyHelp.delay(
            one, one, one, one, (a, b, c, d) -> a + b + c + d);
        LazyHelp.force(four);
        assertEquals("4", four.toString());
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
    public void testListThunkUnforced() {
        final List<Lazy<String>> lazies = ImmutableList.of(
            LazyHelp.delay("alpha"), LazyHelp.delay("beta"));
        final Lazy<List<String>> list = LazyHelp.sequence(lazies);
        assertEquals("(List[2]) -> ...", list.toString());
    }

    @Test
    public void testListThunkForced() {
        // Use ArrayList just to show that it can pretty-print itself.
        final List<Lazy<String>> lazies = new ArrayList<>();
        lazies.add(LazyHelp.delay("alpha"));
        lazies.add(LazyHelp.delay("beta"));
        final Lazy<List<String>> list = LazyHelp.sequence(lazies);
        LazyHelp.force(list);
        assertEquals("[alpha, beta]", list.toString());
    }
}
