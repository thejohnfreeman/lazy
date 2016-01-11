package com.jfreeman.lazy;

import java.util.Iterator;
import java.util.Stack;

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
        Lazy<A> a, Function<A, T> func)
    {
        return Thunk.create(a, func);
    }

    public static <T, A, B> Lazy<T> bind(
        Lazy<A> a, Lazy<B> b, BiFunction<A, B, T> func)
    {
        return BiThunk.create(a, b, func);
    }

    public static <T, A, B, C> Lazy<T> bind(
        Lazy<A> a, Lazy<B> b, Lazy<C> c,
        TriFunction<A, B, C, T> func)
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
    public static <T> T force(Lazy<T> value)
        throws IllegalStateException
    {
        Stack<Context> values = new Stack<>();
        values.push(new Context(value));

        while (!values.empty()) {
            forceTop(values);
        }

        return value.force();
    }

    /**
     * If the top value is ready, force it; otherwise, push one of its unforced
     * dependencies. This is a separate function to make use of `return` as a
     * multi-level `break`. This particular case does not fit the labeled break
     * pattern for Java.
     */
    private static void forceTop(Stack<Context> values) {
        final Context ctx = values.peek();
        for (Lazy<?> dep : ctx) {
            if (!dep.isForced()) {
                values.push(new Context(dep));
                return;
            }
        }
        ctx.value.force();
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
