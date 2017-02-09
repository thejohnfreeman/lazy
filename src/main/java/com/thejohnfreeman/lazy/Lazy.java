package com.thejohnfreeman.lazy;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.thejohnfreeman.function.Function3;
import com.thejohnfreeman.function.Function4;
import com.thejohnfreeman.function.Function5;
import com.thejohnfreeman.function.Function6;

/**
 * A lazy value with dependencies.
 *
 * <p>
 * A lazy value is stateful. Before it is forced, it can return its
 * dependencies, but not its value. After it is forced, it can return its value,
 * but might not be able to return its (former) dependencies.
 *
 * <p>
 * A lazy value cannot be forced until all of its dependencies are forced.
 * An iterative algorithm for forcing a directed acyclic graph of lazy values
 * is provided as the default implementation of {@link #force()}. It is the
 * recommended interface for forcing evaluation.
 *
 * <p>
 * Note: In generic implementing types from this package, the value type is
 * always the first type parameter, unlike functional interfaces, where the
 * result type is always the last type parameter.
 */
@SuppressWarnings("PMD.TooManyMethods")
public interface Lazy<T>
{
    /**
     * Returns whether this value has been forced yet.
     *
     * @return {@code true} iff this value has been successfully forced
     */
    boolean isForced();

    /**
     * Returns an immutable list of this value's dependencies.
     *
     * @return an immutable list of this value's dependencies
     * @throws IllegalStateException if {@code isForced()}
     */
    Iterable<? extends Lazy<?>> getDependencies() throws IllegalStateException;

    /**
     * Evaluates and returns this value. May only be called once. Assumes all
     * of its dependencies are forced. Generally, you should call
     * {@link #force()} instead.
     *
     * @return the value
     * @throws IllegalStateException
     *     if any dependencies are unforced, or if {@code isForced()}
     * @see #force()
     */
    T forceThis() throws IllegalStateException;

    /**
     * Evaluates and returns this value.
     *
     * @return the value
     * @throws IllegalStateException
     *     if any transitive dependencies cannot be evaluated
     */
    default T force()
        throws IllegalStateException
    {
        return Force.force(this);
    }

    /**
     * Returns this value. May not be called unless {@code isForced()}.
     *
     * @return the value
     * @throws IllegalStateException if {@code !isForced()}
     * @see #force()
     */
    T getValue() throws IllegalStateException;

    default String toStringUnforced(final String name) {
        return "() -> " + name;
    }

    static <T> LateBound<T> delay() {
        return LateBound.of();
    }

    static <T> TaggableLazy<T> delay(final T value) {
        return Constant.of(value);
    }

    static <T, A> TaggableLazy<T> delay(
        final Lazy<A> a, final Function<A, T> func)
    {
        return Thunk1.of(a, func);
    }

    static <T, A, B> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final BiFunction<A, B, T> func)
    {
        return Thunk2.of(a, b, func);
    }

    static <T, A, B, C> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c,
        final Function3<A, B, C, T> func)
    {
        return Thunk3.of(a, b, c, func);
    }

    static <T, A, B, C, D> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Function4<A, B, C, D, T> func)
    {
        return Thunk4.of(a, b, c, d, func);
    }

    static <T, A, B, C, D, E> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e,
        final Function5<A, B, C, D, E, T> func)
    {
        return Thunk5.of(a, b, c, d, e, func);
    }

    static <T, A, B, C, D, E, F> TaggableLazy<T> delay(
        final Lazy<A> a, final Lazy<B> b, final Lazy<C> c, final Lazy<D> d,
        final Lazy<E> e, final Lazy<F> f,
        final Function6<A, B, C, D, E, F, T> func)
    {
        return Thunk6.of(a, b, c, d, e, f, func);
    }

    static <T, E> TaggableLazy<T> delay(
        final Collection<? extends Lazy<? extends E>> lazies,
        final Function<? super List<E>, T> func)
    {
        return CollectionThunk.of(lazies, func);
    }

    static <T> TaggableLazy<List<T>> sequence(
        final Collection<? extends Lazy<? extends T>> lazies)
    {
        return CollectionThunk.sequence(lazies);
    }
}
