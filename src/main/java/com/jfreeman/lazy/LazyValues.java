package com.jfreeman.lazy;

import java.util.Iterator;
import java.util.Stack;

import com.jfreeman.function.BiFunction;
import com.jfreeman.function.Function;
import com.jfreeman.function.TriFunction;

/**
 * Functions for using lazy values.
 */
public final class LazyValues {

    /** Just a namespace, so no constructor. */
    private LazyValues() {
    }

    public static <T> LazyValue<T> bind(T value) {
        return Constant.create(value);
    }

    public static <T, A> LazyValue<T> bind(
        LazyValue<A> a, Function<A, T> func)
    {
        return Thunk.create(a, func);
    }

    public static <T, A, B> LazyValue<T> bind(
        LazyValue<A> a, LazyValue<B> b, BiFunction<A, B, T> func)
    {
        return BiThunk.create(a, b, func);
    }

    public static <T, A, B, C> LazyValue<T> bind(
        LazyValue<A> a, LazyValue<B> b, LazyValue<C> c,
        TriFunction<A, B, C, T> func)
    {
        return TriThunk.create(a, b, c, func);
    }

    public static <T> LateBoundValue<T> bindLater() {
        return LateBoundValue.create();
    }

    public static <T> T safelyForce(LazyValue<T> value) {
        Stack<Context> values = new Stack<>();
        values.push(new Context(value));

        while (!values.empty()) {
            safelyForceTop(values);
        }

        return value.force();
    }

    /**
     * If the top value is ready, force it; otherwise, push one of its unforced
     * dependencies. This is a separate function to make use of `return` as a
     * multi-level `break`. This particular case does not fit the labeled break
     * pattern for Java.
     */
    private static void safelyForceTop(Stack<Context> values) {
        final Context ctx = values.peek();
        for (LazyValue<?> dep : ctx) {
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
        implements Iterable<LazyValue<?>> {
        public final LazyValue<?> value;
        private final Iterator<LazyValue<?>> _it;

        public Context(LazyValue<?> value) {
            this.value = value;
            _it = value.getDependencies().iterator();
        }

        @Override
        public Iterator<LazyValue<?>> iterator() {
            return _it;
        }
    }
}
