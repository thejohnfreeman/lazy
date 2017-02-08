package com.thejohnfreeman.lazy;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.thejohnfreeman.function.Function3;
import com.thejohnfreeman.function.Function4;
import com.thejohnfreeman.function.Function5;
import com.thejohnfreeman.function.Function6;

/**
 * Functions for using lazy values.
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class LazyHelp
{
    /** Just a namespace, so no constructor. */
    private LazyHelp() {}

    public static <T> LateBound<T> delay() {
        return LateBound.of();
    }

    public static <T> TaggableLazy<T> delay(final T value) {
        return Constant.of(value);
    }

    public static <T, A> TaggableLazy<T> delay(
        final Lazy<A> a, final Function<A, T> func)
    {
        return Thunk1.of(a, func);
    }

    public static <T, A, B> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final BiFunction<A, B, T> func)
    {
        return Thunk2.of(a, b, func);
    }

    public static <T, A, B, C> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c,
        final Function3<A, B, C, T> func)
    {
        return Thunk3.of(a, b, c, func);
    }

    public static <T, A, B, C, D> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Function4<A, B, C, D, T> func)
    {
        return Thunk4.of(a, b, c, d, func);
    }

    public static <T, A, B, C, D, E> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e,
        final Function5<A, B, C, D, E, T> func)
    {
        return Thunk5.of(a, b, c, d, e, func);
    }

    public static <T, A, B, C, D, E, F> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e, final Lazy<F> f,
        final Function6<A, B, C, D, E, F, T> func)
    {
        return Thunk6.of(a, b, c, d, e, f, func);
    }

    public static <T, E> TaggableLazy<T> delay(
        final Collection<? extends Lazy<? extends E>> lazies,
        final Function<? super Collection<E>, T> func)
    {
        return CollectionThunk.of(lazies, func);
    }

    public static <T> TaggableLazy<Collection<T>> sequence(
        final Collection<? extends Lazy<? extends T>> lazies)
    {
        return CollectionThunk.sequence(lazies);
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
    public static <T> T force(final Lazy<T> value)
        throws IllegalStateException
    {
        final ArrayDeque<Context> values = new ArrayDeque<>();
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
    private static void forceTop(final ArrayDeque<Context> values) {
        final Context context = values.getFirst();
        if (!context.value.isForced()) {
            for (final Lazy<?> dependency : context) {
                if (!dependency.isForced()) {
                    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
                    final Context ctx = new Context(dependency);
                    values.push(ctx);
                    return;
                }
            }
            context.value.force();
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
        private Iterator<? extends Lazy<?>> _iterator;

        public Context(final Lazy<?> value) {
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
