package com.jfreeman.lazy;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.jfreeman.function.QuadFunction;
import com.jfreeman.function.TriFunction;

/**
 * Functions for using lazy values.
 */
public final class LazyHelp
{
    /** Just a namespace, so no constructor. */
    private LazyHelp() {}

    public static <T> LateBound<T> delay() {
        return LateBound.of();
    }

    public static <T> AbstractThunk<T> delay(T value) {
        return Constant.of(value);
    }

    public static <T, A> AbstractThunk<T> delay(Lazy<A> a, Function<A, T> func) {
        return Thunk.of(a, func);
    }

    public static <T, A, B> AbstractThunk<T> delay(
        Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func)
    {
        return BiThunk.of(a, b, func);
    }

    public static <T, A, B, C> AbstractThunk<T> delay(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, TriFunction<A, B, C, T> func)
    {
        return TriThunk.of(a, b, c, func);
    }

    public static <T, A, B, C, D> AbstractThunk<T> delay(
        Lazy<A> a, Lazy<B> b, Lazy<C> c, Lazy<D> d,
        QuadFunction<A, B, C, D, T> func)
    {
        return QuadThunk.of(a, b, c, d, func);
    }

    /**
     * When forced, a long chain of lazy values can cause a stack overflow.
     * This method forces a value iteratively.
     *
     * @param <T> the type of the value
     * @param value the lazy value
     * @return the forced value
     * @throws IllegalStateException
     *     if any value in the dependency graph cannot be evaluated
     */
    public static <T> T force(Lazy<T> value)
        throws IllegalStateException
    {
        ArrayDeque<Context> values = new ArrayDeque<>();
        values.push(new Context(value));

        while (!values.isEmpty()) {
            forceTop(values);
        }

        return value.getValue();
    }

    /**
     * If the top value is ready, force it; otherwise, push one of its unforced
     * dependencies. This is a separate function to make use of `return` as a
     * multi-level `break`. This particular case does not fit the labeled break
     * pattern for Java.
     */
    private static void forceTop(ArrayDeque<Context> values) {
        final Context ctx = values.getFirst();
        if (!ctx.value.isForced()) {
            for (Lazy<?> dep : ctx) {
                if (!dep.isForced()) {
                    values.push(new Context(dep));
                    return;
                }
            }
            ctx.value.force();
        }
        values.pop();
    }

    /**
     * A context is a lazy value and an iterator through its dependencies. Once
     * all of the dependencies have been forced, the value is forced.
     */
    private static class Context
        implements Iterable<Lazy<?>>, Iterator<Lazy<?>>
    {
        public final Lazy<?> value;
        private Iterator<? extends Lazy<?>> _iterator = null;

        public Context(Lazy<?> value) {
            this.value = value;
        }

        @Override
        public Iterator<Lazy<?>> iterator() {
            if (_iterator == null) {
                _iterator = value.getDependencies().iterator();
            }
            return this;
        }

        @Override
        public boolean hasNext() {
            return _iterator.hasNext();
        }

        @Override
        public Lazy<?> next() {
            return _iterator.next();
        }
    }
}
