package com.jfreeman.lazy;

import java.util.ArrayDeque;
import java.util.Iterator;

import com.sun.istack.internal.NotNull;

import com.jfreeman.function.BiFunction;
import com.jfreeman.function.Function;
import com.jfreeman.function.TriFunction;

/**
 * Functions for using lazy values.
 */
public final class LazyHelp
{

    /** Just a namespace, so no constructor. */
    private LazyHelp() {}

    public static <T> Lazy<T> bind(T value) {
        return Constant.create(value);
    }

    public static <T, A> Lazy<T> bind(
        @NotNull Lazy<A> a, @NotNull Function<A, T> func)
    {
        return Thunk.create(a, func);
    }

    public static <T, A, B> Lazy<T> bind(
        @NotNull Lazy<A> a, @NotNull Lazy<B> b,
        @NotNull BiFunction<A, B, T> func)
    {
        return BiThunk.create(a, b, func);
    }

    public static <T, A, B, C> Lazy<T> bind(
        @NotNull Lazy<A> a, @NotNull Lazy<B> b, @NotNull Lazy<C> c,
        @NotNull TriFunction<A, B, C, T> func)
    {
        return TriThunk.create(a, b, c, func);
    }

    public static <T> LateBound<T> bindLater() {
        return LateBound.create();
    }

    /**
     * When forced, a long chain of lazy values can cause a stack overflow.
     * This method forces a value iteratively.
     * @param value the lazy value.
     * @return the forced value.
     * @throws IllegalStateException
     */
    public static <T> T force(@NotNull Lazy<T> value)
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
        implements Iterable<Lazy<?>>
    {
        public final Lazy<?> value;
        private final Iterator<Lazy<?>> _it;

        public Context(Lazy<?> value) {
            this.value = value;
            _it = value.getDependencies().iterator();
        }

        @Override
        public Iterator<Lazy<?>> iterator() {
            return _it;
        }
    }
}
