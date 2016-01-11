package com.jfreeman.function;

/**
 * A function of three arguments.
 *
 * @param <T> the type of the first argument.
 * @param <U> the type of the second argument.
 * @param <V> the type of the third argument.
 * @param <R> the type of the result.
 * @author jfreeman
 */
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
