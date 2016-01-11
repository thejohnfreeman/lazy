package com.jfreeman.function;

/**
 * A function of two arguments.
 *
 * @param <T> the type of the first argument.
 * @param <U> the type of the second argument.
 * @param <R> the type of the result.
 * @author jfreeman
 */
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}
