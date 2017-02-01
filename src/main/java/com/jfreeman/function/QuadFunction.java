package com.jfreeman.function;

/**
 * A function of three arguments.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <V> the type of the third argument
 * @param <W> the type of the fourth argument
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface QuadFunction<T, U, V, W, R>
{
    R apply(T t, U u, V v, W w);
}
